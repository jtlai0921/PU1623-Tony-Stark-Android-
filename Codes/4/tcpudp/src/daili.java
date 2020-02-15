
import java.net.*;
import java.io.*;
import java.util.*;

public class daili
{
	Proxy proxy;
	URL url;
	URLConnection conn;
	//從網路透過代理讀資料
	Scanner scan;
	PrintStream ps ;
	//下面是代理伺服器的位址和通訊埠，
	//換成實際有效的代理伺服器的位址和通訊埠
	String proxyAddress = "78.39.195.11";
	int proxyPort;
	//下面是你試圖開啟的網站位址
	String urlStr = "http://www.xxx.cn";

	public void init()
	{
		try
		{
			url = new URL(urlStr);
			//建立一個代理伺服器物件
			proxy = new Proxy(Proxy.Type.HTTP,
				new InetSocketAddress(proxyAddress , proxyPort));
			//使用特殊的代理伺服器開啟連線
			conn = url.openConnection(proxy);
			//設定逾時時長。
			conn.setConnectTimeout(5000);
			scan = new Scanner(conn.getInputStream());
			//起始化輸出流
			ps = new PrintStream("Index.htm");
			while (scan.hasNextLine())
			{
				String line = scan.nextLine();
				//在主控台輸出網頁資源內容
				System.out.println(line);
				//將網頁資源內容輸出到指定輸出流
				ps.println(line);
			}
		}
		catch(MalformedURLException ex)
		{
			System.out.println(urlStr + "不是有效的位址！");
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		//關閉資源
		finally
		{
			if (ps != null)
			{
				ps.close();
			}
		}
	}

    public static void main(String[] args) 
    {
		new daili().init();
    }
}