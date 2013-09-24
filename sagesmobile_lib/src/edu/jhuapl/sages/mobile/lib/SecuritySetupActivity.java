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
package edu.jhuapl.sages.mobile.lib;

import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;


/**
 * Admin view for setting up Security within calling app
 * 
 * @author sages
 * @date 05/28/13
 */
public class SecuritySetupActivity extends Activity {
    private SharedPreferences prefs;
    protected String prefsFileName = "edu.jhuapl.sages.mobile.lib.app";
    private static final String KEY_AESKEY = "KEY_AESKEY";
    private static final String no_key_set = "no key set";
    private static final String ENCRYPTION_ON = "ENCRYPTION_ON";
    protected static SharedObjects so;

    private Button btnGenAesKey;
    private EditText txtAesKeyVal;
    private ToggleButton tglEncryptionOnOff;
    protected boolean showEncryptionToggle;

    public SecuritySetupActivity() {
        this.showEncryptionToggle = true;
    }


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_setup);

        // View objects
        btnGenAesKey = (Button) findViewById(R.id.btn_makeaeskey);
        txtAesKeyVal = (EditText) findViewById(R.id.txt_aeskey);
        tglEncryptionOnOff = (ToggleButton) findViewById(R.id.tgl_encryption);
        
        setPreferencesFileName();
        prefs = this.getSharedPreferences(prefsFileName, Context.MODE_PRIVATE);

        String aeskey = prefs.getString(KEY_AESKEY, no_key_set);
        txtAesKeyVal.setText(aeskey);
        
        // initialize dummy key if user has not set key yet
        if (no_key_set.equals(aeskey)) {
        	btnGenAesKey.setEnabled(false);
        	try {
        		so = new SharedObjects();
        	} catch (NoSuchAlgorithmException e) {
        		e.printStackTrace();
        	} catch (NoSuchPaddingException e) {
        		e.printStackTrace();
        	}
        } else if (!no_key_set.equals(aeskey)) {
            TextView aeskeyview = (TextView) findViewById(R.id.txtv_aeskey);
            btnGenAesKey.setEnabled(true);
            try {
                SharedObjects.updateCryptoEngine(aeskey);
            } catch (NoSuchAlgorithmException e) {
                aeskeyview.setText("Error re-initializing system KEY from saved preferences");
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                aeskeyview.setText("Error re-initializing system KEY from saved preferences");
                e.printStackTrace();
            }
        }

        txtAesKeyVal.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 16) {
                    btnGenAesKey.setEnabled(false);
                } else if (s.length() == 16) {
                    btnGenAesKey.setEnabled(true);
                };
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });



        btnGenAesKey.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String aeskeyval = txtAesKeyVal.getText().toString();
                prefs.edit().putString(KEY_AESKEY, aeskeyval).commit();

                String shared_aeskeyval = prefs.getString(KEY_AESKEY, null);
                TextView aeskeyview = (TextView) findViewById(R.id.txtv_aeskey);
                aeskeyview.setText("Shared Prefs: " + prefsFileName + "\n" + shared_aeskeyval);
                try {
                    SharedObjects.updateCryptoEngine(shared_aeskeyval);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                }
            }
        });

        // Some views (i.e. RapidAndroid) should not display the toggle button
        if (showEncryptionToggle) {
            tglEncryptionOnOff.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        SharedObjects.setEncryptionOn(true);
                        prefs.edit().putBoolean(ENCRYPTION_ON, true).commit();
                    } else {
                        SharedObjects.setEncryptionOn(false);
                        prefs.edit().putBoolean(ENCRYPTION_ON, false).commit();
                    }
                }
            });

            if (prefs.getBoolean(ENCRYPTION_ON, false)) {
                tglEncryptionOnOff.setChecked(true);
            } else {
                tglEncryptionOnOff.setChecked(false);
            }

        } else {
            ViewGroup layout = (ViewGroup) tglEncryptionOnOff.getParent();
            layout.removeView(tglEncryptionOnOff);
        }

    }

    
    @Override
    protected void onResume() {
    	Log.d("sages security", "Resuming Security Activity");
    	super.onResume();
    	
    }
    
    protected void setPreferencesFileName() {
        prefsFileName = "edu.jhuapl.sages.mobile.lib.app";
    }

}
