package com.guan.internet.service;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * 業務Bean，實現對資料的動作
 *
 */
public class FileService {
	private DBOpenHelper openHelper;	//宣告資料庫管理器

	public FileService(Context context) {
		openHelper = new DBOpenHelper(context);	//根據上下文物件案例化資料庫管理器
	}
	/**
	 * 取得特定URI的每條執行緒已經下載的檔案長度
	 * @param path
	 * @return
	 */
	public Map<Integer, Integer> getData(String path){
		SQLiteDatabase db = openHelper.getReadableDatabase();	//取得讀取的資料庫控制碼，一般情況下在該動作的內定實現中其傳回的其實是寫入的資料庫控制碼
		Cursor cursor = db.rawQuery("select threadid, downlength from filedownlog where downpath=?", new String[]{path});	//根據下載路徑查詢所有執行緒下載資料，傳回的Cursor指向第一條記錄之前
		Map<Integer, Integer> data = new HashMap<Integer, Integer>();	//建立一個哈希表用於存放每條執行緒的已經下載的檔案長度
		while(cursor.moveToNext()){	//從第一條記錄開始開始檢查Cursor物件
			data.put(cursor.getInt(0), cursor.getInt(1));	//把執行緒id和該執行緒已下載的長度設定進data哈希表中
			data.put(cursor.getInt(cursor.getColumnIndexOrThrow("threadid")), cursor.getInt(cursor.getColumnIndexOrThrow("downlength")));
		}
		cursor.close();	//關閉cursor，釋放資源
		db.close();	//關閉資料庫
		return data;	//傳回獲得的每條執行緒和每條執行緒的下載長度
	}
	/**
	 * 儲存每條執行緒已經下載的檔案長度
	 * @param path	下載的路徑
	 * @param map 現在的id和已經下載的長度的集合
	 */
	public void save(String path,  Map<Integer, Integer> map){
		SQLiteDatabase db = openHelper.getWritableDatabase();	//取得寫入的資料庫控制碼
		db.beginTransaction();	//開始交易，因為此處要插入多批資料
		try{
			for(Map.Entry<Integer, Integer> entry : map.entrySet()){	//采用For-Each的模式檢查資料集合
				db.execSQL("insert into filedownlog(downpath, threadid, downlength) values(?,?,?)",
						new Object[]{path, entry.getKey(), entry.getValue()});	//插入特定下載路徑特定執行緒ID已經下載的資料
			}
			db.setTransactionSuccessful();	//設定交易執行的標志為成功
		}finally{	//此部分的程式碼肯定是被執行的，若果不殺死虛擬機的話
			db.endTransaction();	//結束一個交易，若果交易設立了成功標志，則傳送交易，否則會滾交易
		}
		db.close();	//關閉資料庫，釋放關聯資源
	}
	/**
	 * 實時更新每條執行緒已經下載的檔案長度
	 * @param path
	 * @param map
	 */
	public void update(String path, int threadId, int pos){
		SQLiteDatabase db = openHelper.getWritableDatabase();	//取得寫入的資料庫控制碼
		db.execSQL("update filedownlog set downlength=? where downpath=? and threadid=?",
				new Object[]{pos, path, threadId});	//更新特定下載路徑下特定執行緒已經下載的檔案長度
		db.close();	//關閉資料庫，釋放關聯的資源
	}
	/**
	 * 當檔案下載完成後，移除對應的下載記錄
	 * @param path
	 */
	public void delete(String path){
		SQLiteDatabase db = openHelper.getWritableDatabase();	//取得寫入的資料庫控制碼
		db.execSQL("delete from filedownlog where downpath=?", new Object[]{path});	//移除特定下載路徑的所有執行緒記錄
		db.close();	//關閉資料庫，釋放資源
	}
	
}
