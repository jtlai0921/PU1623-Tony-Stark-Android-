package daili;
/*************************************
 * 一個基礎的代理伺服器類別
 *************************************
 */
import java.net.*;
import java.io.*;

public class Httpdaili extends Thread {
    static public int CONNECT_RETRIES=5;
    static public int CONNECT_PAUSE=5;
    static public int TIMEOUT=50;
    static public int BUFSIZ=1024;
    static public boolean logging = false;
    static public OutputStream log=null;
    // 傳入資料用的Socket
    protected Socket socket;
    // 上級代理伺服器，可選
    static private String parent=null;
    static private int parentPort=-1;
    static public void setParentProxy(String name, int pport) {
	parent=name;
	parentPort=pport;
    }

    // 在指定Socket上建立一個代理執行緒。
    public Httpdaili(Socket s) { socket=s; start(); }

    public void writeLog(int c, boolean browser) throws IOException {
	log.write(c);
    }

    public void writeLog(byte[] bytes,int offset, int len, boolean browser) throws IOException {
	for (int i=0;i<len;i++) writeLog((int)bytes[offset+i],browser);
    }


    // 預設情況下，日志訊息輸出到
    // 標准輸出裝置
    // 衍生類別可以覆蓋它
    public String processHostName(String url, String host, int port, Socket sock) {
	java.text.DateFormat cal=java.text.DateFormat.getDateTimeInstance();
	System.out.println(cal.format(new java.util.Date()) + " - " + url + " " 
              + sock.getInetAddress()+"\n");
	return host;
    }



    // 執行動作的執行緒
    public void run() {
	String line;
	String host;
	int port=80;
        Socket outbound=null;
	try {
	    socket.setSoTimeout(TIMEOUT);
	    InputStream is=socket.getInputStream();
	    OutputStream os=null;
	    try {
                // 取得請求行的內容
		line="";
		host="";
		int state=0;
		boolean space;
		while (true) {
		    int c=is.read();
		    if (c==-1) break;
		    if (logging) writeLog(c,true);
		    space=Character.isWhitespace((char)c); 
		    switch (state) {
		    case 0:
			if (space) continue; 
		        state=1;
		    case 1:
			if (space) {
			    state=2;
			    continue;
			}
			line=line+(char)c;
			break;
		    case 2:
			if (space) continue; // 略過多個空白字元
  		        state=3;
		    case 3:
			if (space) {
			    state=4; 
                            // 只取出主電腦名稱部分
			    String host0=host;
			    int n;
			    n=host.indexOf("//");
			    if (n!=-1) host=host.substring(n+2);
			    n=host.indexOf('/');
			    if (n!=-1) host=host.substring(0,n);
                            // 分析可能存在的通訊埠號
			    n=host.indexOf(":");
			    if (n!=-1) { 
				port=Integer.parseInt(host.substring(n+1));
				host=host.substring(0,n);
			    }
			    host=processHostName(host0,host,port,socket);
			    if (parent!=null) {
				host=parent;
				port=parentPort;
			    }
			    int retry=CONNECT_RETRIES;
			    while (retry--!=0) {
				try {
				    outbound=new Socket(host,port);
				    break;
				} catch (Exception e) { }
                                // 等待
				Thread.sleep(CONNECT_PAUSE);
			    }
			    if (outbound==null) break;
			    outbound.setSoTimeout(TIMEOUT);
			    os=outbound.getOutputStream();
			    os.write(line.getBytes());
			    os.write(' ');
			    os.write(host0.getBytes());
			    os.write(' ');
			    pipe(is,outbound.getInputStream(),os,socket.getOutputStream());
			    break;
			}
			host=host+(char)c;
			break;
		    }
		}
	    }
	    catch (IOException e) { }

    } catch (Exception e) { }
    finally {
		    try { socket.close();} catch (Exception e1) {}
		    try { outbound.close();} catch (Exception e2) {}
       }
    }


    void pipe(InputStream is0, InputStream is1,
         OutputStream os0,  OutputStream os1) throws IOException {
	try {
	    int ir;
	    byte bytes[]=new byte[BUFSIZ];
	    while (true) {
		try {
		    if ((ir=is0.read(bytes))>0) {
			os0.write(bytes,0,ir);
			if (logging) writeLog(bytes,0,ir,true);
		    }
		    else if (ir<0)
			break;
		} catch (InterruptedIOException e) { }
		try {
		    if ((ir=is1.read(bytes))>0) {
			os1.write(bytes,0,ir);
			if (logging) writeLog(bytes,0,ir,false);
		    }
		    else if (ir<0) 
			break;
		} catch (InterruptedIOException e) { }
	    }
	} catch (Exception e0) {
	    System.out.println("Pipe例外: " + e0);
	}
    }


    static public void startProxy(int port,Class clobj) {
	ServerSocket ssock;
	Socket sock;
        try {
	    ssock=new ServerSocket(port);
	    while (true) {
		Class [] sarg = new Class[1];
		Object [] arg= new Object[1];
		sarg[0]=Socket.class;
		try {
		    java.lang.reflect.Constructor cons = clobj.getDeclaredConstructor(sarg);
		    arg[0]=ssock.accept();
		    cons.newInstance(arg); // 建立HttpProxy或其衍生類別的案例
		} catch (Exception e) { 
		    Socket esock = (Socket)arg[0];
		    try { esock.close(); } catch (Exception ec) {}
		}
	    }
	} catch (IOException e) {
	}
    }


    // 測試用的簡單main方法
    static public void main(String args[]) {
	System.out.println("在通訊埠808啟動代理伺服器\n");
	Httpdaili.log=System.out;
	Httpdaili.logging=false;
	Httpdaili.startProxy(808,Httpdaili.class);
      }
    }
