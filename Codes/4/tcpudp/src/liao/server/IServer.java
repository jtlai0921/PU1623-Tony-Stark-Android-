package liao.server;
import java.net.*;
import java.io.*;
import java.util.*;


public class IServer
{
	//定義儲存所有Socket的ArrayList
	public static ArrayList<Socket> socketList = new ArrayList<Socket>();
    public static void main(String[] args) 
		throws IOException
    {
        ServerSocket ss = new ServerSocket(30000);
		while(true)
		{
			//此行程式碼會阻塞，將一直等待別人的連線
			Socket s = ss.accept();
			socketList.add(s);
			//每當用戶端連線後啟動一條ServerThread執行緒為該用戶端服務
			new Thread(new Serverxian(s)).start();
		}
    }
}