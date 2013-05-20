package edu.jhuapl.sages.mobile.lib.message;


/**
 * 
 * @author pokuam1
 * @date 05/06/13
 * 
 */
public class StatusMessage extends SagesMessage {

	public StatusMessage(MsgTypeEnum msgType, SagesMessageHeader header, SagesMessageBody body) {
		super(MsgTypeEnum.STATUS, header, body);
	}

	public StatusMessage(){
		super(MsgTypeEnum.STATUS, new SagesMessageHeader(null), new SagesMessageBody(null));
	}
}
