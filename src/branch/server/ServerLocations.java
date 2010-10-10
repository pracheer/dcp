package branch.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * 
 * @author qsh2
 *
 */

public class ServerLocations {
	public static class Location {
		private String ip_;
		private int port_;
		
		public Location(String ip, String port) {
			ip_ = ip;
			port_ = Integer.parseInt(port);
		}
		
		public String getIp() {
			return ip_;
		}
		
		public int getPort() {
			return port_;
		}
	}
	
	
	HashMap<String, Location> locationMap_;
	
	public ServerLocations(String locationFile) throws IOException {
		FileReader fr = null;
		try {
			fr = new FileReader(new File(locationFile));
		} catch (FileNotFoundException fe) {
			throw new IOException(fe.getMessage());
		}
		
		locationMap_ = new HashMap<String, Location>();
		
		BufferedReader in = new BufferedReader(fr);
		while(true) {
			String str = in.readLine();
			if (str == null) break;
			
			String[] tokens = str.split(" ");
			
			if (tokens.length != 3) {
				System.err.println("Invalid line in location: " + str);
			}
			
			locationMap_.put(tokens[0], new Location(tokens[1], tokens[2]));
		}
		
		in.close();
		fr.close();
	}
	
	public Location getLocationForNode(Node node) {
		return locationMap_.get(node.toString());
	}
}
