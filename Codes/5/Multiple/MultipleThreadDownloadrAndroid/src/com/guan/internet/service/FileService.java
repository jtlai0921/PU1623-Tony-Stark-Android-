package com.guan.internet.service;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * �~��Bean�A��{���ƪ��ʧ@
 *
 */
public class FileService {
	private DBOpenHelper openHelper;	//�ŧi��Ʈw�޲z��

	public FileService(Context context) {
		openHelper = new DBOpenHelper(context);	//�ھڤW�U�媫��רҤƸ�Ʈw�޲z��
	}
	/**
	 * ���o�S�wURI���C��������w�g�U�����ɮת���
	 * @param path
	 * @return
	 */
	public Map<Integer, Integer> getData(String path){
		SQLiteDatabase db = openHelper.getReadableDatabase();	//���oŪ������Ʈw����X�A�@�뱡�p�U�b�Ӱʧ@�����w��{����Ǧ^�����O�g�J����Ʈw����X
		Cursor cursor = db.rawQuery("select threadid, downlength from filedownlog where downpath=?", new String[]{path});	//�ھڤU�����|�d�ߩҦ�������U����ơA�Ǧ^��Cursor���V�Ĥ@���O�����e
		Map<Integer, Integer> data = new HashMap<Integer, Integer>();	//�إߤ@�ӫ��ƪ�Ω�s��C����������w�g�U�����ɮת���
		while(cursor.moveToNext()){	//�q�Ĥ@���O���}�l�}�l�ˬdCursor����
			data.put(cursor.getInt(0), cursor.getInt(1));	//������id�M�Ӱ�����w�U�������׳]�w�idata���ƪ�
			data.put(cursor.getInt(cursor.getColumnIndexOrThrow("threadid")), cursor.getInt(cursor.getColumnIndexOrThrow("downlength")));
		}
		cursor.close();	//����cursor�A����귽
		db.close();	//������Ʈw
		return data;	//�Ǧ^��o���C��������M�C����������U������
	}
	/**
	 * �x�s�C��������w�g�U�����ɮת���
	 * @param path	�U�������|
	 * @param map �{�b��id�M�w�g�U�������ת����X
	 */
	public void save(String path,  Map<Integer, Integer> map){
		SQLiteDatabase db = openHelper.getWritableDatabase();	//���o�g�J����Ʈw����X
		db.beginTransaction();	//�}�l����A�]�����B�n���J�h����
		try{
			for(Map.Entry<Integer, Integer> entry : map.entrySet()){	//����For-Each���Ҧ��ˬd��ƶ��X
				db.execSQL("insert into filedownlog(downpath, threadid, downlength) values(?,?,?)",
						new Object[]{path, entry.getKey(), entry.getValue()});	//���J�S�w�U�����|�S�w�����ID�w�g�U�������
			}
			db.setTransactionSuccessful();	//�]�w������檺�ЧӬ����\
		}finally{	//���������{���X�֩w�O�Q���檺�A�Y�G����������������
			db.endTransaction();	//�����@�ӥ���A�Y�G����]�ߤF���\�ЧӡA�h�ǰe����A�_�h�|�u���
		}
		db.close();	//������Ʈw�A�������p�귽
	}
	/**
	 * ��ɧ�s�C��������w�g�U�����ɮת���
	 * @param path
	 * @param map
	 */
	public void update(String path, int threadId, int pos){
		SQLiteDatabase db = openHelper.getWritableDatabase();	//���o�g�J����Ʈw����X
		db.execSQL("update filedownlog set downlength=? where downpath=? and threadid=?",
				new Object[]{pos, path, threadId});	//��s�S�w�U�����|�U�S�w������w�g�U�����ɮת���
		db.close();	//������Ʈw�A�������p���귽
	}
	/**
	 * ���ɮפU��������A�����������U���O��
	 * @param path
	 */
	public void delete(String path){
		SQLiteDatabase db = openHelper.getWritableDatabase();	//���o�g�J����Ʈw����X
		db.execSQL("delete from filedownlog where downpath=?", new Object[]{path});	//�����S�w�U�����|���Ҧ�������O��
		db.close();	//������Ʈw�A����귽
	}
	
}
