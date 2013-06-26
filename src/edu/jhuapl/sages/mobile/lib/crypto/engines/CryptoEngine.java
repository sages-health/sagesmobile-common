//*****************************************************************************/
/* Copyright (c) 2013 The Johns Hopkins University/Applied Physics Laboratory */
/*                            All rights reserved.                            */
/*                                                                            */
/* This material may be used, modified, or reproduced by or for the U.S.      */
/* Government pursuant to the rights granted under the clauses at             */
/* DFARS 252.227-7013/7014 or FAR 52.227-14.                                  */
/*                                                                            */
/* Licensed under the Apache License, Version 2.0 (the "License");            */
/* you may not use this file except in compliance with the License.           */
/* You may obtain a copy of the License at                                    */
/*                                                                            */
/*     http://www.apache.org/licenses/LICENSE-2.0                             */
/*                                                                            */
/* NO WARRANTY.   THIS MATERIAL IS PROVIDED "AS IS."  JHU/APL DISCLAIMS ALL   */
/* WARRANTIES IN THE MATERIAL, WHETHER EXPRESS OR IMPLIED, INCLUDING (BUT NOT */
/* LIMITED TO) ANY AND ALL IMPLIED WARRANTIES OF PERFORMANCE,                 */
/* MERCHANTABILITY,FITNESS FOR A PARTICULAR PURPOSE, AND NON-INFRINGEMENT OF  */
/* INTELLECTUAL PROPERTY RIGHTS. ANY USER OF THE MATERIAL ASSUMES THE ENTIRE  */
/* RISK AND LIABILITY FOR USING THE MATERIAL.  IN NO EVENT SHALL JHU/APL BE   */
/* LIABLE TO ANY USER OF THE MATERIAL FOR ANY ACTUAL, INDIRECT,               */
/* CONSEQUENTIAL,SPECIAL OR OTHER DAMAGES ARISING FROM THE USE OF, OR         */
/* INABILITY TO USE, THE MATERIAL, INCLUDING, BUT NOT LIMITED TO, ANY DAMAGES */
/* FOR LOST PROFITS.                                                          */
//*****************************************************************************/
package edu.jhuapl.sages.mobile.lib.crypto.engines;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Performs the actual cryptography. So far configured as a 128-bit AES symmetric crypto system. 
 * 
 * @author sages
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
