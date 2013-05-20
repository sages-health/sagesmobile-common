package edu.jhuapl.sages.mobile.lib.crypto.engines;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Performs the actual cryptography. So far configured as a 128-bit AES symmetric crypto system. 
 * 
 * @author pokuam1
 * @date 5/16/13
 */
public class CryptoEngine {

	private SecretKeySpec skeySpec;
	private Cipher cipher; 
	
	public CryptoEngine(String raw) throws NoSuchAlgorithmException, NoSuchPaddingException{
		this.skeySpec = new SecretKeySpec(raw.getBytes(), "AES");
		this.cipher = Cipher.getInstance("AES");
	}
	
	public CryptoEngine(byte[] raw) throws NoSuchAlgorithmException, NoSuchPaddingException{
		this.skeySpec = new SecretKeySpec(raw, "AES");
		this.cipher = Cipher.getInstance("AES");
	}
	
	/**
	 * @return the skeySpec
	 */
	public SecretKeySpec getSkeySpec() {
		return skeySpec;
	}
	
	public byte[] encrypt(byte[] clear) throws Exception {
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(clear);
		return encrypted;
	}
	
	public  byte[] decrypt(byte[] encrypted) throws Exception {
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}

	private  byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(clear);
		return encrypted;
	}
	
	private  byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}
	
	private static byte[] encryptS(byte[] raw, byte[] clear) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(clear);
		return encrypted;
	}
	
	private static byte[] decryptS(byte[] raw, byte[] encrypted) throws Exception {
		SecretKeySpec skKeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skKeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}
}
