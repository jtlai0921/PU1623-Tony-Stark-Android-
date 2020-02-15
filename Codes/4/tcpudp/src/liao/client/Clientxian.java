package liao.client;
import java.io.*;
import java.net.*;
import java.util.*;


public class Clientxian implements Runnable
{
	//�Ӱ�����t�d�B�z��Socket
	private Socket s;
	//�Ӳ{���ҳB�z��Socket�ҹ�������J�y
	BufferedReader br = null;
	public Clientxian(Socket s)
		throws IOException
	{
		this.s = s;
		br = new BufferedReader(
			new InputStreamReader(s.getInputStream()));
	}
	public void run()
	{
		try
		{
			String content = null;
			//���_Ū��Socket��J�y�������e�A�ñN�o�Ǥ��e�C�L��X
			while ((content = br.readLine()) != null)
			{
				System.out.println(content);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}