import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.*;

//�������O��{Runnable���f�A�����O���רҥi�@���������target
public class manySocket implements Runnable
{
	//�ϥα`�Ƨ@�����{�����h�I�s��IP��}
	private static final String IP
		= "230.0.0.1";
	//�ϥα`�Ƨ@�����{�����h�I�s���ت����q�T��
	public static final int PORT = 30000;
	//�w�q�C�Ӹ�Ƴ����̤j�j�p��4K
	private static final int LEN = 2048;

	
	//�w�q���{����MulticastSocket�ר�
	private MulticastSocket socket = null;
	private InetAddress bAddress = null;
	private Scanner scan = null;
	//�w�q����������ƪ��줸�հ}�C
	byte[] inBuff = new byte[LEN];
	//�H���w�줸�հ}�C�إ߷ǳƱ�����ƪ�DatagramPacket����
	private DatagramPacket inPacket = 
		new DatagramPacket(inBuff , inBuff.length);
	//�w�q�@�ӥΩ�ǰe��DatagramPacket����
	private DatagramPacket oPacket = null;
	public void init()throws IOException
	{
		try
		{
			//�إߥΩ�ǰe�B������ƪ�MulticastSocket����
			//�]����MulticastSocket����ݭn�����A�ҥH�����w�q�T��
			socket = new MulticastSocket(PORT);
			bAddress = InetAddress.getByName(IP);
			//�N��socket�[�J�S���h�I�s����}
			socket.joinGroup(bAddress);
			//�]�w��MulticastSocket�ǰe����Ƴ��Q�^�e��ۤv
			socket.setLoopbackMode(false);
			//�_�l�ƶǰe�Ϊ�DatagramSocket�A���]�A�@�Ӫ��׬�0���줸�հ}�C
			oPacket = new DatagramPacket(new byte[0] , 0 ,
				bAddress , PORT);
			//�ҰʥH���רҪ�run()��k�@��������骺�����
			new Thread(this).start();
			//�إ���п�J�y
			scan = new Scanner(System.in);
			//���_Ū����п�J
			while(scan.hasNextLine())
			{
				//�N��п�J���@��r���ഫ�줸�հ}�C
				byte[] buff = scan.nextLine().getBytes();
				//�]�w�ǰe�Ϊ�DatagramPacket�̪��줸�ո��
				oPacket.setData(buff);
				//�ǰe��Ƴ�
				socket.send(oPacket);
			}
		}
		finally
		{
			socket.close();
		}
	}
	
	public void run()
	{
		try
		{
			while(true)
			{
				//Ū��Socket������ơAŪ�쪺��Ʃ�binPacket�ҫʸ˪��줸�հ}�C�̡C
				socket.receive(inPacket);
				//�C�L��X�qsocket��Ū�������e
				System.out.println("��ѰT���G" + new String(inBuff , 0 , 
					inPacket.getLength()));
			}
		}
		//�����ҥ~
		catch (IOException ex)
		{
			ex.printStackTrace();
			try
			{
				if (socket != null)
				{
					//����Socket���}�Ӧh�IIP�s����}
					socket.leaveGroup(bAddress);
					//������Socket����
					socket.close();
				}
				System.exit(1);	
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) 
		throws IOException
	{
		new manySocket().init();
	}
}
