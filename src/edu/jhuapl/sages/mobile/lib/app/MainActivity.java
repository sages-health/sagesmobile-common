package edu.jhuapl.sages.mobile.lib.app;

import java.io.File;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import edu.jhuapl.sages.mobile.lib.app.tests.crypto.SagesKeyTest;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKey.KeyEnum;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKeyException;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKeyStore;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesPublicKey;
import edu.jhuapl.sages.mobile.lib.message.MessageBuilderUtil;
import edu.jhuapl.sages.mobile.lib.message.SagesMessage;
import edu.jhuapl.sages.mobile.lib.message.SagesMessage.MsgTypeEnum;

public class MainActivity extends Activity {

	private ListView listView;
	
	private TextView msgText;
	//private File keyStoreFile;
	public static final File KEYSTORE_FILE = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/sageslib_testout");
	
	private static TelephonyManager tMgr; 
	public static  String NUMBER_THIS_DEVICE;
	private static int CONTACT_DEVICE_INDEX = 0;
	
	private String  recipientId = null;
	private static SagesKeyStore keystore;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		tMgr = (TelephonyManager)this.getSystemService(TELEPHONY_SERVICE);
		NUMBER_THIS_DEVICE = tMgr.getLine1Number();
		
		TextView deviceNumTxt = (TextView)findViewById(R.id.txt_deviceNum);
		deviceNumTxt.append(" " + NUMBER_THIS_DEVICE);
		
		Toast.makeText(this, "onCreate()", Toast.LENGTH_SHORT).show();
		
		try {
			//keyStoreFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/sageslib_testout");
			keystore = new SagesKeyStore(KEYSTORE_FILE);
		} catch (SagesKeyException e1) {
			Log.e("SagesCrypto", "Keystore file was not created.");
			e1.printStackTrace();
		}
		
		
		// Grabbing contacts from dummy string-array resource file
		String[]  contacts = getResources().getStringArray(R.array.contacts_array);
		
		
		// will disable this device's phone number from the view using this index
		/*	TODO wasn't working as expected	
		 * for (String c: contacts){
			if (c.contains(NUMBER_THIS_DEVICE)){
			  break;
			}
			CONTACT_DEVICE_INDEX++;
		}*/
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contacts);
		
		listView = (ListView)findViewById(R.id.contacts_list);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				try {
					recipientId = (String) ((TextView)view).getText();
					SagesPublicKey key = (SagesPublicKey) keystore.lookupKey(recipientId, KeyEnum.PUBLIC);
					TextView textview = (TextView)findViewById(R.id.publickey_contact);
					if (key !=null){
						textview.setText(new String(key.getData()));
						textview.setBackgroundColor(Color.GREEN);
					} else {
						textview.setText("There is no key for this contact [" +  recipientId + "]. Initiate a key exchange session");
						textview.setBackgroundColor(Color.WHITE);
					}
					refreshKeyExchangeMsg();
				} catch (SagesKeyException e) {
					e.printStackTrace();
				}
			}
			
		});
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// alert dialog to delete key
				final String selectedPhoneNum = ((TextView)view).getText().toString();
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
				alertDialogBuilder.setTitle("Delete Keys for " + selectedPhoneNum);
				alertDialogBuilder
					.setMessage("Yes to delete")
					.setCancelable(true)
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {
								keystore.removeAllKeysFor(selectedPhoneNum);
								Toast.makeText(MainActivity.this, "Keys deleted for " + selectedPhoneNum , Toast.LENGTH_SHORT).show();
								
							} catch (SagesKeyException e) {
								e.printStackTrace();
								Log.e("SagesCrypto", "Issue occurred trying to delete keys.");
								Toast.makeText(MainActivity.this, "Keys were not deleted. Failure occured.", Toast.LENGTH_SHORT).show();
							}
						}
					})
					.setNegativeButton("No", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
					
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
					
				
				return false;
			}
		});
		
		Button keyExchangeButton = (Button)findViewById(R.id.btn_KeyExchange);
		keyExchangeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				msgText = ((TextView)findViewById(R.id.txt_keyexchange_msg));
				refreshKeyExchangeMsg();
				SmsManager smsManager = SmsManager.getDefault();
				if (recipientId.startsWith("+")){
					//recipientId = recipientId.substring(1);
				}
				try {
					saveOwnKeyPriorToSMS(MainActivity.this, "+" + NUMBER_THIS_DEVICE,msgText.getText().toString());
					smsManager.sendTextMessage(recipientId, null, msgText.getText().toString(), null, null);
					Toast.makeText(MainActivity.this, "Key Exchange SMS sent to " + recipientId, Toast.LENGTH_SHORT).show();
				} catch (SagesKeyException e) {
					e.printStackTrace();
					Toast.makeText(MainActivity.this, "Couldn't save key. Key Exchange NOT SMS sent to " + recipientId, Toast.LENGTH_SHORT).show();
				}

			}


		});

	}
	
	public static void saveOwnKeyPriorToSMS(Context context, String agentId, String msg) throws SagesKeyException{
		if (keystore.lookupKey(agentId, KeyEnum.PUBLIC) == null){
			SagesPublicKey key = new SagesPublicKey(context, agentId, null, msg.getBytes());
			keystore.addKey(key);
		}
		
/*		if (keystore.lookupKey("+" + NUMBER_THIS_DEVICE,KeyEnum.PUBLIC) == null){
			SagesPublicKey key = new SagesPublicKey(MainActivity.this, "+" + NUMBER_THIS_DEVICE, null, msgText.getText().toString().getBytes());
			keystore.addKey(key);
		}
*/
	}
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

	};
	
	private void refreshKeyExchangeMsg() {
		msgText = (TextView)findViewById(R.id.txt_keyexchange_msg);
		String msgHeader = MessageBuilderUtil.genMetaDataHeader(MsgTypeEnum.KEY_EXCH, "init");
		String msgBody = SagesMessage.my_key + SagesKeyTest.testData + " (" + NUMBER_THIS_DEVICE +") - " + 
				Calendar.getInstance().get(Calendar.HOUR) + Calendar.getInstance().get(Calendar.MINUTE) + Calendar.getInstance().get(Calendar.SECOND);
		
		msgText.setText(msgHeader + msgBody);

	}
		
	
	@Override
	protected void onStart() {
		super.onStart();
		Toast.makeText(this, "onStart()", Toast.LENGTH_SHORT).show();
		
	};
	
	@Override
	protected void onResume() {
		super.onResume();
		Toast.makeText(this, "onResume()", Toast.LENGTH_SHORT).show();
	};
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		Toast.makeText(this, "onRestoreInstnceSate(Bundle)", Toast.LENGTH_SHORT).show();
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
