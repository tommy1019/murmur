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

			System.out.println("Sent public key");

			int msgSize = in.readInt();
			byte[] msg = new byte[msgSize];
			in.read(msg);

			System.out.println("Got message");

			String secretMessage = new String(Murmur.crypt.decrpyt(msg));
			out.write(secretMessage.getBytes());

			System.out.println("Sent decrypted message");

			Murmur.accountDatabase.readFromFile(Murmur.crypt, in);

			int packetType = -1;
			while ((packetType = in.read()) != -1)
			{
				switch (packetType)
				{
				case 1:
					System.out.println("Got heartbeat");
					out.write(1);
					break;
				case 3:
					System.out.println("Replying with data");
					byte[] requester = Util.readPublicKey(in);
					out.write(4);
					out.write(requester);
					out.write(Murmur.accountDatabase.displayName.getBytes().length);
					out.write(Murmur.accountDatabase.displayName.getBytes());
				case 4:
					System.out.println("Received data");
					byte[] sender = Util.readPublicKey(in);
					String name = Util.readString(in);
					
					for (Contact c : Murmur.accountDatabase.contacts)
					{
						for (int i = 0; i < c.publicKey.length; i++)
							if (c.publicKey[i] != sender[i])
								break;
						
						c.displayName = name;
						//update frame
						break;
					}
					
					updateClientList();
					
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
