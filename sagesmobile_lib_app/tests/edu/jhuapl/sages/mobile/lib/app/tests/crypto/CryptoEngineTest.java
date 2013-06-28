package edu.jhuapl.sages.mobile.lib.app.tests.crypto;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.NoSuchPaddingException;

import android.test.AndroidTestCase;
import android.util.Base64;
import edu.jhuapl.sages.mobile.lib.crypto.engines.CryptoEngine;

/**
 * Tests that encryption and decryption is working as expected. AES-128 bit key so far.
 * 
 * @author pokuam1
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
