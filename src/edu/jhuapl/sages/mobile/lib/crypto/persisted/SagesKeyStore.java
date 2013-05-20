package edu.jhuapl.sages.mobile.lib.crypto.persisted;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.os.Environment;
import android.util.Log;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKey.KeyEnum;

/**
 * 
 * @author pokuam1
 * @date 05/04/13
 */
public class SagesKeyStore implements KeyStoreI {

	private File keyStoreFile; 
	public static final String dummy_key_store_file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "sageslib_testout";
	
	public SagesKeyStore(File keyStoreFile) throws SagesKeyException{
		this.keyStoreFile = keyStoreFile;
		init();
	}
	
	/**
	 * Initializes key stores directory for whatever file location was passed into constructor. Null values not allowed
	 * @throws SagesKeyException if no file location set, or if error occurs during initialization
	 */
	protected void init() throws SagesKeyException{
		if (keyStoreFile == null) throw new SagesKeyException("You must specify a file location for the KeyStore");
		
		if (!keyStoreFile.mkdir()){
			if (!keyStoreFile.exists()) {
				throw new SagesKeyException("key store file failed to create at: " + keyStoreFile.getAbsolutePath());
			}
		}
		Log.i("SagesCrypto", "Keystore located: " + keyStoreFile.getAbsolutePath());
	}

	@Override
	public Map<KeyEnum, SagesKey> lookupKeys(SagesContact contact) throws SagesKeyException {
		return lookupKeys(contact.getAgentId());
	}
	
	@Override
	public SagesKey lookupKey(String agentId, KeyEnum keyType) throws SagesKeyException {
		return SagesKey.loadKey(keyStoreFile.getAbsolutePath(), agentId, keyType);
	}

	@Override
	public Map<KeyEnum, SagesKey> lookupKeys(String agentId) throws SagesKeyException {
		Map<KeyEnum, SagesKey> keymap = new HashMap<KeyEnum, SagesKey>();
		
		SagesPrivateKey privateKey = (SagesPrivateKey) SagesKey.loadKey(keyStoreFile.getAbsolutePath(), agentId, KeyEnum.PRIVATE);
		SagesPublicKey publicKey = (SagesPublicKey) SagesKey.loadKey(keyStoreFile.getAbsolutePath(), agentId, KeyEnum.PUBLIC);
		
		keymap.put(KeyEnum.PRIVATE, privateKey);
		keymap.put(KeyEnum.PUBLIC, publicKey);
		return keymap;
	}

	@Override
	public void addKey(SagesKey key) throws SagesKeyException {
		if (key.checkKey()){
			throw new SagesKeyException("This key contains invalid values. Fix the key before saving.");
		}
		
		// TODO: makesure this is behavior we want. basically file could get deleted manually if on sdcard while testing.
		if (!keyStoreFile.exists()){
			Log.w("SagesCrypto", "The keystore file does not exist. It is going to be recreated in order to save the key.");
			if(!keyStoreFile.mkdir()){
				Log.e("SagesCrypto", "The keystore file could not be recreated at " + keyStoreFile.getAbsolutePath() + ".");
				throw new SagesKeyException("The keystore file could not be recreated at " + keyStoreFile.getAbsolutePath() + ".");
			}
		}
		key.saveKey(keyStoreFile.getAbsolutePath());
	}

	/**
	 * This probably won't be made public using it for testing right now. TODO
	 */
	@Override
	public void removeAllKeysFor(String agentId) throws SagesKeyException {
		try {
			File dir = getDirForKeys(agentId);
			if (dir.isDirectory()){
				delete(dir);
			}
			} catch (IOException e) {
				e.printStackTrace();
				throw new SagesKeyException("Serious issue occured trying to delete keys.");
			}
	}
	
	protected File getDirForKeys(String agentId) throws SagesKeyException{
		return new File(keyStoreFile.getAbsolutePath() + "/keys_" + agentId);
	}

	
	private void delete(File f) throws IOException {
		  if (f.isDirectory()) {
		    for (File c : f.listFiles())
		      delete(c);
		  }
		  if (!f.delete())
		    throw new FileNotFoundException("Failed to delete file: " + f);
	}



}
