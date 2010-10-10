package test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import branch.server.Node;
import branch.server.Topology;

import junit.framework.TestCase;

public class TopologyTest extends TestCase {
	private File tempFile_;
private Node node_;
	protected void setUp() throws Exception {
		tempFile_ = File.createTempFile("topology", ".txt");
		
		node_ = new Node("S01");
		
		String str = "";
		str += "G01 S01\n";
		str += "S01 G01\n";
		str += "S01 S05\n";
		str += "S02 S04\n";
		str += "S02 S01\n";
		str += "S02 S03\n";
		str += "err err (topology file should ignore)\n";
		str += "S02 G02\n";
		str += "G02 S02\n";
		
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

	public void testIsReachable() {
		Topology tpl = null;
		try {
			tpl = new Topology(tempFile_.getAbsolutePath(), node_);
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		// Connected.
		assertTrue(tpl.isReachable("S01"));
		assertTrue(tpl.isReachable("G01"));
		assertTrue(tpl.isReachable("S05"));
		assertTrue(tpl.isReachable("S04"));
		assertTrue(tpl.isReachable("S01"));
		assertTrue(tpl.isReachable("S03"));
		assertTrue(tpl.isReachable("S02"));
		assertTrue(tpl.isReachable("G02"));
		
		
		// Not connected.
		assertFalse(tpl.isReachable("S04"));
		assertFalse(tpl.isReachable("S02"));
		assertFalse(tpl.isReachable("S04"));
		assertFalse(tpl.isReachable("S05"));
		assertFalse(tpl.isReachable("S02"));
		assertFalse(tpl.isReachable("S02"));
		assertFalse(tpl.isReachable("S02"));
	}
	
	public void testTopologyCreation() {
		Topology tpl = null;
//		try {
//			tpl = new Topology("no-file");
			fail("Creating topology from bad file should raise exception.");
//		} catch(IOException e) {
//			assertEquals(
//					"no-file (No such file or directory)",
//					e.getMessage());
//		}
	}
	
	public void testWhoNeighbors() {
		Topology tpl = null;
		try {
			tpl = new Topology(tempFile_.getAbsolutePath(), node_);
		} catch (IOException e) {
			fail(e.getMessage());
		}

		/**
		 * G01 S01
		 * S01 G01
		 * S01 S05
		 * S02 S04
		 * S02 S01
		 * S02 S03
		 * S02 G02
		 * G02 S02
		 */
		
		Vector<String> expectedNeighbors,neighbors;
		expectedNeighbors = new Vector<String>();
		
		expectedNeighbors.add("S01");
		neighbors = tpl.getOutNeighbors();
		assertTrue(expectedNeighbors.equals(neighbors));
		expectedNeighbors.clear();
		
		expectedNeighbors.add("G01");
		expectedNeighbors.add("S05");
		neighbors = tpl.getOutNeighbors();
		assertTrue(expectedNeighbors.equals(neighbors));
		expectedNeighbors.clear();
		
		expectedNeighbors.add("S04");
		expectedNeighbors.add("S01");
		expectedNeighbors.add("S03");
		expectedNeighbors.add("G02");
		neighbors = tpl.getOutNeighbors();
		assertTrue(expectedNeighbors.equals(neighbors));
		expectedNeighbors.clear();
		
		expectedNeighbors.add("S02");
		neighbors = tpl.getOutNeighbors();
		assertTrue(expectedNeighbors.equals(neighbors));
		expectedNeighbors.clear();
	}
}
