package com.guan.internet.service.download;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.util.Log;

import com.guan.internet.service.FileService;

public class FileDownloader {
	private static final String TAG = "FileDownloader";	//設定標簽，方便Logcat日志記錄
	private static final int RESPONSEOK = 200;	//響應碼為200，即存取成功
	private Context context;	//套用程式的上下文物件
	private FileService fileService;	//取得本機資料庫的業務Bean
	private boolean exited;	//停止下載標志
	private int downloadedSize = 0;	//已下載檔案長度
	private int fileSize = 0;	//原始檔案長度
	private DownloadThread[] threads;	//根據執行緒數設定下載執行緒池
	private File saveFile;	//資料儲存到的本機檔案
	private Map<Integer, Integer> data = new ConcurrentHashMap<Integer, Integer>();	//快取各執行緒下載的長度
	private int block;	//每條執行緒下載的長度
	private String downloadUrl;	//下載路徑
	
	/**
	 * 取得執行緒數
	 */
	public int getThreadSize() {
		return threads.length;	//根據陣列長度傳回執行緒數
	}
	
	/**
	 * 離開下載
	 */
	public void exit(){
		this.exited = true;	//設定離開標志為true
	}
	public boolean getExited(){
		return this.exited;
	}
	/**
	 * 取得檔案大小
	 * @return
	 */
	public int getFileSize() {
		return fileSize;	//從類別成員變數中取得下載檔案的大小
	}
	
	/**
	 * 累計已下載大小
	 * @param size
	 */
	protected synchronized void append(int size) {	//使用同步關鍵字解決平行存取問題
		downloadedSize += size;	//把實時下載的長度加入到總下載長度中
	}
	
