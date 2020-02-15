
import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class UdpClient
{
	//定義傳送資料報的目的地
	public static final int DEST_PORT = 30000;
	public static final String DEST_IP = "127.0.0.1";

	//定義每個資料報的最大大小為4K
	private static final int DATA_LEN = 4096;
	//定義該用戶端使用的DatagramSocket
	private DatagramSocket socket = null;
	//定義接收網路資料的位元組陣列
	byte[] inBuff = new byte[DATA_LEN];
	//以指定位元組陣列建立準備接受資料的DatagramPacket物件
	private DatagramPacket inPacket = 
		new DatagramPacket(inBuff , inBuff.length);
	//定義一個用於傳送的DatagramPacket物件
	private DatagramPacket outPacket = null;
	public void init()throws IOException
	{
		try
		{
			//建立一個用戶端DatagramSocket，使用隨機通訊埠
			socket = new DatagramSocket();
			//起始化傳送用的DatagramSocket，它包括一個長度為0的位元組陣列
			outPacket = new DatagramPacket(new byte[0] , 0 ,
				InetAddress.getByName(DEST_IP) , DEST_PORT);
			//建立鍵碟輸入流
			Scanner scan = new Scanner(System.in);
			//不斷讀取鍵碟輸入
			while(scan.hasNextLine())
			{
				//將鍵碟輸入的一行字串轉換位元組陣列
				byte[] buff = scan.nextLine().getBytes();
				//設定傳送用的DatagramPacket裡的位元組資料
				outPacket.setData(buff);
				//傳送資料報
				socket.send(outPacket);
				//讀取Socket中的資料，讀到的資料放在inPacket所封裝的位元組陣列裡。
				socket.receive(inPacket);
				System.out.println(new String(inBuff , 0 , 
					inPacket.getLength()));
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
		new UdpClient().init();
	}
}
