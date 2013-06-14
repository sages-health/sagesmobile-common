package edu.jhuapl.sages.mobile.lib.app.tests.message;

import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import edu.jhuapl.sages.mobile.lib.app.tests.crypto.CryptoEngineTest;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKeyException;
import edu.jhuapl.sages.mobile.lib.message.DataMessage;
import edu.jhuapl.sages.mobile.lib.message.MessageBuilderUtil;
import edu.jhuapl.sages.mobile.lib.message.SagesMessage;
import edu.jhuapl.sages.mobile.lib.message.SagesMessage.MsgTypeEnum;

/**
 * Tests that the MessageBuilderUtil constructs different types of messages as expected
 * 
 * @author pokuam1
 * @date 5/20/13 
 */
public class MessageDecomposerTest extends CryptoEngineTest {

	
	public MessageDecomposerTest() throws NoSuchAlgorithmException,
			NoSuchPaddingException {
		super();
		// TODO Auto-generated constructor stub
	}

	private MessageBuilderUtil msgUtil;
	private String body = "";
	
	public void setUp(){
		msgUtil  = new MessageBuilderUtil();
		
	}
	
	
	public void testBuildKeyExchangeMessage(){
		String msg = buildKeyExchangeMessage();
		assertEquals("key_exch init aes|my_keyPASSWORD12345678", msg);
	}


	private String buildKeyExchangeMessage() {
		String msgHeader = MessageBuilderUtil.genMetaDataHeader(MsgTypeEnum.KEY_EXCH, "init aes");
		String msgBody = SagesMessage.my_key + new String(this.getCrypto().getSkeySpec().getEncoded());
		String text = msgHeader + msgBody;
		return text;
	}
	
	
	public void testBuildDataMessageEncrypted(){
		String msg = buildEncAesDataMessage();
		
		assertEquals("data enc aes |This is the test message to encrypt and decrypt", msg);

		//SENDER
		// b64 enc body: [67, 109, 118, 115, 47, 76, 78, 55, 57, 103, 100, 74, 72, 104, 108, 116, 115, 84, 51, 104, 85, 47, 88, 101, 112, 77, 118, 87, 98, 87, 79, 54, 109, 82, 121, 56, 77, 69, 114, 111, 104, 80, 67, 83, 76, 81, 100, 110, 55, 110, 103, 105, 110, 109, 47, 74, 113, 57, 43, 79, 117, 47, 70, 43, 67, 80, 49, 117, 105, 104, 87, 102, 54, 88, 51, 100, 57, 90, 69, 66, 49, 73, 122, 73, 102, 81, 61, 61]
		
		// SDR:  data enc aes |Cmvs/LN79gdJHhltsT3hU/XepMvWbWO6mRy8MErohPCSLQdn7nginm/Jq9+Ou/F+CP1uihWf6X3d9ZEB1IzIfQ==
		
		// b64 enc body: [67, 109, 118, 115, 47, 76, 78, 55, 57, 103, 100, 74, 72, 104, 108, 116, 115, 84, 51, 104, 85, 47, 88, 101, 112, 77, 118, 87, 98, 87, 79, 54, 109, 82, 121, 56, 77, 69, 114, 111, 104, 80, 67, 83, 76, 81, 100, 110, 55, 110, 103, 105, 110, 109, 47, 74, 113, 57, 43, 79, 117, 47, 70, 43, 67, 80, 49, 117, 105, 104, 87, 102, 54, 88, 51, 100, 57, 90, 69, 66, 49, 73, 122, 73, 102, 81, 61, 61]
		// b64 dec body: [10, 107, -20, -4, -77, 123, -10, 7, 73, 30, 25, 109, -79, 61, -31, 83, -11, -34, -92, -53, -42, 109, 99, -70, -103, 28, -68, 48, 74, -24, -124, -16, -110, 45, 7, 103, -18, 120, 34, -98, 111, -55, -85, -33, -114, -69, -15, 126, 8, -3, 110, -118, 21, -97, -23, 125, -35, -11, -111, 1, -44, -116, -56, 125]
		
		// RCVR: data enc aes |Cmvs/LN79gdJHhltsT3hU/XepMvWbWO6mRy8MErohPCSLQdn7nginm/Jq9+Ou/F+CP1uihWf6X3d9ZEB1IzIfQ==
		
//		byte[] cipher = null;
//		try {
//			cipher = this.getCrypto().encrypt(CryptoEngineTest.DUMMY_MESSAGE_CLEAR.getBytes());
//		} catch (Exception e) {
//			e.printStackTrace();
//			fail();
//		}
//		
//		byte[] cipher64 = Base64.encode(cipher);
//		msgBody = new String(cipher64);
//		String text = msgHeader + msgBody;
//		byte[] textBytes = text.getBytes();


	}


	private String buildEncAesDataMessage() {
		String msgHeader = MessageBuilderUtil.genMetaDataHeader(MsgTypeEnum.DATA, "enc aes");
		String msgBody = CryptoEngineTest.DUMMY_MESSAGE_CLEAR;
		String text =  msgHeader + msgBody;
		return text;
	}
	
	public void testDataMessageEncryptDecrypt(){
		
		DataMessage dataMsg = new DataMessage();
		

//		dataMsg.setSender(sender);
		try {
			dataMsg.processRawMessage(body);
		} catch (SagesKeyException e) {
			e.printStackTrace();
			fail();
		}
	}
	
}
