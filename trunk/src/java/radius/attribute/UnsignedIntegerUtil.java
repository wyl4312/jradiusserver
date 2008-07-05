package radius.attribute;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
class UnsignedIntegerUtil {

	private UnsignedIntegerUtil() {
	}

	public static long parseLong(byte[] data) {
		long l = (data[0] << 24) | (data[1] << 16) | (data[2] << 8) | (data[3]);
		l &= 0x0ffffffff;
		return l;
	}

	public static byte[] encodeLong(long l) {
		byte[] data = new byte[4];
		data[0] = (byte) (l >> 24);
		data[1] = (byte) (l >> 16);
		data[2] = (byte) (l >> 8);
		data[3] = (byte) (l);
		return data;
	}

}
