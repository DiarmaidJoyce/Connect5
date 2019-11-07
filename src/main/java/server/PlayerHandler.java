package server;

import util.Action;
import util.GameState;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class PlayerHandler implements Runnable
{

	private Scanner inputStream;
	private ObjectOutputStream outputStream;
	private GameBoardHandler gameHandler;
	private char token;
	private String playerName;
	private boolean isAlive = true;

	public PlayerHandler(Scanner scanner, ObjectOutputStream objectStream,  GameBoardHandler game)
	{
		this.gameHandler = game;
		this.inputStream = scanner;
		this.outputStream = objectStream;
	}

	@Override public void run()
	{
		try
		{
			setUpPlayerOnGameBoard();
			play();
		}
		catch (IOException e)
		{
			System.out.println("Player Handler shut down unexpectedly");
			e.printStackTrace();
			shutDown();
		}
		finally
		{
			shutDown();
		}
	}

	public void play() throws IOException
	{

		while (isAlive)
		{
			boolean successful_move = false;
			String command;
			try
			{
				command = inputStream.nextLine();

			}
			catch (NoSuchElementException exp)
			{
				break;
			}

			if (command != null)
			{

				if (command.equalsIgnoreCase("quit"))
				{
					gameHandler.notifyOpponent(this, new GameState(Action.QUIT, this.getPlayerName() + " has quit. Game Ended."
							+ "\nPress any button to end session"));
					setAlive(false);
					break;
				}

				if (!gameHandler.isOpponentConnected())
				{
					writeToClient("Opponent is not connected. Please Wait..");
				}
				else
				{
					successful_move = playMove(command);
				}

				if (successful_move)
				{
					gameHandler.notifyOpponent(this,
							new GameState(gameHandler.getBoard(), gameHandler.getBoardSize(),
									Action.SUCCESS, "It's your turn " + gameHandler.getCurrentPlayer().getPlayerName() +
									", please enter column (1-9). Your token: " + gameHandler.getCurrentPlayer().getToken()));
				}
			}
		}

	}

	public void setUpPlayerOnGameBoard() throws IOException
	{
		introMessage();
		gameHandler.addPlayerToGame(this);

		if (!gameHandler.isOpponentConnected())
		{
			writeToClient("Opponent is not connected. Please Wait..");
		}
		else
		{
			writeToClient("Game has begun. Please wait for opponent to make move.");
			gameHandler.notifyOpponent(this, new GameState(gameHandler.getBoard(), gameHandler.getBoardSize(),
					Action.SUCCESS, "Other player has connected.\nPlease make first move. Please enter column (1-9)"));
		}
	}

	public void writeToClient(Object obj) throws IOException
	{
		outputStream.reset();
		outputStream.writeObject(obj);
	}


	public void shutDown()
	{
		try
		{
			inputStream.close();
			outputStream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private boolean playMove(String command) throws IOException
	{
		Object result;
		boolean successful_move = false;

		Action action = gameHandler.enterMove(this, command);

		if (action == (Action.ILLEGAL_MOVE))
		{
			result = "Please enter an integer between 1 and 9";
		}
		else if (action == Action.NOT_YOUR_TURN)
		{
			result = "It is not your turn, please wait until notified";
		}
		else if (action == Action.NOT_NUMBER)
		{
			result = "Illegal character entered, Please enter a number between 1 and 9";
		}
		else if (action == Action.WINNER)
		{
			result = new GameState(gameHandler.getBoard(), gameHandler.getBoardSize(),
					Action.WINNER, "You've Won.\nGame Ended\nPress any button to end session");
			setAlive(false);
			gameHandler.notifyOpponent(this, new GameState(gameHandler.getBoard(), gameHandler.getBoardSize(),
					Action.WINNER, this.getPlayerName() + " has Won.\nGame Ended"
					+ "\nPress any button to end session"));
		}
		else
		{
			result = new GameState(gameHandler.getBoard(), gameHandler.getBoardSize(),
					Action.SUCCESS, "Now wait for other player to make move.");
			successful_move = true;
		}

		writeToClient(result);

		return successful_move;
	}

	private void introMessage() throws IOException
	{
		writeToClient("\n ****** \n "
				+ "Welcome to Connect 5.\n "
				+ "How to play:\n "
				+ "To make a move, enter a number between 1 - 9 and your token will be placed onto the board.\n "
				+ "You win when you place 5 token in a row either horizontally, vertically or diagonally.\n "
				+ "You can quit anytime by typing 'quit' into your console.\n "
				+ "Enjoy the Game.\n ****** \n ");

		writeToClient("\nPlease enter your name:");

		String name = inputStream.nextLine();
		this.setPlayerName(name);
		writeToClient("Welcome " + getPlayerName());
	}



	public char getToken()
	{
		return this.token;
	}

	public void setToken(char token)
	{
		this.token = token;
	}

	public String getPlayerName()
	{
		return playerName;
	}

	public void setPlayerName(String playerName)
	{
		this.playerName = playerName;
	}

	public boolean isAlive() { return isAlive; }

	public void setAlive(boolean alive) { isAlive = alive; }

}

