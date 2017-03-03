package org.apel.hermes.core.exception;

public class DBETLException extends ETLException {

	private static final long serialVersionUID = -6695795654435934147L;

	public DBETLException() {
		super();
	}

	public DBETLException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DBETLException(String message, Throwable cause) {
		super(message, cause);
	}

	public DBETLException(String message) {
		super(message);
	}

	public DBETLException(Throwable cause) {
		super(cause);
	}

}
