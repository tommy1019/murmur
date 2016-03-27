package me.murmurchat.client;

import java.util.ArrayList;

public class Util
{
	public static byte[] toByteArray(ArrayList<Byte> list)
	{
		byte[] data = new byte[list.size()];
		for (int i = 0; i < list.size(); i++)
			data[i] = list.get(i);
		return data;
	}

	public static final char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes)
	{
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++)
		{
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
}