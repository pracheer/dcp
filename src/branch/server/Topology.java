package branch.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

/**
 * 
 * @author qsh2
 * 
 */

public class Topology {
	public static class Connection {
		Node src_;
		Node dest_;

		public Connection() {
			src_ = new Node();
			dest_ = new Node();
		}

		// Parses string of the form, JVM1 JVM2
		// and updates the connection variables.
		public boolean parseString(String str) {
			int spaceAt = str.indexOf(' ');

			src_ = new Node();
			String sourceString = new String(str.substring(0, spaceAt));
			if (!src_.parseString(sourceString)) {
				return false;
			}

			dest_ = new Node();
			String destinationString = new String(str.substring(spaceAt + 1));
			if (!dest_.parseString(destinationString)) {
				return false;
			}

			return true;
		}

		public String getSourceString() {
			return src_.toString();
		}

		public String getDestinationString() {
			return dest_.toString();
		}
	}

	final Vector<Connection> connections_;
	private Vector<String> inNeighbors;
	private Vector<String> outNeighbors;

	public Topology(String topologyFileLocation, Node node) throws IOException {
		FileReader fr = null;
		try {
			fr = new FileReader(new File(topologyFileLocation));
		} catch (FileNotFoundException fe) {
			throw new IOException(fe.getMessage());
		}

		connections_ = new Vector<Connection>();

		BufferedReader in = new BufferedReader(fr);
		while (true) {
			String str = in.readLine();
			if (str == null)
				break;

			Connection c = new Connection();
			if (c.parseString(str)) {
				connections_.add(c);
			} else {
				System.err.println("Error in connection line: " + str);
			}
		}
		
		in.close();
		fr.close();

		inNeighbors = whoInNeighbors(node.toString());
		outNeighbors = whoOutNeighbors(node.toString());

	}

	public boolean isReachable(String dest) {
		for (int i = 0; i < outNeighbors.size(); ++i) {
			if (outNeighbors.get(i).equalsIgnoreCase(dest)) {
				return true;
			}
		}

		return false;
	}

	private Vector<String> whoOutNeighbors(String src) {
		Vector<String> outNeighbors = new Vector<String>();

		for (int i = 0; i < connections_.size(); ++i) {
			final Connection curr = connections_.elementAt(i);
			if (curr.getSourceString().equals(src)) {
				outNeighbors.add(curr.getDestinationString());
			}
		}

		return outNeighbors;
	}

	private Vector<String> whoInNeighbors(String src) {
		Vector<String> inNeighbors = new Vector<String>();

		for (int i = 0; i < connections_.size(); ++i) {
			final Connection curr = connections_.elementAt(i);
			if (curr.getDestinationString().equals(src)) {
				inNeighbors.add(curr.getSourceString());
			}
		}

		return inNeighbors;
	}

	public Vector<String> getInNeighbors() {
		return inNeighbors;
	}

	public Vector<String> getOutNeighbors() {
		return outNeighbors;
	}

	
}
