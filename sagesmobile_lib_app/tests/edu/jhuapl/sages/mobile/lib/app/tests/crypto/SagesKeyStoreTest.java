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

import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKey.KeyEnum;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKeyException;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKeyStore;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesPublicKey;
import android.content.Context;
import android.os.Environment;
import android.test.AndroidTestCase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 
 * @author sages
 * @date 05/06/13
 */
public class SagesKeyStoreTest extends AndroidTestCase {

	private Context context;
	public static final String keystorepath_sdcard = Environment.getExternalStorageDirectory() + "/sages_keystore_test";
	
	@Override
	public void setUp(){
		context = getContext();
		File file = new File(keystorepath_sdcard);
		try {
			if (file.exists())
				delete(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void testCreateNewDir() throws IOException{
		File file = new File(keystorepath_sdcard);
		assertFalse(file.exists());
		assertTrue(file.mkdir());
		assertTrue(file.isDirectory());
		delete(file);
	}
	
	public void testNullFile() {
		try {
			@SuppressWarnings("unused")
			SagesKeyStore nullStore = new SagesKeyStore(null);
			fail("SagesKeyException should have been thrown for a null file parameter.");
		} catch (SagesKeyException e) {
		}  
	}
	
	public void testNewStoreWhenAlreadyExists() {
		File file = new File(keystorepath_sdcard);
		try {
			@SuppressWarnings("unused")
			SagesKeyStore newStore = new SagesKeyStore(file);
			newStore = new SagesKeyStore(file);
		} catch (SagesKeyException e) {
			e.printStackTrace();
			fail("If store already was created in this location no exceptions should occur.");
		}  
	}
	
	public void testNewKeyStoreAddAndGetKey(){
		File file = new File(keystorepath_sdcard);
		try {
			SagesKeyStore store = new SagesKeyStore(file);
			
			SagesPublicKey pubKey = new SagesPublicKey(context, "8888", null, "Test Public Key for SagesKeyStoreTest".getBytes());
			store.addKey(pubKey);
			
			assertEquals(new String(pubKey.getData()), new String(store.lookupKey("8888", KeyEnum.PUBLIC).getData()));
			assertNull(store.lookupKey("8888", KeyEnum.PRIVATE));
		} catch (SagesKeyException e) {
			e.printStackTrace();
			fail();
		} finally {
			try {
				delete(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	/**
	 * should not happen in production -- but with file saved to sdcard, the directory could be deleted.
	 * Keystore should always check for existence of its underlying file?
	 */
	public void testDeleteKeyStoreFileSaveKey(){
		
	}
	
	//http://stackoverflow.com/a/779529
	private void delete(File f) throws IOException {
		  if (f.isDirectory()) {
		    for (File c : f.listFiles())
		      delete(c);
		  }
		  if (!f.delete())
		    throw new FileNotFoundException("Failed to delete file: " + f);
		}
}
