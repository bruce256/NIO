package cn.nio.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NormalClient {

	Socket	clientSocket	= null;

	public NormalClient initClient(String ip, int port) {
		try {
			clientSocket = new Socket(ip, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	public void read() throws IOException {
		PrintWriter pw = new PrintWriter((clientSocket.getOutputStream()));
		BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		String line = null;
		while (true) {
			pw.println("客户端发来的消息");
			pw.flush();
			line = br.readLine();
			System.out.println(line);
		}
	}

	public static void main(String[] args) throws IOException {
		new NormalClient().initClient("127.0.0.1", 8000).read();
	}
}
