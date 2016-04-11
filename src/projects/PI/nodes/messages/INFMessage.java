package projects.PI.nodes.messages;

import sinalgo.nodes.messages.Message;

public class INFMessage extends Message {
	public int senderID;
	private int hops;
	private int seq;
	
	public INFMessage(int senderID, int hops, int seq) {
		this.senderID = senderID;
		this.hops = hops;
		this.seq = seq;
	}

	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new INFMessage(this.senderID, this.hops, this.seq);
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
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
