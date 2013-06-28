package edu.jhuapl.sages.mobile.lib.app.tests.message;

import edu.jhuapl.sages.mobile.lib.app.tests.crypto.SagesKeyTest;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKeyException;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesPublicKey;
import edu.jhuapl.sages.mobile.lib.message.DataMessage;
import edu.jhuapl.sages.mobile.lib.message.KeyExchangeMessage;
import edu.jhuapl.sages.mobile.lib.message.SagesMessage;
import edu.jhuapl.sages.mobile.lib.message.StatusMessage;
import edu.jhuapl.sages.mobile.lib.message.SagesMessage.MsgTypeEnum;
import android.test.AndroidTestCase;

/**
 * 
 * @author pokuam1
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
