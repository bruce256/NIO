package cn.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * 传统阻塞式IO服务端
 *
 * @author 吕胜 lvheng1
 * @date 2022/8/12
 **/
public class BioServer {
	
	private ServerSocket serverSocket;
	
	public BioServer initServer(int port) {
		try {
			serverSocket = new ServerSocket();
			SocketAddress socketAddress = new InetSocketAddress("127.0.0.1", port);
			serverSocket.bind(socketAddress);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return this;
	}
	
	public void listen() throws IOException {
		while (true) {
			Socket socket   = serverSocket.accept();
			Thread ioThread = new Thread(new IoThread(socket));
			ioThread.start();
		}
	}
}

class IoThread implements Runnable {
	
	private Socket clientSocket;
	
	public IoThread(Socket socket) {
		this.clientSocket = socket;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				InputStream    inputStream    = clientSocket.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				String         line           = bufferedReader.readLine();
				System.out.println(line);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
}
