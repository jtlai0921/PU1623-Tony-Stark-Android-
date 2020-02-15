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
	private static final String TAG = "FileDownloader";	//�]�w��ñ�A��KLogcat��ӰO��
	private static final int RESPONSEOK = 200;	//�T���X��200�A�Y�s�����\
	private Context context;	//�M�ε{�����W�U�媫��
	private FileService fileService;	//���o������Ʈw���~��Bean
	private boolean exited;	//����U���Ч�
	private int downloadedSize = 0;	//�w�U���ɮת���
	private int fileSize = 0;	//��l�ɮת���
	private DownloadThread[] threads;	//�ھڰ�����Ƴ]�w�U���������
	private File saveFile;	//����x�s�쪺�����ɮ�
	private Map<Integer, Integer> data = new ConcurrentHashMap<Integer, Integer>();	//�֨��U������U��������
	private int block;	//�C��������U��������
	private String downloadUrl;	//�U�����|
	
	/**
	 * ���o�������
	 */
	public int getThreadSize() {
		return threads.length;	//�ھڰ}�C���׶Ǧ^�������
	}
	
	/**
	 * ���}�U��
	 */
	public void exit(){
		this.exited = true;	//�]�w���}�ЧӬ�true
	}
	public boolean getExited(){
		return this.exited;
	}
	/**
	 * ���o�ɮפj�p
	 * @return
	 */
	public int getFileSize() {
		return fileSize;	//�q���O�����ܼƤ����o�U���ɮת��j�p
	}
	
	/**
	 * �֭p�w�U���j�p
	 * @param size
	 */
	protected synchronized void append(int size) {	//�ϥΦP�B����r�ѨM����s�����D
		downloadedSize += size;	//���ɤU�������ץ[�J���`�U�����פ�
	}
	
	/**
	 * ��s���w������̫�U������m
	 * @param threadId �����id
	 * @param pos �̫�U������m
	 */
	protected synchronized void update(int threadId, int pos) {
		this.data.put(threadId, pos);	//���w�����ID��������ᤩ�̷s���U�����סA�H�e���ȷ|�Q�л\��
		this.fileService.update(this.downloadUrl, threadId, pos);	//��s��Ʈw�����w��������U������
	}
	/**
	 * �غc�ɮפU����
	 * @param downloadUrl �U�����|
	 * @param fileSaveDir �ɮ��x�s�ؿ�
	 * @param threadNum �U���������
	 */
	public FileDownloader(Context context, String downloadUrl, File fileSaveDir, int threadNum) {
		try {
			this.context = context;	//��W�U�媫�󵹤���
			this.downloadUrl = downloadUrl;	//��U�������|������
			fileService = new FileService(this.context);	//�רҤƸ�ưʧ@�~��Bean�A���B�ݭn�ϥ�Context�A�]�����B����Ʈw�O�M�ε{���p��
			URL url = new URL(this.downloadUrl);	//�ھڤU�����|�רҤ�URL
			if(!fileSaveDir.exists()) fileSaveDir.mkdirs();	//�Y�G�S���ɮפ��s�b�A�h�إߥؿ��A���B�i�H�إߦh�h�ؿ�
			this.threads = new DownloadThread[threadNum];	//�ھڤU����������ƫإߤU���������				
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();	//�إߤ@�ӻ��ݳs�u����X�A���ɩ|���u���s�u
			conn.setConnectTimeout(5*1000);	//�]�w�s�u�O�ɮɶ���5��
			conn.setRequestMethod("GET");	//�]�w�ШD�Ҧ���GET
			conn.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");	//�]�w�Τ�ݥi�H�������C�髬�A
			conn.setRequestProperty("Accept-Language", "zh-CN");	//�]�w�Τ�ݻy�t
			conn.setRequestProperty("Referer", downloadUrl); 	//�]�w�ШD���ӷ������A�K��A�Ⱥݶi��ӷ��έp
			conn.setRequestProperty("Charset", "UTF-8");	//�]�w�Τ�ݸѽX
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");	//�]�w�ϥΪ̥N�z
			conn.setRequestProperty("Connection", "Keep-Alive");	//�]�wConnection���Ҧ�
			conn.connect();	//�M���ݸ귽�إ߯u�����s�u�A���|�L�Ǧ^����Ƭy
			printResponseHeader(conn);	//�����Ǧ^��HTTP���Y�r�q���X
			if (conn.getResponseCode()==RESPONSEOK) {	//���B���ШD�|�}�ҶǦ^�y�è��o�Ǧ^�����A�X�A�Ω��ˬd�O�_�ШD���\�A��Ǧ^�X��200�ɰ���U�����{���X
				this.fileSize = conn.getContentLength();//�ھ��T�����o�ɮפj�p
				if (this.fileSize <= 0) throw new RuntimeException("Unkown file size ");	//���ɮפj�p���p�󵥩�s�ɩߥX����ɨҥ~
						
				String filename = getFileName(conn);//���o�ɦW��	
				this.saveFile = new File(fileSaveDir, filename);//�ھ��ɮ��x�s�ؿ��M�ɦW�غc�x�s�ɮ�
				Map<Integer, Integer> logdata = fileService.getData(downloadUrl);//���o�U���O��
				
				if(logdata.size()>0){//�Y�G�s�b�U���O��
					for(Map.Entry<Integer, Integer> entry : logdata.entrySet())	//�ˬd���X�������
						data.put(entry.getKey(), entry.getValue());//��U��������w�g�U������ƪ��ש�Jdata��
				}
				
				if(this.data.size()==this.threads.length){//�Y�G�w�g�U������ƪ�������ƩM�{�b�]�w��������ƬۦP�ɫh�p��Ҧ�������w�g�U��������`����
					for (int i = 0; i < this.threads.length; i++) {	//�ˬd�C��������w�g�U�������
						this.downloadedSize += this.data.get(i+1);	//�p��w�g�U������Ƥ��M
					}
					print("�w�g�U��������"+ this.downloadedSize + "�Ӧ줸��");	//�C�L�X�w�g�U��������`�M
				}

				this.block = (this.fileSize % this.threads.length)==0? this.fileSize / this.threads.length : this.fileSize / this.threads.length + 1;	//�p��C��������U������ƪ���
			}else{
				print("���A���T�����~:" + conn.getResponseCode() + conn.getResponseMessage());	//�C�L���~
				throw new RuntimeException("server response error ");	//�ߥX����ɦ��A���Ǧ^�ҥ~
			}
		} catch (Exception e) {
			print(e.toString());	//�C�L���~
			throw new RuntimeException("Can't connection this url");	//�ߥX����ɵL�k�s�u���ҥ~
		}
	}
	/**
	 * ���o�ɦW
	 */
	private String getFileName(HttpURLConnection conn) {
		String filename = this.downloadUrl.substring(this.downloadUrl.lastIndexOf('/') + 1);	//�q�U�����|���r�ꤤ���o�ɦW��
		
		if(filename==null || "".equals(filename.trim())){//�Y�G���o�����ɦW��
			for (int i = 0;; i++) {	//�L���`���ˬd
				String mine = conn.getHeaderField(i);	//�q�Ǧ^���y�����o�S�w���ު��Y�r�q��
				if (mine == null) break;	//�Y�G�ˬd��F�Ǧ^�Y���ݳo���}�`��
				if("content-disposition".equals(conn.getHeaderFieldKey(i).toLowerCase())){	//���ocontent-disposition�Ǧ^�Y�r�q�A�̭��i��|�]�A�ɦW
					Matcher m = Pattern.compile(".*filename=(.*)").matcher(mine.toLowerCase());	//�ϥΥ��W��ܪk�d���ɦW
					if(m.find()) return m.group(1);	//�Y�G���ŦX���h��F�W�h���r��
				}
			}
			filename = UUID.randomUUID()+ ".tmp";//�Ѻ����d�W���лx�Ʀr(�C�Ӻ����d�����ߤ@���лx��)�H�� CPU ���骺�ߤ@�Ʀr���ͪ����@�� 16 �줸�ժ��G�i��@���ɦW
		}
		return filename;
	}
	
	/**
	 *  �}�l�U���ɮ�
	 * @param listener ��ť�U���ƶq���ܤ�,�Y�G���ݭn�F�ѹ�ɤU�����ƶq,�i�H�]�w��null
	 * @return �w�U���ɮפj�p
	 * @throws Exception
	 */
	public int download(DownloadProgressListener listener) throws Exception{	//�i��U���A�éߥX�ҥ~���I�s�̡A�Y�G���ҥ~����
		try {
			RandomAccessFile randOut = new RandomAccessFile(this.saveFile, "rwd");	//The file is opened for reading and writing. Every change of the file's content must be written synchronously to the target device.
			if(this.fileSize>0) randOut.setLength(this.fileSize);	//�]�w�ɮת��j�p
			randOut.close();	//�������ɮסA�ϳ]�w�ͮ�
			URL url = new URL(this.downloadUrl);	//A URL instance specifies the location of a resource on the internet as specified by RFC 1738
			if(this.data.size() != this.threads.length){	//�Y�G��������U���άO������U��������ƻP�{�b��������Ƥ��@�P
				this.data.clear();	//Removes all elements from this Map, leaving it empty.
				for (int i = 0; i < this.threads.length; i++) {	//�ˬd�������
					this.data.put(i+1, 0);//�_�l�ƨC��������w�g�U������ƪ��׬�0
				}
				this.downloadedSize = 0;	//�]�w�w�g�U�������׬�0
			}
			for (int i = 0; i < this.threads.length; i++) {//�}�Ұ�����i��U��
				int downloadedLength = this.data.get(i+1);	//�z�L�S�w�������ID���o�Ӱ�����w�g�U������ƪ���
				if(downloadedLength < this.block && this.downloadedSize < this.fileSize){//�P�_������O�_�w�g�����U��,�_�h�~��U��	
					this.threads[i] = new DownloadThread(this, url, this.saveFile, this.block, this.data.get(i+1), i+1);	//�_�l�ƯS�wid�������
					this.threads[i].setPriority(7);	//�]�w��������u���šAThread.NORM_PRIORITY = 5 Thread.MIN_PRIORITY = 1 Thread.MAX_PRIORITY = 10
					this.threads[i].start();	//�Ұʰ����
				}else{
					this.threads[i] = null;	//����u�W�{�w�g�����U���u�@
				}
			}
			fileService.delete(this.downloadUrl);	//�Y�G�s�b�U���O���A�������̡A�M�᭫�s�[�J
			fileService.save(this.downloadUrl, this.data);	//��w�g�U������ɸ�Ƽg�J��Ʈw
			boolean notFinished = true;//�U��������
			while (notFinished) {// �`���P�_�Ҧ�������O�_�����U��
				Thread.sleep(900);
				notFinished = false;//���]����������U������
				for (int i = 0; i < this.threads.length; i++){
					if (this.threads[i] != null && !this.threads[i].isFinished()) {//�Y�G�o�{������������U��
						notFinished = true;//�]�w�ЧӬ��U���S������
						if(this.threads[i].getDownloadedLength() == -1){//�Y�G�U������,�A���s�b�w�g�U������ƪ��ת���¦�W�U��
							this.threads[i] = new DownloadThread(this, url, this.saveFile, this.block, this.data.get(i+1), i+1);	//���s�}�P�U�������
							this.threads[i].setPriority(7);	//�]�w�U�����u����
							this.threads[i].start();	//�}�l�U�������
						}
					}
				}				
				if(listener!=null) listener.onDownloadSize(this.downloadedSize);//���|�ثe�w�g�U����������ƪ���
			}
			if(downloadedSize == this.fileSize) fileService.delete(this.downloadUrl);//�U�����������O��
		} catch (Exception e) {
			print(e.toString());	//�C�L���~
			throw new Exception("File downloads error");	//�ߥX�ɮפU���ҥ~
		}
		return this.downloadedSize;
	}
	/**
	 * ���oHttp�T���Y�r�q
	 * @param http	HttpURLConnection����
	 * @return	�Ǧ^�Y�r�q��LinkedHashMap
	 */
	public static Map<String, String> getHttpResponseHeader(HttpURLConnection http) {
		Map<String, String> header = new LinkedHashMap<String, String>();	//�ϥ�LinkedHashMap�T�O�g�J�M�ˬd���ɭԪ����ǬۦP�A�ӥB�e�\�ŭȦs�b
		for (int i = 0;; i++) {	//���B���L���`���A�]�������D�Y�r�q���ƶq
			String fieldValue = http.getHeaderField(i);	//getHeaderField(int n)�Ω�Ǧ^ ��n���Y�r�q���ȡC

			if (fieldValue == null) break;	//�Y�G��i�Ӧr�q�S���ȤF�A�h����Y�r�q�����w�g�`�������A���B�ϥ�Break���}�`��
			header.put(http.getHeaderFieldKey(i), fieldValue);	//getHeaderFieldKey(int n)�Ω�Ǧ^ ��n���Y�r�q����C
		}
		return header;
	}
	/**
	 * �C�LHttp�Y�r�q
	 * @param http HttpURLConnection����
	 */
	public static void printResponseHeader(HttpURLConnection http){
		Map<String, String> header = getHttpResponseHeader(http);	//���oHttp�T���Y�r�q
		for(Map.Entry<String, String> entry : header.entrySet()){	//�ϥ�For-Each�`�����Ҧ��ˬd���o���Y�r�q���ȡA�����ˬd���`�ǩM��J�����ǬۦP
			String key = entry.getKey()!=null ? entry.getKey()+ ":" : "";	//���䪺�ɭԳo���o��A�Y�G�S���h���Ŧr��
			print(key+ entry.getValue());	//������M�Ȫ��s�զX
		}
	}
	
	/**
	 * �C�L�T��
	 * @param msg	�T���r��
	 */
	private static void print(String msg){
		Log.i(TAG, msg);	//�ϥ�LogCat��Information�Ҧ��C�L�T��
	}
}
