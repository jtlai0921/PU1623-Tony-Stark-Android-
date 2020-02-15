package liao.client;
import java.io.*;
import java.net.*;
import java.util.*;


public class Clientxian implements Runnable
{
	//該執行緒負責處理的Socket
	private Socket s;
	//該現成所處理的Socket所對應的輸入流
	BufferedReader br = null;
	public Clientxian(Socket s)
		throws IOException
	{
		this.s = s;
		br = new BufferedReader(
			new InputStreamReader(s.getInputStream()));
	}
	public void run()
	{
		try
		{
			String content = null;
			//不斷讀取Socket輸入流中的內容，並將這些內容列印輸出
			while ((content = br.readLine()) != null)
			{
				System.out.println(content);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}