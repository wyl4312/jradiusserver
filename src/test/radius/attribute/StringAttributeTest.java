package radius.attribute;

import junit.framework.TestCase;

public class StringAttributeTest extends TestCase {
	private StringAttribute sa = new StringAttribute(1);

	protected void setUp() throws Exception {
		super.setUp();
		sa.setString("abcdefg");
	}

	public void testGetString() {
		assertEquals("abcdefg",sa.getString());
		assertEquals("abcdefg".length()+2,sa.getLength());
	}

	public void testGetValue() {
		assertEquals("abcdefg",new String(sa.getValue()));
	}

	public void testIsValid() {
		assertTrue(sa.isValid());
		StringBuffer sb = new StringBuffer();
		for (int i=0;i<300;i++) {
			sb.append("x");
		}
		sa.setString(sb.toString());
		assertFalse(sa.isValid());
	}

}
