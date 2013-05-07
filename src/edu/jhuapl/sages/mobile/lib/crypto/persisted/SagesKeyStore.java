package edu.jhuapl.sages.mobile.lib.crypto.persisted;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.os.Environment;
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
			throw new SagesKeyException("key store file failed to create at: " + keyStoreFile.getAbsolutePath());
		}
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
		key.saveKey(keyStoreFile.getAbsolutePath());
	}



}
