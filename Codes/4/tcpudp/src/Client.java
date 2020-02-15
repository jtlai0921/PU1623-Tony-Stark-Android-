import java.net.*;
import java.io.*;

public class Client
{
	public static void main(String[] args) 
		throws IOException
	{
		Socket socket = new Socket("127.0.0.1" , 30000);
		//將Socket對應的輸入流包裝成BufferedReader
		BufferedReader br = new BufferedReader(
			new InputStreamReader(socket.getInputStream()));
		//進行普通IO動作
		String line = br.readLine();
		System.out.println("來自伺服器的資料：" + line);
		//關閉輸入流、socket
		br.close();
		socket.close();
	}
}
