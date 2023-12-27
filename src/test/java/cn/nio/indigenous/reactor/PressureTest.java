package cn.nio.indigenous.reactor;

import cn.nio.indigenous.client.NormalClient;
import org.junit.Test;

import java.io.IOException;

/**
 * @author 吕胜 lvheng1
 * @date 2023/12/25
 **/
public class PressureTest {
	
	@Test
	public void pressure() {
		for (int i = 0; i < 2000; i++) {
			new Thread(() -> {
				try {
					new NormalClient().initClient("127.0.0.1", 8090).read();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}).start();
		}
	}

	@Test
	public void pressure1() {
		for (int i = 0; i < 2000; i++) {
			new Thread(() -> {
				try {
					new NormalClient().initClient("127.0.0.1", 8090).read();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}).start();
		}
	}
	
	@Test
	public void pressure2() {
		for (int i = 0; i < 2000; i++) {
			new Thread(() -> {
				try {
					new NormalClient().initClient("127.0.0.1", 8090).read();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}).start();
		}
	}
}
