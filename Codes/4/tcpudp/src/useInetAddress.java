
import java.net.*;

public class useInetAddress
{
	public static void main(String[] args)
		throws Exception
	{
		//�ھڥD�q���W�Ө��o������InetAddress�ר�
		InetAddress ip = InetAddress.getByName("www.sohu.cn");
		//�P�_�O�_�i�F
		System.out.println("sohu�O�_�i�F�G" + ip.isReachable(2000)); 
		//���o��InetAddress�רҪ�IP�r��
		System.out.println(ip.getHostAddress());
		//�ھڭ�lIP��}�Ө��o������InetAddress�ר�
		InetAddress local = InetAddress.getByAddress(new byte[]
		{127,0,0,1});
		System.out.println("�����O�_�i�F�G" + local.isReachable(5000)); 
		//���o��InetAddress�רҹ����������w��W
		System.out.println(local.getCanonicalHostName());		
	}
}
