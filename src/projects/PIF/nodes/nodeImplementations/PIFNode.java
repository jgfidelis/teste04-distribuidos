package projects.PIF.nodes.nodeImplementations;
import java.awt.Color;
import java.awt.Graphics;

import projects.PIF.Utils;
import projects.PIF.nodes.messages.FEEDBACKMessage;
import projects.PIF.nodes.messages.INFMessage;
import projects.PIF.nodes.timers.PIF_FeedbackTimer;
import projects.PIF.nodes.timers.MessageTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;

public class PIFNode extends Node {
	//private boolean reached = false;
	private int nextHopToSource;
	public static int sentINF = 0;
	public static int sentFeedback = 0;
	public static int receivedFeedback = 0;
	public int vizinhos = 0;
	private boolean[] reached = new boolean[Utils.NUM_INSTANCES];
	public static double sendChance = 40.0;
	
	public enum TNO {TNO_FEEDBACK };
	private PIF_FeedbackTimer feedbackTimer;
	
	@Override
	public void handleMessages(Inbox inbox) {
		// TODO Auto-generated method stub
		int sender;
		
		while(inbox.hasNext()) {
			Message msg = inbox.next();
			sender = inbox.getSender().ID;
			
			//N� recebeu uma mensagem INF	
			if(msg instanceof INFMessage) {
				if (((INFMessage) msg).numero == 1) {
					vizinhos++; //vizinho achado			
				}
				System.out.println("Node: "+this.ID+" recebeu INF do Node  "+sender);
				INFMessage msgINF = (INFMessage) msg;
				
				if(!this.reached[((INFMessage) msg).numero - 1])//se reached[msg.numero - 1] for false, marcar como recebida
				{
					this.setColor(Color.GREEN);
					this.reached[((INFMessage) msg).numero -1 ] = true;	
					this.nextHopToSource = msgINF.getSenderID();
					msgINF.setSenderID(this.ID);
					MessageTimer infMSG = new MessageTimer(msgINF);
					infMSG.startRelative(0.1,this);
					
					//Agenda o FEEDBACK
					feedbackTimer = new PIF_FeedbackTimer(this, TNO.TNO_FEEDBACK);
					feedbackTimer.tnoStartRelative(10, this, TNO.TNO_FEEDBACK);	
				}
			}
			
			//Mensagem de Confirma��o
			if(msg instanceof FEEDBACKMessage) {
				FEEDBACKMessage msgFeedback = (FEEDBACKMessage) msg;
				if (this.ID != 1){
					if(msgFeedback.getDestinationID() == this.ID){
						msgFeedback.setDestinationID(this.nextHopToSource);
						MessageTimer feedbackMSG = new MessageTimer(msgFeedback);
						feedbackMSG.startRelative(0.1,this);
						System.out.println("Node: "+this.ID+" Recebeu Feedback do Node "+ msgFeedback.getSourceFeedbackID() + " encaminhada pelo Node " +msgFeedback.getSenderID());	
					}
				}else{ 
					System.out.println("Source node recebeu Feedback do Node "+ msgFeedback.getSourceFeedbackID());
					receivedFeedback = receivedFeedback + 1;
				}
			}
		}
	}
			
		
	public void feedbackStart(){
		MessageTimer feedbackMSG = new MessageTimer (new FEEDBACKMessage(this.ID, this.ID, this.nextHopToSource));
	  	feedbackMSG.startRelative(0.1, this);		
	}
	
	public void timeout(TNO tno){
		switch(tno){
			case TNO_FEEDBACK:
				feedbackStart();
				break;	
		}
	}

    @Override
	public void init() {
		//Considerando que o n� 1 tem a mensagem inf
		if (this.ID==1){
			this.setColor(Color.RED);
			this.nextHopToSource = this.ID;
			this.reached[0] = true;
			MessageTimer infMSG = new MessageTimer (new INFMessage(this.ID, 1));
	  		infMSG.startRelative(0.1, this);
		}
	}
    
	@Override
	public void postStep() {
		// TODO Auto-generated method stub
	}

	@Override
	public void preStep() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
		// TODO Auto-generated method stub
		//if (this.ID == 1) highlight = true;
		super.drawNodeAsDiskWithText(g, pt, highlight, Integer.toString(this.ID), 6, Color.WHITE);
		
		
	}
	
	@Override
	public void neighborhoodChange() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkRequirements() throws WrongConfigurationException {
		// TODO Auto-generated method stub
		
	}

}