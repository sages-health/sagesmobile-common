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
package edu.jhuapl.sages.mobile.lib.receiver;

import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKeyException;
import edu.jhuapl.sages.mobile.lib.message.SagesMessage;
import edu.jhuapl.sages.mobile.lib.message.SagesMessage.MsgTypeEnum;

import android.content.Context;
import android.content.Intent;

/**
 * {@link RapidAndroidSagesSmsReceiver} is a broadcast receiver that detects received SMS. It is intended to have RapidAndroid
 * specific handling within a SAGES system.
 * 
 * @author sages
 * @date 06/13/13
 *
 */
public class RapidAndroidSagesSmsReceiver extends SagesSmsReceiver {

	
	private static final String RAPIDANDROID_SMS_RECEIVED = "edu.jhuapl.sages.mobile.lib.android.provider.Telephony.RAPIDANDROID_SMS_RECEIVED";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}
	
	
	/**
	 * Upon determing message is of type {@linkplain MsgTypeEnum.DATA}  then sends our broadcast for
	 * new Intent("edu.jhuapl.sages.mobile.lib.android.provider.Telephony.RAPIDANDROID_SMS_RECEIVED")
	 * 
	 * @param sender
	 * @param body
	 * @throws SagesKeyException 
	 */
	@Override
	protected void handleDataMessage(Context context, Intent intent, String sender, String body) {
		if (!body.contains(SagesMessage.data)){ //TODO Handle this case.
			return;
		}
		
		Intent broadcast = new Intent(RAPIDANDROID_SMS_RECEIVED);
		broadcast.putExtras(intent.getExtras());
		context.sendBroadcast(broadcast);
	}
	
	/**
	 * Sends our broadcast for new Intent("edu.jhuapl.sages.mobile.lib.android.provider.Telephony.RAPIDANDROID_SMS_RECEIVED")
	 * 
	 * @param sender
	 * @param body
	 * @throws SagesKeyException 
	 */
	protected void handleUnknownMessage(Context context, Intent intent, String sender, String body) {
		
		Intent broadcast = new Intent(RAPIDANDROID_SMS_RECEIVED);
		broadcast.putExtras(intent.getExtras());
		context.sendBroadcast(broadcast);
	}
	
}
