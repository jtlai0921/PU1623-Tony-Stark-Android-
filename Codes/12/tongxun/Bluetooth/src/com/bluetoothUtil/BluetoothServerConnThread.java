package com.bluetoothUtil;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

/**
 * ���A���s�u�����
 */
public class BluetoothServerConnThread extends Thread {
	
	private Handler serviceHandler;		//�Ω�PService�q�T��Handler
	private BluetoothAdapter adapter;
	private BluetoothSocket socket;		//�Ω�q�T��Socket
	private BluetoothServerSocket serverSocket;
	
	/**
	 * �غc���
	 * @param handler
	 */
	public BluetoothServerConnThread(Handler handler) {
		this.serviceHandler = handler;
		adapter = BluetoothAdapter.getDefaultAdapter();
	}
	
	@Override
	public void run() {
		
		try {
			serverSocket = adapter.listenUsingRfcommWithServiceRecord("Server", BluetoothTools.PRIVATE_UUID);
			socket = serverSocket.accept();
		} catch (Exception e) {
			//�ǰe�s�u���ѰT��
			serviceHandler.obtainMessage(BluetoothTools.MESSAGE_CONNECT_ERROR).sendToTarget();
			e.printStackTrace();
			return;
		} finally {
			try {
				serverSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (socket != null) {
			//�ǰe�s�u���\�T���A�T����obj�r�q���s�u��socket
			Message msg = serviceHandler.obtainMessage();
			msg.what = BluetoothTools.MESSAGE_CONNECT_SUCCESS;
			msg.obj = socket;
			msg.sendToTarget();
		} else {
			//�ǰe�s�u���ѰT��
			serviceHandler.obtainMessage(BluetoothTools.MESSAGE_CONNECT_ERROR).sendToTarget();
			return;
		}
	}
	
	
}
