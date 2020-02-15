
import java.io.*;
import java.net.*;

//�w�q�U���qstart��end�����e�������
class DownThread extends Thread
{
	//�w�q�줸�հ}�C������
	private final int BUFF_LEN = 32;
	//�w�q�U�����_�l�I
	private long begin;
	//�w�q�U���������I
	private long end;
	//�U���귽��������J�y
	private InputStream is;
	//�N�U���쪺�줸�տ�X��mm��
	private RandomAccessFile mm ;

	//�غc���A�ǤJ��J�y�A��X�y�M�U���_�l�I�B�����I
	public DownThread(long start , long end 
		, InputStream is , RandomAccessFile raf)
	{
		//��X�Ӱ�����t�d�U�����줸�զ�m
		System.out.println(start + "---->"  + end);
		this.begin = start;
		this.end = end;
		this.is = is;
		this.mm = raf;
	}
	public void run()
	{
		try
		{
			is.skip(begin);
			mm.seek(begin); 
			//�w�qŪ����J�y���e�����֨��}�C
			byte[] buff = new byte[BUFF_LEN];
			//��������t�d�U���귽���j�p
			long contentLen = end - begin;
			//�w�q�̦h�ݭnŪ���X���N�i�H��������������U��
			long times = contentLen / BUFF_LEN + 4;
			//���Ū�����줸�ռ�
			int hasRead = 0;
			for (int i = 0; i < times ; i++)
			{
				hasRead = is.read(buff);
				//�Y�GŪ�����줸�ռƤp��0�A�h���}�`���I
				if (hasRead < 0)
				{
					break;
				}
				mm.write(buff , 0 , hasRead);
			}			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		//�ϥ�finally�϶��������ثe���������J�y�B��X�y
		finally
		{
			try
			{
				if (is != null)
				{
					is.close();
				}
				if (mm != null)
				{
					mm.close();
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
}
public class down
{
	public static void main(String[] args)
	{
		final int DOWN_THREAD_NUM = 4;
		final String OUT_FILE_NAME = "down.jpg";
		InputStream[] isArr = new InputStream[DOWN_THREAD_NUM];
		RandomAccessFile[] outArr = new RandomAccessFile[DOWN_THREAD_NUM];
		try
		{
			//�إߤ@��URL����
			URL url = new URL("http://hiphotos.baidu.com/"+ "baidu/pic/item/8546bd003af33a8727f50057c65c10385243b566.jpg");
			//�H��URL����}�ҲĤ@�ӿ�J�y
			isArr[0] = url.openStream();
			long fileLen = getFileLength(url);
			System.out.println("�����귽���j�p" + fileLen);
			//�H��X�ɦW�إ߲Ĥ@��RandomAccessFile��X�y
			outArr[0] = new RandomAccessFile(OUT_FILE_NAME , "rw");
			//�إߤ@�ӻP�U���귽�ۦP�j�p�����ɮ�
			for (int i = 0 ; i < fileLen ; i++ )
			{
				outArr[0].write(0);
			}
			//�C��������ӤU�����줸�ռ�
			long numPerThred = fileLen / DOWN_THREAD_NUM;
			//��ӤU���귽�㰣��ѤU���E��
			long left = fileLen % DOWN_THREAD_NUM;
			for (int i = 0 ; i < DOWN_THREAD_NUM; i++)
			{
				//���C�Ӱ�����}�Ҥ@�ӿ�J�y�B�@��RandomAccessFile����A
				//���C�Ӱ�������O�t�d�U���귽�����P�����C
				if (i != 0)
				{
					//�HURL�}�Ҧh�ӿ�J�y
					isArr[i] = url.openStream();
					//�H���w��X�ɮ׫إߦh��RandomAccessFile����
					outArr[i] = new RandomAccessFile(OUT_FILE_NAME , "rw");
				}
				//���O�Ұʦh�Ӱ�����ӤU�������귽
				if (i == DOWN_THREAD_NUM - 1 )
				{
					//�̫�@�Ӱ�����U�����wnumPerThred+left�Ӧ줸��
					new DownThread(i * numPerThred , (i + 1) * numPerThred + left
						, isArr[i] , outArr[i]).start();
				}
				else
				{
					//�C�Ӱ�����t�d�U���@�w��numPerThred�Ӧ줸��
					new DownThread(i * numPerThred , (i + 1) * numPerThred
						, isArr[i] , outArr[i]).start();
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	//�w�q���o���w�����귽�����ת���k
	public static long getFileLength(URL url) throws Exception
	{
		long length = 0;
		//�}�Ҹ�URL������URLConnection�C
		URLConnection con = url.openConnection();
		//���o�s�uURL�귽������
		long size = con.getContentLength();
		length = size;
		return length;
	}
}
