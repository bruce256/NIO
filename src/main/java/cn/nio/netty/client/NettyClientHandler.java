package cn.nio.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author 吕胜 lvheng1
 * @date 2023/12/5
 **/
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
	
	/**
	 * 连接上服务的回调方法
	 *
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 发送数据
		System.out.println("连接上了 服务器....");
		ctx.writeAndFlush(Unpooled.copiedBuffer("哈哈 你好呀!!!", CharsetUtil.UTF_8));
	}
	
	/**
	 * 读取服务端返回的信息
	 *
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		System.out.println("服务端返回的信息：" + buf.toString(CharsetUtil.UTF_8));
	}
}