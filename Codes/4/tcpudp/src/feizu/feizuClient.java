package feizu;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.*;

public class feizuClient
{
	//定義檢驗SocketChannel的Selector物件
	private Selector selector = null;
	//定義處理解碼和解碼的字集
	private Charset charset = Charset.forName("UTF-8");
	//用戶端SocketChannel
	private SocketChannel sc = null;
	public void init()throws IOException
	{
		selector = Selector.open();
		InetSocketAddress isa = new InetSocketAddress("127.0.0.1", 30000);
		//呼叫open靜態方法建立連線到指定主電腦的SocketChannel
		sc = SocketChannel.open(isa);
		//設定該sc以非阻塞模式工作
		sc.configureBlocking(false);
		//將SocketChannel物件登錄到指定Selector
		sc.register(selector, SelectionKey.OP_READ);
		//啟動讀取伺服器端資料的執行緒
		new ClientThread().start();
		//建立鍵碟輸入流
		Scanner scan = new Scanner(System.in);
		while (scan.hasNextLine())
		{
			//讀取鍵碟輸入
			String line = scan.nextLine();
			//將鍵碟輸入的內容輸出到SocketChannel中
			sc.write(charset.encode(line));
		}
	}
	//定義讀取伺服器資料的執行緒
	private class ClientThread extends Thread
	{
		public void run()
		{
			try
			{
				while (selector.select() > 0) 
				{
					//檢查每個有可用IO動作Channel對應的SelectionKey
					for (SelectionKey sk : selector.selectedKeys())
					{
						//移除正在處理的SelectionKey
						selector.selectedKeys().remove(sk);
						//若果該SelectionKey對應的Channel中有讀取的資料
						if (sk.isReadable())
						{
							//使用NIO讀取Channel中的資料
							SocketChannel sc = (SocketChannel)sk.channel();
							ByteBuffer buff = ByteBuffer.allocate(1024);
							String content = "";
							while(sc.read(buff) > 0)
							{
								sc.read(buff); 
								buff.flip();
								content += charset.decode(buff);
							}
							//列印輸出讀取的內容
							System.out.println("聊天訊息：" + content);
							//為下一次讀取作準備
							sk.interestOps(SelectionKey.OP_READ);
						}
					}
				}
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

    public static void main(String[] args)
		throws IOException
	{
		new feizuClient().init();
    }
}