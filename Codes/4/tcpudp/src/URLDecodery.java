
import java.net.*;

public class URLDecodery
{
	public static void main(String[] args) 
		throws Exception
	{
		//將application/x-www-form-urlencoded字串
		//轉換成普通字串
		String keyWord = URLDecoder.decode(
			"%E7%8B%97%E7%8B%97%E6%90%9E%E7%AC%91", "UTF-8");
		System.out.println(keyWord);
		//將普通字串轉換成
		//application/x-www-form-urlencoded字串
		String urlStr = URLEncoder.encode(
			"會當凌絕頂" , "GBK");
		System.out.println(urlStr);
	}
}
