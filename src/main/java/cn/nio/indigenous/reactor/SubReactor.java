package cn.nio.indigenous.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 主要负责网络的读写（从网络中读字节流、将字节流发送到网络中）
 *
 * @author 吕胜 lvheng1
 * @date 2023/12/13
 **/
public class SubReactor implements Runnable {
	
	private Selector           ioSelector;
	private ThreadPoolExecutor businessThreadPool;
	
	public SubReactor(ThreadPoolExecutor businessThreadPool) {
		this.businessThreadPool = businessThreadPool;
		try {
			this.ioSelector = Selector.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void register(SocketChannel clientChannel) {
		try {
			clientChannel.register(this.ioSelector, SelectionKey.OP_READ);
			// 不调用这个方法，或者select方法不设置超时时间，会读不到消息
//			this.ioSelector.wakeup();
		} catch (ClosedChannelException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				ioSelector.select(1000);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 当注册的事件到达时，方法返回；否则,该方法会一直阻塞
			Iterator<SelectionKey> iterator = this.ioSelector.selectedKeys().iterator();
			while (iterator.hasNext()) {
				SelectionKey key = (SelectionKey) iterator.next();
				// 必须删除，否则下次遍历时还会遍历旧的key
				iterator.remove();
				
				if (key.isReadable()) {
					read(key);
				}
			}
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
			
			businessThreadPool.execute(new BizProcessHandler(msg));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
