package edu.jhuapl.sages.mobile.lib.crypto.persisted;

import android.content.Context;

/**
 * 
 * @author pokuam1
 * @date 05/02/13
 */
public class SagesPublicKey extends SagesKey {

	private String tag = "public_";
	
	public SagesPublicKey(Context context, String locationId, String[] params, byte[] data) {
		super(context, locationId, params, data);
		super.tag = tag;
	}


}
