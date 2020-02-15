package com.guan.internet.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite管理器，實現建立資料庫和表，但版本變化時實現對表的資料庫表的動作
 *
 */
public class DBOpenHelper extends SQLiteOpenHelper {
	private static final String DBNAME = "eric.db";	//設定資料庫的名稱
	private static final int VERSION = 1;	//設定資料庫的版本
	
	/**
	 * 透過建構方法
	 * @param context 套用程式的上下文物件
	 */
	public DBOpenHelper(Context context) {
		super(context, DBNAME, null, VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {	//建立資料表
		db.execSQL("CREATE TABLE IF NOT EXISTS filedownlog (id integer primary key autoincrement, downpath varchar(100), threadid INTEGER, downlength INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {	//當版本變化時系統會呼叫該回調方法
		db.execSQL("DROP TABLE IF EXISTS filedownlog");	//此處是移除資料表，在實際的業務中一般是需要資料備份的
		onCreate(db);	//呼叫onCreate方法重新建立資料表，也可以自己根據業務需要建立新的的資料表
	}

}

