package branch.server;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * 
 * @author qsh2
 *
 * NetworkManager that encapsulates network behaviors for the branch server.
 */
public class ServerNetworkWrapper extends NetworkWrapper {
	private Socket clientSocket_;
	private Node clientNode_;
	private BranchServerProp properties_;
	
	public ServerNetworkWrapper(BranchServerProp properties) {
		super(properties.getServerLocations());
		properties_ = properties;
		clientSocket_ = null;
	}
	
	public ServerNetworkWrapper(
			BranchServerProp properties,
			Socket clientSocket,
			Node clientNode) {
		super(properties.getServerLocations());
		properties_ = properties;
		clientSocket_ = clientSocket;
		clientNode_ = clientNode;
	}
	
	public void sendResponse(String msg) {
		// We cannot send a response back to the client
		// if we do not have a valid client socket.
		if (clientSocket_ == null) {
			return;
		}
		
		try {
			PrintWriter out = new PrintWriter(
					clientSocket_.getOutputStream(), true);
			out.println(msg);
		} catch (IOException e) {
			System.err.println(
					"Could not send response to: " + clientNode_.toString());
			System.err.println(e.getMessage());
		}
	}
	
	public boolean sendRequest(Node serverNode, String msg) {
		final Topology topology = properties_.getTopology();
		
		// Checking whether the topology allows us to 
		if (!topology.isReachable(
				serverNode.toString())) {
			return false;
		}
		
		super.socket_ = super.getSocketForNode(serverNode);
		return super.send(msg);
	}
	
	public void close() {
		try {
			if (socket_ != null && !socket_.isClosed()) {
				socket_.close();
			}
			if (clientSocket_ != null && !clientSocket_.isClosed()) {
				clientSocket_.close();
			}
		} catch (IOException e) {
			System.err.println(
					"Cannot close socket connection: " + socket_.toString());
			System.err.println(e.getMessage());
		}
	}
}
