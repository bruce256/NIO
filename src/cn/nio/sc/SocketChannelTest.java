package cn.nio.sc;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @author lvsheng
 *         project NIO
 *         date 2016年2月4日
 *         time 下午3:08:03
 */
public class SocketChannelTest {

	public static void main(String[] args) throws IOException {
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.connect(new InetSocketAddress("www.tmall.com", 80));
		socketChannel.write(Charset.forName("GBK").encode("GET " + "/ HTTP/1.1" + "\r\n\r\n"));
		ByteBuffer bb = ByteBuffer.allocate(1024);
		socketChannel.read(bb);
		bb.flip();
		System.out.println(Charset.forName("GBK").decode(bb));
	}
}
