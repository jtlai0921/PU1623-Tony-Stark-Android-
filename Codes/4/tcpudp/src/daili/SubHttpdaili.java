package daili;
// HttpProxy的簡單衍生類別
// 不記錄主電腦名字
// 在日志輸出的每一行前面加上一個'*'

import java.io.*;
import java.net.*;

public class SubHttpdaili extends Httpdaili {
    static private boolean first=true;
    public SubHttpdaili(Socket s) {
	super(s);
    }
    public void writeLog(int c, boolean browser) throws IOException {
	if (first) log.write('*');
	first=false;
	log.write(c);
	if (c=='\n') log.write('*');
    }
    public String processHostName(String url, String host, int port, Socket sock) {
	// 直接傳回
	return host;
    }
    // 測試用的簡單main方法
    static public void main(String args[]) {
	System.out.println("在通訊埠808啟動代理伺服器\n");
	Httpdaili.log=System.out;
	Httpdaili.logging=true;
	Httpdaili.startProxy(808,SubHttpdaili.class);
      }


}
