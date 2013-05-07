package edu.jhuapl.sages.mobile.lib.app.tests.crypto;

import java.io.File;
import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.os.Environment;
import android.test.AndroidTestCase;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKey;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKey.KeyEnum;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKeyException;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesPrivateKey;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesPublicKey;

/**
 * 
 * @author pokuam1
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
	public void setUp(){
		this.context = getContext();
		
		keyStoreFile = new File(Environment.getExternalStorageDirectory() + "/sageslib_testout");
		
		boolean success = true;
		if (!keyStoreFile.exists()) {
			success = keyStoreFile.mkdir(); 
			assertTrue(true);
		}
	}
	
	public void testSimpleKey(){
		assertTrue(true);
		keyStoreFile = new File(Environment.getExternalStorageDirectory() + "/sageslib_testout");
		
		boolean success = true;
		if (!keyStoreFile.exists()) {
			success = keyStoreFile.mkdir(); 
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
	
	public void testSagesPublicKey() throws UnsupportedEncodingException{
		
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
			assertEquals(testKey.getBytes("UTF-8"), key.getData());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testSagesPrivateKey(){
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
