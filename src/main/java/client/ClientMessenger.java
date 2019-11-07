package client;

import util.Action;
import util.GameState;
import java.io.IOException;
import java.io.ObjectInputStream;


public class ClientMessenger implements Runnable
{
	private ObjectInputStream inputStream;
	private boolean isAlive = true;

	public ClientMessenger(ObjectInputStream inputStream)
	{
		this.inputStream = inputStream;
	}

	@Override public void run()
	{
		while (isAlive)
		{
			serverResponse();
		}
	}

	private void serverResponse()
	{
		Object serverResponse = null;
		try
		{
			serverResponse = inputStream.readObject();
		}
		catch (NullPointerException | ClassNotFoundException exp)
		{
			exp.printStackTrace();
		}
		catch(IOException exp)
		{
			setAliveStatus(false);
			closeStream();
			System.out.println("Connection Closed");
		}

		if(serverResponse != null)
		{
			if (serverResponse instanceof GameState)
			{
				GameState response = (GameState) serverResponse;

				if (response.getCurrentAction() == Action.WINNER || response.getCurrentAction() == Action.QUIT)
				{
					setAliveStatus(false);
				}
				printCurrentStateOfBoard(response.getCurrentStateBoard(), response.getBoardSize());
				System.out.println(response.getMessage());
			}
			else
			{
				System.out.println(serverResponse.toString());
			}
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

	private void closeStream(){
		try
		{
			if(inputStream != null){ inputStream.close(); }

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void setAliveStatus(boolean alive)
	{
		isAlive = alive;
	}

	public boolean isAlive() { return isAlive; }

}
