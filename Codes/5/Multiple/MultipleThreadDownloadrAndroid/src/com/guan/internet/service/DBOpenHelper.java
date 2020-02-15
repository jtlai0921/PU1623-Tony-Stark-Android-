package com.guan.internet.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite�޲z���A��{�إ߸�Ʈw�M��A�������ܤƮɹ�{�����Ʈw���ʧ@
 *
 */
public class DBOpenHelper extends SQLiteOpenHelper {
	private static final String DBNAME = "eric.db";	//�]�w��Ʈw���W��
	private static final int VERSION = 1;	//�]�w��Ʈw������
	
	/**
	 * �z�L�غc��k
	 * @param context �M�ε{�����W�U�媫��
	 */
	public DBOpenHelper(Context context) {
		super(context, DBNAME, null, VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {	//�إ߸�ƪ�
		db.execSQL("CREATE TABLE IF NOT EXISTS filedownlog (id integer primary key autoincrement, downpath varchar(100), threadid INTEGER, downlength INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {	//�����ܤƮɨt�η|�I�s�Ӧ^�դ�k
		db.execSQL("DROP TABLE IF EXISTS filedownlog");	//���B�O������ƪ�A�b��ڪ��~�Ȥ��@��O�ݭn��Ƴƥ���
		onCreate(db);	//�I�sonCreate��k���s�إ߸�ƪ�A�]�i�H�ۤv�ھڷ~�Ȼݭn�إ߷s������ƪ�
	}

}

