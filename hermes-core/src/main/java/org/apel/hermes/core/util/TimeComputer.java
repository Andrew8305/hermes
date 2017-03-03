package org.apel.hermes.core.util;

public class TimeComputer {
	
	public static String computeDuration(String templateMsg, Long startTime, Long endTime){
		Long duration = endTime - startTime;
		String durationMsg = "";
		if(duration > 1000){
			durationMsg = duration / 1000 + "秒";
		}else{
			durationMsg = duration + "毫秒";
		}
		return templateMsg + durationMsg;
	}
	
}
