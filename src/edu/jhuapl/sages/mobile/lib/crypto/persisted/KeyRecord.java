package edu.jhuapl.sages.mobile.lib.crypto.persisted;

import android.content.Context;

/**
 * 
 * @author pokuam1
 * @date 05/02/13
 */
public class KeyRecord implements RecordI {

	protected final String locationId;
	protected final Context context;
	
	public KeyRecord (Context context, String locationId) {
		this.context = context;
		this.locationId = locationId;
	}
	
	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

}
