
import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class UdpClient
{
	//�w�q�ǰe��Ƴ����ت��a
	public static final int DEST_PORT = 30000;
	public static final String DEST_IP = "127.0.0.1";

	//�w�q�C�Ӹ�Ƴ����̤j�j�p��4K
	private static final int DATA_LEN = 4096;
	//�w�q�ӥΤ�ݨϥΪ�DatagramSocket
	private DatagramSocket socket = null;
	//�w�q����������ƪ��줸�հ}�C
	byte[] inBuff = new byte[DATA_LEN];
	//�H���w�줸�հ}�C�إ߷ǳƱ�����ƪ�DatagramPacket����
	private DatagramPacket inPacket = 
		new DatagramPacket(inBuff , inBuff.length);
	//�w�q�@�ӥΩ�ǰe��DatagramPacket����
	private DatagramPacket outPacket = null;
	public void init()throws IOException
	{
		try
		{
			//�إߤ@�ӥΤ��DatagramSocket�A�ϥ��H���q�T��
			socket = new DatagramSocket();
			//�_�l�ƶǰe�Ϊ�DatagramSocket�A���]�A�@�Ӫ��׬�0���줸�հ}�C
			outPacket = new DatagramPacket(new byte[0] , 0 ,
				InetAddress.getByName(DEST_IP) , DEST_PORT);
			//�إ���п�J�y
			Scanner scan = new Scanner(System.in);
			//���_Ū����п�J
			while(scan.hasNextLine())
			{
				//�N��п�J���@��r���ഫ�줸�հ}�C
				byte[] buff = scan.nextLine().getBytes();
				//�]�w�ǰe�Ϊ�DatagramPacket�̪��줸�ո��
				outPacket.setData(buff);
				//�ǰe��Ƴ�
				socket.send(outPacket);
				//Ū��Socket������ơAŪ�쪺��Ʃ�binPacket�ҫʸ˪��줸�հ}�C�̡C
				socket.receive(inPacket);
				System.out.println(new String(inBuff , 0 , 
					inPacket.getLength()));
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
		new UdpClient().init();
	}
}
