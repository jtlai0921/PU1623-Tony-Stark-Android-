import java.net.*;
import java.io.*;

public class Client
{
	public static void main(String[] args) 
		throws IOException
	{
		Socket socket = new Socket("127.0.0.1" , 30000);
		//�NSocket��������J�y�]�˦�BufferedReader
		BufferedReader br = new BufferedReader(
			new InputStreamReader(socket.getInputStream()));
		//�i�洶�qIO�ʧ@
		String line = br.readLine();
		System.out.println("�Ӧۦ��A������ơG" + line);
		//������J�y�Bsocket
		br.close();
		socket.close();
	}
}
