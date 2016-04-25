package communication;

import java.nio.ByteBuffer;



public class DoubleToByteArray {

	
	public static byte[] toByteArray(double value) {
	    byte[] bytes = new byte[8];
	    ByteBuffer.wrap(bytes).putDouble(value);
	    return bytes;
	}

	public static double toDouble(byte[] bytes) {
	    return ByteBuffer.wrap(bytes).getDouble();
	}
	
}
