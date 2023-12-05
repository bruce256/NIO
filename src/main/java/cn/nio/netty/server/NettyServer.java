package cn.nio.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 参考示例
 * https://cloud.tencent.com/developer/article/1562150
 *
 * @author 吕胜 lvheng1
 * @date 2023/12/5
 **/
public class NettyServer {
	
	public static void main(String[] args) {
		// 创建对应的 线程池
		// 创建Boss group
		EventLoopGroup boosGroup = new NioEventLoopGroup(1);
		// 创建 workgroup
		EventLoopGroup workGroup = new NioEventLoopGroup();
		// 创建对应的启动类
		ServerBootstrap bootstrap = new ServerBootstrap();
		try {
			// 设置相关的配置信息
			bootstrap.group(boosGroup, workGroup) // 设置对应的线程组
					 .channel(NioServerSocketChannel.class) // 设置对应的通道
					 .option(ChannelOption.SO_BACKLOG, 1024) // 设置线程的连接个数
					 .childHandler(new ChannelInitializer<SocketChannel>() { // 设置
						 /**
						  * 给pipeline 设置处理器
						  * @param socketChannel
						  * @throws Exception
						  */
						 @Override
						 protected void initChannel(SocketChannel socketChannel) throws Exception {
							 socketChannel.pipeline().addLast(new NettyServerHandler());
						 }
					 });
			System.out.println("服务启动了....");
			// 绑定端口  启动服务
			ChannelFuture channelFuture = bootstrap.bind(9999).sync();
			// 对关闭通道进行监听
			channelFuture.channel().closeFuture().sync();
		} catch (Exception e) {
		
		} finally {
			// 优雅停服
			boosGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
		
	}
}
