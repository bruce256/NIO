package cn.nio.indigenous.reactor;

import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 吕胜 lvheng1
 * @date 2023/12/25
 **/
public class SubReactorGroup {
	
	private int                corePoolSize       = Runtime.getRuntime().availableProcessors();
	private SubReactor[]       subReactors;
	private ThreadPoolExecutor businessThreadPool = new ThreadPoolExecutor(corePoolSize, corePoolSize * 2, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(100000), new ThreadFactory() {
		@Override
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r);
			thread.setName("business-thread");
			return thread;
		}
	});
	
	public SubReactorGroup() {
		subReactors = new SubReactor[corePoolSize];
		
		for (int i = 0; i < corePoolSize; i++) {
			subReactors[i] = new SubReactor(businessThreadPool);
			Thread thread = new Thread(subReactors[i]);
			thread.setName("subreactor");
			thread.start();
		}
	}
	
	public void dispatch(SocketChannel clientChannel, long clientNo) {
		// round-robin 算法
		subReactors[(int) (clientNo % corePoolSize)].register(clientChannel);
	}
	
}
