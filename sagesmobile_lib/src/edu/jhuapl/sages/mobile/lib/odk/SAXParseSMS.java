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

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * @author sages
 * @created Aug 31, 2011
 */
public class SAXParseSMS extends DefaultHandler {

	private StringBuilder smsStr = new StringBuilder();
	private String tmpChar = "";
	private static String formprefix;
	
	private static int count = -1;
	private boolean m_preserveFormat = false;
	private boolean m_useFieldTags = false;
	private boolean m_fillBlanks = true;
	private boolean m_useTicks = true;
	private static String DELIMITTER = " ";
	private static String OPENTICK = "[";
	private static String CLOSETICK = "]";
	
	private static boolean tagPresent = false;
	private static String lastTag;
	private static String s_smsprefix = "sms://";
	
	private static boolean s_isMultiSms = false;
	
	public static boolean isMultiSms() {
		return s_isMultiSms;
	}

	public static void setIsMultiSms(boolean isMultiSms) {
		SAXParseSMS.s_isMultiSms = isMultiSms;
	}

	public SAXParseSMS(){
		super();
	}
	
	public SAXParseSMS(String delimitter, boolean preserveFormat, boolean useFieldTags){
		super();
		DELIMITTER = delimitter;
		m_preserveFormat =  preserveFormat;
		m_useFieldTags = useFieldTags;
	}
	
	public SAXParseSMS(String delimitter, boolean preserveFormat, boolean useFieldTags, boolean fillBlanks, boolean useTicks){
		super();
		DELIMITTER = delimitter;
		m_preserveFormat =  preserveFormat;
		m_useFieldTags = useFieldTags;
		m_fillBlanks = fillBlanks;
		m_useTicks = useTicks;
		if (!m_useTicks){
			OPENTICK = "";
			CLOSETICK = "";
		}
	}

	public SAXParseSMS(String delimitter, boolean preserveFormat, boolean useFieldTags, boolean fillBlanks){
		super();
		DELIMITTER = delimitter;
		m_preserveFormat =  preserveFormat;
		m_useFieldTags = useFieldTags;
		m_fillBlanks = fillBlanks;
	}
	
	public SAXParseSMS(boolean preserveFormat, boolean useFieldTags){
		super();
		m_preserveFormat =  preserveFormat;
		m_useFieldTags = useFieldTags;
	}
	
