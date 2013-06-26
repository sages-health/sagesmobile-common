//*****************************************************************************/
/* Copyright (c) 2013 The Johns Hopkins University/Applied Physics Laboratory */
/*                            All rights reserved.                            */
/*                                                                            */
/* This material may be used, modified, or reproduced by or for the U.S.      */
/* Government pursuant to the rights granted under the clauses at             */
/* DFARS 252.227-7013/7014 or FAR 52.227-14.                                  */
/*                                                                            */
/* Licensed under the Apache License, Version 2.0 (the "License");            */
/* you may not use this file except in compliance with the License.           */
/* You may obtain a copy of the License at                                    */
/*                                                                            */
/*     http://www.apache.org/licenses/LICENSE-2.0                             */
/*                                                                            */
/* NO WARRANTY.   THIS MATERIAL IS PROVIDED "AS IS."  JHU/APL DISCLAIMS ALL   */
/* WARRANTIES IN THE MATERIAL, WHETHER EXPRESS OR IMPLIED, INCLUDING (BUT NOT */
/* LIMITED TO) ANY AND ALL IMPLIED WARRANTIES OF PERFORMANCE,                 */
/* MERCHANTABILITY,FITNESS FOR A PARTICULAR PURPOSE, AND NON-INFRINGEMENT OF  */
/* INTELLECTUAL PROPERTY RIGHTS. ANY USER OF THE MATERIAL ASSUMES THE ENTIRE  */
/* RISK AND LIABILITY FOR USING THE MATERIAL.  IN NO EVENT SHALL JHU/APL BE   */
/* LIABLE TO ANY USER OF THE MATERIAL FOR ANY ACTUAL, INDIRECT,               */
/* CONSEQUENTIAL,SPECIAL OR OTHER DAMAGES ARISING FROM THE USE OF, OR         */
/* INABILITY TO USE, THE MATERIAL, INCLUDING, BUT NOT LIMITED TO, ANY DAMAGES */
/* FOR LOST PROFITS.                                                          */
//*****************************************************************************/
package edu.jhuapl.sages.mobile.lib.message;

import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKey;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKeyException;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesPrivateKey;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesPublicKey;

/**
 * 
 * @author sages
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
		if (rawMsg.contains("aes")){
			processKeyFromBody("aes", super.getBody());
		} else {
		processKeyFromBody(super.getBody());
		}
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
	
	private void processKeyFromBody(String keyType, SagesMessageBody body) throws SagesKeyException {
		if (this.getSender() == null){
			throw new SagesKeyException("Null value for sender of the key. Unable to process key for a sender.");
		}
		SagesKey key = null;
		if (body.getBody().contains(SagesMessage.my_key)){
			String keyText = (body.getBody()).substring(SagesMessage.my_key.length());
			if (keyType.contains("aes")){
				key = new SagesPrivateKey(null, this.getSender(), null, keyText.getBytes());
			} else {
				key =  new SagesPublicKey(null, this.getSender(), null, keyText.getBytes());
			}
			this.setKey(key);
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
