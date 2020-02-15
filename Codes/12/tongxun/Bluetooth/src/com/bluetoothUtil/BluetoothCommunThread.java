package com.bluetoothUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
	
/**
 * �Ū޳q�H�����
 */
public class BluetoothCommunThread extends Thread {

	private Handler serviceHandler;		//�PService�q�T��Handler
	private BluetoothSocket socket;
	private ObjectInputStream inStream;		//�����J�y
	private ObjectOutputStream outStream;	//�����X�y
	public volatile boolean isRun = true;	//����ЧӦ�
	
	/**
	 * �غc���
	 * @param handler �Ω󱵦��T��
	 * @param socket
	 */
	public BluetoothCommunThread(Handler handler, BluetoothSocket socket) {
		this.serviceHandler = handler;
		this.socket = socket;
		try {
			this.outStream = new ObjectOutputStream(socket.getOutputStream());
			this.inStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
		} catch (Exception e) {
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			//�ǰe�s�u���ѰT��
			serviceHandler.obtainMessage(BluetoothTools.MESSAGE_CONNECT_ERROR).sendToTarget();
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while (true) {
			if (!isRun) {
				break;
			}
			try {
				Object obj = inStream.readObject();
				//�ǰe���\Ū���쪫�󪺰T���A�T����obj�ѼƬ�Ū���쪺����
				Message msg = serviceHandler.obtainMessage();
				msg.what = BluetoothTools.MESSAGE_READ_OBJECT;
				msg.obj = obj;
				msg.sendToTarget();
			} catch (Exception ex) {
				//�ǰe�s�u���ѰT��
				serviceHandler.obtainMessage(BluetoothTools.MESSAGE_CONNECT_ERROR).sendToTarget();
				ex.printStackTrace();
				return;
			}
		}
		
		//�����y
		if (inStream != null) {
			try {
				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (outStream != null) {
			try {
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * �g�J�@�ӥi�ǦC�ƪ�����
	 * @param obj
	 */
	public void writeObject(Object obj) {
		try {
			outStream.flush();
			outStream.writeObject(obj);
			outStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
