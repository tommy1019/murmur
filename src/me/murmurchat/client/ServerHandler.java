package me.murmurchat.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerHandler extends Thread
{
	public static final String IP = "10.21.7.93";
	public static final int PORT = 21212;

	Socket socket;

	DataInputStream in;
	DataOutputStream out;

	Crypt crypt;

	public ServerHandler(Crypt crypt)
	{
		this.crypt = crypt;

		try
		{
			socket = new Socket();
			socket.connect(new InetSocketAddress(IP, PORT), 1000);

			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
			return;
			//System.exit(1);
		}
		catch (IOException e)
		{
			System.out.println("Error connecting to server.");
			return;
			//System.exit(1);
		}

		this.start();
	}

	public void run()
	{
		try
		{
			out.write(crypt.keyPair.getPublic().getEncoded());

			System.out.println("Sent public key");

			int msgSize = in.readInt();
			byte[] msg = new byte[msgSize];
			in.read(msg);

			System.out.println("Got message");

			String secretMessage = new String(crypt.decrpyt(msg));
			out.write(secretMessage.getBytes());

			System.out.println("Sent decrypted message");

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
}
