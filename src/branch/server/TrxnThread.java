package branch.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import branch.server.Trxn.TransxType;

public class TrxnThread extends Thread {

	MsgQueue messages_;
	BranchServerProp properties_;

	HashMap<Integer, Snapshot> snapshots;
	private Topology topology_;

	public TrxnThread(BranchServerProp properties, MsgQueue messages) {
		messages_ = messages;
		properties_ = properties;
		topology_ = properties_.getTopology();
	}

	public void run() {

		while (true) {
			
			// will block if the queue is empty.
			Message msg = messages_.getMsg();

			ServerNetworkWrapper nm = null;
			if (msg.getSrcNode().isGui()) {
				nm = new ServerNetworkWrapper(properties_,
						msg.getClientSocket(), msg.getSrcNode());
			} else {
				try {
					msg.getClientSocket().close();
				} catch (IOException e) {
					System.err.println("Error closing client socket.");
					e.printStackTrace();
				}
				nm = new ServerNetworkWrapper(properties_);
			}

			// TAKESNAPSHOT is the marker
			if(msg.getTrxn().getType() == TransxType.TAKESNAPSHOT) {
				
				// check if the msg's snapshot has already been started or not.
				int snapshotId = /*TODO*/(new Random()).nextInt();
				if (snapshots.containsKey(snapshotId)) {
					Snapshot snapshot = snapshots.get(snapshotId);
					int val = snapshot.closeChannel(msg.getSrcNode().toString());
					if(val == 0) {
						// TODO snapshot over. do something
					}
				}
				else {

					Vector<String> inNeighbors = topology_.getInNeighbors();

					Snapshot snapshot = new Snapshot(AccDetailsWrapper.getAllAccnts(), inNeighbors);
					snapshots.put(snapshotId, snapshot);

					// send out a msg on all outgoing channels.
					Vector<String> outNeighbors = topology_.getOutNeighbors();
					// TODO send to neighbors.
				}
			}
			else { // Message is a normal Message - Request.
				
				TrxnManager tm = new TrxnManager(
						msg.getTrxn(), nm, properties_);
				tm.processTransaction();
				
				
				if(!snapshots.isEmpty()) {
					for (int i = 0; i < snapshots.size(); i++) {
						Snapshot snapshot = snapshots.get(i);
						
						if(snapshot.isChannelOpen(msg.getSrcNode().toString()))
							snapshot.addMsgChannel(msg.getSrcNode().toString(), msg);
					}
				}
			}
		}

	}

}
