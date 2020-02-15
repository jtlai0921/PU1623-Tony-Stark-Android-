package feizu;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.*;

public class feizuClient
{
	//�w�q����SocketChannel��Selector����
	private Selector selector = null;
	//�w�q�B�z�ѽX�M�ѽX���r��
	private Charset charset = Charset.forName("UTF-8");
	//�Τ��SocketChannel
	private SocketChannel sc = null;
	public void init()throws IOException
	{
		selector = Selector.open();
		InetSocketAddress isa = new InetSocketAddress("127.0.0.1", 30000);
		//�I�sopen�R�A��k�إ߳s�u����w�D�q����SocketChannel
		sc = SocketChannel.open(isa);
		//�]�w��sc�H�D����Ҧ��u�@
		sc.configureBlocking(false);
		//�NSocketChannel����n������wSelector
		sc.register(selector, SelectionKey.OP_READ);
		//�Ұ�Ū�����A���ݸ�ƪ������
		new ClientThread().start();
		//�إ���п�J�y
		Scanner scan = new Scanner(System.in);
		while (scan.hasNextLine())
		{
			//Ū����п�J
			String line = scan.nextLine();
			//�N��п�J�����e��X��SocketChannel��
			sc.write(charset.encode(line));
		}
	}
	//�w�qŪ�����A����ƪ������
	private class ClientThread extends Thread
	{
		public void run()
		{
			try
			{
				while (selector.select() > 0) 
				{
					//�ˬd�C�Ӧ��i��IO�ʧ@Channel������SelectionKey
					for (SelectionKey sk : selector.selectedKeys())
					{
						//�������b�B�z��SelectionKey
						selector.selectedKeys().remove(sk);
						//�Y�G��SelectionKey������Channel����Ū�������
						if (sk.isReadable())
						{
							//�ϥ�NIOŪ��Channel�������
							SocketChannel sc = (SocketChannel)sk.channel();
							ByteBuffer buff = ByteBuffer.allocate(1024);
							String content = "";
							while(sc.read(buff) > 0)
							{
								sc.read(buff); 
								buff.flip();
								content += charset.decode(buff);
							}
							//�C�L��XŪ�������e
							System.out.println("��ѰT���G" + content);
							//���U�@��Ū���@�ǳ�
							sk.interestOps(SelectionKey.OP_READ);
						}
					}
				}
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

    public static void main(String[] args)
		throws IOException
	{
		new feizuClient().init();
    }
}