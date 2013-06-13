package edu.jhuapl.sages.mobile.lib.receiver;

import android.content.Context;
import android.content.Intent;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKeyException;
import edu.jhuapl.sages.mobile.lib.message.SagesMessage;
import edu.jhuapl.sages.mobile.lib.message.SagesMessage.MsgTypeEnum;

/**
 * {@link OdkSagesSmsReceiver} is a broadcast receiver that detects received SMS. It is intended to have ODK specific handling within
 * a SAGES system.
 *  
 * @author pokuam1
 * @date 06/13/13
 */
public class OdkSagesSmsReceiver extends SagesSmsReceiver {

	private static final String ODK_SMS_RECEIVED = "edu.jhuapl.sages.mobile.lib.android.provider.Telephony.ODK_SMS_RECEIVED";
	
	/**
	 * Upon determing message is of type {@linkplain MsgTypeEnum.DATA}  then sends our broadcast for
	 * new Intent("edu.jhuapl.sages.mobile.lib.android.provider.Telephony.ODK_SMS_RECEIVED")
	 * 
	 * @param sender
	 * @param body
	 * @throws SagesKeyException 
	 */
	@Override
	protected void handleDataMessage(Context context, Intent intent, String sender, String body) {
		if (!body.contains(SagesMessage.data)){ //TODO Handle this case.
			return;
		}
		
		Intent broadcast = new Intent(ODK_SMS_RECEIVED);
		broadcast.putExtras(intent.getExtras());
		context.sendBroadcast(broadcast);
	}
	
	/**
	 * Sends our broadcast for new Intent("edu.jhuapl.sages.mobile.lib.android.provider.Telephony.ODK_SMS_RECEIVED")
	 * 
	 * @param sender
	 * @param body
	 * @throws SagesKeyException 
	 */
	protected void handleUnknownMessage(Context context, Intent intent, String sender, String body) {
		
		Intent broadcast = new Intent(ODK_SMS_RECEIVED);
		broadcast.putExtras(intent.getExtras());
		context.sendBroadcast(broadcast);
	}
	
}
