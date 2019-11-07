package client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;


public class Client
{
	private static final int SERVER_PORT = 9090;
	private Socket clientSocket;
	private PrintWriter outputStream;
	private ClientMessenger clientMessenger;
	private Thread serverMessengerThread;

	public static void main(String[] args)
	{

		Client client = null;
		try
		{
			InetAddress localhost = InetAddress.getLocalHost();
			Socket socket = new Socket(localhost, SERVER_PORT);

			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			client = new Client(socket, out);
			client.run();

		}
		catch (IOException exp)
		{
			exp.printStackTrace();
		}finally
		{
			client.stop();
		}
	}

	public Client(Socket socket, PrintWriter outputStream)
	{
		this.clientSocket = socket;
		this.outputStream = outputStream;
	}

	public void run()
	{
		createServerMessenger();
		Scanner keyboard = new Scanner(new InputStreamReader(System.in));

		while (keyboard.hasNextLine() && clientMessenger.isAlive())
		{
			String command = keyboard.nextLine();

			if (command.equalsIgnoreCase("quit"))
			{
				System.out.println("You quit the game. Goodbye");
				shutdown();
				break;
			}
			else if (command != null)
			{
				sendMessageToServer(command);
			}
		}
	}

	private void createServerMessenger()
	{
		try
		{
			clientMessenger = new ClientMessenger(new ObjectInputStream(clientSocket.getInputStream()));
			serverMessengerThread = new Thread(clientMessenger);
			serverMessengerThread.start();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public void shutdown()
	{
		sendMessageToServer("quit");

		if(outputStream != null){ outputStream.close();}

		clientMessenger.setAliveStatus(false);
		serverMessengerThread.interrupt();
		stop();
	}

	public void stop(){
		try
		{
			clientSocket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void sendMessageToServer(String message)
	{
		outputStream.println(message);
	}
}





