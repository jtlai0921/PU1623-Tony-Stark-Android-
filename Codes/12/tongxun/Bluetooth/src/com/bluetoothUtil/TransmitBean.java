package com.bluetoothUtil;

import java.io.Serializable;

/**
 * 用於傳輸的資料類別
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
