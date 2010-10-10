package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import branch.server.Node;
import branch.server.ServerLocations;
import branch.server.ServerLocations.Location;
import junit.framework.TestCase;


public class ServerLocationsTest extends TestCase {
	private File tempFile_;
	
	protected void setUp() throws Exception {
		tempFile_ = File.createTempFile("locations", ".txt");
		
		String str = "";
		str += "S01 localhost 10001\n";
		str += "S02 11.12.13.14 10002\n";
		str += "S03 12.13.14.15 10004\n";
		str += "S04 localhost 10005\n";
		try {
			FileWriter fw = new FileWriter(tempFile_);
			fw.write(str.toCharArray());
			fw.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	protected void tearDown() {
		tempFile_.delete();
	}
	
	public void testLocation() {
		ServerLocations locations = null;
		try {
			locations = new ServerLocations(tempFile_.getAbsolutePath());
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		Node node = new Node("S01");
		ServerLocations.Location l = locations.getLocationForNode(node);
		assertEquals("localhost", l.getIp());
		assertEquals(10001, l.getPort());
		
		node = new Node("S02");
		l = locations.getLocationForNode(node);
		assertEquals("11.12.13.14", l.getIp());
		assertEquals(10002, l.getPort());
		
		node = new Node("S03");
		l = locations.getLocationForNode(node);
		assertEquals("12.13.14.15", l.getIp());
		assertEquals(10004, l.getPort());
		
		node = new Node("S04");
		l = locations.getLocationForNode(node);
		assertEquals("localhost", l.getIp());
		assertEquals(10005, l.getPort());
	}
}
