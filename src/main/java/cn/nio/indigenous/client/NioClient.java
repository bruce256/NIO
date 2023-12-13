package cn.nio.indigenous.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author LvSheng
 */
public class NioClient {
	
	private Selector      selector;
	private SocketChannel clientChannel;
	
	public NioClient initClient(String ip, int port) throws IOException {
		clientChannel = SocketChannel.open();
		// 非阻塞
		clientChannel.configureBlocking(false);
		this.selector = Selector.open();
		
		// connect方法的注释：此方法返回 false，并且必须在以后通过调用 finishConnect 方法来完成该连接操作。
		boolean result = clientChannel.connect(new InetSocketAddress(ip, port));
		System.out.println(result); // 返回false
		// 注册
		clientChannel.register(selector, SelectionKey.OP_CONNECT);
		return this;
	}
	
	public void startListening() throws IOException {
		while (true) {
			selector.select();
			Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
			while (iterator.hasNext()) {
				SelectionKey key = (SelectionKey) iterator.next();
				// 必须删除，否则下次遍历时还会遍历旧的key
				iterator.remove();
				if (key.isConnectable()) {
					connect(key);
				} else if (key.isReadable()) {
					read(key);
				} else if (key.isWritable()) {
					write(key);
				}
			}
		}
	}
	
	private void connect(SelectionKey key) throws IOException, ClosedChannelException {
		SocketChannel channel = (SocketChannel) key.channel();
		// 如果正在连接，则完成连接
		if (channel.isConnectionPending()) {
			channel.finishConnect();
		}
		channel.configureBlocking(false);
		channel.write(ByteBuffer.wrap(new String("客户端连接成功").getBytes()));
		// 注册
		channel.register(this.selector, SelectionKey.OP_READ);
	}
	
	private void read(SelectionKey key) throws IOException {
		// 服务器可读取消息:得到事件发生的Socket通道
		SocketChannel channel = (SocketChannel) key.channel();
		// 创建读取的缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		channel.read(buffer);
		buffer.flip();
		String msg = new String(buffer.array()).trim();
		System.out.println("客户端收到信息：" + msg);
		channel.write(ByteBuffer.wrap("hello, I'm client.".getBytes()));
//		channel.register(this.selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
	}
	
	private void write(SelectionKey key) {
		// 服务器可读取消息:得到事件发生的Socket通道
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer    buffer  = ByteBuffer.allocate(1024);
		Scanner       scanner = new Scanner(System.in);
		while (true) {
			String line = scanner.nextLine();
			try {
				channel.write(buffer.put(line.getBytes()));
				buffer.flip();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * 启动客户端测试
	 *
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		new NioClient().initClient("127.0.0.1", 8080)
					   .startListening();
	}
	
}