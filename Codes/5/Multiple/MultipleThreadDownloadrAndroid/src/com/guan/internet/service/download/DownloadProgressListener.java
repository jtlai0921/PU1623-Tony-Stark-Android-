package com.guan.internet.service.download;

/**
 * 下載進度監聽器
 *
 */
public interface DownloadProgressListener {
	/**
	 * 下載進度監聽方法 取得和處理下載點資料的大小
	 * @param size 資料大小
	 */
	public void onDownloadSize(int size);
}
