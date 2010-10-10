package branch.server;
import java.util.Vector;

/**
 * 
 * @author qsh2
 *
 * This class uses the FlagParser to parse the flags.
 * From the FlagParser class we receive a vector of Arguments which is
 * (argument-name, argument-value) pairs.
 * From that vector, this class populates the related properties fields.
 * 
 */
public class BranchServerProp {
	private int port_;
	private int branchId_;
	private String topologyFile_;
	private String serverLocationFile_;
	
	private Topology topology_;
	private ServerLocations serverLocations_;
	private Node node_;

	public BranchServerProp() {
		port_ = -1;
		branchId_ = -1;
		topologyFile_ = "";
		serverLocationFile_ = "";
		
		topology_ = null;
		node_ = null;
	}
	
	public void parseCommandLine(String[] args) throws	FlagParser.FlagParseException {
		FlagParser parser = new FlagParser();
		Vector<FlagParser.Argument> parsedArguments = parser.parseFlags(args);
		
		for (int i = 0; i < parsedArguments.size(); ++i) {
			FlagParser.Argument argument = parsedArguments.elementAt(i);
			
			try {
				if (argument.getName().equals("bid")) {
					branchId_ = Integer.parseInt(argument.getValue());
				} else if (argument.getName().equals("topology")) {
					topologyFile_ = argument.getValue();
				} else if (argument.getName().equals("servers")) {
					serverLocationFile_ = argument.getValue();
				} else {
					throw new FlagParser.FlagParseException(
							"Unknown flag: " + argument.getName());
				}
			} catch(NumberFormatException ne) {
				throw new FlagParser.FlagParseException(
						"Could not parse integer. " + ne.getMessage());
			} 
		}
	}
	
	public void setPort(int port) {
		port_ = port;
	}
	
	public int getPort() {
		return port_;
	}
	
	public int getBranchId() {
		return branchId_;
	}
	
	public String getTopologyFileLocation() {
		return topologyFile_;
	}
	
	public void setTopology(Topology topology) {
		topology_ = topology;
	}
	
	public Topology getTopology() {
		return topology_;
	}
	
	public String getServerLocationFile() {
		return serverLocationFile_;
	}
	
	public void setServerLocations(ServerLocations sl) {
		serverLocations_ = sl;
	}
	
	public ServerLocations getServerLocations() {
		return serverLocations_;
	}
	
	public void setNode(Node node) {
		node_ = node;
	}
	
	public Node getNode() {
		return node_;
	}

	public String print() {
		return "Server " + branchId_ + " starting at port:" + port_ + " and using the topology file:" + topologyFile_;
	}
}