/*
 * 创建日期 2005-5-4
 *
 */
package radius.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class NetUtil {

    public static boolean isValidAddress(String address) {
        try {
            InetAddress ia = InetAddress.getByName(address);
            return ia != null;
        } catch (UnknownHostException e) {
            return false;
        }
    }

    private NetUtil() {
    }

}