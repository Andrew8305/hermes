package org.apel.hermes.core.util;

public class TimeComputer {
	
	public static String computeDuration(String templateMsg, Long startTime, Long endTime){
		Long duration = endTime - startTime;
	    Long hours =  duration / (60 * 60 * 1000);
	    Long remainder =  duration - hours * 3600000;
	    Long mins = remainder / (60 * 1000);
	    remainder = remainder - mins * 60 * 1000;
	    Long secs = remainder / 1000;
	    remainder = remainder - secs * 1000;
	    Long millSecs = remainder;
	    StringBuffer durationMsg = new StringBuffer();
	    if (hours != 0L){
	    	durationMsg.append(hours + "小时");
	    }
	    if (mins != 0L){
	    	durationMsg.append(mins + "分钟");
	    }
	    if (secs != 0L){
	    	durationMsg.append(secs + "秒");
	    }
	    if (millSecs != 0L){
	    	durationMsg.append(millSecs + "毫秒");
	    }
		return templateMsg + durationMsg.toString();
	}
	
}
