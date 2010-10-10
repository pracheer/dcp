package test;
import branch.server.Node;
import junit.framework.TestCase;

/**
 * 
 * @author qsh2
 *
 */

public class NodeTest extends TestCase {
	Node node_;
	
	public void setUp() {
		node_ = new Node(false,0);
	}

	public void testInvalidNode() {
		assertFalse(node_.parseString("G0100"));
		assertFalse(node_.parseString("B01"));
		assertFalse(node_.parseString("B01 "));
		assertFalse(node_.parseString("BBB"));
	}
	
	public void testValidNode() {
		assertTrue(node_.parseString("G01"));
		assertTrue(node_.parseString("S01"));
		assertTrue(node_.parseString("G98"));
		assertTrue(node_.parseString("S99"));
	}
	
	public void testNode() {
		assertTrue(node_.parseString("G01"));
		assertTrue(node_.isGui());
		assertEquals("G01", node_.toString());
		assertEquals(1, node_.getBranchId());
		
		assertTrue(node_.parseString("S22"));
		assertFalse(node_.isGui());
		assertEquals("S22", node_.toString());
		assertEquals(22, node_.getBranchId());
	}
}
