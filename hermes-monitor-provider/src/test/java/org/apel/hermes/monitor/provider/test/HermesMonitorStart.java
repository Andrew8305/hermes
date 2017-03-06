package org.apel.hermes.monitor.provider.test;

import java.util.concurrent.CountDownLatch;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HermesMonitorStart {

	public static void main(String[] args) throws Exception {
		CountDownLatch cdl = new CountDownLatch(1);
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:META-INF/spring/module-*.xml");
		context.start();
		System.out.println("系统服务已经启动****");
		cdl.await();
		context.close();
	}
	
	
}
