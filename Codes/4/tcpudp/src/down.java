
import java.io.*;
import java.net.*;

//定義下載從start到end的內容的執行緒
class DownThread extends Thread
{
	//定義位元組陣列的長度
	private final int BUFF_LEN = 32;
	//定義下載的起始點
	private long begin;
	//定義下載的結束點
	private long end;
	//下載資源對應的輸入流
	private InputStream is;
	//將下載到的位元組輸出到mm中
	private RandomAccessFile mm ;

	//建構器，傳入輸入流，輸出流和下載起始點、結束點
	public DownThread(long start , long end 
		, InputStream is , RandomAccessFile raf)
	{
		//輸出該執行緒負責下載的位元組位置
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
			//定義讀取輸入流內容的的快取陣列
			byte[] buff = new byte[BUFF_LEN];
			//本執行緒負責下載資源的大小
			long contentLen = end - begin;
			//定義最多需要讀取幾次就可以完成本執行緒的下載
			long times = contentLen / BUFF_LEN + 4;
			//實際讀取的位元組數
			int hasRead = 0;
			for (int i = 0; i < times ; i++)
			{
				hasRead = is.read(buff);
				//若果讀取的位元組數小於0，則離開循環！
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
		//使用finally區塊來關閉目前執行緒的輸入流、輸出流
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
			//建立一個URL物件
			URL url = new URL("http://hiphotos.baidu.com/"+ "baidu/pic/item/8546bd003af33a8727f50057c65c10385243b566.jpg");
			//以此URL物件開啟第一個輸入流
			isArr[0] = url.openStream();
			long fileLen = getFileLength(url);
			System.out.println("網路資源的大小" + fileLen);
			//以輸出檔名建立第一個RandomAccessFile輸出流
			outArr[0] = new RandomAccessFile(OUT_FILE_NAME , "rw");
			//建立一個與下載資源相同大小的空檔案
			for (int i = 0 ; i < fileLen ; i++ )
			{
				outArr[0].write(0);
			}
			//每執行緒應該下載的位元組數
			long numPerThred = fileLen / DOWN_THREAD_NUM;
			//整個下載資源整除後剩下的余數
			long left = fileLen % DOWN_THREAD_NUM;
			for (int i = 0 ; i < DOWN_THREAD_NUM; i++)
			{
				//為每個執行緒開啟一個輸入流、一個RandomAccessFile物件，
				//讓每個執行緒分別負責下載資源的不同部分。
				if (i != 0)
				{
					//以URL開啟多個輸入流
					isArr[i] = url.openStream();
					//以指定輸出檔案建立多個RandomAccessFile物件
					outArr[i] = new RandomAccessFile(OUT_FILE_NAME , "rw");
				}
				//分別啟動多個執行緒來下載網路資源
				if (i == DOWN_THREAD_NUM - 1 )
				{
					//最後一個執行緒下載指定numPerThred+left個位元組
					new DownThread(i * numPerThred , (i + 1) * numPerThred + left
						, isArr[i] , outArr[i]).start();
				}
				else
				{
					//每個執行緒負責下載一定的numPerThred個位元組
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
	//定義取得指定網路資源的長度的方法
	public static long getFileLength(URL url) throws Exception
	{
		long length = 0;
		//開啟該URL對應的URLConnection。
		URLConnection con = url.openConnection();
		//取得連線URL資源的長度
		long size = con.getContentLength();
		length = size;
		return length;
	}
}
