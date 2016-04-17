package projects.PIF.nodes.messages;

import sinalgo.nodes.messages.Message;

public class INFMessage extends Message {
	public int senderID;
	public int numero;
	public int distToNodeOne;
	
	public INFMessage(int senderID, int numero, int distToNodeOne) {
		this.senderID = senderID;
		this.numero = numero;
		this.distToNodeOne = distToNodeOne;
	}

	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new INFMessage(this.senderID, this.numero, this.distToNodeOne);
	}

	public int getSenderID() {
		return senderID;
	}

	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}
	
}
