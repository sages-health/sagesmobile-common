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



/**
 * Parses the header and body of a message that has been constructed to be a SAGES 
 * mulitpart SMS message with syntax $sages_header$sages_body. 
 * 
 * $sages_header = seg_num|tot_segs|tx_id:
 * $sages_body = #formid dataval1 dataval2 datavalN#formid dataval1 dataval2 datavalN
 * 
 * @author sages
 * @created Feb 2, 2012
 */
public class MessageBodyParser {

	/**
	 * @author POKUAM1
	 * @created Feb 2, 2012
	 */
	public class SagesPdu {

		private int segmentNumber;
		private int totalSegments;
		private long txId;
		private String payload;
		private String sender;
		
		/**
		 * @param segNum
		 * @param totSegs
		 * @param txId2
		 * @param payload2
		 */
		public SagesPdu(int segNum, int totSegs, long txId2, String payload, String sender) {
			this.segmentNumber = segNum;
			this.totalSegments = totSegs;
			this.txId = txId2;
			this.payload = payload;
			this.sender = sender;
		}
		
		public int getSegmentNumber() {
			return segmentNumber;
		}
		public void setSegmentNumber(int segmentNumber) {
			this.segmentNumber = segmentNumber;
		}
		public int getTotalSegments() {
			return totalSegments;
		}
		public void setTotalSegments(int totalSegments) {
			this.totalSegments = totalSegments;
		}
		public long getTxId() {
			return txId;
		}
		public void setTxId(long txId) {
			this.txId = txId;
		}
		public String getPayload() {
			return payload;
		}
		public void setPayload(String payload) {
			this.payload = payload;
		}
		public String getSender() {
			return sender;
		}
		public void setSender(String sender) {
			this.sender = sender;
		}
	}

	public static SagesPdu extractSegmentAsPdu(String msg, String sender){
		SagesPdu pdu = extractPdu(msg, sender);
		return pdu;
	}

	/**
	 * @param msg the SMS message
	 * @param sender phone number which sent the SMS message
	 * @return
	 */
	private static SagesPdu extractPdu(String msg, String sender) {
		String splitMsg = ":";
		String splitPdu = "\\|"; //DO NOT USE "," it will throw off processing of CSV output by ETL;
		String[] msgSplit = msg.split(splitMsg);
		
		if (msgSplit.length == 1){
			String[] demodMsg = Demodulator.demod(msg);
			MessageBodyParser mbd = new MessageBodyParser();
			SagesPdu sagesPdu = mbd.new SagesPdu(1, 1, -1, demodMsg[0], sender);
			return sagesPdu;
		}
		String payload = msgSplit[1];
		String[] pdu = msgSplit[0].split(splitPdu);
		//TODO: pokuam1 Sometimes this throws a number format exception, "2l2l3333:" where those are l not |
		// basically need to do better job of making sure the sages_header is really valid!!!
		int segNum = Integer.valueOf(pdu[0]).intValue();
		int totSegs = Integer.valueOf(pdu[1]).intValue();
		long txId = Long.valueOf(pdu[2]).longValue();
		
		MessageBodyParser mbd = new MessageBodyParser();
		SagesPdu sagesPdu = mbd.new SagesPdu(segNum, totSegs, txId, payload, sender);
		return sagesPdu;
	}
}
