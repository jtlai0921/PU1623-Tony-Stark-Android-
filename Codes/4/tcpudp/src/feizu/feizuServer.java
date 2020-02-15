package feizu;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.channels.spi.*;
import java.net.*;
import java.util.*;
import java.nio.charset.*;

public class feizuServer
{
	//�Ω�����Ҧ�Channel���A��Selector
	private Selector selector = null;
	//�w�q��{�ѽX�B�ѽX���r������
	private Charset charset = Charset.forName("UTF-8");
    public void init()throws IOException
    {
		selector = Selector.open();
		//�z�Lopen��k�Ӷ}�Ҥ@�ӥ��j�w��ServerSocketChannel�ר�
		ServerSocketChannel server = ServerSocketChannel.open();
		InetSocketAddress isa = new InetSocketAddress(
			"127.0.0.1", 30000); 
		//�N��ServerSocketChannel�j�w����wIP��}
		server.socket().bind(isa);
		//�]�wServerSocket�H�D����Ҧ��u�@
		server.configureBlocking(false);
		//�Nserver�n������wSelector����
		server.register(selector, SelectionKey.OP_ACCEPT);
		while (selector.select() > 0) 
		{
			//�̦��B�zselector�W���C�Ӥw�����SelectionKey
			for (SelectionKey sk : selector.selectedKeys())
			{
				//�qselector�W���w���Key�����������b�B�z��SelectionKey
				selector.selectedKeys().remove(sk);
				//�Y�Gsk�������q�D�]�A�Τ�ݪ��s�u�ШD
				if (sk.isAcceptable())
				{
					//�I�saccept��k�����s�u�A���ͦ��A���ݹ�����SocketChannel
					SocketChannel sc = server.accept();
					//�]�w���ΫD����Ҧ�
					sc.configureBlocking(false);
					//�N��SocketChannel�]�n����selector
					sc.register(selector, SelectionKey.OP_READ);
					//�Nsk������Channel�]�w���ǳƱ�����L�ШD
					sk.interestOps(SelectionKey.OP_ACCEPT);
				}
				//�Y�Gsk�������q�D����ƻݭnŪ��
				if (sk.isReadable())
				{
					//���o��SelectionKey������Channel�A��Channel����Ū�������
					SocketChannel sc = (SocketChannel)sk.channel();
					//�w�q�ǳư���Ū����ƪ�ByteBuffer
					ByteBuffer buff = ByteBuffer.allocate(1024);
					String content = "";
					//�}�lŪ�����
					try
					{
						while(sc.read(buff) > 0)
						{
							buff.flip();
							content += charset.decode(buff);
						}
						//�C�L�q��sk������Channel��Ū���쪺���
						System.out.println("=====" + content);
						//�Nsk������Channel�]�w���ǳƤU�@��Ū��
						sk.interestOps(SelectionKey.OP_READ);
					}
					//�Y�G�������sk������Channel�X�{�F�ҥ~�A�Y�����Channel
					//������Client�X�{�F���D�A�ҥH�qSelector������sk���n��
					catch (IOException ex)
					{
						//�qSelector�������S��SelectionKey
						sk.cancel();
						if (sk.channel() != null)
						{
							sk.channel().close();
						}
					}
					//�Y�Gcontent�����פj��0�A�Y��ѰT��������
					if (content.length() > 0)
					{
						//�ˬd��selector�̵n�����Ҧ�SelectKey
						for (SelectionKey key : selector.keys())
						{
							//���o��key������Channel
							Channel targetChannel = key.channel();
							//�Y�G��channel�OSocketChannel����
							if (targetChannel instanceof SocketChannel)
							{
								//�NŪ�쪺���e�g�J��Channel��
								SocketChannel dest = (SocketChannel)targetChannel;
								dest.write(charset.encode(content));
							}
						}
					}
				}
			}
		}
    }
	
	public static void main(String[] args)
		throws IOException
	{
		new feizuServer().init();
	}
}
