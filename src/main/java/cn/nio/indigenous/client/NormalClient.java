package cn.nio.indigenous.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class NormalClient {
	
	Socket clientSocket = null;
	
	public NormalClient initClient(String ip, int port) {
		try {
			clientSocket = new Socket(ip, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public void read() throws IOException {
		if (clientSocket == null) return;
		PrintWriter    pw   = new PrintWriter((clientSocket.getOutputStream()));
		BufferedReader br   = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		String         line = null;
		while (true) {
			pw.println("客户端发来的消息");
			pw.flush();
			line = br.readLine();
			System.out.println(line);
			
			/*Scanner scanner = new Scanner(System.in);
			String  keyboardLine       = scanner.nextLine();
			pw.write(keyboardLine);
			pw.flush();*/
		}
	}
	
	public static void main(String[] args)  {
		for (int i = 0; i < 2000; i++) {
			new Thread(() -> {
				try {
					new NormalClient().initClient("127.0.0.1", 8090).read();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}).start();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		
		Scanner scanner      = new Scanner(System.in);
		String  keyboardLine = scanner.nextLine();
	}
}
