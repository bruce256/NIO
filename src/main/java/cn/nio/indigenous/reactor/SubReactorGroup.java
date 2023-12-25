package cn.nio.indigenous.reactor;

import java.nio.channels.SocketChannel;

/**
 * @author 吕胜 lvheng1
 * @date 2023/12/25
 **/
public class SubReactorGroup {
	
	private int          corePoolSize = Runtime.getRuntime().availableProcessors();
	private SubReactor[] subReactors;
	
	public SubReactorGroup() {
		subReactors = new SubReactor[corePoolSize];
		
		for (int i = 0; i < corePoolSize; i++) {
			subReactors[i] = new SubReactor();
			Thread thread = new Thread(subReactors[i]);
			thread.setName("subreactor");
			thread.start();
		}
	}
	
	public void dispatch(SocketChannel clientChannel, long clientNo) {
		subReactors[(int) (clientNo % corePoolSize)].register(clientChannel);
	}
	
}