	/**
	 * 更新指定執行緒最後下載的位置
	 * @param threadId 執行緒id
	 * @param pos 最後下載的位置
	 */
	protected synchronized void update(int threadId, int pos) {
		this.data.put(threadId, pos);	//把制定執行緒ID的執行緒賦予最新的下載長度，以前的值會被覆蓋掉
		this.fileService.update(this.downloadUrl, threadId, pos);	//更新資料庫中指定執行緒的下載長度
	}
	/**
	 * 建構檔案下載器
	 * @param downloadUrl 下載路徑
	 * @param fileSaveDir 檔案儲存目錄
	 * @param threadNum 下載執行緒數
	 */
	public FileDownloader(Context context, String downloadUrl, File fileSaveDir, int threadNum) {
		try {
			this.context = context;	//對上下文物件給予值
			this.downloadUrl = downloadUrl;	//對下載的路徑給予值
			fileService = new FileService(this.context);	//案例化資料動作業務Bean，此處需要使用Context，因為此處的資料庫是套用程式私有
			URL url = new URL(this.downloadUrl);	//根據下載路徑案例化URL
			if(!fileSaveDir.exists()) fileSaveDir.mkdirs();	//若果特殊的檔案不存在，則建立目錄，此處可以建立多層目錄
			this.threads = new DownloadThread[threadNum];	//根據下載的執行緒數建立下載執行緒池				
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();	//建立一個遠端連線控制碼，此時尚未真正連線
			conn.setConnectTimeout(5*1000);	//設定連線逾時時間為5秒
			conn.setRequestMethod("GET");	//設定請求模式為GET
			conn.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");	//設定用戶端可以接受的媒體型態
			conn.setRequestProperty("Accept-Language", "zh-CN");	//設定用戶端語系
			conn.setRequestProperty("Referer", downloadUrl); 	//設定請求的來源頁面，便於服務端進行來源統計
			conn.setRequestProperty("Charset", "UTF-8");	//設定用戶端解碼
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");	//設定使用者代理
			conn.setRequestProperty("Connection", "Keep-Alive");	//設定Connection的模式
			conn.connect();	//和遠端資源建立真正的連線，但尚無傳回的資料流
			printResponseHeader(conn);	//答應傳回的HTTP表頭字段集合
			if (conn.getResponseCode()==RESPONSEOK) {	//此處的請求會開啟傳回流並取得傳回的狀態碼，用於檢查是否請求成功，當傳回碼為200時執行下面的程式碼
				this.fileSize = conn.getContentLength();//根據響應取得檔案大小
				if (this.fileSize <= 0) throw new RuntimeException("Unkown file size ");	//當檔案大小為小於等於零時拋出執行時例外
						
				String filename = getFileName(conn);//取得檔名稱	
				this.saveFile = new File(fileSaveDir, filename);//根據檔案儲存目錄和檔名建構儲存檔案
				Map<Integer, Integer> logdata = fileService.getData(downloadUrl);//取得下載記錄
				
				if(logdata.size()>0){//若果存在下載記錄
					for(Map.Entry<Integer, Integer> entry : logdata.entrySet())	//檢查集合中的資料
						data.put(entry.getKey(), entry.getValue());//把各條執行緒已經下載的資料長度放入data中
				}
				
				if(this.data.size()==this.threads.length){//若果已經下載的資料的執行緒數和現在設定的執行緒數相同時則計算所有執行緒已經下載的資料總長度
					for (int i = 0; i < this.threads.length; i++) {	//檢查每條執行緒已經下載的資料
						this.downloadedSize += this.data.get(i+1);	//計算已經下載的資料之和
					}
					print("已經下載的長度"+ this.downloadedSize + "個位元組");	//列印出已經下載的資料總和
				}

				this.block = (this.fileSize % this.threads.length)==0? this.fileSize / this.threads.length : this.fileSize / this.threads.length + 1;	//計算每條執行緒下載的資料長度
			}else{
				print("伺服器響應錯誤:" + conn.getResponseCode() + conn.getResponseMessage());	//列印錯誤
				throw new RuntimeException("server response error ");	//拋出執行時伺服器傳回例外
			}
		} catch (Exception e) {
			print(e.toString());	//列印錯誤
			throw new RuntimeException("Can't connection this url");	//拋出執行時無法連線的例外
		}
	}
	/**
	 * 取得檔名
	 */
	private String getFileName(HttpURLConnection conn) {
		String filename = this.downloadUrl.substring(this.downloadUrl.lastIndexOf('/') + 1);	//從下載路徑的字串中取得檔名稱
		
		if(filename==null || "".equals(filename.trim())){//若果取得不到檔名稱
			for (int i = 0;; i++) {	//無限循環檢查
				String mine = conn.getHeaderField(i);	//從傳回的流中取得特定索引的頭字段值
				if (mine == null) break;	//若果檢查到了傳回頭尾端這離開循環
				if("content-disposition".equals(conn.getHeaderFieldKey(i).toLowerCase())){	//取得content-disposition傳回頭字段，裡面可能會包括檔名
					Matcher m = Pattern.compile(".*filename=(.*)").matcher(mine.toLowerCase());	//使用正規表示法查詢檔名
					if(m.find()) return m.group(1);	//若果有符合正則表達規則的字串
				}
			}
			filename = UUID.randomUUID()+ ".tmp";//由網路卡上的標誌數字(每個網路卡都有唯一的標誌號)以及 CPU 時鍾的唯一數字產生的的一個 16 位元組的二進位作為檔名
		}
		return filename;
	}
	
