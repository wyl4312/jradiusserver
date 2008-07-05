package radius.attribute;

import junit.framework.TestCase;

public class TextAttributeTest extends TestCase {
	private TextAttribute att;

	protected void setUp() throws Exception {
		super.setUp();
		att = new TextAttribute(1);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetText() {
		String s = "1111";
		att.setValue(s.getBytes());
		assertEquals(s,att.getText());
	}

	public void testGetValue() {
		String s = "1111";
		att.setText(s);
		assertEquals(s,new String(att.getValue()));
	}

	public void testIsValid() {
		byte[] data = new byte[1024];
		for (int i=0;i<1024;i++) {
			data[i] = 'a';
		}
		String s = new String(data,0,200);
		att.setText(s);
		assertTrue(att.isValid());
		s = new String(data);
		att.setText(s);
		assertFalse(att.isValid());
	}

}
