package cn.nio.indigenous.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author 吕胜 lvheng1
 * @date 2023/12/13
 **/
public class MainReactor implements Runnable {
	
	private ServerSocketChannel serverSocketChannel;
	private Selector   serverSelector;
	private SubReactor subReactor;
	
	public MainReactor(ServerSocketChannel serverSocketChannel) {
		try {
			serverSelector = Selector.open();
			// 仅注册accept事件
			serverSocketChannel.register(serverSelector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		subReactor = new SubReactor();
		Thread thread = new Thread(() -> subReactor.read());
		thread.setName("subreactor");
		thread.start();
	}
	
	@Override
	public void run() {
		System.out.println("服务端启动成功");
		while (true) {
			// 当注册的事件到达时，方法返回；否则,该方法会一直阻塞
			try {
				serverSelector.select(1000L);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Iterator<SelectionKey> iterator = this.serverSelector.selectedKeys().iterator();
			
			while (iterator.hasNext()) {
				SelectionKey key = (SelectionKey) iterator.next();
				// 必须删除，否则下次遍历时还会遍历旧的key
				iterator.remove();
				if (key.isAcceptable()) {
					// 这里不能放在一个新的线程里
					ServerSocketChannel server = (ServerSocketChannel) key.channel();
					try {
						// 获得和客户端连接的通道
						// 在非阻塞模式下，accept() 方法会立刻返回，如果还没有新进来的连接，返回的将是 null。
						SocketChannel clientChannel = server.accept();
						if (clientChannel != null) {
							// 设置成非阻塞
							clientChannel.configureBlocking(false);
							clientChannel.write(ByteBuffer.wrap(new String("客户端连接成功\n").getBytes()));
							subReactor.register(clientChannel);
							System.out.println("运行到这里了");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	
	public static void main(String[] args) {
	}
}
