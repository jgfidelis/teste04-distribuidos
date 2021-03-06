package projects.PIF.nodes.nodeImplementations;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TimerTask;
import java.util.Vector;

import projects.PIF.PrimElement;
import projects.PIF.Utils;
import projects.PIF.nodes.messages.FEEDBACKMessage;
import projects.PIF.nodes.messages.INFMessage;
import projects.PIF.nodes.timers.MessageTimerModificado;
import projects.PIF.nodes.timers.PIF_FeedbackTimer;
import projects.PIF.nodes.timers.PrimTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;

public class PIFNode extends Node {
	//private boolean g = false;
	private boolean amLeaf = true;
	private int nextHopToSource;
	public static int sentINF = 0;
	public static int sentFeedback = 0;
	public static int receivedFeedback = 0;
	public int vizinhos = 0;
	public static int[] cobertura = new int[Utils.NUM_INSTANCES];
	public static int nnodes = 0;
	public static List<List<Integer>> feedback = new ArrayList<List<Integer>>(Utils.NUM_INSTANCES);
	private boolean[] reached = new boolean[Utils.NUM_INSTANCES];
	public static double sendChance = 40.0;
	public static int msgUm = 0;
	public int fatherDist = 999999;
	public int father;
	public static Vector<Vector<Integer>> graph = new Vector<Vector<Integer>>();
	
	public static int[] parents;
	
	public enum TNO {
		FIRST_FEEDBACK,
		TNO_FEEDBACK 
	};
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
				INFMessage msgINF = (INFMessage) msg;
				
				if (msgINF.numero == 1) {
					vizinhos++; //vizinho achado
					graph.get(this.ID-1).add(((INFMessage) msg).getSenderID());
					
					if (msgINF.distToNodeOne < fatherDist) {
						fatherDist = msgINF.distToNodeOne;
						father = msgINF.getSenderID();
					}
				}
				
				System.out.println("Node: "+ this.ID +" recebeu INF numero " + String.valueOf(msgINF.numero) + " do Node "+sender);				
				
				if(!this.reached[((INFMessage) msg).numero - 1]) {
					this.setColor(Color.GREEN);
					this.reached[msgINF.numero -1 ] = true;
					cobertura[msgINF.numero -1]++;
					this.nextHopToSource = msgINF.getSenderID();
					msgINF.setSenderID(this.ID);
					MessageTimerModificado infMSG = new MessageTimerModificado(msgINF);
					infMSG.startRelative(0.1,this);
						
					//Agenda o FEEDBACK
					if(msgINF.numero == 1) {
						feedbackTimer = new PIF_FeedbackTimer(this, TNO.FIRST_FEEDBACK);
						feedbackTimer.tnoStartRelative(5, this, TNO.FIRST_FEEDBACK);
					} else {
						feedbackTimer = new PIF_FeedbackTimer(this, TNO.TNO_FEEDBACK);
						feedbackTimer.tnoStartRelative(5, this, TNO.TNO_FEEDBACK);
					}			
				}
			}
			
			//Mensagem de Confirma��o
			if(msg instanceof FEEDBACKMessage) {
				FEEDBACKMessage msgFeedback = (FEEDBACKMessage) msg;
				if(msgFeedback.getDestinationID() == this.ID) {
					if(this.ID == 1) {
						feedback.get(msgFeedback.numero).addAll(msgFeedback.sourceFeedbackID);
						System.out.println("Source node recebeu Feedback do Node "+ msgFeedback.getSourceFeedbackID());
					} else {
						System.out.println("Node " + this.ID + " recebeu feedback de " + msgFeedback.getSenderID() + ", redirecionando para " + this.father);
						this.amLeaf = false;
						msgFeedback.setDestinationID(this.father);
						msgFeedback.addSourceFeedbackID(this.ID);
						MessageTimerModificado feedbackMSG = new MessageTimerModificado(msgFeedback);
						feedbackMSG.startRelative(0.1,this);
					}
				}
			}
		}
	}
			
	public void firstFeedbackStart() {
		MessageTimerModificado feedbackMSG = new MessageTimerModificado (new FEEDBACKMessage(this.ID, this.ID, this.nextHopToSource, 1));
	  	feedbackMSG.startRelative(0.1, this);
	}
	
	public void feedbackStart(){
		if(amLeaf) {
			MessageTimerModificado feedbackMSG = new MessageTimerModificado (new FEEDBACKMessage(this.ID, this.ID, this.nextHopToSource, 1));
		  	feedbackMSG.startRelative(0.1, this);
		}
	}
	
	public void timeout(TNO tno){
		switch(tno){
			case TNO_FEEDBACK:
				feedbackStart();
				break;	
			case FIRST_FEEDBACK:
				firstFeedbackStart();
				break;
		}
	}

    @Override
	public void init() {
		//Considerando que o n� 1 tem a mensagem inf
    	nnodes++;
    	graph.add(new Vector<Integer>());
    	
		if (this.ID==1){
			this.setColor(Color.RED);
			this.nextHopToSource = this.ID;
			this.reached[0] = true;
			MessageTimerModificado infMSG = new MessageTimerModificado (new INFMessage(this.ID, 1, 0));
	  		infMSG.startRelative(0.1, this);
	  		//iniciando multiplas instancias
	  		for (int i = 1; i < Utils.NUM_INSTANCES; i++) {
	  			this.reached[i] = true;
	  			infMSG = new MessageTimerModificado (new INFMessage(this.ID, i+1, 0));
	  			infMSG.startRelative(i*Utils.INTERVAL_INTANCES, this);
	  		}
	  		
	  		PrimTimer primTimer = new PrimTimer();
	  		primTimer.startRelative(10, this);
		}
	}
    
    public static void prim() {
    	System.out.println("COMECANDO PRIM");
    	parents = new int[nnodes];
    	int[] dist = new int[nnodes];
    	boolean[] vis = new boolean[nnodes];
    	PriorityQueue<PrimElement> priorityQueue = new PriorityQueue<PrimElement>(nnodes, new Comparator<PrimElement>() {
            public int compare(PrimElement e1, PrimElement e2) {
                return Integer.valueOf(e1.key).compareTo(e2.key);
            }
        });
    	
    	for (int i =1; i < nnodes; i++) {
    		dist[i] = 99999;
    		parents[i] = -1;
    		vis[i] = false;
    		priorityQueue.add(new PrimElement(dist[i], i));
    	}
    	
    	dist[0] = 0;
    	vis[0] = false;
    	priorityQueue.add(new PrimElement(0, 0));
    	
    	
    	while (!priorityQueue.isEmpty()){
    		int u = priorityQueue.remove().v;
    		System.out.println("Checando node : " + u);
    		if (vis[u] == true) {
    			continue;
    		}
    		
    		vis[u] = true;
    		
    		for (int i = 0; i< graph.get(u).size(); i++) {
    			int v = graph.get(u).get(i);
    			System.out.println("V: " + v);
    			if (vis[v-1] == false) {//o v ta smpre um a mais!!!!
    				parents[v-1] = u;
    				priorityQueue.add(new PrimElement(1, v-1));
    			}
    		}
    	}
    	System.out.println("PRIM FINALIZADO");
    	for (int i = 0; i < nnodes; i++) {
    		System.out.println("Node " + i+1 + " tem pai " + parents[i]);
    	}
    	//No fim disso disparar feedback
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

	class ExecutePrim extends TimerTask {
	    public void run() {
	       PIFNode.prim();
	    }
	 }
	
}