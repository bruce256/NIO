package cn.nio.indigenous.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author LvSheng
 */
public class NioServer {
	
	private Selector serverSelector;
	
	private int                corePoolSize = Runtime.getRuntime().availableProcessors();
	private ThreadPoolExecutor ioThreadPool = new ThreadPoolExecutor(corePoolSize, corePoolSize * 2, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(100000), new ThreadFactory() {
		@Override
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r);
			thread.setName("IO-thread");
			return thread;
		}
	});
	
	public NioServer initServer(int port) throws IOException {
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		// 非阻塞模式
		serverChannel.configureBlocking(false);
		// 绑定端口号
		serverChannel.socket().bind(new InetSocketAddress(port));
		this.serverSelector = Selector.open();
		serverChannel.register(serverSelector, SelectionKey.OP_ACCEPT);
		return this;
	}
	
	public void startListening() throws IOException {
		System.out.println("服务端启动成功！");
		while (true) {
			// 当注册的事件到达时，方法返回；否则,该方法会一直阻塞
			serverSelector.select();
			Iterator<SelectionKey> iterator = this.serverSelector.selectedKeys().iterator();
			while (iterator.hasNext()) {
				SelectionKey key = (SelectionKey) iterator.next();
				// 必须删除，否则下次遍历时还会遍历旧的key
				iterator.remove();
				if (key.isAcceptable()) {
					// 这里不能放在一个新的线程里
//					ioThreadPool.execute(() -> accept(key));
					accept(key);
				} else if (key.isReadable()) {
					ioThreadPool.execute(() -> read(key));
				} else if (key.isWritable()) {
					ioThreadPool.execute(() -> write(key));
				}
			}
		}
	}
	
	// 接受客户端的连接请求
	private void accept(SelectionKey key) {
		ServerSocketChannel server = (ServerSocketChannel) key.channel();
		try {
			// 获得和客户端连接的通道
			// 在非阻塞模式下，accept() 方法会立刻返回，如果还没有新进来的连接，返回的将是 null。
			SocketChannel channel = server.accept();
			if (channel != null) {
				// 设置成非阻塞
				channel.configureBlocking(false);
				channel.write(ByteBuffer.wrap(new String("客户端连接成功\n").getBytes()));
				// 注册
				channel.register(this.serverSelector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void read(SelectionKey key) {
		try {
			SocketChannel channel = (SocketChannel) key.channel();
			// 创建读取的缓冲区
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			channel.read(buffer);
			buffer.flip();
			String msg = new String(buffer.array()).trim();
			System.out.println("服务端接收到消息：" + msg);
			channel.write(ByteBuffer.wrap("hello, I'm server".getBytes()));
			channel.register(this.serverSelector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void write(SelectionKey key) {
		// 服务器可读取消息:得到事件发生的Socket通道
		SocketChannel channel = (SocketChannel) key.channel();
		try {
			channel.write(ByteBuffer.wrap("来自服务端的消息".getBytes()));
			channel.register(this.serverSelector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		new NioServer().initServer(8080).startListening();
	}
}