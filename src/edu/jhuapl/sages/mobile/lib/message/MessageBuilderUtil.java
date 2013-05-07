package edu.jhuapl.sages.mobile.lib.message;

import edu.jhuapl.sages.mobile.lib.message.SagesMessage.MsgTypeEnum;


/**
 * 
 * @author pokuam1
 * @date 05/06/13
 * 
 */
public class MessageBuilderUtil {

	public static String genTestHeader(MsgTypeEnum msgType, String params){
		String testHeader = "";
		switch (msgType){
		case KEY_EXCH: 
			String stage = (params.contains(SagesMessage.init)) ? SagesMessage.init : SagesMessage.reply;   
			testHeader = SagesMessage.key_exch + " " + stage + SagesMessage.DELIM_HeaderToBody;
			break;
		case STATUS:
			testHeader = SagesMessage.DELIM_HeaderToBody;
		case DATA:
			testHeader = SagesMessage.DELIM_HeaderToBody;
			break;
		}
		return testHeader;
	}
}
