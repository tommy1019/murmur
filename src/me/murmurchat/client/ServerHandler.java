package me.murmurchat.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

import javafx.application.Platform;
import me.murmurchat.client.GUI.GUI;

public class ServerHandler extends Thread
{
	public static final String IP = "192.168.111.143";
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

			out.write(Murmur.profile.publicKey.getEncoded());

			int msgSize = in.readInt();
			byte[] msg = new byte[msgSize];
			in.read(msg);

			String secretMessage = new String(Murmur.profile.decrpyt(msg));
			out.write(secretMessage.getBytes());

			int accountStatus = in.read();

			if (accountStatus == 0)
			{
				Murmur.accountDatabase = new AccountDatabase(in);
			}
			else if (accountStatus == 1)
			{
				Platform.runLater(new Runnable()
				{
					@Override
					public void run()
					{
						GUI.launchUserInfoDialog();
					}
				});

				try
				{
					this.wait();
				}
				catch (InterruptedException e)
				{
					Murmur.fatalError(e);
				}
				
				Murmur.accountDatabase.writeToFile(out);
			}

			Platform.runLater(new Runnable()
			{
				@Override
				public void run()
				{
					GUI.launchMainWindow();
				}
			});

			try
			{
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
							if (Arrays.equals(senderKey, c.contactPublicKey))
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
				System.err.println("Error reading from server.");
				e.printStackTrace();
			}
		}
		catch (IOException e)
		{
			System.out.println("Error connecting to server.");
			Murmur.fatalError(e);
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

	public void sendMessage(Contact recipient, Message message)
	{
		try
		{
			out.write(8);

			//out.write(currentContact.contactPublicKey);
		}
		catch (IOException e)
		{

		}

		// TODO: Write method
		System.out.println("SENDING!");
	}
}
