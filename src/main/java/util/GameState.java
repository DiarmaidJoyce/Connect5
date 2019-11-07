package util;

import java.io.Serializable;


public class GameState implements Serializable
{
	private static final long serialVersionUID = -5399605122490543456L;

	private String[][] currentStateBoard;
	private Action currentAction;
	private String message;

	private int boardSize;

	public GameState(String[][] board, int boardSize, Action action, String message){
		this.currentStateBoard = board;
		this.currentAction = action;
		this.message = message;
		this.boardSize = boardSize;
	}

	public GameState(Action move, String message){
		this.currentAction = move;
		this.message = message;
	}

	public Action getCurrentAction()
	{
		return currentAction;
	}

	public String[][] getCurrentStateBoard()
	{
		return currentStateBoard;
	}

	public String getMessage() {return message;}


	public int getBoardSize()
	{
		return boardSize;
	}
}
