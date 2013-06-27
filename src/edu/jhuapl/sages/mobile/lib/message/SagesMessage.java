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
package edu.jhuapl.sages.mobile.lib.message;

import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKeyException;


/**
 * 
 * @author sages
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
