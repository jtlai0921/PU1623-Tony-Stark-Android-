
import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class UdpServer
{
	public static final int PORT = 30000;
	//�w�q�C�Ӹ�Ƴ����̤j�j�p��4K
	private static final int DATA_LEN = 4096;
	//�w�q�Ӧ��A���ϥΪ�DatagramSocket
	private DatagramSocket socket = null;
	//�w�q����������ƪ��줸�հ}�C
	byte[] inBuff = new byte[DATA_LEN];
	//�H���w�줸�հ}�C�إ߷ǳƱ�����ƪ�DatagramPacket����
	private DatagramPacket inPacket = 
		new DatagramPacket(inBuff , inBuff.length);
	//�w�q�@�ӥΩ�ǰe��DatagramPacket����
	private DatagramPacket outPacket;
	//�w�q�@�Ӧr��}�C�A���A���ǰe�Ӱ}�C��������
	String[] books = new String[]
	{
		"AAA",
		"BBB",
		"CCC",
		"DDD"
	};
	public void init()throws IOException
	{
		try
		{
			//�إ�DatagramSocket����
			socket = new DatagramSocket(PORT);
			//���δ`���������
			for (int i = 0; i < 1000 ; i++ )
			{
				//Ū��Socket������ơAŪ�쪺��Ʃ�binPacket�ҫʸ˪��줸�հ}�C�̡C
				socket.receive(inPacket);
				//�P�_inPacket.getData()�MinBuff�O�_�O�P�@�Ӱ}�C
				System.out.println(inBuff == inPacket.getData());
				//�N�����쪺���e�ন�r����X
				System.out.println(new String(inBuff ,
					0 , inPacket.getLength()));
				//�q�r��}�C�����X�@�Ӥ����@���ǰe�����
				byte[] sendData = books[i % 4].getBytes();
				//�H���w�줸�հ}�C�@���ǰe��ơB�H�豵���쪺DatagramPacket��
				//��SocketAddress�@���ت�SocketAddress�إ�DatagramPacket�C
				outPacket = new DatagramPacket(sendData ,
					sendData.length , inPacket.getSocketAddress());
				//�ǰe���
				socket.send(outPacket);	
			}
		}
		//�ϥ�finally�϶��T�O�����귽
		finally
		{
			if (socket != null)
			{
				socket.close();
			}
		}
	}
	public static void main(String[] args) 
		throws IOException
	{
		new UdpServer().init();
	}
}
