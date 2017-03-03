package org.apel.hermes.core.util;

import java.util.List;
import java.util.Map;

import org.apel.hermes.core.listener.JobListener;
import org.apel.hermes.core.listener.StepListener;
import org.apel.hermes.core.listener.TaskListener;
import org.apel.hermes.core.optional.ETLOptional;
import org.apel.hermes.core.resource.ETLResource;

public class ETLListenerUtil {
	
	public static void jobOnStart(JobListener jobListener){
		if(jobListener != null){
			jobListener.onStart();
		}
	}
	
	public static void jobOnEnd(JobListener jobListener){
		if(jobListener != null){
			jobListener.onEnd();
		}
	}
	
	public static void taskOnStart(TaskListener taskListener){
		if(taskListener != null){
			taskListener.onStart();
		}
	}
	
	public static void taskOnEnd(TaskListener taskListener){
		if(taskListener != null){
			taskListener.onEnd();
		}
	}
	
	public static void stepBeforeExtract(StepListener stepListener, ETLResource inputEtlResource, ETLResource outputEtlResource, ETLOptional optional){
		if(stepListener != null){
			stepListener.beforeExtract(inputEtlResource, outputEtlResource, optional);
		}
	}
	
	public static void stepAfterExtract(StepListener stepListener, ETLResource inputEtlResource, ETLResource outputEtlResource, ETLOptional optional){
		if(stepListener != null){
			stepListener.afterExtract(inputEtlResource, outputEtlResource, optional);
		}
	}
	
	public static void stepBeforeConvert(StepListener stepListener, ETLResource inputEtlResource, ETLResource outputEtlResource, ETLOptional optional){
		if(stepListener != null){
			stepListener.beforeConvert(inputEtlResource, outputEtlResource, optional);
		}
	}
	
	public static void stepAfterConvert(StepListener stepListener, ETLResource inputEtlResource, ETLResource outputEtlResource, ETLOptional optional){
		if(stepListener != null){
			stepListener.afterConvert(inputEtlResource, outputEtlResource, optional);
		}
	}
	
	public static void stepBeforeLoad(StepListener stepListener, ETLResource inputEtlResource, ETLResource outputEtlResource, ETLOptional optional, List<Map<String, Object>> data){
		if(stepListener != null){
			stepListener.beforeLoad(inputEtlResource, outputEtlResource, optional, data);
		}
	}
	
	public static void stepAfterLoad(StepListener stepListener, ETLResource inputEtlResource, ETLResource outputEtlResource, ETLOptional optional){
		if(stepListener != null){
			stepListener.afterLoad(inputEtlResource, outputEtlResource, optional);
		}
	}
	
	public static void stepOnStart(StepListener stepListener, ETLResource inputEtlResource, ETLResource outputEtlResource){
		if(stepListener != null){
			stepListener.onStart(inputEtlResource, outputEtlResource);
		}
	}
	
	public static void stepOnEnd(StepListener stepListener, ETLResource inputEtlResource, ETLResource outputEtlResource){
		if(stepListener != null){
			stepListener.onEnd(inputEtlResource, outputEtlResource);
		}
	}
	
}
