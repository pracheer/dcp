package test;
/**
 * 
 * @author qsh2
 */

import branch.server.BranchServerProp;
import branch.server.FlagParser;
import branch.server.FlagParser.FlagParseException;
import junit.framework.TestCase;

public class BranchServerPropertiesTest extends TestCase {
	private BranchServerProp properties_;
	
	public void setUp() {
		properties_ = new BranchServerProp();
	}
	
	public void testValidBranchServerProperties() {
		String[] args = new String[6];
		
		args[0] = "-port";
		args[1] = "20";
		args[2] = "-bid";
		args[3] = "2000";
		args[4] = "-topology";
		args[5] = "c:\topology.txt";
		
		try {
			properties_.parseCommandLine(args);
		} catch (FlagParser.FlagParseException e) {
			System.err.println(e.getMessage());
			fail("Flag parser exception for valid command line.");
		}
		
		assertEquals(20, properties_.getPort());
		assertEquals(2000, properties_.getBranchId());
		assertEquals("c:\topology.txt", properties_.getTopologyFileLocation());
	}
	
	private void testInvalidArgument(String[] args, String expectedError) {
		try {
			properties_.parseCommandLine(args);
			fail("Invalid flag should raise exception.");
		} catch (FlagParser.FlagParseException e) {
			assertEquals(expectedError, e.getMessage());
		}
	}
	
	public void testInValidIntegerProperty() {
		String[] args = new String[2];		
		args[0] = "-port";
		args[1] = "not-an-int";
		
		testInvalidArgument(
				args,
				"Could not parse integer. For input string: \"not-an-int\"");
	}
	
	public void testInValidName() {
		String[] args = new String[2];
		args[0] = "-unknown";
		args[1] = "20";
		
		testInvalidArgument(args, "Unknown flag: unknown");
	}
	
	public void testInvalidFormat() {
		String[] args = new String[1];		
		args[0] = "-incomplete";
		
		testInvalidArgument(args, "Value not present for argument: incomplete");
	}
}