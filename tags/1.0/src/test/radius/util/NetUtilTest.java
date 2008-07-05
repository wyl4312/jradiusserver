/*
 * 创建日期 2005-5-4
 *
 */
package radius.util;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 *
 */
public class NetUtilTest extends TestCase {

    public void testIsValidteAddress() {
        String host = "localhost";
        assertTrue(NetUtil.isValidAddress(host));
        host = host+"1234 sdfsd";
        assertFalse(NetUtil.isValidAddress(host));
        host= "127.0.0.1";
        assertTrue(NetUtil.isValidAddress(host));
        host = "10.1.210.1";
        assertTrue(NetUtil.isValidAddress(host));
        host = "12345.22334.2234.33";
        assertFalse(NetUtil.isValidAddress(host));
        host = "192.168.1.1";
        assertTrue(NetUtil.isValidAddress(host));
        host = "www.yahoo.com";
        assertTrue(NetUtil.isValidAddress(host));
    }

}
