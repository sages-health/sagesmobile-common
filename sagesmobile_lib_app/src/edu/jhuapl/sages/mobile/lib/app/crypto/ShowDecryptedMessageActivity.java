package edu.jhuapl.sages.mobile.lib.app.crypto;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import edu.jhuapl.sages.mobile.lib.app.MainActivity;
import edu.jhuapl.sages.mobile.lib.app.R;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKey.KeyEnum;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKeyException;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKeyStore;

/**
 * Shows the decrypted data message (AES-128 bit encrypted, base64 encoded) once received and processed by the @link{SagesSmsReceiver}
 * 
 * @author pokuam1
 * @date 5/16/2013
 */
public class ShowDecryptedMessageActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_show_decrypted_msg);
		File file = MainActivity.KEYSTORE_FILE;
		try {
		SagesKeyStore keystore = new SagesKeyStore(file);
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		String msg_body = extras.getString("msg_body");
		String msg_body_decrypted = new String(extras.getByteArray("msg_body_decrypted"));
		
		TextView encryptedMsg = (TextView)findViewById(R.id.txt_encrypted_cipher);
		encryptedMsg.setText(msg_body);
		
		TextView decryptedMsg = (TextView)findViewById(R.id.txt_decrypted_msg);
		decryptedMsg.setText(msg_body_decrypted);
		
		
			keystore.lookupKey(extras.getString("msg_sender"), KeyEnum.PRIVATE);
		} catch (SagesKeyException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} 
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		String msg_body = extras.getString("msg_body");
		String msg_body_decrypted = new String(extras.getByteArray("msg_body_decrypted"));
		
		TextView encryptedMsg = (TextView)findViewById(R.id.txt_encrypted_cipher);
		encryptedMsg.setText(msg_body);
		
		TextView decryptedMsg = (TextView)findViewById(R.id.txt_decrypted_msg);
		decryptedMsg.setText(msg_body_decrypted);
		
	}

}
