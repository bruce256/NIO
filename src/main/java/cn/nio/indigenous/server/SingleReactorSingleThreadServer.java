package cn.nio.indigenous.server;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 单线程版本
 *
 * @author 吕胜 lvheng1
 * @date 2023/12/4
 **/
public class SingleReactorSingleThreadServer {
	
	private Selector serverSelector;
	
	public SingleReactorSingleThreadServer initServer(int port) throws IOException {
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
					accept(key);
				} else if (key.isReadable()) {
					read(key);
				}
			}
		}
	}
	
	// 接受客户端的连接请求
	private void accept(SelectionKey key) throws IOException {
		ServerSocketChannel server = (ServerSocketChannel) key.channel();
		// 获得和客户端连接的通道
		SocketChannel channel = server.accept();
		// 设置成非阻塞
		channel.configureBlocking(false);
		channel.write(ByteBuffer.wrap(new String("客户端连接成功\n").getBytes()));
		// 注册
		channel.register(this.serverSelector, SelectionKey.OP_READ);
	}
	
	private void read(SelectionKey key) {
		try {
			SocketChannel channel = (SocketChannel) key.channel();
			// 创建读取的缓冲区
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			channel.read(buffer);
			String msg = new String(buffer.array()).trim();
			System.out.println("服务端：" + msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		new SingleReactorSingleThreadServer().initServer(8080).startListening();
	}
}
