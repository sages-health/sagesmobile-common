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
package edu.jhuapl.sages.mobile.lib.rapidandroid;

import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKeyException;
import edu.jhuapl.sages.mobile.lib.message.MessageBuilderUtil;
import edu.jhuapl.sages.mobile.lib.message.SagesMessage.MsgTypeEnum;
import edu.jhuapl.sages.mobile.lib.odk.DataChunker;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Demodulates a concatenated message (a blob). Takes the complete concatenated message that adheres to the SAGES multisms syntax scheme, and 
 * breaks the message around the various qualifiers indicating where new forms data begins.
 * 
 * @author sages
 * @created Feb 16, 2012
 */
public class Demodulator {

	/**
	 * Multiple forms are indicated by a # syntax
	 */
	// parentheses causes issues. won't allow unit tests to run unless escaped, and then it doesn't
	// match in the demodulator as an escaped string. too much hassle for paren.  \(
	// Test run failed: Instrumentation run failed due to 'java.util.regex.PatternSyntaxException'
	private static String splitQualifier = "#"; 
	
	
	/**
	 * Demodulates all blobs in a map and places individual complete form values into a map corresponding
	 * to the original SMS submission txId
	 * @param blobMap
	 * @return Map of SMS submission txId and all form values it contained
	 */
	public static Map<Long, String[]> deModulateBlobMap(Map<Long, String> blobMap){
		Map<Long, String[]> demodMap = new HashMap<Long, String[]>();

		for (Entry<Long, String> e : blobMap.entrySet()){
			Long tx_id = e.getKey();
			String blob = e.getValue();
			demodMap.put(tx_id, demod(blob));
		}
		
		return demodMap;
	} 
	
	/**
	 * Demodulates a blob (concatenated message adhering to the SAGES multisms syntax)
	 * @param blob
	 * @return Array of the individual form values that were in the blob
	 */
	public static String[] demod(String blob){
		/**
		 * 1. check meta-data header for: isEncrypted, encryptionAlgo
		 * 2. b64Decode
		 * 3. decrypt
		 */
		
		//TODO  need to handle the non encrypted case!!!
		String data_enc_aes_hdr = MessageBuilderUtil.genMetaDataHeader(MsgTypeEnum.DATA, null); 
//		String data_enc_aes_hdr = SagesMessage.data + " " + SagesMessage.enc_aes + SagesMessage.DELIM_HeaderToBody; 
		if (blob.startsWith(data_enc_aes_hdr)){
			blob = blob.substring(data_enc_aes_hdr.length());
			
			byte[] b64DecodedBlob = DataChunker.base64DecodeCipherGo(blob.getBytes());
			byte[] blobCipherDecrypted;
			try {
				blobCipherDecrypted = DataChunker.decryptDataGo(b64DecodedBlob);
				blob = new String(blobCipherDecrypted);
			} catch (SagesKeyException e) {
				e.printStackTrace();
				Log.e(Demodulator.class.getCanonicalName(), e.getMessage());
			}
		}
		blob = blob.startsWith(splitQualifier) ? blob.substring(1) : blob;
		String[] demodedBlob = blob.split(splitQualifier);
		return demodedBlob;
	}
/*	
 * ORIGINAL VERSION: WITHOUT b64DECODING/DECRYPTION
	*//**
	 * Demodulates a blob (concatenated message adhering to the SAGES multisms syntax)
	 * @param blob
	 * @return Array of the individual form values that were in the blob
	 *//*
	public static String[] demod(String blob){
		blob = blob.startsWith(splitQualifier) ? blob.substring(1) : blob;
		String[] demodedBlob = blob.split(splitQualifier);
		return demodedBlob;
	}
	
*/	
}
