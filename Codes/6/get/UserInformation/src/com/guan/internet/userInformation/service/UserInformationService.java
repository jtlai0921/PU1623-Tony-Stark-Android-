package com.guan.internet.userInformation.service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class UserInformationService {
	public static boolean save(String title, String length) throws Exception{
		String path = "http://192.168.1.100:8080/ServerForGETMethod/ServletForGETMethod";
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", title);
		params.put("age", length);
		return sendGETRequest(path, params, "UTF-8");
	}
	
	
	
	/**
	 * 傳送GET請求
	 * @param path 請求路徑
	 * @param params 請求參數
	 * @return
	 */
	private static boolean sendGETRequest(String path, Map<String, String> params, String encoding) throws Exception{
		// http://192.178.1.100:8080/ServerForGETMethod/ServletForGETMethod?title=xxxx&length=90
		StringBuilder sb = new StringBuilder(path);
		if(params!=null && !params.isEmpty()){
			sb.append("?");
			for(Map.Entry<String, String> entry : params.entrySet()){
				sb.append(entry.getKey()).append("=");
				sb.append(URLEncoder.encode(entry.getValue(), encoding));
				sb.append("&");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		HttpURLConnection conn = (HttpURLConnection) new URL(sb.toString()).openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if(conn.getResponseCode() == 200){
			return true;
		}
		return false;
	}
}
