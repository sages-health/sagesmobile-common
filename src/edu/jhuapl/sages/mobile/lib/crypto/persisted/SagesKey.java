package edu.jhuapl.sages.mobile.lib.crypto.persisted;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.content.Context;

/**
 * 
 * @author pokuam1
 * @date 05/02/13
 */
public class SagesKey {

	public enum KeyEnum {
		NONE, PUBLIC, PRIVATE
	}

	protected String tag = "key_";
	protected String locationId;
	protected String[] params;
	protected byte[] data;
	protected Context context;
	
	public SagesKey(Context context, String locationId, String[] params, byte[] data){
		this.context = context;
		this.tag = tag;
		this.locationId = locationId;
		this.params = params;
		this.data = data;
	}
	
	/**
	 * 
	 * @return false if locationId or data has null value
	 */
	public boolean checkKey(){
		boolean failsCheck = false;
		
		failsCheck = (locationId == null) || data == null;
		
		return failsCheck;
	}
	public void saveKey(String path) throws SagesKeyException{
		File file = getFileToSaveKey(path);
		try {
			writeKeytoFile(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new SagesKeyException("File not found when trying to save key", e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new SagesKeyException("File not found when trying to save key", e);
		}
	}
	
	public static SagesKey loadKey(String path, String locationId, KeyEnum keyType) throws SagesKeyException{
		SagesKey key = new SagesKey(null, locationId, null, null);
		String keyTag = "keys_";
		switch (keyType) {
		case NONE: keyTag = "keys_";
			break;
		case PUBLIC: keyTag = "public_";
			key = new SagesPublicKey(null, locationId, null, null);
			break;
		case PRIVATE: keyTag = "private_";
			key = new SagesPrivateKey(null, locationId, null, null);
			break;
		}
		try {
			File loadKeyFile = getFileToLoadKey(path, locationId, keyTag);
			if (loadKeyFile == null){
				return null;
			}
			byte[] data = readKeyFromFile(loadKeyFile);
			key.setData(data);
		} catch (IOException e) {
			e.printStackTrace();
			throw new SagesKeyException("IO Exception while loading key from file", e);
		}
		return key;
	}
	
	private void setData(byte[] data) {
		this.data = data;
	}
	
	public byte[] getData(){
		return this.data;
	}

	private String makeKeyFileName(){
		return this.tag + ".txt";
	}
	
	protected File getFileToSaveKey(String path) throws SagesKeyException{
		File file = new File (path + "/keys_" + locationId);
		if (!file.exists()){
			if (!file.mkdir()){
				throw new SagesKeyException("Unable to create directory for locationId's storage of all keys", new IOException());
			}
		}
		File fileraw = new File(file, locationId + makeKeyFileName());
		try {
			fileraw.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileraw;
	}
	
	protected void writeKeytoFile(File file) throws IOException{
	/*	FileOutputStream out = new FileOutputStream(file);  
		out.write(data);
		out.close();*/
		
		// TODO: refactor to use RandomAccessFile -- Elegant way to read file into byte[] in Java
		// http://stackoverflow.com/q/6058003
		
		RandomAccessFile rafile = new RandomAccessFile(file.getAbsolutePath(), "rw");
		try {
		rafile.write(data);
		} finally {
			rafile.close();
		}
	}
	
	
	protected static File getFileToLoadKey(String path, String locationId, String keyTag){
		File file = new File(path + "/" +  "keys_" + locationId + "/" + locationId + keyTag + ".txt");
		if (file.exists()){
			return file;
		} else {
			return null;
		}
	}
	
	protected static byte[] readKeyFromFile(File file) throws IOException{
	/*  FileInputStream in = new FileInputStream(file);

		byte[] filedata = new byte[(int) in.getChannel().size()];
		int i = 0;
		while (in.read() != -1){
			filedata[i] = (byte) in.read();
			i++;
		}
		in.close();
		return filedata;
		*/
		RandomAccessFile rafile = new RandomAccessFile(file.getAbsolutePath(), "r");
		
		try {
			long fileLength = rafile.length();
			int length = (int) fileLength;
			if (length != fileLength) throw new IOException("File size >= 2 GB"); // TODO: why? http://stackoverflow.com/a/7591216
			
			byte[] filedata = new byte[length];
			rafile.readFully(filedata);
			return filedata;
			
		} finally {
			rafile.close();
		}
	}
}
