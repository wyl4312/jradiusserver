package radius.util;

import java.nio.ByteBuffer;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class ConvertUtil {
	
	private ConvertUtil() {}
	
	public static String toHex(byte[] buf) {
		StringBuffer sb = new StringBuffer();
		toHex(buf,sb);
		return sb.toString();
	}
	
	public static void toHex(byte[] buf,StringBuffer sb) {
		if (buf==null)
			return;
		byte[] temp = new byte[2];
		for (int i=0;i<buf.length;i++) {
			byte2Hex(buf[i],temp);
			sb.append((char)temp[0]).append((char)temp[1]).append(" ");
		}
	}
	
	public static String toHex(ByteBuffer data) {
	    StringBuffer sb = new StringBuffer();
	    toHex(data,sb);
	    return sb.toString();
	}
	
	public static void toHex(ByteBuffer data,StringBuffer sb) {
        int pos = data.position();
		data.flip();
        byte[] temp = new byte[2];
        while (data.hasRemaining()) {
            byte2Hex(data.get(),temp);
			sb.append((char)temp[0]).append((char)temp[1]).append(" ");
        }
		data.clear();
		data.position(pos);
    }
    
    private static byte[] data = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
    
    private static void byte2Hex(byte b,byte[] buf) {
        buf[0] = data[(b>>>4)&0x0f];
        buf[1] = data[(b&0x0f)];
    }

	public static void main(String[] args) {
		byte[] b = new byte[]{1};
		System.out.println(toHex(b));
	}
}
