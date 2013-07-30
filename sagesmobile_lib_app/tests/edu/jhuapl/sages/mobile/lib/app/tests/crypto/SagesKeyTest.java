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

import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKey;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKey.KeyEnum;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKeyException;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesPrivateKey;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesPublicKey;

import android.content.Context;
import android.os.Environment;
import android.test.AndroidTestCase;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * 
 * @author sages
 * @date 05/01/13
 */
public class SagesKeyTest extends AndroidTestCase {

	public static final String testData = "This is my new key. It will be turned into bytes. Then it will save to disk";
	private Context context;
	private File keyStoreFile; 
	
	//TODO: Unit tests to still write
	/**
	 * Tests (including edge cases):
	 * - no file for locationId
	 * - locationId is left blank
	 * - Keystorefile is left blank or is wrong
	 * 
	 */
	@Override
	public void setUp(){
		this.context = getContext();
		
		keyStoreFile = new File(Environment.getExternalStorageDirectory() + "/sageslib_testout");
		
		if (!keyStoreFile.exists()) {
			keyStoreFile.mkdir(); 
			assertTrue(true);
		}
	}
	
	public void testSimpleKeySave(){
		assertTrue(true);
		keyStoreFile = new File(Environment.getExternalStorageDirectory() + "/sageslib_testout");
		
		if (!keyStoreFile.exists()) {
			keyStoreFile.mkdir(); 
			assertTrue(true);
		}
		
		SagesKey key = new SagesKey(context, "5554", new String[]{""}, testData.getBytes());
		
	
		
		try {
			key.saveKey(keyStoreFile.getAbsolutePath());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}
	
	public void testSagesPublicKeySaveGetAsString() throws UnsupportedEncodingException{
		
		String testKey = "PUBLIC KEY".concat(testData);
				
		SagesPublicKey publickey = new SagesPublicKey(context, "5554", new String[]{""}, testKey.getBytes("UTF-8"));
		try {
			publickey.saveKey(keyStoreFile.getAbsolutePath());
		} catch (SagesKeyException e) {
			e.printStackTrace();
			fail();
		}
		String locationId = "5554";
		try {
			SagesPublicKey key = (SagesPublicKey) SagesKey.loadKey(keyStoreFile.getPath(), locationId, KeyEnum.PUBLIC);
			
			
			assertEquals(testKey, new String (key.getData()));
			assertEquals(testKey.getBytes().length, key.getData().length);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	public void testSagesPublicKeySaveGetBytes() throws UnsupportedEncodingException{
		
		String testKey = "PUBLIC KEY".concat(testData);
		
		SagesPublicKey publickey = new SagesPublicKey(context, "5554", new String[]{""}, testKey.getBytes(/*"UTF-8"*/));
		try {
			publickey.saveKey(keyStoreFile.getAbsolutePath());
		} catch (SagesKeyException e) {
			e.printStackTrace();
			fail();
		}
		String locationId = "5554";
		try {
			SagesPublicKey key = (SagesPublicKey) SagesKey.loadKey(keyStoreFile.getPath(), locationId, KeyEnum.PUBLIC);
			
			
			assertEquals(testKey, new String (key.getData()));
			assertEquals(testKey.getBytes().length, key.getData().length);
			assertTrue(Arrays.equals(testKey.getBytes("UTF-8"), key.getData()));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testSagesPrivateKeySaveGetAsString(){
		SagesPrivateKey privateKey = new SagesPrivateKey(context, "5554", new String[]{""}, "PRIVATE KEY".concat(testData).getBytes());
		try {
			privateKey.saveKey(keyStoreFile.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		String locationId = "5554";
		try {
			SagesPrivateKey key = (SagesPrivateKey) SagesKey.loadKey(keyStoreFile.getPath(), locationId, KeyEnum.PRIVATE);
			assertEquals("PRIVATE KEY".concat(testData), new String(key.getData()));
		} catch (SagesKeyException e) {
			e.printStackTrace();
			fail();
		}
		
	}
}
