package server;

public class ConnectBoard implements Board
{

	private String[][] board;
	private int SIZE_OF_BOARD = 9;
	private String OPENING_BRACKET = "[";
	private String CLOSING_BRACKET = "]";
	private String EMPTY = OPENING_BRACKET + " " + CLOSING_BRACKET;

	public ConnectBoard()
	{
		initialize();
	}

	public String[][] initialize()
	{
		board = new String[SIZE_OF_BOARD][SIZE_OF_BOARD];

		for (int row = 0; row < SIZE_OF_BOARD; row++)
		{
			for (int col = 0; col < SIZE_OF_BOARD; col++)
			{
				board[row][col] = EMPTY;
			}
		}
		return board;
	}

	public void dropTokenOnBoard(final char token, final int column)
	{
		for (int col =  SIZE_OF_BOARD - 1; col >= 0; col--)
		{
			if (board[col][column] == EMPTY)
			{
				board[col][column] = OPENING_BRACKET + token + CLOSING_BRACKET;
				break;
			}
		}
	}

	public boolean hasWon(final char token)
	{
		String tokenString = String.valueOf(token);

		for (int row = 0; row < SIZE_OF_BOARD; row++)
		{
			for (int col = 0; col < SIZE_OF_BOARD; col++)
			{
				if (checkHorizontal(tokenString, row, col) || checkVertical(tokenString, row, col)
						|| checkDiagonal(tokenString, row, col) || checkReverseDiagonal(tokenString, row, col))
				{
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkHorizontal(final String token, final int row, final int col)
	{
		return p(row, col).contains(token)
				&& p(row, col).equals(p(row, col + 1))
				&& p(row, col).equals(p(row, col + 2))
				&& p(row, col).equals(p(row, col + 3))
				&& p(row, col).equals(p(row, col + 4));
	}

	private boolean checkVertical(final String token, final int row, final int col)
	{
		return p(row, col).contains(token)
				&& p(row, col).equals(p(row + 1, col))
				&& p(row, col).equals(p(row + 2, col))
				&& p(row, col).equals(p(row + 3, col))
				&& p(row, col).equals(p(row + 4, col));
	}

	private boolean checkReverseDiagonal(final String token, final int row, final int col)
	{
		return p(row, col).contains(token)
				&& p(row, col).equals(p(row + 1, col + 1)) // this is put 1 because 0 starts at the top
				&& p(row, col).equals(p(row + 2, col + 2))
				&& p(row, col).equals(p(row + 3, col + 3))
				&& p(row, col).equals(p(row + 4, col + 4));
	}

	private boolean checkDiagonal(final String token, final int row, final int col)
	{
		return p(row, col).contains(token)
				&& p(row, col).equals(p(row - 1, col + 1))
				&& p(row, col).equals(p(row - 2, col + 2))
				&& p(row, col).equals(p(row - 3, col + 3))
				&& p(row, col).equals(p(row - 4, col + 4));
	}


	private String p(final int row, final int col)
	{
		return (row < 0 || row >= SIZE_OF_BOARD || col < 0  || col >= SIZE_OF_BOARD) ? EMPTY : board[row][col];
	}

	public String[][] getBoard()
	{
		return board;
	}

	public void setBoard(String[][] board)
	{
		this.board = board;
	}

	public int getBoardSize()
	{
		return SIZE_OF_BOARD;
	}

	public void setSIZE_OF_BOARD(int SIZE_OF_BOARD)
	{
		this.SIZE_OF_BOARD = SIZE_OF_BOARD;
	}

}
