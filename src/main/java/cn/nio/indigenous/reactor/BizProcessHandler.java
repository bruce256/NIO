package cn.nio.indigenous.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author 吕胜 lvheng1
 * @date 2024/1/1
 **/
public class BizProcessHandler implements Runnable {

    private String msg;
    private SocketChannel socketChannel;

    public BizProcessHandler(SocketChannel socketChannel, String msg) {
        this.socketChannel = socketChannel;
        this.msg = msg;
    }

    @Override
    public void run() {
        System.out.println("服务端接收到消息：" + msg);
        ByteBuffer byteBuffer = ByteBuffer.wrap("服务端收到了你的消息".getBytes());
        try {
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
