package edu.jhuapl.sages.mobile.lib.message;

import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKey;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKeyException;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesPublicKey;

/**
 * 
 * @author pokuam1
 * @date 05/06/13
 * 
 */
public class KeyExchangeMessage extends SagesMessage {

	private SagesKey key;
	
	public KeyExchangeMessage(MsgTypeEnum msgType, SagesMessageHeader header, SagesMessageBody body) {
		super(MsgTypeEnum.KEY_EXCH, header, body);
	}
	
	public KeyExchangeMessage(){
		super(MsgTypeEnum.KEY_EXCH, new SagesMessageHeader(null), new SagesMessageBody(null));
	}

	
	@Override
	public void processRawMessage(String rawMsg) throws SagesKeyException {
		super.processRawMessage(rawMsg);
		processKeyFromBody(super.getBody());
	};

	private void processKeyFromBody(SagesMessageBody body) throws SagesKeyException {
		if (this.getSender() == null){
			throw new SagesKeyException("Null value for sender of the key. Unable to process key for a sender.");
		}
		if (body.getBody().contains(SagesMessage.my_key)){
			String keyText = (body.getBody()).substring(SagesMessage.my_key.length());
			SagesPublicKey pubKey =  new SagesPublicKey(null, this.getSender(), null, keyText.getBytes());
			this.setKey(pubKey);
		} else {
			throw new SagesKeyException("key exchange message is missing the sender's key");
		}
	}

	/**
	 * @return the key
	 */
	public SagesKey getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(SagesKey key) {
		this.key = key;
	}
	

}
