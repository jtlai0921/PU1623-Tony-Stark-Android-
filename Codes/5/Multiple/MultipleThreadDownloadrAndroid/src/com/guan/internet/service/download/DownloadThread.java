package com.guan.internet.service.download;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

/**
 * �U��������A�ھڹ�ڤU����}�B�O���쪺�ɮסB�U�������j�p�B�w�g�U������Ƥj�p���T���i��U��
 *
 */
public class DownloadThread extends Thread {
	private static final String TAG = "DownloadThread";	//�w�qTAG�A��K��l���C�L��X
	private File saveFile;	//�U��������x�s�쪺�ɮ�
	private URL downUrl;	//�U����URL
	private int block;	//�C��������U�����j�p
	private int threadId = -1;	//�_�l�ư����id�]�w
	private int downloadedLength;	//�Ӱ�����w�g�U������ƪ���
	private boolean finished = false;	//�Ӱ�����O�_�����U�����Ч�
	private FileDownloader downloader;	//�ɮפU����
	
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
		if(downloadedLength < block){//���U������
			try {
				HttpURLConnection http = (HttpURLConnection) downUrl.openConnection();	//�}��HttpURLConnection�s�u
				http.setConnectTimeout(5 * 1000);	//�]�w�s�u�O�ɮɶ���5����
				http.setRequestMethod("GET");	//�]�w�ШD����k��GET
				http.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");	//�]�w�Τ�ݥi�H�������Ǧ^��ƫ��A
				http.setRequestProperty("Accept-Language", "zh-CN");	//�]�w�Τ�ݨϥΪ��y�t�ݤ���
				http.setRequestProperty("Referer", downUrl.toString()); 	//�]�w�ШD���ӷ��A�K���s���ӷ��i��έp
				http.setRequestProperty("Charset", "UTF-8");	//�]�w�q�T�ѽX��UTF-8
				int startPos = block * (threadId - 1) + downloadedLength;//�}�l��m
				int endPos = block * threadId -1;//������m
				http.setRequestProperty("Range", "bytes=" + startPos + "-"+ endPos);//�]�w���o�����ƪ��d��,�Y�G�W�L�F�����ƪ��j�p�|�۰ʶǦ^��ڪ���Ƥj�p
				http.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");	//�Τ�ݨϥΪ̥N�z
				http.setRequestProperty("Connection", "Keep-Alive");	//�ϥΪ��s�u
				
				InputStream inStream = http.getInputStream();	//���o���ݳs�u����J�y
				byte[] buffer = new byte[1024];	//�]�w������Ƨ֨����j�p��1M
				int offset = 0;	//�]�w�C��Ū������ƶq
				print("Thread " + this.threadId + " starts to download from position "+ startPos);	//�C�L�Ӱ�����}�l�U������m
				RandomAccessFile threadFile = new RandomAccessFile(this.saveFile, "rwd");	//If the file does not already exist then an attempt will be made to create it and it require that every update to the file's content be written synchronously to the underlying storage device. 
				threadFile.seek(startPos);	//�ɮ׫��Ы��V�}�l�U������m
				while (!downloader.getExited() && (offset = inStream.read(buffer, 0, 1024)) != -1) {	//���ϥΪ̨S���n�D����U���A�P�ɨS����F�ШD��ƪ����ݮɭԷ|�~��j��Ū�����
					threadFile.write(buffer, 0, offset);	//�������Ƽg���ɮפ�
					downloadedLength += offset;	//��s�U�����w�g�g���ɮפ�����ƥ[�J��U�����פ�
					downloader.update(this.threadId, downloadedLength);	//��Ӱ�����w�g�U������ƪ��ק�s���Ʈw�M�O���髢�ƪ�
					downloader.append(offset);	//��s�U������ƪ��ץ[�J��w�g�U��������`���פ�
				}//�Ӱ�����U����Ƨ����άO�U���Q�ϥΪ̰���
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
				
				this.finished = true;	//�]�w�����ЧӬ�true�A�L�׬O�U�������٬O�ϥΪ̥D�ʤ��_�U��
			} catch (Exception e) {	//�X�{�ҥ~
				this.downloadedLength = -1;	//�]�w�Ӱ�����w�g�U�������׬�-1
				print("Thread "+ this.threadId+ ":"+ e);	//�C�L�X�ҥ~�T��
			}
		}
	}
	/**
	 * �C�L�T��
	 * @param msg	�T��
	 */
	private static void print(String msg){
		Log.i(TAG, msg);	//�ϥ�Logcat��Information�Ҧ��C�L�T��
	}
	
	/**
	 * �U���O�_����
	 * @return
	 */
	public boolean isFinished() {
		return finished;
	}
	
	/**
	 * �w�g�U�������e�j�p
	 * @return �Y�G�Ǧ^�Ȭ�-1,�N��U������
	 */
	public long getDownloadedLength() {
		return downloadedLength;
	}
}


