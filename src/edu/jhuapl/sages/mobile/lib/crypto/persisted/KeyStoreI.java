package edu.jhuapl.sages.mobile.lib.crypto.persisted;

import java.util.Map;

import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKey.KeyEnum;

/**
 * 
 * @author pokuam1
 * @date 05/02/13
 */
public interface KeyStoreI {

	//TODO consider using the java.security.KeyStore under the hood
	
	public SagesKey lookupKey(String agentId, KeyEnum keyType) throws SagesKeyException;
	public Map<KeyEnum, SagesKey> lookupKeys(String agentId) throws SagesKeyException;
	public Map<KeyEnum, SagesKey> lookupKeys(SagesContact contact) throws SagesKeyException;
	public void addKey(SagesKey key) throws SagesKeyException;
	
}
