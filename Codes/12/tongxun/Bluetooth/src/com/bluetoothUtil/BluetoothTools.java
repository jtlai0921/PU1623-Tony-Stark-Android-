package com.bluetoothUtil;

import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

/**
 * 藍芽工具類別
 */
public class BluetoothTools {

	private static BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
	
	/**
	 * 本程式所使用的UUID
	 */
	public static final UUID PRIVATE_UUID = UUID.fromString("0f3561b9-bda5-4672-84ff-ab1f98e349b6");
	
	/**
	 * 字串常數，存放在Intent中的裝置物件
	 */
	public static final String DEVICE = "DEVICE";
	
	/**
	 * 字串常數，伺服器所在裝置清單中的位置
	 */
	public static final String SERVER_INDEX = "SERVER_INDEX";
	
	/**
	 * 字串常數，Intent中的資料
	 */
	public static final String DATA = "DATA";
	
	/**
	 * Action型態標誌符，Action型態 為讀到資料
	 */
	public static final String ACTION_READ_DATA = "ACTION_READ_DATA";
	
	/**
	 * Action型態標誌符，Action型態為 沒有找到裝置
	 */
	public static final String ACTION_NOT_FOUND_SERVER = "ACTION_NOT_FOUND_DEVICE";
	
	/**
	 * Action型態標誌符，Action型態為 開始搜尋裝置
	 */
	public static final String ACTION_START_DISCOVERY = "ACTION_START_DISCOVERY";
	
	/**
	 * Action：裝置清單
	 */
	public static final String ACTION_FOUND_DEVICE = "ACTION_FOUND_DEVICE";
	
	/**
	 * Action：選取的用於連線的裝置
	 */
	public static final String ACTION_SELECTED_DEVICE = "ACTION_SELECTED_DEVICE";
	
	/**
	 * Action：開啟伺服器
	 */
	public static final String ACTION_START_SERVER = "ACTION_STARRT_SERVER";
	
	/**
	 * Action：關閉背景Service
	 */
	public static final String ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE";
	
	/**
	 * Action：到Service的資料
	 */
	public static final String ACTION_DATA_TO_SERVICE = "ACTION_DATA_TO_SERVICE";
	
	/**
	 * Action：到游戲業務中的資料
	 */
	public static final String ACTION_DATA_TO_GAME = "ACTION_DATA_TO_GAME";
	
	/**
	 * Action：連線成功
	 */
	public static final String ACTION_CONNECT_SUCCESS = "ACTION_CONNECT_SUCCESS";
	
	/**
	 * Action：連線錯誤
	 */
	public static final String ACTION_CONNECT_ERROR = "ACTION_CONNECT_ERROR";
	
	/**
	 * Message型態標誌符，連線成功
	 */
	public static final int MESSAGE_CONNECT_SUCCESS = 0x00000002;
	
	/**
	 * Message：連線失敗
	 */
	public static final int MESSAGE_CONNECT_ERROR = 0x00000003;
	
	/**
	 * Message：讀取到一個物件
	 */
	public static final int MESSAGE_READ_OBJECT = 0x00000004;
	
	/**
	 * 開啟藍芽功能
	 */
	public static void openBluetooth() {
		adapter.enable();
	}
	
	/**
	 * 關閉藍芽功能
	 */
	public static void closeBluetooth() {
		adapter.disable();
	}
	
	/**
	 * 設定藍芽發現功能
	 * @param duration 設定藍芽發現功能開啟持續秒數（值為0至300之間的整數）
	 */
	public static void openDiscovery(int duration) {
		if (duration <= 0 || duration > 300) {
			duration = 200;
		}
		Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, duration);
	}
	
	/**
	 * 停止藍芽搜尋
	 */
	public static void stopDiscovery() {
		adapter.cancelDiscovery();
	}
	
}
