package com.lan;

import com.lan.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Activity01 extends Activity
{
	/* 取得預設的藍芽介面卡 */
	private BluetoothAdapter	_bluetooth				= BluetoothAdapter.getDefaultAdapter();

	/* 請求開啟藍芽 */
	private static final int	REQUEST_ENABLE			= 0x1;
	/* 請求能夠被搜尋 */
	private static final int	REQUEST_DISCOVERABLE	= 0x2;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}


	/* 開啟藍芽 */
	public void onEnableButtonClicked(View view)
	{
		// 使用者請求開啟藍芽
		//Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		//startActivityForResult(enabler, REQUEST_ENABLE);
		//開啟藍芽
		_bluetooth.enable();
	}


	/* 關閉藍芽 */
	public void onDisableButtonClicked(View view)
	{

		_bluetooth.disable();
	}


	/* 使裝置能夠被搜尋 */
	public void onMakeDiscoverableButtonClicked(View view)
	{

		Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		startActivityForResult(enabler, REQUEST_DISCOVERABLE);
	}


	/* 開始搜尋 */
	public void onStartDiscoveryButtonClicked(View view)
	{

		Intent enabler = new Intent(this, DiscoveryActivity.class);
		startActivity(enabler);
	}


	/* 用戶端 */
	public void onOpenClientSocketButtonClicked(View view)
	{

		Intent enabler = new Intent(this, ClientSocketActivity.class);
		startActivity(enabler);
	}


	/* 服務端 */
	public void onOpenServerSocketButtonClicked(View view)
	{

		Intent enabler = new Intent(this, ServerSocketActivity.class);
		startActivity(enabler);
	}


	/* OBEX伺服器 */
	public void onOpenOBEXServerSocketButtonClicked(View view)
	{

		Intent enabler = new Intent(this, OBEXActivity.class);
		startActivity(enabler);
	}
}
