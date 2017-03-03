package org.apel.hermes.core.context;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apel.hermes.core.job.ETLJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ETL同步程序上下文
 * 上下文中会记录当前环境中所有的job
 * @author lijian
 *
 */
@Component
public class ETLContext {

	private static Logger LOG = LoggerFactory.getLogger(ETLContext.class);
	
	private Map<String, ETLJob> jobs = new HashMap<>();
	
	public void addJob(String jobId, ETLJob job){
		job.setContext(this);
		jobs.put(jobId, job);
	}
	
	public void removeJob(String jobId){
		jobs.remove(jobId);
	}
	
	public void clear(){
		jobs.clear();
	}
	
	public ETLJob getJob(String jobId){
		return jobs.get(jobId);
	}
	
	public Map<String, ETLJob> getAllJobs(){
		return this.jobs;
	}
	
	/**
	 * 定时运行环境当中所有的job
	 */
	public void schedule(){
		for (ETLJob job : jobs.values()) {
			if(!StringUtils.isEmpty(job.getSchedule())){
				String[] scheduleStrArray = job.getSchedule().split(";");
				if(scheduleStrArray.length > 2){
					LOG.warn("作业id为:" + job.id() + "的作业配置schedule" + job.getSchedule() + "有错误，不能运行，请检查");
					continue;
				}else if(scheduleStrArray.length == 2){
					if(!ETLJob.SCHEDULE_NONE.equals(scheduleStrArray[0])){
						LOG.warn("作业id为:" + job.id() + "的作业配置schedule" + job.getSchedule() + "有错误，不能运行，请检查");
						continue;
					}else{
						job.start();
						job.setSchedule(scheduleStrArray[1]);
						job.schedule();
					}
				}else{
					if(ETLJob.SCHEDULE_NONE.equals(job.getSchedule())){
						job.start();
					}else{
						job.schedule();
					}
				}
			}else{
				LOG.warn("作业id为:" + job.id() + "的作业配置schedule为空，不能运行，请检查！");
			}
		}
	}
	
	public void start(){
		for (ETLJob job : jobs.values()) {
			job.start();
		}
	}
}
