package branch.server;
/**
 * 
 * @author qsh2
 * 
 * Node structure of a branch server or a branch GUI.
 *
 */
public class Node {
	private boolean gui_;
	private int branchId_;
	
	public Node() {
	}
	
	public Node(String str) {
		parseString(str);
	}

	public Node(boolean gui, int bid) {
		gui_ = gui;
		branchId_ = bid;
	}
	
	private boolean setBranchId(String str) {
		try {
			branchId_ = Integer.parseInt(str);	
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	public boolean parseString(String str) {
		if (str.length() != 3) {
			return false;
		}
		
		if (str.startsWith("G")) {
			gui_ = true;
			if (setBranchId(str.substring(1))) {
				return true;
			}
		} else if (str.startsWith("S")) {
			gui_ = false;
			if (setBranchId(str.substring(1))) {
				return true;
			}
		}

		return false;
	}
		
	public String toString() {
		String str = "";
		if (gui_) {
			str += "G";
		} else {
			str += "S";
		}
		str += String.format("%02d", branchId_);
		
		return str;
	}
	
	public boolean isGui() {
		return gui_;
	}
	
	public int getBranchId() {
		return branchId_;
	}
	
	public boolean equals(Node node) {
		return (gui_ == node.gui_ && branchId_ == node.branchId_);
	}
	
}
