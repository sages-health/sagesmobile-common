package edu.jhuapl.sages.mobile.lib.message;


/**
 * 
 * @author pokuam1
 * @date 05/06/13
 */
public class DataMessage extends SagesMessage {

	public DataMessage(MsgTypeEnum msgType, SagesMessageHeader header, SagesMessageBody body) {
		super(MsgTypeEnum.DATA, header, body);
	}

	public DataMessage(){
		super(MsgTypeEnum.DATA, new SagesMessageHeader(null), new SagesMessageBody(null));
	}
	
}