	public static void main(String arcv[]){
		
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			
			//SAXParseSMS handler = new SAXParseSMS();
			//format=false, tags=true
//			SAXParseSMS handler = new SAXParseSMS(false, true);
//			SAXParseSMS handler = new SAXParseSMS(",", false, true, true, true);
//			SAXParseSMS handler = new SAXParseSMS(" ", false, true, true, true);
//			SAXParseSMS handler = new SAXParseSMS(",", false, false, false, false);
			SAXParseSMS handler = new SAXParseSMS(",",false,true,true,true);
			handler.setTickSymbols("'");
			
/*			parser1 = new SAXParseSMS(false, true);
			parser2 = new SAXParseSMS(",",formatFalse,useTagsTrue,replaceBlanksTrue,useTicksTrue);
			parser3 = new SAXParseSMS(" ",formatFalse,useTagsTrue,replaceBlanksTrue,useTicksTrue);
			parser4 = new SAXParseSMS(",",formatFalse,useTagsFalse,replaceBlanksFalse,useTicksFalse);*/
			System.out.println("START");
//			saxParser.parse("C:\\dev\\hg-repositories\\adjoap-odkcollect-pt2\\poku-sandbox\\smsformex.xml", handler);
			saxParser.parse("C:\\dev\\hg-repositories\\adjoap-odkcollect-pt2\\poku-sandbox\\oevisit.xml", handler);
			System.out.println("DONE");
			System.out.println("\n" + "SMS OUTPUT STRING \n");
			System.out.println(handler.smsStr);
			
			System.out.println(isValidSMSURL("SMS://"));
			System.out.println(isValidSMSURL("sms://"));
			System.out.println(isValidSMSURL("SMS://2323232"));
			System.out.println(isValidSMSURL("sms://2130980823324098"));
			System.out.println(isValidSMSURL("sms://0"));
			System.out.println(isValidSMSURL("sms://3243294letters"));
			
			System.out.println(parseSMSURL("sms://1234567"));
			System.out.println(parseSMSURL("sms://123"));
			System.out.println(parseSMSURL("sms://99999999999"));
			System.out.println(parseSMSURL("sms://0"));
		} catch (Exception e){
			
		}
	}

	@Override
	public void startDocument(){
		System.out.println("*START DOC*");
		count++;
	}
	
	@Override
	public void endDocument(){
		System.out.println("*END DOC*");
		if (useFieldTags()){
			String fieldtag = lastTag;
			System.out.println("TAG HERE: " + fieldtag);
			
			if (fieldtag != null){
				smsStr.append(DELIMITTER).append(fieldtag);
				tagPresent = true;
			} 
		}else {
			smsStr.append(DELIMITTER).append(OPENTICK + tmpChar + CLOSETICK);
			//smsStr.append(DELIMITTER).append(tmpChar).append(DELIMITTER).append(qName);
		}
		count = -1;
		lastTag = null;
	}
	@Override
	public void startElement(String uri, String name, String qName, Attributes atts) {
		System.out.println("_start_name: " + name + ", qname: " + qName);
		System.out.println("atts: " + atts.getValue(0));
		
		
		if (count > 1) {
			if (!isPreserveFormat()){
				tmpChar = tmpChar.replaceAll("\\t", "");
				tmpChar = tmpChar.replaceAll("\\r", "");
				tmpChar = tmpChar.replaceAll("\\n", "");
			}
			
			// if replace blank with "." symbol
			if (m_fillBlanks){
				tmpChar = " ".equals(tmpChar) || "".equals(tmpChar) ? "." : tmpChar;
			}
			
			if (useFieldTags()){
				String fieldtag = atts.getValue("tag");
				lastTag = fieldtag;
				System.out.println("TAG HERE: " + fieldtag);
				
				if (fieldtag != null){
					
					if (tagPresent){
						smsStr.append(" ").append(OPENTICK + tmpChar + CLOSETICK).append(DELIMITTER).append(fieldtag);
					} else {
					smsStr.append(DELIMITTER).append(OPENTICK + tmpChar + CLOSETICK).append(DELIMITTER).append(fieldtag);
					}
					tagPresent = true;
					
				} else {
					fieldtag = "";
					if (tagPresent){
						smsStr.append(" ").append(OPENTICK + tmpChar + CLOSETICK);
					} else {
					smsStr.append(DELIMITTER).append(OPENTICK + tmpChar + CLOSETICK);
					}
					tagPresent = false;
					//smsStr.append(DELIMITTER).append(OPENTICK + (tmpChar.contains("\n") ? tmpChar.substring(tmpChar.lastIndexOf("\n")-1).replace("\n", CLOSETICK + "\n") : tmpChar + CLOSETICK));
					// above needs work to get the formatting and TICKS correct...
				}
			} else {
				smsStr.append(DELIMITTER).append(OPENTICK + tmpChar + CLOSETICK);
				//smsStr.append(DELIMITTER).append(tmpChar).append(DELIMITTER).append(qName);
			}			
		} else if (count == 1){
			count++;
			if (useFieldTags()){
				String fieldtag = atts.getValue("tag");
				lastTag = fieldtag;
				System.out.println("TAG HERE: " + fieldtag);
				
				if (fieldtag != null){
					smsStr.append(DELIMITTER).append(fieldtag);
					tagPresent = true;
				} 
			}
			// nothing else to do. the char from first element is blank, was formprefix element 
		} else if (count == 0) {
			formprefix = atts.getValue("id"); // "prefix" is the proper attribute to grab integrate later
			if (!isMultiSms()) smsStr.append(formprefix);
			count++;
		} else {
			//throw new Exception("something bad happened. ");
		}
		
		System.out.println("tmpChar: " + tmpChar);
		System.out.println("reset tmpChar");
		tmpChar = "";
		
/*		
		//TODO flip this count > 0, since bulk of time going here anyway
		if (count == 0){
			formprefix = atts.getValue("id"); // "prefix" is the proper attribute to grab integrate later
			smsStr.append(formprefix);
			count++;
			
		} else if (count == 1){
			count++;
			if (useFieldTags()){
				String fieldtag = atts.getValue("tag");
				System.out.println("TAG HERE: " + fieldtag);
				
				if (fieldtag != null){
					smsStr.append(DELIMITTER).append(fieldtag);
				} 
			}
			// nothing else to do. the char from first element is blank, was formprefix element 
		} else {
		if (!isPreserveFormat()){
			tmpChar = tmpChar.replaceAll("\\t", "");
			tmpChar = tmpChar.replaceAll("\\r", "");
			tmpChar = tmpChar.replaceAll("\\n", "");
		}
		
		// if replace blank with "." symbol
		if (m_fillBlanks){
			tmpChar = " ".equals(tmpChar) || "".equals(tmpChar) ? "." : tmpChar;
		}
		
		if (useFieldTags()){
			String fieldtag = atts.getValue("tag");
			System.out.println("TAG HERE: " + fieldtag);
			
			if (fieldtag != null){
				smsStr.append(DELIMITTER).append(OPENTICK + tmpChar + CLOSETICK).append(DELIMITTER).append(fieldtag);
			} else {
				fieldtag = "";
				smsStr.append(DELIMITTER).append(OPENTICK + tmpChar + CLOSETICK);
				//smsStr.append(DELIMITTER).append(OPENTICK + (tmpChar.contains("\n") ? tmpChar.substring(tmpChar.lastIndexOf("\n")-1).replace("\n", CLOSETICK + "\n") : tmpChar + CLOSETICK));
				// above needs work to get the formatting and TICKS correct...
			}
		} else {
			smsStr.append(DELIMITTER).append(OPENTICK + tmpChar + CLOSETICK);
			//smsStr.append(DELIMITTER).append(tmpChar).append(DELIMITTER).append(qName);
		}
		}
		System.out.println("tmpChar: " + tmpChar);
		System.out.println("reset tmpChar");
		tmpChar = "";
*/
	}

	@Override
	public void endElement(String uri, String name, String qName) {
		System.out.println("_end_name: " + name + ", qname: " + qName);
	}

	//TODO: POKU need handling for formatted SMS also
	@Override
	public void characters(char ch[], int start, int length) {
		
/*			for (int i = start; i < start + length; i++) {
			    switch (ch[i]) {
				    case '\\':
					System.out.print("\\\\");
					break;
				    case '"':
					System.out.print("\\\"");
					break;
				    case ' ':
				    	//System.out.print("__");
				    	//ch[i] = "_".charAt(0);
				    	break;
				    case '\n':
					System.out.print("\\n");
					if (!m_preserveFormat){
						//ch[i] = "$".charAt(0);
					}
					break;
				    case '\r':
					System.out.print("\\r");
					//ch[i] = "#".charAt(0);
					break;
				    case '\t':
					System.out.print("\\t");
					//ch[i] = "\\b".charAt(0); 
					//ch[i] = new Character(' ');
					break;
				    default:
					System.out.print(ch[i]);
					break;
			    }
			}
*/
		tmpChar = tmpChar + new String(ch, start, length);
		System.out.println("chars: " +  tmpChar);
	}
	
	/**
	 * sms://phonenumber  
	 * @param smsurl
	 * @return
	 */
	public static boolean isValidSMSURL(String smsurl) {
		return ( smsurl.startsWith(s_smsprefix) && smsurl.substring(s_smsprefix.length()).matches("\\d+"));
	}
	
	/**
	 * extracts the phoneumber portion from the SMS URL, sms://phonenumber
	 * @param smsurl
	 * @return
	 */
	public static String parseSMSURL(String smsurl){
		return smsurl.substring(s_smsprefix.length());
	}

	public boolean isPreserveFormat() {
		return m_preserveFormat;
	}
	public void setPreserveFormat(boolean preserveFormat) {
		m_preserveFormat = preserveFormat;
	}
	
	public boolean useFieldTags() {
		return m_useFieldTags;
	}
	
	public void setUseFieldTags(boolean useFieldTags) {
		m_useFieldTags = useFieldTags;
	}

	public StringBuilder getSMSstring() {
		return smsStr;
	}
	
	/**
	 * Sets the delimiter to use during parsing. Default is " " blank space
	 * @param delimiter
	 */
	public void setDelimiter(String delimiter) {
		DELIMITTER = delimiter;
	}

	/**
	 * Tick symbol to surround values, this symbol is used for both open and close tick
	 * @param ticksymbol
	 */
	public void setTickSymbols(String ticksymbol) {
		OPENTICK = ticksymbol;
		CLOSETICK = ticksymbol;
	}

	/**Tick symbols to surround vlaues.
	 * @param openTicksymbol
	 * @param closeTicksymbol
	 */
	public void setTickSymbols(String openTicksymbol, String closeTicksymbol) {
		OPENTICK = openTicksymbol;
		CLOSETICK = closeTicksymbol;
	}
}
