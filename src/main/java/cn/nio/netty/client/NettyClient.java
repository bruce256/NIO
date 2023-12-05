package cn.nio.netty.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author 吕胜 lvheng1
 * @date 2023/12/5
 **/
public class NettyClient {
	
	public static void main(String[] args) {
		// 客户端就只需要创建一个 线程组了
		EventLoopGroup loopGroup = new NioEventLoopGroup();
		// 创建 启动器
		Bootstrap bootstrap = new Bootstrap();
		try {
			// 设置相关的参数
			bootstrap.group(loopGroup)
					 .channel(NioSocketChannel.class)
					 .handler(new ChannelInitializer<SocketChannel>() {
						 @Override
						 protected void initChannel(SocketChannel socketChannel) throws Exception {
							 socketChannel.pipeline().addLast(new NettyClientHandler());
						 }
					 });
			// 连接服务
			ChannelFuture future = bootstrap.connect("localhost", 9999).sync();
			// 对服务关闭 监听
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			loopGroup.shutdownGracefully();
		}
		
	}
}