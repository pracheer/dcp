package branch.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class Snapshot {


	private HashMap<String, Double> accounts;

	// This will keep track of channels on which markers HAVE NOT been received (open Channels).
	private HashMap<String, ArrayList<Message>> oChannels;

	// This will keep track of channels on which markers HAVE been received (closed Channels).
	private HashMap<String, ArrayList<Message>> cChannels;

	public Snapshot(HashMap<String,Double> accounts, Vector<String> inNeighbors) {
		this.accounts = accounts;
		this.oChannels = new HashMap<String, ArrayList<Message>>(inNeighbors.size());
		this.cChannels = new HashMap<String, ArrayList<Message>>(inNeighbors.size());

		for (String inNeighbor : inNeighbors) {
			oChannels.put(inNeighbor, new ArrayList<Message>());
			cChannels.put(inNeighbor, new ArrayList<Message>());
		}
	}

	/**
	 * Will return false to indicate an error
	 * if channel has already been closed.
	 * 
	 * @param srcNode
	 * @param msg
	 * @return
	 */
	public boolean addMsgChannel(String srcNode, Message msg) {
		if(oChannels.containsKey(srcNode)) {
			ArrayList<Message> msgs = oChannels.get(srcNode);
			msgs.add(msg);
			return true;
		}
		else {
			System.err.println(srcNode + " channel has already been closed.");
		}
		return false;
	}

	/**
	 * returns 0 if snapshot has been completed.
	 * else returns 1 if there are some channels 
	 * where marker is yet to be received
	 * -1 is returned to indicate errors.
	 * 
	 * @param srcNode
	 * @return
	 */
	public int closeChannel(String srcNode) {
		if(oChannels.containsKey(srcNode)) {
			if(!cChannels.containsKey(srcNode)) {
				cChannels.put(srcNode, oChannels.get(srcNode));
				oChannels.remove(srcNode);
				if(oChannels.isEmpty())
					return 0;
				else
					return 1;
			}
			else {
				System.err.println(srcNode + " has already closed the channels");
			}
		}
		else {
			System.err.println(srcNode + " does not have an open channel");
		}

		return -1;
	}

	public boolean isChannelOpen(String srcNode) {
		
		if(oChannels.containsKey(srcNode) && !cChannels.containsKey(srcNode)) {
			return true;
		}
		
		return false;
	}
	
}
