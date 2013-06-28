package edu.jhuapl.sages.mobile.lib.app.tests.odk;

import java.util.ArrayList;

import android.test.AndroidTestCase;
import edu.jhuapl.sages.mobile.lib.message.SagesMessage;
import edu.jhuapl.sages.mobile.lib.odk.SagesOdkMessage;

public class SagesOdkMessageTest extends AndroidTestCase {

	
	private final String sms_lessthan160chars = "oevisit . john 23 laurel vomitting fatigue dengue";
	private final String sms_exactly160chars = "oevisit . amy 34 5 texas fatigue dengue oevisit . " +
			"amy 34 5 texas fatigue dengue oevisit . amy 34 5 texas fatigue dengue oevisit . amy 34 5 texas fatigue dengue!";
	private final String sms_greaterthan160chars = "oevisit . carlton 88 190 maine hallucinations sweats upset-stomach 'account we went' +" +
			" to the store to purchase some food for the storm. during that time the power went out and we did not know that the food we" +
			" bought was spoiled. we purchased dairy and meat from the coolers. we ate that food in the evening and in just a few hours we" +
			" began to develop our awful symptoms. i was seeing things and developed profuse sweating. just awful. so awful.";
	private final String sms_approximately900chars = "oevisit . carlton 88 190 maine hallucinations sweats upset-stomach 'account we went' +" +
			" to the store to purchase some food for the storm. during that time the power went out and we did not know that the food we" +
			" bought was spoiled. we purchased dairy and meat from the coolers. we ate that food in the evening and in just a few hours we" +
			" began to develop our awful symptoms. i was seeing things and developed profuse sweating. just awful. so awful." +
			"oevisit . carlton 88 190 maine hallucinations sweats upset-stomach 'account we went' +" +
			" to the store to purchase some food for the storm. during that time the power went out and we did not know that the food we" +
			" bought was spoiled. we purchased dairy and meat from the coolers. we ate that food in the evening and in just a few hours we" +
			" began to develop our awful symptoms. i was seeing things and developed profuse sweating. just awful. so awful.";

	public void testLessThan160Chars(){
		assertTrue(sms_lessthan160chars.length() < 160);
		SagesOdkMessage som = new SagesOdkMessage(sms_lessthan160chars);
		som.configure(false);
		ArrayList<String> divided_lessthan160chars = som.getDividedBlob();
		assertEquals(true, divided_lessthan160chars.size() == 1);
		assertEquals(sms_lessthan160chars, divided_lessthan160chars.get(0));
		
		som.configure(true);
		divided_lessthan160chars = som.getDividedBlob();
		assertEquals(true, divided_lessthan160chars.size() == 1);
		assertTrue(divided_lessthan160chars.get(0).startsWith(SagesMessage.data + " " + SagesMessage.enc_aes + SagesMessage.DELIM_HeaderToBody));
		
	}
	
	public void testExactly160Chars(){
		assertEquals(160, sms_exactly160chars.length());
		SagesOdkMessage som = new SagesOdkMessage(sms_exactly160chars);
		som.configure(false);
		ArrayList<String> divided_exactly160chars = som.getDividedBlob();
		assertEquals(1, divided_exactly160chars.size());

		som.configure(true);
		divided_exactly160chars = som.getDividedBlob();
		assertEquals(2, divided_exactly160chars.size());
	}
	

	public void testGreaterThan160Chars(){
		assertTrue(sms_greaterthan160chars.length() > 160);
		SagesOdkMessage som = new SagesOdkMessage(sms_greaterthan160chars);
		som.configure(false);
		ArrayList<String> divided_greaterThan160chars = som.getDividedBlob();
		assertEquals(4, divided_greaterThan160chars.size());
		
		som.configure(true);
		divided_greaterThan160chars = som.getDividedBlob();
		assertEquals(5, divided_greaterThan160chars.size());
		
	}
	public void testApproximately900Chars(){
		assertTrue(sms_approximately900chars.length() > 800);
		SagesOdkMessage som = new SagesOdkMessage(sms_approximately900chars);
		som.configure(false);
		ArrayList<String> divided_approximately900chars = som.getDividedBlob();
		assertEquals(7, divided_approximately900chars.size());
		
		som.configure(true);
		divided_approximately900chars = som.getDividedBlob();
		assertEquals(9, divided_approximately900chars.size());
		
	}
	
}
