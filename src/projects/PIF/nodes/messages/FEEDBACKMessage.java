package projects.PIF.nodes.messages;

import sinalgo.nodes.messages.Message;

public class FEEDBACKMessage extends Message {
	public int destinationID;
	public int senderID;
	public int sourceFeedbackID;

	public FEEDBACKMessage(int sourceFeedbackID, int senderID, int destinationID) {
		this.destinationID = destinationID;
		this.senderID = senderID;
		this.sourceFeedbackID = sourceFeedbackID;
	}

	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new FEEDBACKMessage(this.sourceFeedbackID, this.senderID, this.destinationID);
	}

	public int getSourceFeedbackID() {
		return sourceFeedbackID;
	}

	public void setSourceFeedbackID(int sourceFeedbackID) {
		this.sourceFeedbackID = sourceFeedbackID;
	}

	public int getDestinationID() {
		return destinationID;
	}

	public int getSenderID() {
		return senderID;
	}

	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}

	public void setDestinationID(int destinationID) {
		this.destinationID = destinationID;
	}
	
}
