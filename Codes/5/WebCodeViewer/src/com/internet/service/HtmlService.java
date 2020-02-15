package com.internet.service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.internet.utils.StreamTool;

public class HtmlService {
	/**
	 * 取得網頁原始程式
	 * @param path 網頁路徑
	 * @return
	 */
	public static String getHtml(String path) throws Exception {
		HttpURLConnection conn = (HttpURLConnection)new URL(path).openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if(conn.getResponseCode() == 200){
			InputStream inStream = conn.getInputStream();
			byte[] data = StreamTool.read(inStream);
			return new String(data);
		}
		return null;
	}
}
