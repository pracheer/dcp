package test;
/**
 * 
 * @author qsh2
 */

import java.util.Vector;

import branch.server.FlagParser;
import branch.server.FlagParser.Argument;
import branch.server.FlagParser.FlagParseException;

import junit.framework.TestCase;

public class FlagParserTest extends TestCase {
	protected FlagParser parser_;
	protected void setUp() {
		parser_ = new FlagParser();
	}
	
	public void testArgumentDoesNotStartWithDash() {
		String[] args = new String[2];
		args[0] = "port";
		args[1] = "20";
		
		try {
			parser_.parseFlags(args);
			fail("Should have caught FlagParserException.");
		} catch (FlagParser.FlagParseException e) {
			assertEquals("port is expected to start with '-'", e.getMessage());
		}
	}
	
	public void testValueNotPresent() {
		String[] args = new String[3];
		args[0] = "-port";
		args[1] = "20";
		args[2] = "-bid";
		
		try {
			parser_.parseFlags(args);
			fail("Should have caught FlagParserException.");
		} catch (FlagParser.FlagParseException e) {
			assertEquals("Value not present for argument: bid", e.getMessage());
		}
	}
	
	public void testValidArguments() {
		String[] args = new String[6];
		args[0] = "-port";
		args[1] = "2000";
		args[2] = "-bid";
		args[3] = "20";
		args[4] = "-topology";
		args[5] = "c:\toplogyfile.txt";
		
		try {
			Vector<FlagParser.Argument> parsedArguments = parser_.parseFlags(args);
			
			assertEquals(3, parsedArguments.size());
			assertEquals("port", parsedArguments.get(0).getName());
			assertEquals("2000", parsedArguments.get(0).getValue());
			assertEquals("bid", parsedArguments.get(1).getName());
			assertEquals("20", parsedArguments.get(1).getValue());
			assertEquals("topology", parsedArguments.get(2).getName());
			assertEquals("c:\toplogyfile.txt", parsedArguments.get(2).getValue());
		} catch (FlagParser.FlagParseException e) {
			fail("FlagParseException occurred in a valid test case");
		}
	}
}