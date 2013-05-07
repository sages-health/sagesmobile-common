package edu.jhuapl.sages.mobile.lib.crypto.persisted;

/**
 * 
 * @author pokuam1
 * @date 05/04/13
 */
public interface SagesContactsI {

	public void addContact(SagesContact contact);
	public SagesContact getContact(String agentId);
}
