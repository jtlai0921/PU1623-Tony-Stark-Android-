package com.lan;

import com.lan.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Activity01 extends Activity
{
	/* ���o�w�]���Ūޤ����d */
	private BluetoothAdapter	_bluetooth				= BluetoothAdapter.getDefaultAdapter();

	/* �ШD�}���Ū� */
	private static final int	REQUEST_ENABLE			= 0x1;
	/* �ШD����Q�j�M */
	private static final int	REQUEST_DISCOVERABLE	= 0x2;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}


	/* �}���Ū� */
	public void onEnableButtonClicked(View view)
	{
		// �ϥΪ̽ШD�}���Ū�
		//Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		//startActivityForResult(enabler, REQUEST_ENABLE);
		//�}���Ū�
		_bluetooth.enable();
	}


	/* �����Ū� */
	public void onDisableButtonClicked(View view)
	{

		_bluetooth.disable();
	}


	/* �ϸ˸m����Q�j�M */
	public void onMakeDiscoverableButtonClicked(View view)
	{

		Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		startActivityForResult(enabler, REQUEST_DISCOVERABLE);
	}


	/* �}�l�j�M */
	public void onStartDiscoveryButtonClicked(View view)
	{

		Intent enabler = new Intent(this, DiscoveryActivity.class);
		startActivity(enabler);
	}


	/* �Τ�� */
	public void onOpenClientSocketButtonClicked(View view)
	{

		Intent enabler = new Intent(this, ClientSocketActivity.class);
		startActivity(enabler);
	}


	/* �A�Ⱥ� */
	public void onOpenServerSocketButtonClicked(View view)
	{

		Intent enabler = new Intent(this, ServerSocketActivity.class);
		startActivity(enabler);
	}


	/* OBEX���A�� */
	public void onOpenOBEXServerSocketButtonClicked(View view)
	{

		Intent enabler = new Intent(this, OBEXActivity.class);
		startActivity(enabler);
	}
}
