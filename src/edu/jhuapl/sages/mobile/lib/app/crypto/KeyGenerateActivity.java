package edu.jhuapl.sages.mobile.lib.app.crypto;

import java.io.File;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import edu.jhuapl.sages.mobile.lib.app.R;
import edu.jhuapl.sages.mobile.lib.crypto.engines.CryptoEngine;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKeyException;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKeyStore;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesPrivateKey;
import edu.jhuapl.sages.mobile.lib.message.MessageBuilderUtil;
import edu.jhuapl.sages.mobile.lib.message.SagesMessage;
import edu.jhuapl.sages.mobile.lib.message.SagesMessage.MsgTypeEnum;

/**
 * Demonstrates generation of a true cryptographic key (AES-128 bit for now), and the results of 
 * encrypting, base64 encoding and base64 decoding, decrypting a hard coded string. The key that is generated 
 * contains a pseudo-random suffix to demonstrate correct functionality.
 * 
 * @author pokuam1
 * @date 5/16/2013
 */
public class KeyGenerateActivity extends Activity {

	private static final String DUMMY_MESSAGE_CLEAR = "This is the test message to encrypt and decrypt. Over and over.";
	private String seedpwd = "DUMMYPASSWORD1";
	private String pwd = "DUMMYPASSWORD1";
	private int a;
	private int b;
	
	private TextView key;
	private TextView msg;
	private TextView msgEncrypted;
	private TextView msgDecrypted;
	private CryptoEngine crypto;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_keygen);
		
		makeNewPassword();
		try {
			
			crypto = new CryptoEngine((pwd).getBytes());
			key = (TextView)findViewById(R.id.txt_privateKey);
			key.setText("KEY ENCODED: "  + pwd + "-" + crypto.getSkeySpec().getEncoded().toString());
			
			msg = (TextView)findViewById(R.id.txt_messageclear);
			msg.setText(DUMMY_MESSAGE_CLEAR);
			
			byte[] encryptedBytes = crypto.encrypt(DUMMY_MESSAGE_CLEAR.getBytes());
			msgEncrypted = (TextView)findViewById(R.id.txt_msg_encrypted);
			msgEncrypted.setText("Encrypted: " + encryptedBytes.toString());
			
			
			byte[] decryptedBytes = crypto.decrypt(encryptedBytes);
			msgDecrypted = (TextView)findViewById(R.id.txt_msg_decrypted);
			msgDecrypted.setText("Decrypted: " + new String(decryptedBytes));
			
			TelephonyManager tMgr =  (TelephonyManager)this.getSystemService(TELEPHONY_SERVICE);
			final String phoneNum = tMgr.getLine1Number();
			
			final SagesKeyStore keystore = new SagesKeyStore(new File(Environment.getExternalStorageDirectory() + "/sageslib_testout/"));

			Button genButton = (Button)findViewById(R.id.btn_genkey);
			genButton.setOnClickListener(new OnClickListener() {
				
				
				@Override
				public void onClick(View v) {
					makeNewPassword();
					demonstrateEncryptionDecryption();
					SagesPrivateKey privateKey = new SagesPrivateKey(KeyGenerateActivity.this, "+" + phoneNum, null, crypto.getSkeySpec().getEncoded());
					try {
						keystore.addKey(privateKey);
						Toast.makeText(KeyGenerateActivity.this, "Private key saved for " + phoneNum, Toast.LENGTH_LONG).show();
					} catch (SagesKeyException e) {
						e.printStackTrace();
						Toast.makeText(KeyGenerateActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
					}
				}
			});
			
			
			Button sendKeyButton = (Button)findViewById(R.id.btn_send_aes_key);
			sendKeyButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					SmsManager smsMgr = SmsManager.getDefault();
					
					String msgHeader = MessageBuilderUtil.genMetaDataHeader(MsgTypeEnum.KEY_EXCH, "init aes");
					String msgBody = SagesMessage.my_key + new String(crypto.getSkeySpec().getEncoded());
					String text = msgHeader + msgBody;
					smsMgr.sendTextMessage("2404759981", null, text, null, null);
					
					
				}
			});
			
			Button encryptAndSendMsgButton = (Button)findViewById(R.id.btn_ecnrypt_send_msg);
			encryptAndSendMsgButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
					SmsManager smsMgr = SmsManager.getDefault();
					String msgHeader = MessageBuilderUtil.genMetaDataHeader(MsgTypeEnum.DATA, "enc aes");
					String msgBody;
					byte[] cipheredBody = crypto.encrypt(KeyGenerateActivity.DUMMY_MESSAGE_CLEAR.getBytes());
					
					byte[] cipheredBodyB64 = Base64.encode(cipheredBody, Base64.DEFAULT);
						msgBody = new String(cipheredBodyB64);
						String text = msgHeader + msgBody;
						Toast.makeText(KeyGenerateActivity.this, "SMS length: " + text.length(), Toast.LENGTH_SHORT).show();
						smsMgr.sendTextMessage("2404759981", null, text, null, null);
