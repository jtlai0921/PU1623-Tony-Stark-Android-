package com.bluetoothUtil;

import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

/**
 * �Ūޤu�����O
 */
public class BluetoothTools {

	private static BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
	
	/**
	 * ���{���ҨϥΪ�UUID
	 */
	public static final UUID PRIVATE_UUID = UUID.fromString("0f3561b9-bda5-4672-84ff-ab1f98e349b6");
	
	/**
	 * �r��`�ơA�s��bIntent�����˸m����
	 */
	public static final String DEVICE = "DEVICE";
	
	/**
	 * �r��`�ơA���A���Ҧb�˸m�M�椤����m
	 */
	public static final String SERVER_INDEX = "SERVER_INDEX";
	
	/**
	 * �r��`�ơAIntent�������
	 */
	public static final String DATA = "DATA";
	
	/**
	 * Action���A�лx�šAAction���A ��Ū����
	 */
	public static final String ACTION_READ_DATA = "ACTION_READ_DATA";
	
	/**
	 * Action���A�лx�šAAction���A�� �S�����˸m
	 */
	public static final String ACTION_NOT_FOUND_SERVER = "ACTION_NOT_FOUND_DEVICE";
	
	/**
	 * Action���A�лx�šAAction���A�� �}�l�j�M�˸m
	 */
	public static final String ACTION_START_DISCOVERY = "ACTION_START_DISCOVERY";
	
	/**
	 * Action�G�˸m�M��
	 */
	public static final String ACTION_FOUND_DEVICE = "ACTION_FOUND_DEVICE";
	
	/**
	 * Action�G������Ω�s�u���˸m
	 */
	public static final String ACTION_SELECTED_DEVICE = "ACTION_SELECTED_DEVICE";
	
	/**
	 * Action�G�}�Ҧ��A��
	 */
	public static final String ACTION_START_SERVER = "ACTION_STARRT_SERVER";
	
	/**
	 * Action�G�����I��Service
	 */
	public static final String ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE";
	
	/**
	 * Action�G��Service�����
	 */
	public static final String ACTION_DATA_TO_SERVICE = "ACTION_DATA_TO_SERVICE";
	
	/**
	 * Action�G������~�Ȥ������
	 */
	public static final String ACTION_DATA_TO_GAME = "ACTION_DATA_TO_GAME";
	
	/**
	 * Action�G�s�u���\
	 */
	public static final String ACTION_CONNECT_SUCCESS = "ACTION_CONNECT_SUCCESS";
	
	/**
	 * Action�G�s�u���~
	 */
	public static final String ACTION_CONNECT_ERROR = "ACTION_CONNECT_ERROR";
	
	/**
	 * Message���A�лx�šA�s�u���\
	 */
	public static final int MESSAGE_CONNECT_SUCCESS = 0x00000002;
	
	/**
	 * Message�G�s�u����
	 */
	public static final int MESSAGE_CONNECT_ERROR = 0x00000003;
	
	/**
	 * Message�GŪ����@�Ӫ���
	 */
	public static final int MESSAGE_READ_OBJECT = 0x00000004;
	
	/**
	 * �}���Ūޥ\��
	 */
	public static void openBluetooth() {
		adapter.enable();
	}
	
	/**
	 * �����Ūޥ\��
	 */
	public static void closeBluetooth() {
		adapter.disable();
	}
	
	/**
	 * �]�w�Ū޵o�{�\��
	 * @param duration �]�w�Ū޵o�{�\��}�ҫ����ơ]�Ȭ�0��300��������ơ^
	 */
	public static void openDiscovery(int duration) {
		if (duration <= 0 || duration > 300) {
			duration = 200;
		}
		Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, duration);
	}
	
	/**
	 * �����Ū޷j�M
	 */
	public static void stopDiscovery() {
		adapter.cancelDiscovery();
	}
	
}
