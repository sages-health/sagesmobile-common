package edu.jhuapl.sages.mobile.lib.crypto.persisted;

import java.util.List;

/**
 * 
 * @author pokuam1
 * @date 05/04/13
 */
public class SagesContact {
	
	private String agentId;
	private String phoneNumber; 
	private List<String> sessionIds;
	private String currentSessionId;
	private String lastSessionId;
	
	public SagesContact(String agentId, String phonenumber){
		this.agentId = agentId;
		this.phoneNumber = phonenumber;
	}

	public String getAgentId() {
		return agentId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}


}
