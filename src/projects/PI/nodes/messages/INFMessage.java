package projects.PI.nodes.messages;

import sinalgo.nodes.messages.Message;

public class INFMessage extends Message {
	public int senderID;
	private int hops;
	
	public INFMessage(int senderID) {
		this.senderID = senderID;
	}

	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new INFMessage(this.senderID);
	}

	public int getHops() {
		return hops;
	}

	public void setHops(int hops) {
		this.hops = hops;
	}


	public int getSenderID() {
		return senderID;
	}

	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}
	
}
