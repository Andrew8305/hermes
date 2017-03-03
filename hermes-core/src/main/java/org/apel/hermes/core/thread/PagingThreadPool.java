package org.apel.hermes.core.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 分页线程池，当分页读取数据时候会进行使用
 * @author lijian
 *
 * @param <T>
 */
public class PagingThreadPool<T> {

	private List<Callable<T>> callables = new ArrayList<>();
	private static int DEFAULT_THREAD_SIZE = 10;
	private ExecutorService threadPool;
	private int threadSize;
	
	public PagingThreadPool(int threadSize){
		//当分页后的总页数大于阈值时，以最大10个线程创建线程池，反之则创建更小的线程池
		if(threadSize > DEFAULT_THREAD_SIZE){
			this.threadSize = DEFAULT_THREAD_SIZE;
		}else{
			this.threadSize = threadSize;
		}
		threadPool = Executors.newFixedThreadPool(this.threadSize);
	}
	
	public void addCallable(Callable<T> callable){
		callables.add(callable);
	}
	
	public void setCallables(List<Callable<T>> callables){
		this.callables = callables;
	}
	
	/**
	 * 执行线程
	 */
	public List<T> execute(){
		List<T> futureResult = new ArrayList<>();
		try {
			List<Future<T>> futures = threadPool.invokeAll(callables);
			for (Future<T> future : futures) {
				futureResult.add(future.get());
			}
			threadPool.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return futureResult;
	}
	
}
