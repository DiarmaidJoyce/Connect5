
import client.Client;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.Server;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import static org.junit.Assert.fail;


public class ClientServerTest
{
	private Server gameServer;
	private final int PORT = 9090;
	private final int MAXIMUM_NO_CONNECTIONS = 2;
	private InetAddress localhost;
	private Thread serverThread;

	private Client player1;
	private Client player2;

	@Before
	public void setup() throws UnknownHostException
	{
		/* Setting up the server */

			localhost = InetAddress.getLocalHost();
			serverThread = new Thread(() -> {
				try
				{
					gameServer = new Server(PORT, MAXIMUM_NO_CONNECTIONS, localhost);
					gameServer.run();
				}
				catch (IOException e)
				{
					e.printStackTrace();
					fail();
				}
			});
			serverThread.start();
	}

	/*
	* What this test entails is to test that output is
	*  printing in the terminal for client and server
	* */

	@Test
	public void ClientAndServerCommunicatingTest()
	{
		try
		{
			Socket player1Socket = new Socket(localhost, PORT);
			PrintWriter player1OutputStream = new PrintWriter(player1Socket.getOutputStream(), true);
			player1OutputStream.println("Richie");

			Thread player1Thread = new Thread(() -> {

				player1 = new Client(player1Socket, player1OutputStream);
				player1.run();
			});
			player1Thread.start();

			player1Thread.sleep(200);


			Socket player2Socket = new Socket(localhost, PORT);
			PrintWriter player2OutputStream = new PrintWriter(player2Socket.getOutputStream(), true);
			player2OutputStream.println("Peter");

			Thread player2Thread = new Thread(() -> {

				player2 = new Client(player2Socket, player2OutputStream);
				player2.run();

			});
			player2Thread.start();

			System.out.println("----------");
			System.out.println("----------");

			makeMove(player1Thread, player1OutputStream, "2" );

			System.out.println("----------");
			System.out.println("----------");

			makeMove(player2Thread, player2OutputStream, "3" );

		}
		catch (IOException exp)
		{
			exp.printStackTrace();
			fail();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
			fail();
		}
	}

	private void makeMove(Thread thread,
			PrintWriter outputStream, String move) throws InterruptedException
	{
		outputStream.println(move);
		thread.sleep(1000);
	}

	@After
	public void cleanup()
	{

		player1.stop();
		player2.stop();
		gameServer.stop();
	}
}
