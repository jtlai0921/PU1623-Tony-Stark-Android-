import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.*;

//讓該類別實現Runnable接口，該類別的案例可作為執行緒的target
public class manySocket implements Runnable
{
	//使用常數作為本程式的多點廣播IP位址
	private static final String IP
		= "230.0.0.1";
	//使用常數作為本程式的多點廣播目的的通訊埠
	public static final int PORT = 30000;
	//定義每個資料報的最大大小為4K
	private static final int LEN = 2048;

	
	//定義本程式的MulticastSocket案例
	private MulticastSocket socket = null;
	private InetAddress bAddress = null;
	private Scanner scan = null;
	//定義接收網路資料的位元組陣列
	byte[] inBuff = new byte[LEN];
	//以指定位元組陣列建立準備接受資料的DatagramPacket物件
	private DatagramPacket inPacket = 
		new DatagramPacket(inBuff , inBuff.length);
	//定義一個用於傳送的DatagramPacket物件
	private DatagramPacket oPacket = null;
	public void init()throws IOException
	{
		try
		{
			//建立用於傳送、接收資料的MulticastSocket物件
			//因為該MulticastSocket物件需要接收，所以有指定通訊埠
			socket = new MulticastSocket(PORT);
			bAddress = InetAddress.getByName(IP);
			//將該socket加入特殊的多點廣播位址
			socket.joinGroup(bAddress);
			//設定本MulticastSocket傳送的資料報被回送到自己
			socket.setLoopbackMode(false);
			//起始化傳送用的DatagramSocket，它包括一個長度為0的位元組陣列
			oPacket = new DatagramPacket(new byte[0] , 0 ,
				bAddress , PORT);
			//啟動以本案例的run()方法作為執行緒體的執行緒
			new Thread(this).start();
			//建立鍵碟輸入流
			scan = new Scanner(System.in);
			//不斷讀取鍵碟輸入
			while(scan.hasNextLine())
			{
				//將鍵碟輸入的一行字串轉換位元組陣列
				byte[] buff = scan.nextLine().getBytes();
				//設定傳送用的DatagramPacket裡的位元組資料
				oPacket.setData(buff);
				//傳送資料報
				socket.send(oPacket);
			}
		}
		finally
		{
			socket.close();
		}
	}
	
	public void run()
	{
		try
		{
			while(true)
			{
				//讀取Socket中的資料，讀到的資料放在inPacket所封裝的位元組陣列裡。
				socket.receive(inPacket);
				//列印輸出從socket中讀取的內容
				System.out.println("聊天訊息：" + new String(inBuff , 0 , 
					inPacket.getLength()));
			}
		}
		//捕捉例外
		catch (IOException ex)
		{
			ex.printStackTrace();
			try
			{
				if (socket != null)
				{
					//讓該Socket離開該多點IP廣播位址
					socket.leaveGroup(bAddress);
					//關閉該Socket物件
					socket.close();
				}
				System.exit(1);	
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) 
		throws IOException
	{
		new manySocket().init();
	}
}
