package radius.attribute;

import java.net.*;

import junit.framework.TestCase;

public class AddressAttriubteTest extends TestCase {

	private AddressAttriubte aa = new AddressAttriubte(1);

	protected void setUp() throws Exception {
		super.setUp();
		aa.setAddress(InetAddress.getLocalHost());
	}

	public void testGetValue() throws UnknownHostException {
		byte[] value = aa.getValue();
		assertNotNull(value);
		assertEquals(InetAddress.getByAddress(value),InetAddress.getLocalHost());
	}
	
	public void testIsValid() {
		assertTrue(aa.isValid());
	}

}
