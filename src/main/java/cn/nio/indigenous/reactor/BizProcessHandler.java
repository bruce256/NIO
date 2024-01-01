package cn.nio.indigenous.reactor;

/**
 * @author 吕胜 lvheng1
 * @date 2024/1/1
 **/
public class BizProcessHandler implements Runnable {
	
	String msg;
	
	public BizProcessHandler(String msg) {
		this.msg = msg;
	}
	
	@Override
	public void run() {
		System.out.println("服务端接收到消息：" + msg);
	}
}
