package daili;
// HttpProxy��²��l�����O
// ���O���D�q���W�r
// �b��ӿ�X���C�@��e���[�W�@��'*'

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
	// �����Ǧ^
	return host;
    }
    // ���եΪ�²��main��k
    static public void main(String args[]) {
	System.out.println("�b�q�T��808�ҰʥN�z���A��\n");
	Httpdaili.log=System.out;
	Httpdaili.logging=true;
	Httpdaili.startProxy(808,SubHttpdaili.class);
      }


}
