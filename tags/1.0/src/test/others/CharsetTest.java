package others;

import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

public class CharsetTest extends TestCase {

	public void testISO88591() {
		byte[] data = new byte[256];
		for (int i=0;i<data.length;i++) {
			data[i] = (byte) i;
		}
		try {
			String s= new String(data,"ISO-8859-1");
			assertEquals(256,s.length());
			byte[] temp = s.getBytes("ISO-8859-1");
			for (int i=0;i<data.length;i++) {
				assertEquals(data[i],temp[i]);
			}
		} catch (UnsupportedEncodingException e) {
			fail();
		}
	}
}
