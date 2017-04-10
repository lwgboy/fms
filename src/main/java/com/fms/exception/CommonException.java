package com.fms.exception;

public class CommonException extends RuntimeException {


	private static final long serialVersionUID = 7608330395582067150L;

	/**
	 * @param message
	 *            ԭ�쳣��Ϣ
	 */
	public CommonException(String message) {
		super(message);
	}

	/**
	 * @param message
	 *            �쳣��Ϣ
	 */
	public CommonException(String message, Exception e) {
		super(message, e);
	}
	
}
