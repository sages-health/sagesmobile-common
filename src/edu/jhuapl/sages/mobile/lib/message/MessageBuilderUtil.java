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

import edu.jhuapl.sages.mobile.lib.message.SagesMessage.MsgTypeEnum;


/**
 * 
 * @author sages
 * @date 05/06/13
 * 
 */
public class MessageBuilderUtil {

	/**
	 * Header that provides information on how the sms should be processed. KeyExchange, Status, Data. Includes information
	 * about encryption algorithm used, compression schemes if any, status codes etc. 
	 * 
	 * @param msgType
	 * @param params
	 * @return meta data header to prefix an sms message
	 */
	public static String genMetaDataHeader(MsgTypeEnum msgType, String params){
		String testHeader = "";
		switch (msgType){
		case KEY_EXCH: 
			String stage = (params.contains(SagesMessage.init)) ? SagesMessage.init : SagesMessage.reply;   
			stage = (params.contains("aes")) ? SagesMessage.init + " aes" : SagesMessage.init;
			testHeader = SagesMessage.key_exch + " " + stage + SagesMessage.DELIM_HeaderToBody;
			break;
		case STATUS:
			testHeader = SagesMessage.DELIM_HeaderToBody;
		case DATA:
			testHeader = SagesMessage.data + " " + SagesMessage.enc_aes + SagesMessage.DELIM_HeaderToBody;
			break;
		}
		return testHeader;
	}
}
