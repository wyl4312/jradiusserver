package radius.attribute;

import java.nio.ByteBuffer;

import junit.framework.TestCase;

public class IntegerAttributeTest extends TestCase {
	
	private IntegerAttribute ia = new IntegerAttribute(1);

	protected void setUp() throws Exception {
		super.setUp();
		ia.setInteger(0xff);
	}

	public void testGetInteger() {
		assertEquals(0xffL,ia.getInteger());
	}
	
	public void testSetValue() {
		ByteBuffer bb = ByteBuffer.allocate(16);
		long l = 0x012345678L;
		bb.putLong(l);
		bb.flip();
		byte[] data = new byte[4];
		bb.getInt();
		bb.get(data);
		ia.setValue(data);
		long integer = ia.getInteger();
		assertEquals((int)l,integer);
	}

	public void testClear() {
		ia.clear();
		assertNull(ia.getValue());
		assertEquals(-1L,ia.getInteger());
	}

	public void testIsValid() {
		assertTrue(ia.isValid());
	}

}
