package liao.server;
import java.io.*;
import java.net.*;
import java.util.*;

//�t�d�B�z�C�Ӱ�����q�T����������O
public class Serverxian implements Runnable 
{
	//�w�q�ثe������ҳB�z��Socket
	Socket s = null;
	//�Ӱ�����ҳB�z��Socket�ҹ�������J�y
	BufferedReader br = null;
	public Serverxian(Socket s)
		throws IOException
	{
		this.s = s;
		//�_�l�Ƹ�Socket��������J�y
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
	}
	public void run()
	{
		try
		{
			String content = null;
			//���δ`�����_�qSocket��Ū���Τ�ݶǰe�L�Ӫ����
			while ((content = readFromClient()) != null)
			{
				//�ˬdsocketList�����C��Socket�A
				//�NŪ�쪺���e�V�C��Socket�ǰe�@��
				for (Socket s : IServer.socketList)
				{
					PrintStream ps = new PrintStream(s.getOutputStream());
					ps.println(content);
				}
			}
		}
		catch (IOException e)
		{
			//e.printStackTrace();
		}
	}
	//�w�qŪ���Τ�ݸ�ƪ���k
	private String readFromClient()
	{
		try
		{
			return br.readLine();	
		}
		//�Y�G������ҥ~�A�����Socket�������Τ�ݤw�g����
		catch (IOException e)
		{
			//������Socket�C
			IServer.socketList.remove(s);
		}
		return null;
	}
}
