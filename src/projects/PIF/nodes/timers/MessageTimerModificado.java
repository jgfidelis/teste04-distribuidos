/*
 Copyright (c) 2007, Distributed Computing Group (DCG)
                    ETH Zurich
                    Switzerland
                    dcg.ethz.ch

 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 - Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.

 - Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the
   distribution.

 - Neither the name 'Sinalgo' nor the names of its contributors may be
   used to endorse or promote products derived from this software
   without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package projects.PIF.nodes.timers;

import java.awt.Color;
import java.util.Random;

import projects.PIF.nodes.messages.FEEDBACKMessage;
import projects.PIF.nodes.messages.INFMessage;
import projects.PIF.nodes.nodeImplementations.PIFNode;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;
import sinalgo.nodes.timers.Timer;

/**
 * A timer that sends a message at a given time.
 * The message may be unicast to a specific node or broadcast. 
 */
public class MessageTimerModificado extends Timer {
	
	private Node receiver; // the receiver of the message, null if the message should be broadcast
	private Message msg; // the message to be sent
	private double time;
	private PIFNode sender;
	
	/**
	 * Creates a new MessageTimer object that unicasts a message to a given receiver when the timer fires.
	 * 
	 * Nothing happens when the message cannot be sent at the time when the timer fires.
	 *
	 * @param msg The message to be sent when this timer fires.
	 * @param receiver The receiver of the message.
	 */
	public MessageTimerModificado(Message msg, Node receiver) {
		this.msg = msg;
		this.receiver = receiver;
		this.time = -1; // indicates aperiodicity 
	}
	
	/**
	 * Creates a MessageTimer object that broadcasts a message when the timer fires.
	 *
	 * @param msg The message to be sent when this timer fires.
	 */
	public MessageTimerModificado(Message msg) {
		this.msg = msg;
		this.time = -1; // indicates aperiodicity 
		this.receiver = null; // indicates broadcasting
	}
	/**
	 * Creates a MessageTimer object that periodically broadcasts a message when the timer fires.
	 *
	 * @param msg The message to be sent when this timer fires.
	 * @param time The data rate 
	 */
	public MessageTimerModificado(Message msg, double time) {
		this.msg = msg;
		this.time = time;
		this.receiver = null; // indicates broadcasting
	}
	@Override
	public void fire() {
		if(receiver != null) { // there's a receiver => unicast the message
			this.node.send(msg, receiver);
		} else { // there's no reciever => broadcast the message
			if(msg instanceof INFMessage){
				boolean sent = false;
				
				if (this.node.ID == 1) {
					this.node.broadcast(msg);
					sent = true;
				}
				
				if (((INFMessage) msg).numero == 1) {
					this.node.broadcast(msg); //se for a INF 1, fazer broadcast
					sent = true;
				} else {
					int rand = new Random().nextInt(100);
					PIFNode sender = (PIFNode) this.node;
					if (sender.vizinhos == 2) {
						this.node.broadcast(msg); //se só tem dois vizinhos, enviar
						sent = true;
					}
					else  {
						double chance = 70 - sender.vizinhos*1;
						System.out.println("VIZINHOS " + sender.vizinhos);
						if (rand < chance) {
							System.out.println("Enviado com probabilidade " + chance +" e vizinhos " + sender.vizinhos);
							this.node.broadcast(msg);
							sent = true;
						}
					}
					
					
				}
				//System.out.println("Enviadas: "+ PIFNode.SentINF);
				System.out.println("Node: "+ this.node.ID +" broadcast INF numero " + ((INFMessage) msg).numero);
				PIFNode.sentINF = PIFNode.sentINF + 1;
			}
			
			
			if(msg instanceof FEEDBACKMessage){
				this.node.broadcast(msg);
				PIFNode.sentFeedback = PIFNode.sentFeedback + 1;
				//System.out.println("Feedback Enviadas: "+ PIFNode.SentFeedback);
				if ( this.node.ID == ((FEEDBACKMessage) msg).getSourceFeedbackID() ){
					//System.out.println("Node: "+ this.node.ID +" broadcast FEEDBACK ");
					this.node.setColor(Color.MAGENTA);
				}
				else{
					//System.out.println("Node: "+ this.node.ID +" encaminhou FEEDBACK do Node "+((FEEDBACKMessage) msg).getSourceFeedbackID()+" para o Node "+ ((FEEDBACKMessage) msg).getDestinationID());
				}
			}
				
		}
	}
}
