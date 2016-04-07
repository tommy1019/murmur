package me.murmurchat.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Optional;

import javafx.application.Platform;
import me.murmurchat.client.GUI.GUI;

public class ServerHandler extends Thread
{
	public static final String IP = "0.0.0.0";
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
				Platform.runLater(() ->
				{
					GUI.launchUserInfoDialog();
				});

				try
				{
					synchronized (this)
					{
						this.wait();
					}
				}
				catch (InterruptedException e)
				{
					Murmur.fatalError(e);
				}

				Murmur.accountDatabase.writeToFile(out);
			}

			Platform.runLater(() ->
			{
				GUI.launchMainWindow();
			});

			serverReadLoop();
		}
		catch (IOException e)
		{
			System.out.println("Error connecting to server.");
			Murmur.fatalError(e);
		}
	}

	public void serverReadLoop()
	{
		while (true)
		{
			try
			{
				int packetType = in.read();

				if (packetType == -1)
				{
					System.out.println("Reached end of stream.");
					return;
				}

				switch (packetType)
				{
				case 1:
					out.write(1);
					break;
				case 8:
					System.out.println("");
					byte[] senderKey = Util.readPublicKey(in);

					String msg = new String(Murmur.profile.decrpyt(Util.readPrefixedBytes(in)));

					Optional<Contact> contact = Murmur.accountDatabase.contacts.stream().filter(c -> Arrays.equals(senderKey, c.contactPublicKey)).findFirst();

					if (contact.isPresent())
						Murmur.mainWindowController.receiveMessage(msg);

					break;
				default:
					System.out.println("Client sent unknown packet type " + packetType);
					break;
				}
			}
			catch (IOException e)
			{
				System.out.println("Encountered exception during read loop.");
				disconnect();
				return;
			}
		}
	}

	public void disconnect()
	{
		this.interrupt();

		try
		{
			socket.close();
		}
		catch (IOException e)
		{
			System.out.println("Error closing socket");
		}
	}

	public void sendMessage(Contact recipient, Message message)
	{
		try
		{
			out.write(8);
			out.write(recipient.contactPublicKey);

			byte[] encryptedMessage = recipient.encryptForConcact(message.getBytes());

			out.writeInt(encryptedMessage.length);
			out.write(encryptedMessage);
		}
		catch (IOException e)
		{
			System.out.println("Error sending message.");
		}
	}

	public void updateServerProfile()
	{
		try
		{
			out.write(2);
			Murmur.accountDatabase.writeToFile(out);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
