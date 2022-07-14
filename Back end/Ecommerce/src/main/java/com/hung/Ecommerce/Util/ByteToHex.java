package com.hung.Ecommerce.Util;

public class ByteToHex {

	public static String bytesToHex(byte[] bytes) {
		   StringBuilder sb = new StringBuilder();
	        for (byte hashByte : bytes) {
	            int intVal = 0xff & hashByte;
	            if (intVal < 0x10) {
	                sb.append('0');
	            }
	            sb.append(Integer.toHexString(intVal));
	        }
	        return sb.toString();
	}
}
