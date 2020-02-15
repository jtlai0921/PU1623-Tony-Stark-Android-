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
 * 藍芽模組用戶端主控制Service
 */
public class BluetoothClientService extends Service {
	
	//搜尋到的遠端裝置集合
	private List<BluetoothDevice> discoveredDevices = new ArrayList<BluetoothDevice>();
	
	//藍芽介面卡
	private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	
	//藍芽通信執行緒
	private BluetoothCommunThread communThread;
	
	//控制訊息廣播的接收器
	private BroadcastReceiver controlReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if (BluetoothTools.ACTION_START_DISCOVERY.equals(action)) {
				//開始搜尋
				discoveredDevices.clear();	//清理存放裝置的集合
				bluetoothAdapter.enable();	//開啟藍芽
				bluetoothAdapter.startDiscovery();	//開始搜尋
				
			} else if (BluetoothTools.ACTION_SELECTED_DEVICE.equals(action)) {
				//選取了連線的伺服器裝置
				BluetoothDevice device = (BluetoothDevice)intent.getExtras().get(BluetoothTools.DEVICE);
				
				//開啟裝置連線執行緒
				new BluetoothClientConnThread(handler, device).start();
				
			} else if (BluetoothTools.ACTION_STOP_SERVICE.equals(action)) {
				//停止背景服務
				if (communThread != null) {
					communThread.isRun = false;
				}
				stopSelf();
				
			} else if (BluetoothTools.ACTION_DATA_TO_SERVICE.equals(action)) {
				//取得資料
				Object data = intent.getSerializableExtra(BluetoothTools.DATA);
				if (communThread != null) {
					communThread.writeObject(data);
				}
				
			}
		}
	};
	
	//藍芽搜尋廣播的接收器
	private BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			//取得廣播的Action
			String action = intent.getAction();

			if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
				//開始搜尋
			} else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				//發現遠端藍芽裝置
				//取得裝置
				BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				discoveredDevices.add(bluetoothDevice);

				//傳送發現裝置廣播
				Intent deviceListIntent = new Intent(BluetoothTools.ACTION_FOUND_DEVICE);
				deviceListIntent.putExtra(BluetoothTools.DEVICE, bluetoothDevice);
				sendBroadcast(deviceListIntent);
				
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				//搜尋結束
				if (discoveredDevices.isEmpty()) {
					//若找不到裝置，則發動沒有找到裝置廣播
					Intent foundIntent = new Intent(BluetoothTools.ACTION_NOT_FOUND_SERVER);
					sendBroadcast(foundIntent);
				}
			}
		}
	};
	
	//接收其他執行緒訊息的Handler
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			//處理訊息
			switch (msg.what) {
			case BluetoothTools.MESSAGE_CONNECT_ERROR:
				//連線錯誤
				//傳送連線錯誤廣播
				Intent errorIntent = new Intent(BluetoothTools.ACTION_CONNECT_ERROR);
				sendBroadcast(errorIntent);
				break;
			case BluetoothTools.MESSAGE_CONNECT_SUCCESS:
				//連線成功
				
				//開啟通信執行緒
				communThread = new BluetoothCommunThread(handler, (BluetoothSocket)msg.obj);
				communThread.start();
				
				//傳送連線成功廣播
				Intent succIntent = new Intent(BluetoothTools.ACTION_CONNECT_SUCCESS);
				sendBroadcast(succIntent);
				break;
			case BluetoothTools.MESSAGE_READ_OBJECT:
				//讀取到物件
				//傳送資料廣播（包括資料物件）
				Intent dataIntent = new Intent(BluetoothTools.ACTION_DATA_TO_GAME);
				dataIntent.putExtra(BluetoothTools.DATA, (Serializable)msg.obj);
				sendBroadcast(dataIntent);
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	/**
	 * 取得通信執行緒
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
	 * Service建立時的回調函數
	 */
	@Override
	public void onCreate() {
		//discoveryReceiver的IntentFilter
		IntentFilter discoveryFilter = new IntentFilter();
		discoveryFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		discoveryFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		discoveryFilter.addAction(BluetoothDevice.ACTION_FOUND);
		
		//controlReceiver的IntentFilter
		IntentFilter controlFilter = new IntentFilter();
		controlFilter.addAction(BluetoothTools.ACTION_START_DISCOVERY);
		controlFilter.addAction(BluetoothTools.ACTION_SELECTED_DEVICE);
		controlFilter.addAction(BluetoothTools.ACTION_STOP_SERVICE);
		controlFilter.addAction(BluetoothTools.ACTION_DATA_TO_SERVICE);
		
		//登錄BroadcastReceiver
		registerReceiver(discoveryReceiver, discoveryFilter);
		registerReceiver(controlReceiver, controlFilter);
		super.onCreate();
	}
	
	/**
	 * Service銷毀時的回調函數
	 */
	@Override
	public void onDestroy() {
		if (communThread != null) {
			communThread.isRun = false;
		}
		//解除綁定
		unregisterReceiver(discoveryReceiver);
		super.onDestroy();
	}

}
