package cn.nio.indigenous.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * @author 吕胜 lvheng1
 * @date 2022/8/12
 **/
public class NormalServer {
	private ServerSocket serverSocket;
	
	public NormalServer initServer(int port) {
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
			Socket socket = serverSocket.accept();
			
		}
	}
}
