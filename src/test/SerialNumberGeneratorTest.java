package test;
/**
 * 
 * @author qsh2
 * 
 */

import branch.server.SerialNumberGenerator;
import branch.server.Trxn;
import branch.server.Trxn.TransxType;
import junit.framework.TestCase;

public class SerialNumberGeneratorTest extends TestCase {
	public void testGetNewSerial() {
		assertEquals("T2000000001", SerialNumberGenerator.getNewSerial(Trxn.TransxType.TRANSFER));
		assertEquals("D2000000002", SerialNumberGenerator.getNewSerial(Trxn.TransxType.DEPOSIT));
		assertEquals("W2000000003", SerialNumberGenerator.getNewSerial(Trxn.TransxType.WITHDRAW));
		assertEquals("Q2000000004", SerialNumberGenerator.getNewSerial(Trxn.TransxType.QUERY));

		assertEquals("T0500123456", SerialNumberGenerator.getNewSerial(Trxn.TransxType.TRANSFER));
		assertEquals("D0500123457", SerialNumberGenerator.getNewSerial(Trxn.TransxType.DEPOSIT));
		assertEquals("W0500123458", SerialNumberGenerator.getNewSerial(Trxn.TransxType.WITHDRAW));
		assertEquals("Q0500123459", SerialNumberGenerator.getNewSerial(Trxn.TransxType.QUERY));
	}
}