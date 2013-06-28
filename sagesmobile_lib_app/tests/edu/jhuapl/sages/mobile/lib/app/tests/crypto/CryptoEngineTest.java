/********************************************************************************
* Copyright (c) 2013 The Johns Hopkins University/Applied Physics Laboratory
*                              All rights reserved.
*                    
* This material may be used, modified, or reproduced by or for the U.S. 
* Government pursuant to the rights granted under the clauses at             
* DFARS 252.227-7013/7014 or FAR 52.227-14.
*                     
* Licensed under the Apache License, Version 2.0 (the "License");            
* you may not use this file except in compliance with the License.           
* You may obtain a copy of the License at                                    
*                                                                            
*     http://www.apache.org/licenses/LICENSE-2.0                             
*                                                                            
* NO WARRANTY.   THIS MATERIAL IS PROVIDED "AS IS."  JHU/APL DISCLAIMS ALL
* WARRANTIES IN THE MATERIAL, WHETHER EXPRESS OR IMPLIED, INCLUDING (BUT NOT
* LIMITED TO) ANY AND ALL IMPLIED WARRANTIES OF PERFORMANCE,
* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, AND NON-INFRINGEMENT OF
* INTELLECTUAL PROPERTY RIGHTS. ANY USER OF THE MATERIAL ASSUMES THE ENTIRE
* RISK AND LIABILITY FOR USING THE MATERIAL.  IN NO EVENT SHALL JHU/APL BE
* LIABLE TO ANY USER OF THE MATERIAL FOR ANY ACTUAL, INDIRECT,     
* CONSEQUENTIAL, SPECIAL OR OTHER DAMAGES ARISING FROM THE USE OF, OR    
* INABILITY TO USE, THE MATERIAL, INCLUDING, BUT NOT LIMITED TO, ANY DAMAGES
* FOR LOST PROFITS.
********************************************************************************/
package edu.jhuapl.sages.mobile.lib.app.tests.crypto;

import edu.jhuapl.sages.mobile.lib.crypto.engines.CryptoEngine;

import android.test.AndroidTestCase;
import android.util.Base64;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.NoSuchPaddingException;

/**
 * Tests that encryption and decryption is working as expected. AES-128 bit key so far.
 * 
 * @author sages
 * @date 5/20/13 
 */
public class CryptoEngineTest extends AndroidTestCase {

	private CryptoEngine crypto;
	public static String DUMMY_MESSAGE_CLEAR = "This is the test message to encrypt and decrypt";
	
	public CryptoEngineTest() throws NoSuchAlgorithmException, NoSuchPaddingException{
		crypto = new CryptoEngine("PASSWORD12345678".getBytes());
	}

	
	public void testEncryptDecrypt(){
		
		byte[] encryptedBytes = null;
		byte[] decryptedBytes = null;
		try {
			encryptedBytes = getCrypto().encrypt(DUMMY_MESSAGE_CLEAR.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		
		try {
			decryptedBytes = getCrypto().decrypt(encryptedBytes);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		assertEquals(true, Arrays.equals(DUMMY_MESSAGE_CLEAR.getBytes(), decryptedBytes));
	}
	
	
	public void testEncryptDecrypt_Base64(){
		
		byte[] encryptedBytes = null;
		byte[] decryptedBytes = null;
		byte[] b64EncodedCipher = null;
		byte[] b64DecodedCipher = null;
		
		try {
			encryptedBytes = getCrypto().encrypt(DUMMY_MESSAGE_CLEAR.getBytes());
			b64EncodedCipher  = Base64.encode(encryptedBytes, Base64.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			b64DecodedCipher = Base64.decode(b64EncodedCipher, Base64.DEFAULT);
			assertEquals(true, Arrays.equals(encryptedBytes, b64DecodedCipher));
			decryptedBytes = getCrypto().decrypt(encryptedBytes);
			assertEquals(true, Arrays.equals(DUMMY_MESSAGE_CLEAR.getBytes(), decryptedBytes));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
	}


	/**
	 * @return the crypto engine
	 */
	public CryptoEngine getCrypto() {
		return crypto;
	}

}
