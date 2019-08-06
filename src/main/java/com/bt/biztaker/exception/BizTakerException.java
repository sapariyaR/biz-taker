package com.bt.biztaker.exception;

public class BizTakerException extends Exception {
	private static final long serialVersionUID = 3214139495765355085L;

	private String message = null;

	public BizTakerException() {
		super();
	}

	public BizTakerException(String message) {
		super(message);
		this.message = message;
	}

	public BizTakerException(Throwable cause) {
		super(cause);
	}

	@Override
	public String toString() {
		return message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
