import java.io.*;
import java.net.*;
import java.util.*;

public class useProxySelector
{
	// ���ե���JVM�������w�]�պA
	public void setLocalProxy()
	{
		Properties prop = System.getProperties();
		//�]�wHTTP�s���n�ϥΪ��N�z���A������}
		prop.setProperty("http.proxyHost", "192.168.0.96");
		//�]�wHTTP�s���n�ϥΪ��N�z���A�����q�T��
		prop.setProperty("http.proxyPort", "8080");
		//�]�wHTTP�s�����ݭn�z�L�N�z���A���s�����D�q���A
		//�i�H�ϥ�*�q�t�šA�h�Ӧ�}��|���j
		prop.setProperty("http.nonProxyHosts", "localhost|10.20.*");
		//�]�w�w��HTTP�s���ϥΪ��N�z���A����}�P�q�T��
		//���S��https.nonProxyHosts�ݩʡA������http.nonProxyHosts ���]�w���W�h�s��
		prop.setProperty("https.proxyHost", "10.10.0.96");
		prop.setProperty("https.proxyPort", "443");
		//�]�wFTP�s�����N�z���A�����D�q���B�q�T��H�Τ��ݭn�ϥΥN�z���A�����D�q��
		prop.setProperty("ftp.proxyHost", "10.10.0.96");
		prop.setProperty("ftp.proxyPort", "2121");
		prop.setProperty("ftp.nonProxyHosts", "localhost|10.10.*");
		//�]�wsocks�N�z���A������}�P�q�T��
		prop.setProperty("socks.ProxyHost", "10.10.0.96");
		prop.setProperty("socks.ProxyPort", "1080");
	}

	// �M��proxy�]�w
	public void removeLocalProxy()
	{
		Properties prop = System.getProperties();
		//�M��HTTP�s�����N�z���A���]�w
		prop.remove("http.proxyHost");
		prop.remove("http.proxyPort");
		prop.remove("http.nonProxyHosts");
		//�M��HTTPS�s�����N�z���A���]�w
		prop.remove("https.proxyHost");
		prop.remove("https.proxyPort");
		//�M��FTP�s�����N�z���A���]�w
		prop.remove("ftp.proxyHost");
		prop.remove("ftp.proxyPort");
		prop.remove("ftp.nonProxyHosts");
		//�M��SOCKS���N�z���A���]�w
		prop.remove("socksProxyHost");
		prop.remove("socksProxyPort");
	}
	//����HTTP�s��
	public void showHttpProxy()
		throws MalformedURLException , IOException
	{
		URL url = new URL("http://www.163.cn");
		//�����}�ҳs�u�A���t�η|�I�s��]�w��HTTP�N�z���A��
		URLConnection conn = url.openConnection();
		Scanner scan = new Scanner(conn.getInputStream());
		//Ū�����ݥD�q�������e
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
