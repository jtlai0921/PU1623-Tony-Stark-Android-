
import java.net.*;

public class useInetAddress
{
	public static void main(String[] args)
		throws Exception
	{
		//根據主電腦名來取得對應的InetAddress案例
		InetAddress ip = InetAddress.getByName("www.sohu.cn");
		//判斷是否可達
		System.out.println("sohu是否可達：" + ip.isReachable(2000)); 
		//取得該InetAddress案例的IP字串
		System.out.println(ip.getHostAddress());
		//根據原始IP位址來取得對應的InetAddress案例
		InetAddress local = InetAddress.getByAddress(new byte[]
		{127,0,0,1});
		System.out.println("本機是否可達：" + local.isReachable(5000)); 
		//取得該InetAddress案例對應的全限定域名
		System.out.println(local.getCanonicalHostName());		
	}
}
