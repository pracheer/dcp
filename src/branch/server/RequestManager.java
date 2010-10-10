package branch.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * 
 * @author qsh2
 *
 * RequestManager is the main thread that handles request from server side.
 */

public class RequestManager extends Thread {
	private Socket clientSocket_;
	final private BranchServerProp properties_;
	
	public RequestManager(
			BranchServerProp properties,
			Socket socket) {
		clientSocket_ = socket;
		properties_ = properties;
	}
	
	public void run() {
		try {
			BufferedReader in = new BufferedReader(
					new InputStreamReader(clientSocket_.getInputStream()));
			String msg = in.readLine();

			Message requestMessage = Message.parseString(msg, clientSocket_);
			
			// Branch server does not expect response type messages.
			if (requestMessage.getType() == Message.MsgType.RESP) {
				System.err.println("Received response: " + msg);
				return;
			}
			
			ServerNetworkWrapper nm = null;
			if (requestMessage.getSrcNode().isGui()) {
				nm = new ServerNetworkWrapper(properties_, clientSocket_,
						requestMessage.getSrcNode());  
			} else {
				clientSocket_.close();
				nm = new ServerNetworkWrapper(properties_);
			} 
			
			// RequestMessage type is REQ
			TrxnManager tm =
				new TrxnManager(
						requestMessage.getTrxn(), nm, properties_);
			
			tm.processTransaction();
		} catch (IOException e) {
			System.err.println("Could not read from client socket.");
		}
	}
}
