/**
 * 
 */
package edu.jhuapl.sages.mobile.lib.odk;

import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.NoSuchPaddingException;

import  org.spongycastle.jce.provider.BouncyCastleProvider;

import org.spongycastle.util.encoders.Base64;

import edu.jhuapl.sages.mobile.lib.SharedObjects;
import edu.jhuapl.sages.mobile.lib.crypto.engines.CryptoEngine;
import edu.jhuapl.sages.mobile.lib.crypto.persisted.SagesKeyException;


/**
 * @author POKUAM1
 * @created Sep 22, 2011
 */
public class DataChunker {

	static {
		// https://github.com/rtyley/spongycastle/issues/10
		// Adds a new provider, at a specified position
//	    Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
	    
		Security.removeProvider(new org.spongycastle.jce.provider.BouncyCastleProvider().getName());
		Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
	}
	private String txId;

	public String getTxId(){
		return txId;
	}
	
	static int minage = 0;
	static int maxage = 120;
	static int mindis = 0;
	static int maxdis = 55;

	static String diseaseString = "fever,chills,sadness,tired,fatigue,vomit,diarrhea,anger,depression,joy,death,faint,lightheaded,loosestools,denguefever,tears,weightloss,weightgain,drymouth";
	static String[] diseases = diseaseString.split(",");

	static String[] sex = { "m", "f", "unk" };

	public static int getAge(int min, int max) {
		return genRandom(min, max);
	}

	public static int getAge() {
		return genRandom(minage, maxage);
	}

	public static int getDistrict(int min, int max) {
		return genRandom(min, max);
	}

	public static int getDistrict() {
		return genRandom(mindis, maxdis);
	}