	/**
	 *  開始下載檔案
	 * @param listener 監聽下載數量的變化,若果不需要了解實時下載的數量,可以設定為null
	 * @return 已下載檔案大小
	 * @throws Exception
	 */
	public int download(DownloadProgressListener listener) throws Exception{	//進行下載，並拋出例外給呼叫者，若果有例外的話
		try {
			RandomAccessFile randOut = new RandomAccessFile(this.saveFile, "rwd");	//The file is opened for reading and writing. Every change of the file's content must be written synchronously to the target device.
			if(this.fileSize>0) randOut.setLength(this.fileSize);	//設定檔案的大小
			randOut.close();	//關閉該檔案，使設定生效
			URL url = new URL(this.downloadUrl);	//A URL instance specifies the location of a resource on the internet as specified by RFC 1738
			if(this.data.size() != this.threads.length){	//若果原先未曾下載或是原先的下載執行緒數與現在的執行緒數不一致
				this.data.clear();	//Removes all elements from this Map, leaving it empty.
				for (int i = 0; i < this.threads.length; i++) {	//檢查執行緒池
					this.data.put(i+1, 0);//起始化每條執行緒已經下載的資料長度為0
				}
				this.downloadedSize = 0;	//設定已經下載的長度為0
			}
			for (int i = 0; i < this.threads.length; i++) {//開啟執行緒進行下載
				int downloadedLength = this.data.get(i+1);	//透過特定的執行緒ID取得該執行緒已經下載的資料長度
				if(downloadedLength < this.block && this.downloadedSize < this.fileSize){//判斷執行緒是否已經完成下載,否則繼續下載	
					this.threads[i] = new DownloadThread(this, url, this.saveFile, this.block, this.data.get(i+1), i+1);	//起始化特定id的執行緒
					this.threads[i].setPriority(7);	//設定執行緒的優先級，Thread.NORM_PRIORITY = 5 Thread.MIN_PRIORITY = 1 Thread.MAX_PRIORITY = 10
					this.threads[i].start();	//啟動執行緒
				}else{
					this.threads[i] = null;	//表明線上程已經完成下載工作
				}
			}
			fileService.delete(this.downloadUrl);	//若果存在下載記錄，移除它們，然後重新加入
			fileService.save(this.downloadUrl, this.data);	//把已經下載的實時資料寫入資料庫
			boolean notFinished = true;//下載未完成
			while (notFinished) {// 循環判斷所有執行緒是否完成下載
				Thread.sleep(900);
				notFinished = false;//假設全部執行緒下載完成
				for (int i = 0; i < this.threads.length; i++){
					if (this.threads[i] != null && !this.threads[i].isFinished()) {//若果發現執行緒未完成下載
						notFinished = true;//設定標志為下載沒有完成
						if(this.threads[i].getDownloadedLength() == -1){//若果下載失敗,再重新在已經下載的資料長度的基礎上下載
							this.threads[i] = new DownloadThread(this, url, this.saveFile, this.block, this.data.get(i+1), i+1);	//重新開闢下載執行緒
							this.threads[i].setPriority(7);	//設定下載的優先級
							this.threads[i].start();	//開始下載執行緒
						}
					}
				}				
				if(listener!=null) listener.onDownloadSize(this.downloadedSize);//知會目前已經下載完成的資料長度
			}
			if(downloadedSize == this.fileSize) fileService.delete(this.downloadUrl);//下載完成移除記錄
		} catch (Exception e) {
			print(e.toString());	//列印錯誤
			throw new Exception("File downloads error");	//拋出檔案下載例外
		}
		return this.downloadedSize;
	}
	/**
	 * 取得Http響應頭字段
	 * @param http	HttpURLConnection物件
	 * @return	傳回頭字段的LinkedHashMap
	 */
	public static Map<String, String> getHttpResponseHeader(HttpURLConnection http) {
		Map<String, String> header = new LinkedHashMap<String, String>();	//使用LinkedHashMap確保寫入和檢查的時候的順序相同，而且容許空值存在
		for (int i = 0;; i++) {	//此處為無限循環，因為不知道頭字段的數量
			String fieldValue = http.getHeaderField(i);	//getHeaderField(int n)用於傳回 第n個頭字段的值。

			if (fieldValue == null) break;	//若果第i個字段沒有值了，則表明頭字段部分已經循環完畢，此處使用Break離開循環
			header.put(http.getHeaderFieldKey(i), fieldValue);	//getHeaderFieldKey(int n)用於傳回 第n個頭字段的鍵。
		}
		return header;
	}
	/**
	 * 列印Http頭字段
	 * @param http HttpURLConnection物件
	 */
	public static void printResponseHeader(HttpURLConnection http){
		Map<String, String> header = getHttpResponseHeader(http);	//取得Http響應頭字段
		for(Map.Entry<String, String> entry : header.entrySet()){	//使用For-Each循環的模式檢查取得的頭字段的值，此時檢查的循序和輸入的順序相同
			String key = entry.getKey()!=null ? entry.getKey()+ ":" : "";	//當有鍵的時候這取得鍵，若果沒有則為空字串
			print(key+ entry.getValue());	//答應鍵和值的群組合
		}
	}
	
	/**
	 * 列印訊息
	 * @param msg	訊息字串
	 */
	private static void print(String msg){
		Log.i(TAG, msg);	//使用LogCat的Information模式列印訊息
	}
}
