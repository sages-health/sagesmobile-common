package edu.jhuapl.sages.mobile.lib.message;

import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKeyException;


/**
 * 
 * @author pokuam1
 * @date 05/06/13
 * 
 */
public class SagesMessage implements SagesMessageI {

	
	public static final String DELIM_HeaderToBody = "|";

	public static final String key_exch = "key_exch";
	public static final String data = "data";
	public static final String init = "init";
	public static final String reply = "reply";
	public static final String my_key = "my_key";
	public static final String enc_aes = "enc aes";
	
	public enum MsgTypeEnum {
		KEY_EXCH, DATA, STATUS
	}
	
	
	private String sender;
	private MsgTypeEnum msgType;
	private SagesMessageHeader header;
	private SagesMessageBody body;
	
	public SagesMessage(MsgTypeEnum msgType, SagesMessageHeader header, SagesMessageBody body){
		this.msgType = msgType;
		this.header = header;
		this.body = body;
	}

	public SagesMessage(){
		this.msgType = null;
		this.header = new SagesMessageHeader(null);
		this.body = new SagesMessageBody(null);
	}
	
	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @param sender the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * @return the msgType
	 */
	public MsgTypeEnum getMsgType() {
		return msgType;
	}

	/**
	 * @param msgType the msgType to set
	 */
	public void setMsgType(MsgTypeEnum msgType) {
		this.msgType = msgType;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(SagesMessageHeader header) {
		this.header = header;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(SagesMessageBody body) {
		this.body = body;
	}

	/**
	 * @return the body
	 */
	public SagesMessageBody getBody() {
		return body;
	}

	/**
	 * TODO: make this more resilient. for now it's simple but can change easily
	 * Assumes syntax for message is "message_header"|"message_body"
	 * where "|" is the delimeter between the header and body 
	 * @param rawMsg
	 * @return 
	 * @throws SagesKeyException 
	 */
	public void processRawMessage(String rawMsg) throws SagesKeyException{
		extractHeader(rawMsg);
		extractBody(rawMsg);
	}

	
	private SagesMessageBody extractBody(String rawMsg) {
		int bodyindx = rawMsg.indexOf(DELIM_HeaderToBody);
		this.body.setBody(rawMsg.substring(bodyindx + 1));
		return this.body;
	}


	public SagesMessageHeader extractHeader(String rawMsg) {
		int hdrindx = rawMsg.indexOf(DELIM_HeaderToBody);
		this.header.setHeader(rawMsg.substring(0, hdrindx));
		if (this.header.getHeader().contains(SagesMessage.key_exch)){
			this.msgType = msgType.KEY_EXCH;
		} else if (this.header.getHeader().contains(SagesMessage.data)){
			this.msgType = msgType.DATA;
		}
		return this.header;
	}


	@Override
	public SagesMessageHeader retrieveHeader() {
		return header;
	}

	@Override
	public SagesMessageBody retrieveBody() {
		return body;
	}
	
	
}
