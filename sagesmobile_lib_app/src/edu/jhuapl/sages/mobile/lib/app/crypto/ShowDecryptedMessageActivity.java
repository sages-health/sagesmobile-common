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
package edu.jhuapl.sages.mobile.lib.app.crypto;

import edu.jhuapl.sages.mobile.lib.app.MainActivity;
import edu.jhuapl.sages.mobile.lib.app.R;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKey.KeyEnum;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKeyException;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKeyStore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

/**
 * Shows the decrypted data message (AES-128 bit encrypted, base64 encoded) once received and processed by the @link{SagesSmsReceiver}
 * 
 * @author sages
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
