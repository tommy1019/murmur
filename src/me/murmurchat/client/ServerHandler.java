package me.murmurchat.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ServerHandler extends Thread
{
	public static final String IP = "10.21.7.93";
	public static final int PORT = 21212;

	Socket socket;

	DataInputStream in;
	DataOutputStream out;

	public void run()
	{
		try
		{
			socket = new Socket();
			socket.connect(new InetSocketAddress(IP, PORT), 1000);

			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());

			out.write(Murmur.crypt.keyPair.getPublic().getEncoded());

			int msgSize = in.readInt();
			byte[] msg = new byte[msgSize];
			in.read(msg);

			String secretMessage = new String(Murmur.crypt.decrpyt(msg));
			out.write(secretMessage.getBytes());

			Murmur.accountDatabase.readFromFile(Murmur.crypt, in);

			//TODO: Update gui addressbook
			Murmur.mainWindowController.populateContactList();
			
			int packetType = -1;
			while ((packetType = in.read()) != -1)
			{
				switch (packetType)
				{
				case 1:
					System.out.println("Got heartbeat");
					out.write(1);
					break;
				default:
					System.out.println("Client sent unknown packet type " + packetType);
					break;
				}
			}

		}
		catch (IOException e)
		{
			System.out.println("Error reading from server.");
		}
	}

	public void disconnect()
	{
		try
		{
			socket.close();
		}
		catch (IOException e)
		{
			System.out.println("Error closing socket");
		}
		this.interrupt();
	}

	public void updateClientList()
	{
		try
		{
			out.write(2);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		Murmur.accountDatabase.writeToFile(Murmur.crypt, out);
	}
}
