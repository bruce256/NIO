package cn.nio.indigenous.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * 监听端口号
 * 处理客户端新连接，并分派请求到处理器链中
 *
 * @author 吕胜 lvheng1
 * @date 2023/12/13
 **/
public class Acceptor implements Runnable {
	
	private int port;
	
	public Acceptor(int port) {
		this.port = port;
	}
	
	@Override
	public void run() {
		try {
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			// 非阻塞模式
			serverSocketChannel.configureBlocking(false);
			// 绑定端口号
			serverSocketChannel.bind(new InetSocketAddress(port));
			
			Thread thread = new Thread(new MainReactor(serverSocketChannel));
			thread.setName("MainReactor");
			thread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Thread thread = new Thread(new Acceptor(8090));
		thread.setName("Acceptor");
		thread.start();
	}
}
