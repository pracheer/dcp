package branch.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

/**
 * 
 * @author qsh2
 *
 * BranchServer for the distributed banking system.
 * This is the main class in the branch server environment.
 * It creates a ServerSocket that accepts connection request
 * from any client.
 * When a connection is accepted, it creates a thread to handle
 * that client and then again starts listening to the given port.
 * 
 *  Usage:
 *  To run this program you have to give
 *  -bid $branch-id -topology $topology-file-location -servers $server-file-location
 */


public class BranchServer {

	/**
	 * args are in order of 
	 * @param args
	 */
	public static void main(String[] args) {
		BranchServerProp properties = new BranchServerProp();
		ServerSocket serverSocket = null;
		Topology topology = null;
		ServerLocations serverLocations = null;
		Node node = null;
		MsgQueue messages = new MsgQueue();

		// Parse the flags to get the arguments.
		try {
			properties.parseCommandLine(args);
		} catch (FlagParser.FlagParseException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

		// Current node.
		if (properties.getBranchId() == -1) {
			System.err.println("Branch Id (bid) parameter not provided.");
			System.exit(1);
		}
		node = new Node("S" + String.format("%02d", properties.getBranchId()));
		properties.setNode(node);

		// Topology file.
		if (properties.getTopologyFileLocation().equals("")) {
			System.err.println("No topology file.");
			System.exit(1);
		}
		try {
			topology = new Topology(properties.getTopologyFileLocation(), node);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		properties.setTopology(topology);

		// Server-Location file.
		if (properties.getServerLocationFile().equals("")) {
			System.err.println("No server-location file.");
			System.exit(1);
		}
		try {
			serverLocations = new ServerLocations(properties.getServerLocationFile());
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		properties.setServerLocations(serverLocations);

		ServerLocations.Location serverLocation =
			serverLocations.getLocationForNode(node);

		if (!serverLocation.getIp().equals("localhost") &&
				!serverLocation.getIp().equals("127.0.0.1")) {
			InetAddress inet = null;
			try {
				inet = InetAddress.getLocalHost();
			} catch (UnknownHostException e1) {
				System.err.println("Could not get inet-address for machine.");
				System.exit(1);
			}

			if (!inet.getHostAddress().equals(serverLocation.getIp())) {
				System.err.println("Server is supposed to start from: " + serverLocation.getIp());
				System.exit(1);
			}
		}		
		properties.setPort(serverLocation.getPort());

		SerialNumberGenerator.setBranchId(properties.getBranchId());

		// Check that a valid port was provided.
		if (properties.getPort() < 0) {
			System.err.println("port not assigned.");
			System.exit(1);
		}

		// Create a ServerSocket for the given port.
		try {
			serverSocket = new ServerSocket(properties.getPort());
		} catch (IOException e){
			System.err.println(
					"Coult not listen to port: " + properties.getPort());
			System.exit(1);
		}

		System.out.println(properties.print());

		// Initiate the TransactionThread which will wait till a message 
		// in the messageQueue appears.
		TrxnThread tThread = new TrxnThread(properties, messages);
		tThread.start();

		// Server starts listening.
		while (true) {
			try {
				Socket clientSocket = serverSocket.accept();

				BufferedReader in = new BufferedReader(
						new InputStreamReader(clientSocket.getInputStream()));
				String msg = in.readLine();

				Message requestMessage = Message.parseString(msg, clientSocket);

				// Branch server does not expect response type messages.
				if (requestMessage.getType() == Message.MsgType.RESP) {
					System.err.println("Received response: " + msg);
					return;
				}

				messages.addMsg(requestMessage);

				//				RequestManager rm = new RequestManager(
				//						properties, clientSocket);
				//				rm.start();
			} catch (IOException e) {
				System.err.println("Coult not accept connection.");
				System.exit(1);
			}
		}
	}
}
