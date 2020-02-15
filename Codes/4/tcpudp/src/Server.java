import java.net.*;
import java.io.*;

public class Server
{
	public static void main(String[] args) 
		throws IOException
	{
		//建立一個ServerSocket，用於監聽用戶端Socket的連線請求
		ServerSocket ss = new ServerSocket(30000);
		//采用循環不斷接受來自用戶端的請求
		while (true)
		{
			//每當接受到用戶端Socket的請求，伺服器端也對應產生一個Socket
			Socket s = ss.accept();
			//將Socket對應的輸出流包裝成PrintStream
			PrintStream ps = new PrintStream(s.getOutputStream());
			//進行普通IO動作
			ps.println("聖誕快樂！");
			//關閉輸出流，關閉Socket
			ps.close();
			s.close();
		}
	}
}
