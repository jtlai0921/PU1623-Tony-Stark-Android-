package com.bluetoothUtil;

import java.io.Serializable;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

/**
 * �Ū޼Ҳզ��A���ݥD����Service
 */
public class BluetoothServerService extends Service {

	//�Ūޤ����d
	private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	
	//�Ū޳q�H�����
	private BluetoothCommunThread communThread;
	
	//����T���s��������
	private BroadcastReceiver controlReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if (BluetoothTools.ACTION_STOP_SERVICE.equals(action)) {
				//����I���A��
				if (communThread != null) {
					communThread.isRun = false;
				}
				stopSelf();
				
			} else if (BluetoothTools.ACTION_DATA_TO_SERVICE.equals(action)) {
				//�ǰe���
				Object data = intent.getSerializableExtra(BluetoothTools.DATA);
				if (communThread != null) {
					communThread.writeObject(data);
				}
				
			}
		}
	};
	
	//������L������T����Handler
	private Handler serviceHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case BluetoothTools.MESSAGE_CONNECT_SUCCESS:
				//�s�u���\
				//�}�ҳq�H�����
				communThread = new BluetoothCommunThread(serviceHandler, (BluetoothSocket)msg.obj);
				communThread.start();
				
				//�ǰe�s�u���\�T��
				Intent connSuccIntent = new Intent(BluetoothTools.ACTION_CONNECT_SUCCESS);
				sendBroadcast(connSuccIntent);
				break;
				
			case BluetoothTools.MESSAGE_CONNECT_ERROR:
				//�s�u���~
				//�ǰe�s�u���~�s��
				Intent errorIntent = new Intent(BluetoothTools.ACTION_CONNECT_ERROR);
				sendBroadcast(errorIntent);
				break;
				
			case BluetoothTools.MESSAGE_READ_OBJECT:
				//Ū������
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
	public void onCreate() {
		//ControlReceiver��IntentFilter
		IntentFilter controlFilter = new IntentFilter();
		controlFilter.addAction(BluetoothTools.ACTION_START_SERVER);
		controlFilter.addAction(BluetoothTools.ACTION_STOP_SERVICE);
		controlFilter.addAction(BluetoothTools.ACTION_DATA_TO_SERVICE);
		
		//�n��BroadcastReceiver
		registerReceiver(controlReceiver, controlFilter);
		
		//�}�Ҧ��A��
		bluetoothAdapter.enable();	//�}���Ū�
		//�}���Ū޵o�{�\��]300��^
		Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
		discoveryIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(discoveryIntent);
		//�}�ҭI���s�u�����
		new BluetoothServerConnThread(serviceHandler).start();
		
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		if (communThread != null) {
			communThread.isRun = false;
		}
		unregisterReceiver(controlReceiver);
		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
