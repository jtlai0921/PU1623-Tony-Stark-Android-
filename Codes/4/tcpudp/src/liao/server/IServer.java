package liao.server;
import java.net.*;
import java.io.*;
import java.util.*;


public class IServer
{
	//�w�q�x�s�Ҧ�Socket��ArrayList
	public static ArrayList<Socket> socketList = new ArrayList<Socket>();
    public static void main(String[] args) 
		throws IOException
    {
        ServerSocket ss = new ServerSocket(30000);
		while(true)
		{
			//����{���X�|����A�N�@�����ݧO�H���s�u
			Socket s = ss.accept();
			socketList.add(s);
			//�C��Τ�ݳs�u��Ұʤ@��ServerThread��������ӥΤ�ݪA��
			new Thread(new Serverxian(s)).start();
		}
    }
}