package projects.PI.nodes.nodeImplementations;
import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import projects.PI.nodes.messages.INFMessage;
import projects.PI.nodes.timers.MessageTimer;
import projects.PI.nodes.timers.MessageTimerModificado;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;

public class PINode extends Node {
	private boolean[] reached = new boolean[INSTANCIAS];
	public static int numberNodes = 0;
	public static int sentINF = 0;
	public static double sendChance = 40.0;
	
	private static final int INSTANCIAS = 1000;
	private static final int INTERVALO_INSTANCIAS = 5;
	
		
	DecimalFormat deci = new DecimalFormat("0.0000");

	@Override
	public void handleMessages(Inbox inbox) {
		// TODO Auto-generated method stub
		int sender;
		
		while(inbox.hasNext()) {
			Message msg = inbox.next();
			sender = inbox.getSender().ID;
			
			//N� recebeu uma mensagem INF	
			if(msg instanceof INFMessage) {
				//Verifica se � a primeira vez que o n� recebe INF
				if(!this.reached[((INFMessage) msg).getSeq()]){
					numberNodes--;
					this.setColor(Color.GREEN);
					this.reached[((INFMessage) msg).getSeq()] = true;
					Tools.appendToOutput("\n\n TIME: "+ deci.format(Global.currentTime));
					Tools.appendToOutput("\n Node " + this.ID +" recebeu INF de"+ sender);
					MessageTimerModificado infMSG = new MessageTimerModificado(msg, sendChance);
					infMSG.startRelative(1,this);
						
				}
			}
			
		}
		
	}
		

    @Override
	public void init() {
    	numberNodes++;
		//Considerando que o n� 1 tem a mensagem inf
		if (this.ID==1){
			this.setColor(Color.RED);
			for (int i = 0; i < INSTANCIAS; i++) {
				this.reached[i] = true;
				MessageTimerModificado infMSG = new MessageTimerModificado (new INFMessage(this.ID, 0, i));
				infMSG.startRelative(0.1+INTERVALO_INSTANCIAS*i, this);
			}	  		
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
		if (this.ID == 1) highlight = true;
		super.drawNodeAsDiskWithText(g, pt, highlight, Integer.toString(this.ID), 8, Color.WHITE);
		
		
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