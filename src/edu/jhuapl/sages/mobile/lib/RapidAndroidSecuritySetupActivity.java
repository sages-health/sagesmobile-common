package edu.jhuapl.sages.mobile.lib;


/**
 * Admin view for setting up Security within calling app -- in this case RapidAndroid
 * 
 * @author pokuam1
 * @date 05/28/13
 */
public class RapidAndroidSecuritySetupActivity extends SecuritySetupActivity {

	public RapidAndroidSecuritySetupActivity() {
		super.showEncryptionToggle = false;
	}
	
	@Override
	protected void setPreferencesFileName(){
		this.prefsFileName = "edu.jhuapl.sages.mobile.lib.RapidAndroid";
	}
	
}
