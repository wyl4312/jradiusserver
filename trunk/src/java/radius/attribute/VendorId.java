package radius.attribute;

public class VendorId {
	private byte high;

	private String smiCode;
	
	public VendorId(String smiCode) {
		this.smiCode = smiCode;
	}
	
	public static VendorId getVendorId(byte[] data) {
		if (data.length<4) {
			return null;
		}
		if (data[0]!=0) {
			return null;
		}
		String smiCode = new String(data,1,3);
		VendorId vi = new VendorId(smiCode);
		return vi;
	}

	public byte getHigh() {
		return high;
	}

	public String getSmiCode() {
		return smiCode;
	}

	public void setSmiCode(String smiCode) {
		this.smiCode = smiCode;
	}
	
	public byte[] toBytes() {
		byte[] data = new byte[4];
		data[0]  = 0;
		if (smiCode!=null && smiCode.length()==3) {
			data[1] = (byte) smiCode.charAt(0);
			data[2] = (byte) smiCode.charAt(1);
			data[3] = (byte) smiCode.charAt(2);
		}
		else {
			data[1] = 0;
			data[2] = 0;
			data[3] = 0;
		}
		return data;
	}

}
