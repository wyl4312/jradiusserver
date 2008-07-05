package radius.attribute;

import radius.RadiusAttribute;

public class VendorSpecificAttribute extends RadiusAttribute {
	public static int MIN_LENGTH = 7;

	public static int VLA_START_INDEX = 4;

	private VendorId vendorId;

	private VLA[] vlas;

	protected VendorSpecificAttribute(int type) {
		super(type);
	}

	protected void decodeValue() {
		if (value == null || value.length < MIN_LENGTH)
			return;
		vendorId = VendorId.getVendorId(value);
		vlas = VLA.parseVLA(value, 4);
	}

	protected void encodeValue() {
		if (vendorId==null || vlas==null)
			return;
		int total = 4;
		for (int i=0;i<vlas.length;i++) {
			total+=vlas[i].length;
		}
		byte[] data = new byte[total];
		System.arraycopy(vendorId.toBytes(),0,data,0,4);
		int index = 4;
		for (int i=0;i<vlas.length;i++) {
			byte[] temp = vlas[i].toBytes();
			if (temp==null)
				return;
			System.arraycopy(temp,0,data,index,temp.length);
			index+=temp.length;
		}
	}

	protected void clearSub() {
		this.vendorId = null;
		this.vlas = null;
	}

	protected boolean isTypeValid() {
		return type == RadiusAttribute.VENDOR_SPECIFIC;
	}

	protected boolean isValueValid() {
		return vendorId != null && vlas != null;
	}

	public static class VLA {
		public int type;

		public int length;

		public String value;
		
		public byte[] toBytes() {
			if (value==null || value.length()!=length-2) {
				return null;
			}
			byte[] data = new byte[length];
			data[0] = (byte) type;
			data[1] = (byte) length;
			System.arraycopy(value.getBytes(),0,data,0,length-2);
			return data;
		}

		public static VLA[] parseVLA(byte[] data, int offset) {
			if (data == null || data.length < 3) {
				return null;
			}
			int totalLength = data.length;
			VLA[] vlas = new VLA[totalLength / 3];
			int index = offset;
			int count = 0;
			while (index < totalLength) {
				if (count >= vlas.length) {// ensure capacity
					VLA[] temp = new VLA[count + 1];
					System.arraycopy(vlas, 0, temp, 0, count);
					vlas = temp;
				}
				vlas[count].type = data[index++] & 0xff;
				vlas[count].length = data[index++] & 0xff;
				if (vlas[count].length-2 + index <= totalLength + 1) {
					vlas[count].value = new String(data, index,
							vlas[count].length-2);
					index += vlas[count].length-2;
					count++;
				} else {
					break;
				}
			}
			return vlas;

		}
	}

	public VendorId getVendorId() {
		return vendorId;
	}

	public void setVendorId(VendorId vendorId) {
		this.vendorId = vendorId;
	}

	public VLA[] getVlas() {
		return vlas;
	}

	public void setVlas(VLA[] vlas) {
		this.vlas = vlas;
	}

}
