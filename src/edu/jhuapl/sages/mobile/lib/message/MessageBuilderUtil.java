package edu.jhuapl.sages.mobile.lib.message;

import edu.jhuapl.sages.mobile.lib.message.SagesMessage.MsgTypeEnum;


/**
 * 
 * @author pokuam1
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
