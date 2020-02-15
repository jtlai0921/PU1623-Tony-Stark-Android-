import java.io.*;
import java.net.*;
import java.util.*;

public class useProxySelector
{
	// 測試本機JVM的網路預設組態
	public void setLocalProxy()
	{
		Properties prop = System.getProperties();
		//設定HTTP存取要使用的代理伺服器的位址
		prop.setProperty("http.proxyHost", "192.168.0.96");
		//設定HTTP存取要使用的代理伺服器的通訊埠
		prop.setProperty("http.proxyPort", "8080");
		//設定HTTP存取不需要透過代理伺服器存取的主電腦，
		//可以使用*通配符，多個位址用|分隔
		prop.setProperty("http.nonProxyHosts", "localhost|10.20.*");
		//設定安全HTTP存取使用的代理伺服器位址與通訊埠
		//它沒有https.nonProxyHosts屬性，它按照http.nonProxyHosts 中設定的規則存取
		prop.setProperty("https.proxyHost", "10.10.0.96");
		prop.setProperty("https.proxyPort", "443");
		//設定FTP存取的代理伺服器的主電腦、通訊埠以及不需要使用代理伺服器的主電腦
		prop.setProperty("ftp.proxyHost", "10.10.0.96");
		prop.setProperty("ftp.proxyPort", "2121");
		prop.setProperty("ftp.nonProxyHosts", "localhost|10.10.*");
		//設定socks代理伺服器的位址與通訊埠
		prop.setProperty("socks.ProxyHost", "10.10.0.96");
		prop.setProperty("socks.ProxyPort", "1080");
	}

	// 清除proxy設定
	public void removeLocalProxy()
	{
		Properties prop = System.getProperties();
		//清除HTTP存取的代理伺服器設定
		prop.remove("http.proxyHost");
		prop.remove("http.proxyPort");
		prop.remove("http.nonProxyHosts");
		//清除HTTPS存取的代理伺服器設定
		prop.remove("https.proxyHost");
		prop.remove("https.proxyPort");
		//清除FTP存取的代理伺服器設定
		prop.remove("ftp.proxyHost");
		prop.remove("ftp.proxyPort");
		prop.remove("ftp.nonProxyHosts");
		//清除SOCKS的代理伺服器設定
		prop.remove("socksProxyHost");
		prop.remove("socksProxyPort");
	}
	//測試HTTP存取
	public void showHttpProxy()
		throws MalformedURLException , IOException
	{
		URL url = new URL("http://www.163.cn");
		//直接開啟連線，但系統會呼叫剛設定的HTTP代理伺服器
		URLConnection conn = url.openConnection();
		Scanner scan = new Scanner(conn.getInputStream());
		//讀取遠端主電腦的內容
		while(scan.hasNextLine())
		{
			System.out.println(scan.nextLine());
		}
	}

	public static void main(String[] args)throws IOException
	{
		useProxySelector test = new useProxySelector();
		test.setLocalProxy();
		test.showHttpProxy();
		test.removeLocalProxy();
	}
}
