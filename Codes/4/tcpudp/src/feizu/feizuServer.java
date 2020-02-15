package feizu;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.channels.spi.*;
import java.net.*;
import java.util.*;
import java.nio.charset.*;

public class feizuServer
{
	//用於檢驗所有Channel狀態的Selector
	private Selector selector = null;
	//定義實現解碼、解碼的字集物件
	private Charset charset = Charset.forName("UTF-8");
    public void init()throws IOException
    {
		selector = Selector.open();
		//透過open方法來開啟一個未綁定的ServerSocketChannel案例
		ServerSocketChannel server = ServerSocketChannel.open();
		InetSocketAddress isa = new InetSocketAddress(
			"127.0.0.1", 30000); 
		//將該ServerSocketChannel綁定到指定IP位址
		server.socket().bind(isa);
		//設定ServerSocket以非阻塞模式工作
		server.configureBlocking(false);
		//將server登錄到指定Selector物件
		server.register(selector, SelectionKey.OP_ACCEPT);
		while (selector.select() > 0) 
		{
			//依次處理selector上的每個已選取的SelectionKey
			for (SelectionKey sk : selector.selectedKeys())
			{
				//從selector上的已選取Key集中移除正在處理的SelectionKey
				selector.selectedKeys().remove(sk);
				//若果sk對應的通道包括用戶端的連線請求
				if (sk.isAcceptable())
				{
					//呼叫accept方法接受連線，產生伺服器端對應的SocketChannel
					SocketChannel sc = server.accept();
					//設定采用非阻塞模式
					sc.configureBlocking(false);
					//將該SocketChannel也登錄到selector
					sc.register(selector, SelectionKey.OP_READ);
					//將sk對應的Channel設定成準備接受其他請求
					sk.interestOps(SelectionKey.OP_ACCEPT);
				}
				//若果sk對應的通道有資料需要讀取
				if (sk.isReadable())
				{
					//取得該SelectionKey對應的Channel，該Channel中有讀取的資料
					SocketChannel sc = (SocketChannel)sk.channel();
					//定義準備執行讀取資料的ByteBuffer
					ByteBuffer buff = ByteBuffer.allocate(1024);
					String content = "";
					//開始讀取資料
					try
					{
						while(sc.read(buff) > 0)
						{
							buff.flip();
							content += charset.decode(buff);
						}
						//列印從該sk對應的Channel裡讀取到的資料
						System.out.println("=====" + content);
						//將sk對應的Channel設定成準備下一次讀取
						sk.interestOps(SelectionKey.OP_READ);
					}
					//若果捕捉到該sk對應的Channel出現了例外，即表明該Channel
					//對應的Client出現了問題，所以從Selector中取消sk的登錄
					catch (IOException ex)
					{
						//從Selector中移除特殊的SelectionKey
						sk.cancel();
						if (sk.channel() != null)
						{
							sk.channel().close();
						}
					}
					//若果content的長度大於0，即聊天訊息不為空
					if (content.length() > 0)
					{
						//檢查該selector裡登錄的所有SelectKey
						for (SelectionKey key : selector.keys())
						{
							//取得該key對應的Channel
							Channel targetChannel = key.channel();
							//若果該channel是SocketChannel物件
							if (targetChannel instanceof SocketChannel)
							{
								//將讀到的內容寫入該Channel中
								SocketChannel dest = (SocketChannel)targetChannel;
								dest.write(charset.encode(content));
							}
						}
					}
				}
			}
		}
    }
	
	public static void main(String[] args)
		throws IOException
	{
		new feizuServer().init();
	}
}