	public static int genRandom(int min, int max) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}

	public static String buildRecord() {
		int randis1 = genRandom(0, diseases.length - 1);
		int randis2 = genRandom(0, diseases.length - 1);
		int randis3 = genRandom(0, diseases.length - 1);
		int ransex = genRandom(0, sex.length - 1);
		System.out.println(ransex + " " + randis1 + " " + randis2 + " "
				+ randis3);
		String rec = "oevisit " + getDistrict() + " " + sex[ransex] + " "
				+ getAge() + " " + diseases[randis1] + " " + diseases[randis2]
				+ " " + diseases[randis3];

		return rec;
	}

	public static String[] chunkData(String data) {
		int chunkLength = 160;
		int infoLength = 130;
		int expectedChunks = (int) Math.ceil((double) data.length()
				/ (double) infoLength);

		System.out.println("Exptected Chunks: " + expectedChunks);
		System.out.println(data);
		String[] chunks = new String[expectedChunks];
		int hdrLength = chunkLength - infoLength;
		int beginIndex = 0;
		int endIndex = beginIndex + infoLength;
		int i = 0;
		// String txId = getTxID(); //TODO
		String txId = generateTxID_Calendar();

		String trailText = "";
		int trailIndex = -1;
		String header = i + "," + chunks.length + "," + txId + ":" + "formid#";
		while (endIndex <= data.length()) {
			System.out.println("header[" + header.length() + "], data["
					+ data.substring(beginIndex, endIndex).length() + "]");
			chunks[i] = header + data.substring(beginIndex, endIndex);
			trailIndex = endIndex;
			i++;
			beginIndex += infoLength;
			endIndex += infoLength;
		}
		if (trailIndex > -1 && trailIndex < data.length()) {
			chunks[i] = header + data.substring(trailIndex);
			System.out.println("header[" + header.length() + "], data["
					+ data.substring(beginIndex).length() + "]");
		}
		i = 1;
		for (String chunk : chunks) {
			System.out.println("CHUNK[" + chunk.length() + "] " + i + ": "
					+ chunk);
			i++;
		}

		return chunks;
	}

	public static Map<String, String> chunkDataWithHeader(String data/*, String
																	 * formId
																	 */) {
		Map<String, String> payload = new HashMap<String, String>();
		int chunkLength = 160;
		int infoLength = 130;
		int expectedChunks = (int) Math.ceil((double) data.length()
				/ (double) infoLength);

		System.out.println("Exptected Chunks: " + expectedChunks);
		System.out.println(data);
		String[] chunks = new String[expectedChunks];
		int hdrLength = chunkLength - infoLength;
		int beginIndex = 0;
		int endIndex = beginIndex + infoLength;
		int i = 0;
		// String txId = getTxID(); //TODO
		String txId = generateTxID_Calendar();
		String trailText = "";
		int trailIndex = -1;
		// String header = i + "," + chunks.length + "," + txId + ":" + formId +
		// "#" ;
		String header = "HEADER";
		String SYMBOL = "|";
		while (endIndex <= data.length()) {
			header = (i + 1) + SYMBOL + chunks.length + SYMBOL + txId + ":";
			System.out.println("header[" + header.length() + "], data["
					+ data.substring(beginIndex, endIndex).length() + "]");
			// chunks[i] = header + data.substring(beginIndex, endIndex);
			chunks[i] = data.substring(beginIndex, endIndex);
			payload.put(header, chunks[i]);
			trailIndex = endIndex;
			i++;
			beginIndex += infoLength;
			endIndex += infoLength;
		}
		if (trailIndex > -1 && trailIndex < data.length()) {
			header = (i + 1) + SYMBOL + chunks.length + SYMBOL + txId + ":";
			// chunks[i] = header + data.substring(trailIndex);
			chunks[i] = data.substring(trailIndex);
			payload.put(header, chunks[i]);
			System.out.println("header[" + header.length() + "], data["
					+ data.substring(beginIndex).length() + "]");
		}
		i = 1;
		for (String chunk : chunks) {
			System.out.println("CHUNK[" + chunk.length() + "] " + i + ": "
					+ chunk);
			i++;
		}

		return payload;
	}

	public Map<String, String> chunkDataWithHeaderGo(String data, int segSize, int allowedInfoSize /* , String formId */) {
		Map<String, String> payload = new HashMap<String, String>();
//		int chunkLength = 160;
//		int infoLength = 130;
		/*int expectedChunks = (int) Math.ceil((double) data.length()
				/ (double) infoLength);*/

		
		String smsText = data;
//		int numSegs = (int) Math.round(smsText.length() / (double) allowedInfoSize);
		int numSegs = (int) Math.ceil(smsText.length() / (double) allowedInfoSize);
		int expectedChunks = numSegs;
		int chunkLength = segSize;
		int infoLength = allowedInfoSize; //ex. 130
		int start = 0;
		int end = segSize -1;
		
		
		System.out.println("Exptected Chunks: " + expectedChunks);
		System.out.println(data);
		String[] chunks = new String[expectedChunks];
		int hdrLength = chunkLength - infoLength;
		int beginIndex = 0;
		int endIndex = beginIndex + infoLength;
		int i = 0;
		// String txId = getTxID(); //TODO
		String txId = generateTxID_Calendar();
		this.txId = txId;

		String trailText = "";
		int trailIndex = -1;
		// String header = i + "," + chunks.length + "," + txId + ":" + formId +
		// "#" ;
		String header = "HEADER";
		String SYMBOL = "|";
		while (endIndex <= data.length()) {
			header = (i + 1) + SYMBOL + chunks.length + SYMBOL + txId + ":";
			System.out.println("header[" + header.length() + "], data["
					+ data.substring(beginIndex, endIndex).length() + "]");
			// chunks[i] = header + data.substring(beginIndex, endIndex);
			chunks[i] = data.substring(beginIndex, endIndex);
			payload.put(header, chunks[i]);
			trailIndex = endIndex;
			i++;
			beginIndex += infoLength;
			endIndex += infoLength;
		}
		if (trailIndex > -1 && trailIndex < data.length()) {
			header = (i + 1) + SYMBOL + chunks.length + SYMBOL + txId + ":";
			// chunks[i] = header + data.substring(trailIndex);
			chunks[i] = data.substring(trailIndex);
			payload.put(header, chunks[i]);
			System.out.println("header[" + header.length() + "], data["
					+ data.substring(beginIndex).length() + "]");
		}
		i = 1;
		for (String chunk : chunks) {
			System.out.println("CHUNK[" + chunk.length() + "] " + i + ": "
					+ chunk);
			i++;
		}

		return payload;
	}
	
	/**
	 * just passes the smsText as byte[] -- DOES NOT ENCRYPT YET
	 * @param smsText
	 * @return
	 * @throws SagesKeyException 
	 */
	public static byte[] encryptDataGo(String smsText) throws SagesKeyException{
		byte[] cipher = null;
		try {
			CryptoEngine cryptoEngine = SharedObjects.getCryptoEngine();
			
			cipher = cryptoEngine.encrypt(smsText.getBytes());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new SagesKeyException(e.getMessage());
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			throw new SagesKeyException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new SagesKeyException(e.getMessage());
		} 
		
		return cipher;
	}
	
	/**
	 * just returns the original cipher -- DOES NOT DECRYPT YET
	 * @param cipher
	 * @return
	 * @throws SagesKeyException 
	 */
	public static byte[] decryptDataGo(byte[] cipher) throws SagesKeyException{
		byte[] decryptedData = null;
		try {
			CryptoEngine cryptoEngine = SharedObjects.getCryptoEngine();
			decryptedData = cryptoEngine.decrypt(cipher);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new SagesKeyException(e.getMessage());
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			throw new SagesKeyException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new SagesKeyException(e.getMessage());

		}
		return decryptedData;
	}
	
	/**
	 * Base64 encodes the cipher
	 *  
	 * @param cipher
	 * @return
	 */
	public static byte[] base64EncodeCipherGo(byte[] cipher){
		return Base64.encode(cipher);
	}
	
	
	/**
	 * Decodes the base64 encoded cipher
	 * @param b64encodedCipher
	 * @return 
	 */
	public static byte[] base64DecodeCipherGo(byte[] b64encodedCipher){
		return Base64.decode(b64encodedCipher);
	}
	public Map<String, String> chunkDataWithHeaderGo1(String data, int segSize, int allowedInfoSize /* , String formId */) {
		Map<String, String> payload = new HashMap<String, String>();
		
		
		String smsText = data;
		int numSegs = (int) Math.ceil(smsText.length() / (double) allowedInfoSize);
		int expectedChunks = numSegs;
		int chunkLength = segSize;
		int infoLength = allowedInfoSize; //ex. 130
		int start = 0;
		int end = segSize -1;
		
		
		System.out.println("Exptected Chunks: " + expectedChunks);
		System.out.println(data);
		String[] chunks = new String[expectedChunks];
		int hdrLength = chunkLength - infoLength;
		int beginIndex = 0;
		int endIndex = beginIndex + infoLength;
		int i = 0;
		// String txId = getTxID(); //TODO
		String txId = generateTxID_Calendar();
		this.txId = txId;
		
		String trailText = "";
		int trailIndex = -1;
		// String header = i + "," + chunks.length + "," + txId + ":" + formId +
		// "#" ;
		String header = "HEADER";
		String SYMBOL = "|";
		while (endIndex <= data.length()) {
			header = (i + 1) + SYMBOL + chunks.length + SYMBOL + txId + ":";
			System.out.println("header[" + header.length() + "], data[" + data.substring(beginIndex, endIndex).length() + "]");
			chunks[i] = data.substring(beginIndex, endIndex);
			payload.put(header, chunks[i]);
			trailIndex = endIndex;
			i++;
			beginIndex += infoLength;
			endIndex += infoLength;
		}
		if (trailIndex > -1 && trailIndex < data.length()) {
			header = (i + 1) + SYMBOL + chunks.length + SYMBOL + txId + ":";
			chunks[i] = data.substring(trailIndex);
			payload.put(header, chunks[i]);
			System.out.println("header[" + header.length() + "], data[" + data.substring(beginIndex).length() + "]");
		}
		i = 1;
		for (String chunk : chunks) {
			System.out.println("CHUNK[" + chunk.length() + "] " + i + ": " + chunk);
			i++;
		}
		
		return payload;
	}

	public static void main(String arcv[]) {
		DataChunker dc = new DataChunker();
		String data = "";
		int ctr = 0;
		int maxdatalength = 400;
		while (data.length() <= maxdatalength) {
			String tmpRec = buildRecord();
			if (data.length() + tmpRec.length() >= maxdatalength) {
				break;
			}
			data = data + " " + buildRecord();
			ctr++;
		}
		System.out.println("TOTAL RECORDS: " + ctr + "\n" + "DATA LENGTH: "
				+ data.length() + "\n\n");

		dc.chunkData(data);
	}

	public static String generateTxID() {
		Calendar c = Calendar.getInstance();
		Date d = c.getTime();
		int year = c.get(Calendar.DAY_OF_YEAR);
		int doy = c.get(Calendar.DAY_OF_YEAR);
		int hr = c.get(Calendar.HOUR_OF_DAY);
		int min = c.get(Calendar.MINUTE);
		int ms = c.get(Calendar.MILLISECOND);
		return "" + year + doy + hr + min + ms;
	}

	public static String generateTxID_Calendar() {
		Calendar cal = new GregorianCalendar();
		int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
		int hr = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);
		String txId = dayOfYear + "" + hr + "" + min + "" + sec;
		return txId;
	}
}
