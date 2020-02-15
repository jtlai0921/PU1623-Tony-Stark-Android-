package liao.server;
import java.io.*;
import java.net.*;
import java.util.*;

//負責處理每個執行緒通訊的執行緒類別
public class Serverxian implements Runnable 
{
	//定義目前執行緒所處理的Socket
	Socket s = null;
	//該執行緒所處理的Socket所對應的輸入流
	BufferedReader br = null;
	public Serverxian(Socket s)
		throws IOException
	{
		this.s = s;
		//起始化該Socket對應的輸入流
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
	}
	public void run()
	{
		try
		{
			String content = null;
			//采用循環不斷從Socket中讀取用戶端傳送過來的資料
			while ((content = readFromClient()) != null)
			{
				//檢查socketList中的每個Socket，
				//將讀到的內容向每個Socket傳送一次
				for (Socket s : IServer.socketList)
				{
					PrintStream ps = new PrintStream(s.getOutputStream());
					ps.println(content);
				}
			}
		}
		catch (IOException e)
		{
			//e.printStackTrace();
		}
	}
	//定義讀取用戶端資料的方法
	private String readFromClient()
	{
		try
		{
			return br.readLine();	
		}
		//若果捕捉到例外，表明該Socket對應的用戶端已經關閉
		catch (IOException e)
		{
			//移除該Socket。
			IServer.socketList.remove(s);
		}
		return null;
	}
}