//						smsMgr.sendDataMessage("2404759981", null, (Short) null, textBytes, null, null);
						
//						smsMgr.sendTextMessage("2404759981", null, "just a test", null, null);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void makeNewPassword() {
		int min = 0;
		int max = 9;
		
		a = min + (int)(Math.random() * ((max - min) + 1));
		b = min + (int)(Math.random() * ((max - min) + 1));
		
		// password has to be 16 bytes (i.e. 128 bits)
		pwd = seedpwd + a + b;
	}
	
	private void demonstrateEncryptionDecryption(){
		try {
/*			crypto = new CryptoEngine((pwd).getBytes());
			
			key = (TextView)findViewById(R.id.txt_privateKey);
			key.setText("KEY ENCODED: " + pwd + "-" + crypto.getSkeySpec().getEncoded().toString());
			
			msg = (TextView)findViewById(R.id.txt_messageclear);
			msg.setText(DUMMY_MESSAGE_CLEAR);
			
			byte[] encryptedBytes = crypto.encrypt(DUMMY_MESSAGE_CLEAR.getBytes());
			msgEncrypted = (TextView)findViewById(R.id.txt_msg_encrypted);
			msgEncrypted.setText("Encrypted: " + encryptedBytes.toString());
			
			
			byte[] decryptedBytes = crypto.decrypt(encryptedBytes);
			msgDecrypted = (TextView)findViewById(R.id.txt_msg_decrypted);
			msgDecrypted.setText("Decrypted: " + new String(decryptedBytes));
*/			
			crypto = new CryptoEngine((pwd).getBytes());
			
			key = (TextView)findViewById(R.id.txt_privateKey);
			key.setText("KEY ENCODED: " + pwd + "-" + crypto.getSkeySpec().getEncoded().toString());
			
			msg = (TextView)findViewById(R.id.txt_messageclear);
			msg.setText(DUMMY_MESSAGE_CLEAR);
			
			byte[] encryptedBytes = crypto.encrypt(DUMMY_MESSAGE_CLEAR.getBytes());
			byte[] b64Cipher = Base64.encode(encryptedBytes, Base64.DEFAULT);
			msgEncrypted = (TextView)findViewById(R.id.txt_msg_encrypted);
			msgEncrypted.setText("Encrypted: " + new String(encryptedBytes) + " Encoded b64: " + new String(b64Cipher));
			
			byte[] b64DecodedCipher = Base64.decode(b64Cipher, Base64.DEFAULT);
			byte[] decryptedBytes = crypto.decrypt(b64DecodedCipher);
//			byte[] decryptedBytes = crypto.decrypt(encryptedBytes);
			msgDecrypted = (TextView)findViewById(R.id.txt_msg_decrypted);
			msgDecrypted.setText("b64 Decoded: " + new String(b64DecodedCipher) + " Decrypted: " + new String(decryptedBytes));
			
					
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
