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
}
