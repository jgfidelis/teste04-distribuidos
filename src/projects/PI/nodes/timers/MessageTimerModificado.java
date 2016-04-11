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
package projects.PI.nodes.timers;

import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;
import sinalgo.nodes.timers.Timer;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;
import java.text.DecimalFormat;
import java.util.Random;

import projects.PI.nodes.messages.INFMessage;
import projects.PI.nodes.nodeImplementations.PINode;

/**
 * A timer that sends a message at a given time.
 * The message may be unicast to a specific node or broadcast. 
 */
public class MessageTimerModificado extends Timer {
	
	private Node receiver; // the receiver of the message, null if the message should be broadcast
	private Message msg; // the message to be sent
	private double time;
	private double sendChance;
	DecimalFormat deci = new DecimalFormat("0.0000");

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
	
	public MessageTimerModificado(Message msg, double sendChance) {
		this.msg = msg;
		this.time = -1;
		this.receiver = null;
		this.sendChance = sendChance;
	}
	
	@Override
	public void fire() {
		int rand = new Random().nextInt(100);
		if(receiver != null) { // there's a receiver => unicast the message
			this.node.send(msg, receiver);
		} else { // there's no reciever => broadcast the message
			//this.node.broadcast(msg);
			//checar se é par
			double factor = ((double)((INFMessage)msg).getHops())*(0.5);
			double chance = sendChance+factor;
			if (rand <  chance|| this.node.ID == 1) {
				//System.out.println("Mandou msg com " + chance + " chance");
				((INFMessage)msg).setHops(((INFMessage)msg).getHops() + 1);
				//System.out.println(((INFMessage)msg).getHops());
				this.node.broadcast(msg);
				Tools.appendToOutput("\n\n TIME: "+ deci.format(Global.currentTime));
		  		Tools.appendToOutput("\n Node "+ this.node.ID +" transmitiu INF");
		  		PINode.sentINF = PINode.sentINF + 1; 
			}
		}
	}
}
