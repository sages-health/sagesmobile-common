package edu.jhuapl.sages.mobile.lib.app.tests.crypto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.test.AndroidTestCase;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKey.KeyEnum;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKeyException;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKeyStore;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesPublicKey;

/**
 * 
 * @author pokuam1
 * @date 05/06/13
 */
public class SagesKeyStoreTest extends AndroidTestCase {

	private Context context;
	public static final String keystorepath_sdcard = Environment.getExternalStorageDirectory() + "/sages_keystore_test";
	
	public void setUp(){
		context = getContext();
		File file = new File(keystorepath_sdcard);
		try {
			delete(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void testCreateDir() throws IOException{
		File file = new File(keystorepath_sdcard);
		assertFalse(file.exists());
		assertTrue(file.mkdir());
		assertTrue(file.isDirectory());
		delete(file);
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
