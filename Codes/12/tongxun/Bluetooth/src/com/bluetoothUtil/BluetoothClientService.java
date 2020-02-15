package com.bluetoothUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

/**
 * �Ū޼ҲեΤ�ݥD����Service
 */
public class BluetoothClientService extends Service {
	
	//�j�M�쪺���ݸ˸m���X
	private List<BluetoothDevice> discoveredDevices = new ArrayList<BluetoothDevice>();
	
	//�Ūޤ����d
	private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	
	//�Ū޳q�H�����
	private BluetoothCommunThread communThread;
	
	//����T���s����������
	private BroadcastReceiver controlReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if (BluetoothTools.ACTION_START_DISCOVERY.equals(action)) {
				//�}�l�j�M
				discoveredDevices.clear();	//�M�z�s��˸m�����X
				bluetoothAdapter.enable();	//�}���Ū�
				bluetoothAdapter.startDiscovery();	//�}�l�j�M
				
			} else if (BluetoothTools.ACTION_SELECTED_DEVICE.equals(action)) {
				//����F�s�u�����A���˸m
				BluetoothDevice device = (BluetoothDevice)intent.getExtras().get(BluetoothTools.DEVICE);
				
				//�}�Ҹ˸m�s�u�����
				new BluetoothClientConnThread(handler, device).start();
				
			} else if (BluetoothTools.ACTION_STOP_SERVICE.equals(action)) {
				//����I���A��
				if (communThread != null) {
					communThread.isRun = false;
				}
				stopSelf();
				
			} else if (BluetoothTools.ACTION_DATA_TO_SERVICE.equals(action)) {
				//���o���
				Object data = intent.getSerializableExtra(BluetoothTools.DATA);
				if (communThread != null) {
					communThread.writeObject(data);
				}
				
			}
		}
	};
	
	//�Ū޷j�M�s����������
	private BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			//���o�s����Action
			String action = intent.getAction();

			if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
				//�}�l�j�M
			} else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				//�o�{�����Ū޸˸m
				//���o�˸m
				BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				discoveredDevices.add(bluetoothDevice);

				//�ǰe�o�{�˸m�s��
				Intent deviceListIntent = new Intent(BluetoothTools.ACTION_FOUND_DEVICE);
				deviceListIntent.putExtra(BluetoothTools.DEVICE, bluetoothDevice);
				sendBroadcast(deviceListIntent);
				
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				//�j�M����
				if (discoveredDevices.isEmpty()) {
					//�Y�䤣��˸m�A�h�o�ʨS�����˸m�s��
					Intent foundIntent = new Intent(BluetoothTools.ACTION_NOT_FOUND_SERVER);
					sendBroadcast(foundIntent);
				}
			}
		}
	};
	
	//������L������T����Handler
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			//�B�z�T��
			switch (msg.what) {
			case BluetoothTools.MESSAGE_CONNECT_ERROR:
				//�s�u���~
				//�ǰe�s�u���~�s��
				Intent errorIntent = new Intent(BluetoothTools.ACTION_CONNECT_ERROR);
				sendBroadcast(errorIntent);
				break;
			case BluetoothTools.MESSAGE_CONNECT_SUCCESS:
				//�s�u���\
				
				//�}�ҳq�H�����
				communThread = new BluetoothCommunThread(handler, (BluetoothSocket)msg.obj);
				communThread.start();
				
				//�ǰe�s�u���\�s��
				Intent succIntent = new Intent(BluetoothTools.ACTION_CONNECT_SUCCESS);
				sendBroadcast(succIntent);
				break;
			case BluetoothTools.MESSAGE_READ_OBJECT:
				//Ū���쪫��
				//�ǰe��Ƽs���]�]�A��ƪ���^
				Intent dataIntent = new Intent(BluetoothTools.ACTION_DATA_TO_GAME);
				dataIntent.putExtra(BluetoothTools.DATA, (Serializable)msg.obj);
				sendBroadcast(dataIntent);
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	/**
	 * ���o�q�H�����
	 * @return
	 */
	public BluetoothCommunThread getBluetoothCommunThread() {
		return communThread;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		
		super.onStart(intent, startId);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	/**
	 * Service�إ߮ɪ��^�ը��
	 */
	@Override
	public void onCreate() {
		//discoveryReceiver��IntentFilter
		IntentFilter discoveryFilter = new IntentFilter();
		discoveryFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		discoveryFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		discoveryFilter.addAction(BluetoothDevice.ACTION_FOUND);
		
		//controlReceiver��IntentFilter
		IntentFilter controlFilter = new IntentFilter();
		controlFilter.addAction(BluetoothTools.ACTION_START_DISCOVERY);
		controlFilter.addAction(BluetoothTools.ACTION_SELECTED_DEVICE);
		controlFilter.addAction(BluetoothTools.ACTION_STOP_SERVICE);
		controlFilter.addAction(BluetoothTools.ACTION_DATA_TO_SERVICE);
		
		//�n��BroadcastReceiver
		registerReceiver(discoveryReceiver, discoveryFilter);
		registerReceiver(controlReceiver, controlFilter);
		super.onCreate();
	}
	
	/**
	 * Service�P���ɪ��^�ը��
	 */
	@Override
	public void onDestroy() {
		if (communThread != null) {
			communThread.isRun = false;
		}
		//�Ѱ��j�w
		unregisterReceiver(discoveryReceiver);
		super.onDestroy();
	}

}
