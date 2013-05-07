package edu.jhuapl.sages.mobile.lib.crypto.persisted;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author pokuam1
 * @date 05/04/13
 */
public class SagesContacts implements SagesContactsI {

	private Map<String, SagesContact> contacts;
	
	
	public SagesContacts(){
		this.contacts = new HashMap<String, SagesContact>();
	}
	@Override
	public SagesContact getContact(String agentId) {
		return contacts.get(agentId);
	}

	@Override
	public void addContact(SagesContact contact) {
		contacts.put(contact.getAgentId(), contact);
	}

}
