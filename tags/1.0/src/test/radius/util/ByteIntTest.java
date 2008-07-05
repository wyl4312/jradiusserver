package radius.util;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 *
 */
public class ByteIntTest extends TestCase {

    public void testByteInt() {
        byte b = (byte) 135;
        assertTrue(b<0);
        assertTrue((b&0xff)==135);
        int i = b;
        assertTrue(i<0);
        assertTrue((i&0xff)==135);
        assertTrue(((byte)i)==b);
        i=256;
        assertTrue(((byte)i)==0);
    }
}
