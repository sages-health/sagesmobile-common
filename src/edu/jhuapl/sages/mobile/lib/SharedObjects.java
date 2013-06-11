package edu.jhuapl.sages.mobile.lib;

import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import android.util.Log;

import edu.jhuapl.sages.mobile.lib.crypto.engines.CryptoEngine;

public class SharedObjects {
	
	private static CryptoEngine cryptoEngine;
	private static boolean isEncryptionOn;
	
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
			//TODO need to come up with long term solution to handle this. leave null and throw exception?
			cryptoEngine = new CryptoEngine("PASSWORD12345678".getBytes());
		}
		return cryptoEngine;
	}
	
	protected static void updateCryptoEngine(String keyVal) throws NoSuchAlgorithmException, NoSuchPaddingException{
		cryptoEngine = new CryptoEngine(keyVal.getBytes());
		Log.i(SharedObjects.class.getName(), "updated CryptoEnginge.");
	}

	public static boolean isEncryptionOn() {
		return isEncryptionOn;
	}

	protected static void setEncryptionOn(boolean isEncryptionOn) {
		SharedObjects.isEncryptionOn = isEncryptionOn;
	}
	
	public static void test_updateCryptoEngine(String keyVal) throws NoSuchAlgorithmException, NoSuchPaddingException{
		updateCryptoEngine(keyVal);
	}

}
