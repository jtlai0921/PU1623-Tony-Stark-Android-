package com.bluetooth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bluetooth.R;
import com.bluetoothUtil.BluetoothClientService;
import com.bluetoothUtil.BluetoothTools;
import com.bluetoothUtil.TransmitBean;

public class ClientActivity extends Activity {

	private TextView serversText;
	private EditText chatEditText;
	private EditText sendEditText;
	private Button sendBtn;
	private Button startSearchBtn;
	private Button selectDeviceBtn;
	
	private List<BluetoothDevice> deviceList = new ArrayList<BluetoothDevice>();
	
	//�s��������
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if (BluetoothTools.ACTION_NOT_FOUND_SERVER.equals(action)) {
				//�S�����˸m
				serversText.append("not found device\r\n");
				
			} else if (BluetoothTools.ACTION_FOUND_DEVICE.equals(action)) {
				//���o��˸m����
				BluetoothDevice device = (BluetoothDevice)intent.getExtras().get(BluetoothTools.DEVICE);
				deviceList.add(device);
				serversText.append(device.getName() + "\r\n");
				
			} else if (BluetoothTools.ACTION_CONNECT_SUCCESS.equals(action)) {
				//�s�u���\
				serversText.append("�s�u���\");
				sendBtn.setEnabled(true);
				
			} else if (BluetoothTools.ACTION_DATA_TO_GAME.equals(action)) {
				//�������
				TransmitBean data = (TransmitBean)intent.getExtras().getSerializable(BluetoothTools.DATA);
				String msg = "from remote " + new Date().toLocaleString() + " :\r\n" + data.getMsg() + "\r\n";
				chatEditText.append(msg);
			
			} 
		}
	};
	
	
	@Override
	protected void onStart() {
		//�M�z�˸m�M��
		deviceList.clear();
		
		//�}�ҭI��service
		Intent startService = new Intent(ClientActivity.this, BluetoothClientService.class);
		startService(startService);
		
		//�n��BoradcasrReceiver
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothTools.ACTION_NOT_FOUND_SERVER);
		intentFilter.addAction(BluetoothTools.ACTION_FOUND_DEVICE);
		intentFilter.addAction(BluetoothTools.ACTION_DATA_TO_GAME);
		intentFilter.addAction(BluetoothTools.ACTION_CONNECT_SUCCESS);
		
		registerReceiver(broadcastReceiver, intentFilter);
		
		super.onStart();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.client);
		
		serversText = (TextView)findViewById(R.id.clientServersText);
		chatEditText = (EditText)findViewById(R.id.clientChatEditText);
		sendEditText = (EditText)findViewById(R.id.clientSendEditText);
		sendBtn = (Button)findViewById(R.id.clientSendMsgBtn);
		startSearchBtn = (Button)findViewById(R.id.startSearchBtn);
		selectDeviceBtn = (Button)findViewById(R.id.selectDeviceBtn);
		
		sendBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//�ǰe�T��
				if ("".equals(sendEditText.getText().toString().trim())) {
					Toast.makeText(ClientActivity.this, "��J���ର��", Toast.LENGTH_SHORT).show();
				} else {
					//�ǰe�T��
					TransmitBean data = new TransmitBean();
					data.setMsg(sendEditText.getText().toString());
					Intent sendDataIntent = new Intent(BluetoothTools.ACTION_DATA_TO_SERVICE);
					sendDataIntent.putExtra(BluetoothTools.DATA, data);
					sendBroadcast(sendDataIntent);
				}
			}
		});
		
		startSearchBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//�}�l�j�M
				Intent startSearchIntent = new Intent(BluetoothTools.ACTION_START_DISCOVERY);
				sendBroadcast(startSearchIntent);
			}
		});
		
		selectDeviceBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//����Ĥ@�Ӹ˸m
				Intent selectDeviceIntent = new Intent(BluetoothTools.ACTION_SELECTED_DEVICE);
				selectDeviceIntent.putExtra(BluetoothTools.DEVICE, deviceList.get(0));
				sendBroadcast(selectDeviceIntent);
			}
		});
	}

	@Override
	protected void onStop() {
		//�����I��Service
		Intent startService = new Intent(BluetoothTools.ACTION_STOP_SERVICE);
		sendBroadcast(startService);
		
		unregisterReceiver(broadcastReceiver);
		super.onStop();
	}
}