package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class AndroidServer implements Runnable{
	public void run() {
		try {
			ServerSocket serverSocket=new ServerSocket(54321);
			while(true)
			{
				System.out.println("���ݱ����ϥΪ̳s�u�G");
				//�����Τ�ݽШD
				Socket client=serverSocket.accept();
				try
				{
					//�����Τ�ݰT��
					BufferedReader in=new BufferedReader(new InputStreamReader(client.getInputStream()));
					String str=in.readLine();
					System.out.println("read:  "+str);
					//�V���A���ǰe�T��
					PrintWriter out=new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),true);
					out.println("return	"+str);
					in.close();
					out.close();
				}catch(Exception ex)
				{
					System.out.println(ex.getMessage());
					ex.printStackTrace();
				}
				finally
				{
					client.close();
					System.out.println("close");
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	public static void main(String [] args)
	{
		Thread desktopServerThread=new Thread(new AndroidServer());
		desktopServerThread.start();
	}
}
