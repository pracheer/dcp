package branch.server;
/**
 * 
 * @author qsh2
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract class NetworkWrapper {
	protected Socket socket_;
	protected ServerLocations serverLocations_;
	
	private final int maxRetry_;
	private final int[] retryGaps_;
	
	public NetworkWrapper(ServerLocations serverLocations) {
		serverLocations_ = serverLocations;
		maxRetry_ = 5;
		retryGaps_ = new int[maxRetry_];
		int waitTime = 2;
		for (int i = 1; i < maxRetry_; ++i) {
			retryGaps_[i] = waitTime * 1000;
			waitTime *= 2;
		}
	}
	
	// Sends 'msg' to socket_
	// Returns 'true' on successful send.
	// The implementation of the abstract class should make sure
	// socket_ is set before calling send().
	protected boolean send(String msg) {
		if (socket_ == null) {
			return false;
		}
		for (int i = 0; i < maxRetry_; ++i) {
			try {
				PrintWriter out = new PrintWriter(socket_.getOutputStream(), true);
				out.println(msg);
				return true;
			} catch (IOException e) {
				try {
					Thread.sleep(retryGaps_[i]);
				} catch (InterruptedException ie) {
					System.err.println(ie.getMessage());
				}
			}
		}
		
		return false;
	}
	
	// Returns message received from socket.
	// Returns 'null' if failed.
	// The implementation of the abstract class should make sure
	// socket_ is set before calling receive().
	protected String receive() {
		if (socket_ == null) {
			return null;
		}
		
		String msg = null;
		for (int i = 0; i < maxRetry_; ++i) {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(socket_.getInputStream()));
				msg = in.readLine();
			} catch (IOException e) {
				msg = null;
				try {
					Thread.sleep(retryGaps_[i]);
				} catch (InterruptedException ie) {
					System.err.println(ie.getMessage());
				}
				continue;
			}
			break;
		}
		
		return msg;
	}

	protected Socket getSocketForNode(Node node) {
		// This method does not create socket for GUI nodes.
		if (node.isGui() == true) {
			return null;
		}
			
		Socket s = null;
		
		try {
			ServerLocations.Location serverLocation =
				serverLocations_.getLocationForNode(node);
			
			if (serverLocation == null) {
				return null;
			}

			s = new Socket(serverLocation.getIp(), serverLocation.getPort());
		} catch (UnknownHostException e) {
			System.err.println("Could not create socket for " + node.toString());
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println("Could not create socket for " + node.toString());
			System.err.println(e.getMessage());
		}
		
		return s;
	}
}
