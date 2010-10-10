package branch.server;
import java.io.IOException;

/**
 *
 * @author qsh2
 *
 * NetworkManager that encapsulates network behaviors for the branch server.
 */
public class GuiNetworkWrapper extends NetworkWrapper {
	Node currNode_;
	Topology topology_;
	
	public GuiNetworkWrapper(Node node, Topology topology, ServerLocations serverLocations) {
		super(serverLocations);
		currNode_ = node;
		topology_ = topology;
	}
	
	public String sendGuiRequest(String msg) {
		Node destNode = new Node(false, currNode_.getBranchId());
		
		// Branch server is not reachable.
		if (!topology_.isReachable(destNode.toString())) {
			System.err.println("Topology does not allow GUI to connect to server.");
			return null;
		}
		
		super.socket_ = super.getSocketForNode(destNode);
		
		if (!super.send(msg)) return null;
		
		String response = null;
		response = super.receive();
		
		try {
			socket_.close();
		} catch (IOException e) {
			System.err.println("Could not close connection with the server");
			System.err.println(e.getMessage());
		}
		
		return response;
	}
}
