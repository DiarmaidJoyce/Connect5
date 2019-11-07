package server;


import util.Action;
import java.io.IOException;

public class ConnectBoardHandler implements GameBoardHandler
{
	private char player1Token = 'X';
	private char player2Token = 'O';
	private PlayerHandler currentPlayer;
	private PlayerHandler otherPlayer;
	private Board gameBoard;

	public ConnectBoardHandler()
	{
		setup();
	}

	public synchronized void addPlayerToGame(PlayerHandler player)
	{
		if(currentPlayer == null){
			currentPlayer = player;
			player.setToken(player1Token);

		} else {
			otherPlayer = player;
			player.setToken(player2Token);
		}
	}

	public synchronized boolean isOpponentConnected(){

		return currentPlayer != null && otherPlayer != null;
	}

	public synchronized void notifyOpponent(PlayerHandler player, Object message)  throws IOException
	{
		if(currentPlayer != null && otherPlayer != null)
		{
			if(currentPlayer.equals(player)){
				otherPlayer.writeToClient(message);
			} else {
				currentPlayer.writeToClient(message);
			}
		}
	}

	public void setup()
	{
		gameBoard = new ConnectBoard();
	}


	public synchronized Action enterMove(final PlayerHandler player, final String inputColumn)
	{
		int nummber;

		try
		{
			nummber = Integer.parseInt(inputColumn);
		}
		catch (NumberFormatException e)
		{
			return Action.NOT_NUMBER;
		}

		if (player != currentPlayer)
		{
			return Action.NOT_YOUR_TURN;
		}

		else if (!(1 <= nummber && nummber <= gameBoard.getBoardSize()))
		{
			return Action.ILLEGAL_MOVE;
		}

		int adjustInputColumn = nummber - 1;

		gameBoard.dropTokenOnBoard(player.getToken(), adjustInputColumn);

		if(gameBoard.hasWon(player.getToken())){
			return Action.WINNER;
		}

		determineNextPlayer(player);

		return Action.SUCCESS;
	}

	private void determineNextPlayer(final PlayerHandler player)
	{
		if(currentPlayer == player)
		{
			setCurrentPlayer(otherPlayer);
			setOtherPlayer(player);
		}
	}

	public synchronized void setCurrentPlayer(final PlayerHandler player)
	{
		this.currentPlayer = player;
	}

	public synchronized PlayerHandler getCurrentPlayer()
	{
		return this.currentPlayer;
	}

	public PlayerHandler getOtherPlayer() { return this.otherPlayer; }

	public void setOtherPlayer(PlayerHandler otherPlayer) { this.otherPlayer = otherPlayer; }

	public synchronized String[][] getBoard()
	{
		return gameBoard.getBoard();
	}

	public synchronized void setBoard(Board board)
	{
		this.gameBoard = board;
	}

	public int getBoardSize(){
		return gameBoard.getBoardSize();
	}


}
