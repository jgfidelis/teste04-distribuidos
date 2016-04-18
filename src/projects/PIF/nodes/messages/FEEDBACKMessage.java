package projects.PIF.nodes.messages;

import java.util.ArrayList;
import java.util.List;

import sinalgo.nodes.messages.Message;

public class FEEDBACKMessage extends Message {
	public int destinationID;
	public int senderID;
	public List<Integer> sourceFeedbackID = new ArrayList<Integer>();
	public int numero;

	public FEEDBACKMessage(int sourceFeedbackID, int senderID, int destinationID, int numero) {
		this.destinationID = destinationID;
		this.senderID = senderID;
		this.sourceFeedbackID.add(sourceFeedbackID);
		this.numero = numero;
	}
	
	public FEEDBACKMessage(List<Integer> sourceFeedbackID, int senderID, int destinationID, int numero) {
		this.destinationID = destinationID;
		this.senderID = senderID;
		this.sourceFeedbackID  = sourceFeedbackID;
		this.numero = numero;
	}

	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new FEEDBACKMessage(this.sourceFeedbackID, this.senderID, this.destinationID, this.numero);
	}
	
	public void addSourceFeedbackID(Integer sourceFeedbackID) {
		this.sourceFeedbackID.add(sourceFeedbackID);
	}

	public List<Integer> getSourceFeedbackID() {
		return sourceFeedbackID;
	}

	public void setSourceFeedbackID(List<Integer> sourceFeedbackID) {
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
