package projects.PI.nodes.messages;

import sinalgo.nodes.messages.Message;

public class INFMessage extends Message {
	public int senderID;

	
	public INFMessage(int senderID) {
		this.senderID = senderID;
	}


	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new INFMessage(this.senderID);
	}

	

	public int getSenderID() {
		return senderID;
	}

	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}
	
}
