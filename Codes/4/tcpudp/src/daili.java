
import java.net.*;
import java.io.*;
import java.util.*;

public class daili
{
	Proxy proxy;
	URL url;
	URLConnection conn;
	//�q�����z�L�N�zŪ���
	Scanner scan;
	PrintStream ps ;
	//�U���O�N�z���A������}�M�q�T��A
	//������ڦ��Ī��N�z���A������}�M�q�T��
	String proxyAddress = "78.39.195.11";
	int proxyPort;
	//�U���O�A�չ϶}�Ҫ�������}
	String urlStr = "http://www.xxx.cn";

	public void init()
	{
		try
		{
			url = new URL(urlStr);
			//�إߤ@�ӥN�z���A������
			proxy = new Proxy(Proxy.Type.HTTP,
				new InetSocketAddress(proxyAddress , proxyPort));
			//�ϥίS���N�z���A���}�ҳs�u
			conn = url.openConnection(proxy);
			//�]�w�O�ɮɪ��C
			conn.setConnectTimeout(5000);
			scan = new Scanner(conn.getInputStream());
			//�_�l�ƿ�X�y
			ps = new PrintStream("Index.htm");
			while (scan.hasNextLine())
			{
				String line = scan.nextLine();
				//�b�D���x��X�����귽���e
				System.out.println(line);
				//�N�����귽���e��X����w��X�y
				ps.println(line);
			}
		}
		catch(MalformedURLException ex)
		{
			System.out.println(urlStr + "���O���Ī���}�I");
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		//�����귽
		finally
		{
			if (ps != null)
			{
				ps.close();
			}
		}
	}

    public static void main(String[] args) 
    {
		new daili().init();
    }
}