package org.apel.hermes.core.exception;

public class ETLException extends RuntimeException{

	private static final long serialVersionUID = 5543263985869178899L;

	public ETLException() {
		super();
	}

	public ETLException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ETLException(String message, Throwable cause) {
		super(message, cause);
	}

	public ETLException(String message) {
		super(message);
	}

	public ETLException(Throwable cause) {
		super(cause);
	}

	
	
}
