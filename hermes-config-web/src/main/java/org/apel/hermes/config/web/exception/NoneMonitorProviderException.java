package org.apel.hermes.config.web.exception;

import org.apel.gaia.commons.exception.PlatformException;

public class NoneMonitorProviderException extends PlatformException {

	private static final long serialVersionUID = -8660644166219990577L;

	public NoneMonitorProviderException(){}
	public NoneMonitorProviderException(String msg){
		super(msg);
	}
}
