package liao.client;

import java.net.*;
import java.io.*;

public class IClient
{
    public static void main(String[] args)
		throws IOException 
    {
		Socket s = s = new Socket("127.0.0.1" , 30000);
		//用戶端啟動ClientThread執行緒不斷讀取來自伺服器的資料
		new Thread(new Clientxian(s)).start();
		//取得該Socket對應的輸出流
		PrintStream ps = new PrintStream(s.getOutputStream());
		String line = null;
		//不斷讀取鍵碟輸入
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while ((line = br.readLine()) != null)
		{
			//將使用者的鍵碟輸入內容寫入Socket對應的輸出流
			ps.println(line);
		}
    }
}
