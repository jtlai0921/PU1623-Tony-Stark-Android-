package liao.client;

import java.net.*;
import java.io.*;

public class IClient
{
    public static void main(String[] args)
		throws IOException 
    {
		Socket s = s = new Socket("127.0.0.1" , 30000);
		//�Τ�ݱҰ�ClientThread��������_Ū���Ӧۦ��A�������
		new Thread(new Clientxian(s)).start();
		//���o��Socket��������X�y
		PrintStream ps = new PrintStream(s.getOutputStream());
		String line = null;
		//���_Ū����п�J
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while ((line = br.readLine()) != null)
		{
			//�N�ϥΪ̪���п�J���e�g�JSocket��������X�y
			ps.println(line);
		}
    }
}
