
import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class UdpServer
{
	public static final int PORT = 30000;
	//定義每個資料報的最大大小為4K
	private static final int DATA_LEN = 4096;
	//定義該伺服器使用的DatagramSocket
	private DatagramSocket socket = null;
	//定義接收網路資料的位元組陣列
	byte[] inBuff = new byte[DATA_LEN];
	//以指定位元組陣列建立準備接受資料的DatagramPacket物件
	private DatagramPacket inPacket = 
		new DatagramPacket(inBuff , inBuff.length);
	//定義一個用於傳送的DatagramPacket物件
	private DatagramPacket outPacket;
	//定義一個字串陣列，伺服器傳送該陣列的的元素
	String[] books = new String[]
	{
		"AAA",
		"BBB",
		"CCC",
		"DDD"
	};
	public void init()throws IOException
	{
		try
		{
			//建立DatagramSocket物件
			socket = new DatagramSocket(PORT);
			//采用循環接受資料
			for (int i = 0; i < 1000 ; i++ )
			{
				//讀取Socket中的資料，讀到的資料放在inPacket所封裝的位元組陣列裡。
				socket.receive(inPacket);
				//判斷inPacket.getData()和inBuff是否是同一個陣列
				System.out.println(inBuff == inPacket.getData());
				//將接收到的內容轉成字串後輸出
				System.out.println(new String(inBuff ,
					0 , inPacket.getLength()));
				//從字串陣列中取出一個元素作為傳送的資料
				byte[] sendData = books[i % 4].getBytes();
				//以指定位元組陣列作為傳送資料、以剛接受到的DatagramPacket的
				//源SocketAddress作為目的SocketAddress建立DatagramPacket。
				outPacket = new DatagramPacket(sendData ,
					sendData.length , inPacket.getSocketAddress());
				//傳送資料
				socket.send(outPacket);	
			}
		}
		//使用finally區塊確保關閉資源
		finally
		{
			if (socket != null)
			{
				socket.close();
			}
		}
	}
	public static void main(String[] args) 
		throws IOException
	{
		new UdpServer().init();
	}
}
