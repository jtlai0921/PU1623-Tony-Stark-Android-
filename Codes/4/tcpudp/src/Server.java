import java.net.*;
import java.io.*;

public class Server
{
	public static void main(String[] args) 
		throws IOException
	{
		//�إߤ@��ServerSocket�A�Ω��ť�Τ��Socket���s�u�ШD
		ServerSocket ss = new ServerSocket(30000);
		//���δ`�����_�����ӦۥΤ�ݪ��ШD
		while (true)
		{
			//�C������Τ��Socket���ШD�A���A���ݤ]�������ͤ@��Socket
			Socket s = ss.accept();
			//�NSocket��������X�y�]�˦�PrintStream
			PrintStream ps = new PrintStream(s.getOutputStream());
			//�i�洶�qIO�ʧ@
			ps.println("�t�ϧּ֡I");
			//������X�y�A����Socket
			ps.close();
			s.close();
		}
	}
}
