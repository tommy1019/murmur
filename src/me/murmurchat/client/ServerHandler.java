package me.murmurchat.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

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
			Murmur.mainWindowController.populateContactList();

			int packetType = -1;
			while ((packetType = in.read()) != -1)
			{
				switch (packetType)
				{
				case 1:
					out.write(1);
					break;
				case 8:
					System.out.println("");
					byte[] senderKey = Util.readPublicKey(in);
					String chatMsg = Util.readString(in);
					
					for (Contact c : Murmur.accountDatabase.contacts)
					{
						if (Arrays.equals(senderKey, c.publicKey))
						{
							Murmur.mainWindowController.receiveMessage(chatMsg);
						}
					}
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

	public void sendMessage(Contact currentContact, String message)
	{
		try
		{
			System.out.println(currentContact.displayName);
			out.write(8);
			out.write(currentContact.publicKey);
			out.write(message.getBytes().length);
			out.write(message.getBytes());
			out.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
