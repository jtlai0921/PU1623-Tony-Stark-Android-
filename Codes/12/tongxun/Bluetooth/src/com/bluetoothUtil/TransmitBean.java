package com.bluetoothUtil;

import java.io.Serializable;

/**
 * �Ω�ǿ骺������O
 *
 */
public class TransmitBean implements Serializable{

	private String msg = "";
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() {
		return this.msg;
	}
	
}
