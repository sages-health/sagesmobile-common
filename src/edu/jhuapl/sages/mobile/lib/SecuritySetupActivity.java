package edu.jhuapl.sages.mobile.lib;

import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;


/**
 * Admin view for setting up Security within calling app
 * 
 * @author pokuam1
 * @date 05/28/13
 */
public class SecuritySetupActivity extends Activity {
	private SharedPreferences prefs;
	protected static String prefsFileName = "edu.jhuapl.sages.mobile.lib.app";
	private static final String KEY_AESKEY = "KEY_AESKEY";
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
        
        setPreferencesFileName();
        try {
			so = new SharedObjects();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
        
        prefs = this.getSharedPreferences(prefsFileName, Context.MODE_PRIVATE);
        
        
        txtAesKeyVal = (EditText)findViewById(R.id.txt_aeskey);
        txtAesKeyVal.setText(prefs.getString(KEY_AESKEY, "no key set"));
        btnGenAesKey = (Button)findViewById(R.id.btn_makeaeskey);
        btnGenAesKey.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String aeskeyval = txtAesKeyVal.getText().toString();
				prefs.edit().putString(KEY_AESKEY, aeskeyval).commit();
				String shared_aeskeyval = prefs.getString(KEY_AESKEY, null);
				TextView aeskeyview = (TextView)findViewById(R.id.txtv_aeskey);
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
       
        tglEncryptionOnOff = (ToggleButton)findViewById(R.id.tgl_encryption);
        
        // Some views (i.e. RapidAndroid) should not display the toggle button
        if (showEncryptionToggle){
        	tglEncryptionOnOff.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        		
        		@Override
        		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        			if (isChecked){
        				SharedObjects.setEncryptionOn(true);
        			} else {
        				SharedObjects.setEncryptionOn(false);
        			}
        		}
        	});
        } else {
        	ViewGroup layout = (ViewGroup)tglEncryptionOnOff.getParent();
        	layout.removeView(tglEncryptionOnOff);
        }
        
        

    }
    
    protected void setPreferencesFileName(){
    	prefsFileName = "edu.jhuapl.sages.mobile.lib.app";
    }

}
