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
package edu.jhuapl.sages.mobile.lib.odk;

import edu.jhuapl.sages.mobile.lib.message.MessageBuilderUtil;
import edu.jhuapl.sages.mobile.lib.message.SagesMessage.MsgTypeEnum;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SagesOdkMessage extends SagesSmsMessage{

	// kdf . 1 MmmmnnnnnnnmmmmmmmmmmmmmmmmMmmmnnnnnnnmmmmmmmmmmmmmmmmMmmmnnnnnnnmmmmmmmmmmmmmmmmMmmmnnnnnnnmmmmmmmmmmmmmmmmMmmmnnnnnnnmmmmmmmmmmmmmmmm 2013-05-20 1 0 1 2 99 58.0 58 36 88 45.0 ' 1 2 3 4 5 6 7 8 ' 1 ' 1 2 3 4 5 6 7 8 9 10 11 12 13 '
	protected String t = SagesOdkMessage.class.getCanonicalName();
	protected String formId = "formId"; //TODO need to pass this in during creation
	protected String txIdCur = null; //TODO need to pass this in during creation
	
    private static int MULTIPART_SMS_SIZE = 70;
    private static int ENCODING_SMS_SIZE = 50;
    
    
    
	private String smsText; 
	public static int MAX_SIZE = 160;
	private int protocol;
	private int size;
	private SagesOdkHeader sagesrefOdk_header;
	private ArrayList<String> dividedBlob;
	
	private boolean isEncrypted;
	
	public SagesOdkMessage(String smsText, String formId, String txIdCur){
		super(smsText, formId, txIdCur);
		this.smsText = smsText;
		this.formId = formId;
		this.txIdCur = this.txIdCur;
	}
	
	public SagesOdkMessage(String smsText){
		super(smsText);
		this.smsText = smsText;
		this.size = smsText.length();
	}
	
	/**
	 * Configures the message so text processing happens appropriately
	 * @param isEncrypted
	 */
	public void configure(boolean isEncrypted){
		this.isEncrypted = isEncrypted;
		processSmsText(this.smsText);
	}
	
	private void processSmsText(String smsText) {
		ArrayList<String> dividedBlob = new ArrayList<String>();
		
		/**
		 * No need to do this portion if code is being encrypted and base64 encoded
		 */
		Pattern pattern = Pattern.compile("([\\P{InBasic Latin}]+)");
		Matcher matcher = pattern.matcher(smsText);
		boolean containsUnicode = matcher.find();
		System.out.println("did it match? " + containsUnicode);

        
        if ("multisms".equals(formId)){
        		smsText.replaceFirst(formId, "");
        } else {
        	// prepare for header: segNum,totSegs,txID:#formID  //TODO - THIS IS DOING ANYTHING--RA STILL WORKS THOUGH FOR SIMPLE USE CASE
        	smsText.replaceFirst(formId, "#" + formId);
        }
       	try {
        	if (containsUnicode){
        		Log.d(t, "Text has special characters beyone US ASCII. NEED TO SHRINK MSG.");
        		Log.d(t, "Text length= " + smsText.length());
        		MULTIPART_SMS_SIZE = 70;
        		ENCODING_SMS_SIZE = 50;
        	} else {
        		Log.d(t, "GOOD NO special characters beyone US ASCII. SEND BIG FAT MSG.");
        		Log.d(t, "Text length= " + smsText.length());
//        		MULTIPART_SMS_SIZE = 120; //ORIGINALLY HAVE DEPLOYED WITH 120 & 70
//        		ENCODING_SMS_SIZE = 70;
        		MULTIPART_SMS_SIZE = 150; // EXPERIMENTAL LETS TRY TO GET CLOSER TO 160 (header ~20 chars)
        		ENCODING_SMS_SIZE = 130;
        	}
        	
        	if (isEncrypted){
        		byte[] smsTextData = DataChunker.encryptDataGo(smsText);
        		byte[] b64SmsTextData = DataChunker.base64EncodeCipherGo(smsTextData);
        		
        		/** 
        		 *  Add The "meta-data header" ex. :data enc aes|
	        		seg_index|segs_tot|tx_id:data enc aes|here goes the message text here it all goes
        		 *  TODO: pokuam1 turn this into a method
        		 ***/
        		smsText = MessageBuilderUtil.genMetaDataHeader(MsgTypeEnum.DATA, null) + new String(b64SmsTextData);
//        		smsText = SagesMessage.data + " " + SagesMessage.enc_aes + SagesMessage.DELIM_HeaderToBody + new String(b64SmsTextData);
        	} else if (!isEncrypted){
        		/** 
        		 *  Add The "meta-data header" FOR NOW DON'T ADD HEADER -- RAPIDANDROID HASN'T BEEN ADJUSTED 
        		 *  TODO: pokuam1 turn this into a method
        		 ***/
        		//smsText = SagesMessage.data + " " + SagesMessage.DELIM_HeaderToBody + smsText;
        		
        	}
        	
        	if (smsText.length() > 160 ) {
        		dividedBlob = divideTextAddHeader(smsText, MULTIPART_SMS_SIZE, ENCODING_SMS_SIZE, formId);
        	} else {
        		dividedBlob.add(smsText);
        	}
        } catch (Exception e){
        	e.printStackTrace();
        	Log.e(t + ".divideTextAddHeader", e.getMessage());
        }
    	
       	Calendar cal = new GregorianCalendar();
    	int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
    	int hr = cal.get(Calendar.HOUR_OF_DAY);
    	int min = cal.get(Calendar.MINUTE);
    	int sec = cal.get(Calendar.SECOND);
    	String txId = dayOfYear + "" + hr + "" + min + "" + sec;
    	
        this.dividedBlob = dividedBlob;
	}

	/**
	 * Determines whether message will be split for SMS sending
	 * @return true if raw message > {@link MAX_SIZE}
	 */
	public boolean isMultipart() {
		if (this.smsText.length() > MAX_SIZE){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Example usage:
	 * ArrayList<String> dividedBlob = divideTextAddHeader(smsText, MULTIPART_SMS_SIZE, ENCODING_SMS_SIZE, formId);
	 * 
	 * @param smsText
	 * @param segSize
	 * @param allowedInfoSize
	 * @param formId
	 * @return
	 */
	private ArrayList<String> divideTextAddHeader(String smsText, int segSize, int allowedInfoSize, String formId) {
		ArrayList<String> dividedText = new ArrayList<String>();
		int numSegs = (int) Math.round(smsText.length() / (double) segSize);
		int start = 0;
		int end = segSize -1;
		//int allowedInfoSize = 130;
		
		String tmpString = "";
//		String[] s = StringUtils.splitPreserveAllTokens(smsText, null, numSegs);
//TODO		String[] s = DataChunker.chunkData(smsText);
		Log.d("chunk before", "txidcur=" + txIdCur);
		DataChunker dchunker = new DataChunker();
		Map<String,String> s = dchunker.chunkDataWithHeaderGo(smsText, segSize, allowedInfoSize /*formId*/);
//		Map<String,String> s = DataChunker.chunkDataWithHeader(smsText, txIdCur/*, formId*/);
		txIdCur = dchunker.getTxId();
		Log.d("chunk after", "txidcur=" + txIdCur);
//		while (tmpString >= segSize){
//			tmpString = smsText.substring(0,segSize - 1);
//				
//		}
		/*		for (int i=0; i <= numSegs; i++){
			
			dividedText.add(smsText.substring(start, end));
			start = end + 1;
			end = end + segSize;
		}*/
		
		for (Entry<String,String> entry : s.entrySet()){
			System.out.println("PAYLOAD:"+entry.getKey() + "," + entry.getValue());
			dividedText.add(entry.getKey() + entry.getValue());
		}
//		dividedText = Arrays.asList(s);
		return dividedText;
	}

	/**
	 * Returns the process smsText after it has been broken into smaller segments
	 * @return
	 */
	public ArrayList<String> getDividedBlob() {
		return dividedBlob;
	}
	
	public void testHook(String smsText){
		this.processSmsText(smsText);
	}
}
