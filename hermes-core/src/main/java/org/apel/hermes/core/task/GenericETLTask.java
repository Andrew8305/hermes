package org.apel.hermes.core.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.apel.hermes.config.api.domain.JobLogCollector;
import org.apel.hermes.core.context.ETLContext;
import org.apel.hermes.core.listener.TaskListener;
import org.apel.hermes.core.resource.ETLResource;
import org.apel.hermes.core.step.ETLStep;
import org.apel.hermes.core.thread.SingleResourceTaskThread;
import org.apel.hermes.core.util.ThreadUtil;

/**
 * 所有的自行实现的task必须集成此类，该类为顶层ETLtask接口的抽象类实现
 * @author lijian
 *
 */
public abstract class GenericETLTask implements ETLTask{
	
	
	protected static final int DEFAULT_SEMAPHORE_PERMITS = 10;
	
	protected ETLContext context;
	
	private TaskListener taskListener;
	
	protected List<ETLResource> inputETLResources;
	
	protected List<ETLResource> outputETLResources;
	
	protected List<ETLStep> additionSteps = new ArrayList<>();
	
	protected List<ETLStep> scanCustomSteps = new ArrayList<>();
	
	protected List<ETLStep> allOrderedSteps = new ArrayList<>();
	
	public GenericETLTask(){
		if(this instanceof TaskListener){
			this.taskListener = (TaskListener)this;
		}
	}
	
	@Override
	public void addScanCustomSteps(List<ETLStep> scanCustomSteps) {
		this.scanCustomSteps.addAll(scanCustomSteps);
	}
	
	@Override
	public void addScanCustomStep(ETLStep scanCustomStep) {
		this.scanCustomSteps.add(scanCustomStep);
	}
	
	@Override
	public void setContext(ETLContext context) {
		this.context = context;
	}

	@Override
	public ETLContext getContext() {
		return this.context;
	}

	@Override
	public void setInputETLResources(List<ETLResource> inputResources) {
		this.inputETLResources = inputResources;
	}

	@Override
	public void setoutputETLResources(List<ETLResource> outputResources) {
		this.outputETLResources = outputResources;
	}

	@Override
	public List<ETLResource> getInputETLResource() {
		return this.inputETLResources;
	}

	@Override
	public List<ETLResource> getOutputETLResource() {
		// TODO Auto-generated method stub
		return this.outputETLResources;
	}

	@Override
	public void additionInnerStep(List<ETLStep> steps) {
		//适配器 
		//空实现 添加额外step的回调接口
	}

	@Override
	public void sortSteps() {
		//调用添加额外step的回调接口
		additionInnerStep(this.additionSteps);
		//让所有steps排序
		allOrderedSteps.addAll(this.scanCustomSteps);
		allOrderedSteps.addAll(this.additionSteps);
		Collections.sort(allOrderedSteps, new Comparator<ETLStep>(){
			@Override
			public int compare(ETLStep s1, ETLStep s2) {
				if(s1.order() > s2.order()){
					return 1;
				}else if(s1.order() == s2.order()){
					return 0;
				}else{
					return -1;
				}
			}
		});
		//设置context和当前taskId
		for (ETLStep etlStep : allOrderedSteps) {
			etlStep.taskId(id());
			etlStep.setContext(context);
		}
	}
	
	@Override
	public List<ETLStep> getAllSteps() {
		return allOrderedSteps;
	}
	
	/**
	 * job的线程信号量限制，默认大小是10个，可以重写以覆盖默认值
	 * @return
	 */
	protected int semaphorePermits(){
		return DEFAULT_SEMAPHORE_PERMITS;
	}

	@Override
	public void start(JobLogCollector jobLogCollector, String runtimeVersionId) {
		int semaphorePermits = semaphorePermits();
		if(semaphorePermits <= 0){
			semaphorePermits = DEFAULT_SEMAPHORE_PERMITS;
		}
		Semaphore semaphore = new Semaphore(semaphorePermits);
		List<Thread> stepThreads = new ArrayList<>();
		for (ETLResource inputETLResource : inputETLResources) {
			for (ETLResource outputETLResource : outputETLResources) {
				Thread t = new Thread(new SingleResourceTaskThread(id(), inputETLResource, outputETLResource, allOrderedSteps, this.taskListener, jobLogCollector, semaphore, runtimeVersionId));
				stepThreads.add(t);
				t.start();
			}
		}
		ThreadUtil.join(stepThreads);
	}
	
}
