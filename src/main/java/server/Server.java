package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server
{
	private static final int PORT = 9090;
	private static final int MAXIMUM_NO_CONNECTIONS = 2;
	private static ExecutorService pool = Executors.newFixedThreadPool(MAXIMUM_NO_CONNECTIONS);
	private ServerSocket serverSocket;
	private Socket player1Socket;
	private Socket player2Socket;
	private GameBoardHandler gameBoardHandler;

	public static void main(String[] args)
	{

		Server gameServer = null;
		try
		{
			InetAddress localhost = InetAddress.getLocalHost();
			gameServer = new Server(PORT, MAXIMUM_NO_CONNECTIONS, localhost);
			gameServer.run();
		}
		catch (IOException exp)
		{
			exp.printStackTrace();
		}
		finally
		{
			gameServer.stop();
		}
	}

	public Server(int port, int backlog, InetAddress address) throws IOException
	{
		serverSocket = new ServerSocket(port, backlog, address);
	}

	public void run()
	{
		try
		{
			gameBoardHandler = new ConnectBoardHandler();

			System.out.println("[SERVER] Waiting for client connection...");

			player1Socket = serverSocket.accept();
			PlayerHandler player1 = new PlayerHandler(new Scanner(player1Socket.getInputStream()),
					new ObjectOutputStream(player1Socket.getOutputStream()), gameBoardHandler);
			pool.execute(player1);

			System.out.println("[SERVER] Player 1 connected");

			player2Socket = serverSocket.accept();
			PlayerHandler player2 = new PlayerHandler(new Scanner(player2Socket.getInputStream()),
					new ObjectOutputStream(player2Socket.getOutputStream()), gameBoardHandler);
			pool.execute(player2);

			System.out.println("[SERVER] Player 2 Connected");

			System.out.println("[SERVER] Lets Begin");

			pool.shutdown();


			while (!pool.isTerminated())
			{
				Thread.sleep(10000);
				System.out.println("\n[SERVER] Current State of the Board\n");
				printCurrentState();

			}

		}
		catch (IOException | InterruptedException exp)
		{
			System.err.println("[SERVER] Exception occurred");
			exp.printStackTrace();
		}
		finally
		{
			System.out.println("Game Ended.");
			printCurrentState();
			System.out.println("[SERVER] Shutting down");
			stop();
		}

	}

	public void stop()
	{
		try
		{
			player1Socket.close();
			player2Socket.close();
			serverSocket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void printCurrentState()
	{
		printCurrentStateOfBoard(gameBoardHandler.getBoard(), gameBoardHandler.getBoardSize());
		if (gameBoardHandler.getCurrentPlayer() != null && gameBoardHandler.getOtherPlayer() != null)
		{
			System.out.println("Player: " + gameBoardHandler.getCurrentPlayer().getPlayerName() + " & Token : " + gameBoardHandler
					.getCurrentPlayer().getToken());
			System.out.println(
					"Player: " + gameBoardHandler.getOtherPlayer().getPlayerName() + " & Token : " + gameBoardHandler.getOtherPlayer()
							.getToken());
		}
	}

	private void printCurrentStateOfBoard(String[][] board, int boardSize)
	{
		if (board != null)
		{
			System.out.println();
			for (int row = 0; row < boardSize; row++)
			{
				for (int col = 0; col < boardSize; col++)
				{
					System.out.print(board[row][col]);
				}
				System.out.println();
			}
			System.out.println();
		}
	}


}
