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
package edu.jhuapl.sages.mobile.lib.app.tests.message;

import edu.jhuapl.sages.mobile.lib.app.tests.crypto.SagesKeyTest;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKeyException;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesPublicKey;
import edu.jhuapl.sages.mobile.lib.message.DataMessage;
import edu.jhuapl.sages.mobile.lib.message.KeyExchangeMessage;
import edu.jhuapl.sages.mobile.lib.message.SagesMessage;
import edu.jhuapl.sages.mobile.lib.message.SagesMessage.MsgTypeEnum;
import edu.jhuapl.sages.mobile.lib.message.StatusMessage;

import android.test.AndroidTestCase;

/**
 * 
 * @author sages
 * @date 05/06/13
 */
public class SagesMessageTest extends AndroidTestCase {

	private String testSmsBody = SagesMessage.key_exch + " " + SagesMessage.init + SagesMessage.DELIM_HeaderToBody + SagesMessage.my_key + SagesKeyTest.testData;
	
	public void testMessageTypeEnums(){
		DataMessage datamsg = new DataMessage();
		KeyExchangeMessage keyexchmsg = new KeyExchangeMessage();
		StatusMessage statusmsg = new StatusMessage();
		
		assertEquals(MsgTypeEnum.DATA, datamsg.getMsgType());
		assertEquals(MsgTypeEnum.KEY_EXCH, keyexchmsg.getMsgType());
		assertEquals(MsgTypeEnum.STATUS, statusmsg.getMsgType());
	}
	
	
	public void testKeyExchangeProcessing(){
		SagesMessage msg = new SagesMessage();
		try {
			msg.processRawMessage(testSmsBody);
			assertEquals(MsgTypeEnum.KEY_EXCH, msg.getMsgType());
		} catch (SagesKeyException e) {
			e.printStackTrace();
			fail();
		}
		
		KeyExchangeMessage keyExchMsg = new KeyExchangeMessage();
		keyExchMsg.setSender("9999");
		try {
			keyExchMsg.processRawMessage(testSmsBody);
		} catch (SagesKeyException e) {
			e.printStackTrace();
			fail();
		}
		
		assertEquals(MsgTypeEnum.KEY_EXCH, msg.getMsgType());
		
		SagesPublicKey pubKey = (SagesPublicKey)keyExchMsg.getKey();
		assertEquals(new String(SagesKeyTest.testData), new String(pubKey.getData()));
	}
	
}
