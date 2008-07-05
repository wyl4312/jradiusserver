package radius.attribute;

import junit.framework.TestCase;

public class TimeAttributeTest extends TestCase {
	private TimeAttribute ta = new TimeAttribute(1);
	long time = System.currentTimeMillis()/1000;

	protected void setUp() throws Exception {
		super.setUp();
		ta.setTime(time);
	}

	public void testSetTime() {
		try {
			ta.setTime(-1);
			fail();
		}catch (Throwable t) {
		}
		try {
			ta.setTime(0x0ffffffffffL);
			fail();
		}catch (Throwable t) {
		}
		try {
			ta.setTime(0);
		}catch (Throwable t) {
			fail();
		}
		try {
			ta.setTime(0x0ffffffffL);
		}catch (Throwable t) {
			fail();
		}
	}
	
	public void testGetDate() {
		assertEquals(time,ta.getDate().getTime()/1000);
	}
	
	public void testIsValid() {
		assertTrue(ta.isValid());
	}

}
