
import java.net.*;

public class URLDecodery
{
	public static void main(String[] args) 
		throws Exception
	{
		//�Napplication/x-www-form-urlencoded�r��
		//�ഫ�����q�r��
		String keyWord = URLDecoder.decode(
			"%E7%8B%97%E7%8B%97%E6%90%9E%E7%AC%91", "UTF-8");
		System.out.println(keyWord);
		//�N���q�r���ഫ��
		//application/x-www-form-urlencoded�r��
		String urlStr = URLEncoder.encode(
			"�|��⵴��" , "GBK");
		System.out.println(urlStr);
	}
}
