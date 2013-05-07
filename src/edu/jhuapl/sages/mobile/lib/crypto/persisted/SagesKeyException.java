package edu.jhuapl.sages.mobile.lib.crypto.persisted;

/**
 * 
 * @author pokuam1
 * @date 05/03/13
 */
public class SagesKeyException extends Exception {

	public SagesKeyException(String message, Exception e) {
		super(message, e);
	}

	public SagesKeyException(String message) {
		super(message);
	}

}
