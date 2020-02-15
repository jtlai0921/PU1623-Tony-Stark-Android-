package com.guan.internet.service.download;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

/**
 * 下載執行緒，根據實際下載位址、保持到的檔案、下載塊的大小、已經下載的資料大小等訊息進行下載
 *
 */
public class DownloadThread extends Thread {
	private static final String TAG = "DownloadThread";	//定義TAG，方便日子的列印輸出
	private File saveFile;	//下載的資料儲存到的檔案
	private URL downUrl;	//下載的URL
	private int block;	//每條執行緒下載的大小
	private int threadId = -1;	//起始化執行緒id設定
	private int downloadedLength;	//該執行緒已經下載的資料長度
	private boolean finished = false;	//該執行緒是否完成下載的標志
	private FileDownloader downloader;	//檔案下載器
	
	public DownloadThread(FileDownloader downloader, URL downUrl, File saveFile, int block, int downloadedLength, int threadId) {
		this.downUrl = downUrl;
		this.saveFile = saveFile;
		this.block = block;
		this.downloader = downloader;
		this.threadId = threadId;
		this.downloadedLength = downloadedLength;
	}
	
	@Override
	public void run() {
		if(downloadedLength < block){//未下載完成
			try {
				HttpURLConnection http = (HttpURLConnection) downUrl.openConnection();	//開啟HttpURLConnection連線
				http.setConnectTimeout(5 * 1000);	//設定連線逾時時間為5秒鍾
				http.setRequestMethod("GET");	//設定請求的方法為GET
				http.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");	//設定用戶端可以接受的傳回資料型態
				http.setRequestProperty("Accept-Language", "zh-CN");	//設定用戶端使用的語系問中文
				http.setRequestProperty("Referer", downUrl.toString()); 	//設定請求的來源，便於對存取來源進行統計
				http.setRequestProperty("Charset", "UTF-8");	//設定通訊解碼為UTF-8
				int startPos = block * (threadId - 1) + downloadedLength;//開始位置
				int endPos = block * threadId -1;//結束位置
				http.setRequestProperty("Range", "bytes=" + startPos + "-"+ endPos);//設定取得實體資料的範圍,若果超過了實體資料的大小會自動傳回實際的資料大小
				http.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");	//用戶端使用者代理
				http.setRequestProperty("Connection", "Keep-Alive");	//使用長連線
				
				InputStream inStream = http.getInputStream();	//取得遠端連線的輸入流
				byte[] buffer = new byte[1024];	//設定本機資料快取的大小為1M
				int offset = 0;	//設定每次讀取的資料量
				print("Thread " + this.threadId + " starts to download from position "+ startPos);	//列印該執行緒開始下載的位置
				RandomAccessFile threadFile = new RandomAccessFile(this.saveFile, "rwd");	//If the file does not already exist then an attempt will be made to create it and it require that every update to the file's content be written synchronously to the underlying storage device. 
				threadFile.seek(startPos);	//檔案指標指向開始下載的位置
				while (!downloader.getExited() && (offset = inStream.read(buffer, 0, 1024)) != -1) {	//但使用者沒有要求停止下載，同時沒有到達請求資料的尾端時候會繼續迴圈讀取資料
					threadFile.write(buffer, 0, offset);	//直接把資料寫到檔案中
					downloadedLength += offset;	//把新下載的已經寫到檔案中的資料加入到下載長度中
					downloader.update(this.threadId, downloadedLength);	//把該執行緒已經下載的資料長度更新到資料庫和記憶體哈希表中
					downloader.append(offset);	//把新下載的資料長度加入到已經下載的資料總長度中
				}//該執行緒下載資料完畢或是下載被使用者停止
				threadFile.close();	//Closes this random access file stream and releases any system resources associated with the stream.
				inStream.close();	//Concrete implementations of this class should free any resources during close
				if(downloader.getExited())
				{
					print("Thread " + this.threadId + " has been paused");
				}
				else
				{
					print("Thread " + this.threadId + " download finish");
				}
				
				this.finished = true;	//設定完成標志為true，無論是下載完成還是使用者主動中斷下載
			} catch (Exception e) {	//出現例外
				this.downloadedLength = -1;	//設定該執行緒已經下載的長度為-1
				print("Thread "+ this.threadId+ ":"+ e);	//列印出例外訊息
			}
		}
	}
	/**
	 * 列印訊息
	 * @param msg	訊息
	 */
	private static void print(String msg){
		Log.i(TAG, msg);	//使用Logcat的Information模式列印訊息
	}
	
	/**
	 * 下載是否完成
	 * @return
	 */
	public boolean isFinished() {
		return finished;
	}
	
	/**
	 * 已經下載的內容大小
	 * @return 若果傳回值為-1,代表下載失敗
	 */
	public long getDownloadedLength() {
		return downloadedLength;
	}
}


