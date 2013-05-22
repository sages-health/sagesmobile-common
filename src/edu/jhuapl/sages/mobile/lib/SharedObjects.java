package edu.jhuapl.sages.mobile.lib;

import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import edu.jhuapl.sages.mobile.lib.crypto.engines.CryptoEngine;

public class SharedObjects {
	
	private static CryptoEngine cryptoEngine;
	
	public SharedObjects() throws NoSuchAlgorithmException, NoSuchPaddingException{
		cryptoEngine = new CryptoEngine("PASSWORD12345678".getBytes());
	}

	/**
	 * The crypto engine to use on both the client and receiver for testing only
	 * @return
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 */
	public static CryptoEngine getCryptoEngine() throws NoSuchAlgorithmException, NoSuchPaddingException {
		if (cryptoEngine == null){
			SharedObjects so = new SharedObjects();
		}
		return cryptoEngine;
	}
	
	

}
