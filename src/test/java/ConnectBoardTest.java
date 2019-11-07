import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import server.ConnectBoard;



public class ConnectBoardTest
{
	private ConnectBoard game;

	@Before
	public void setup()
	{
		game = new ConnectBoard();
	}

	/* These tests check the functionality of the various win scenarios */

	@Test
	public void checkHorizontalWinTest(){

		String[][] board = new String[][]{
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[X]", "[]", "[]", "[]", "[X]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[X]", "[X]", "[X]", "[X]", "[X]", "[]", "[]", "[]"},
				{ "[]", "[X]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[O]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},

		};

		game.setBoard(board);
		boolean result = game.hasWon('X');

		Assert.assertEquals(result, true);
	}

	@Test
	public void checkVerticalWinTest(){

		String[][] board = new String[][]{
				{ "[]", "[X]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[X]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[X]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[X]", "[]", "[]", "[]", "[X]", "[]", "[]", "[]"},
				{ "[]", "[X]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[X]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[X]", "[X]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[X]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[O]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},

		};


		game.setBoard(board);
		boolean result = game.hasWon('X');

		Assert.assertEquals(result, true);

	}

	@Test
	public void checkDiagonalWinTest(){

		String[][] board = new String[][]{
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[]", "[X]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[X]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[X]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[X]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[X]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[O]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},

		};


		game.setBoard(board);
		boolean result = game.hasWon('X');

		Assert.assertEquals(result, true);

	}

	@Test
	public void checkReverseDiagonalWinTest(){

		String[][] board = new String[][]{
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[X]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[X]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[X]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[X]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[]", "[X]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},

		};

		game.setBoard(board);
		boolean result = game.hasWon('X');

		Assert.assertEquals(result, true);
	}



	@Test
	public void checkNotWinTest(){

		String[][] board = new String[][]{
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[0]", "[X]", "[X]", "[]", "[X]", "[X]", "[]", "[]", "[]"},

		};

		game.setBoard(board);
		boolean result = game.hasWon('X');

		Assert.assertEquals(result, false);
	}

	@Test
	public void checkNotWinTest2(){

		String[][] board = new String[][]{
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[O]"},
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[O]", "[]"},
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[O]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[]", "[O]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[O]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[0]", "[X]", "[X]", "[]", "[X]", "[X]", "[]", "[]", "[]"},

		};

		game.setBoard(board);
		boolean result = game.hasWon('X');

		Assert.assertEquals(result, false);
	}


	@Test
	public void checkDropTokenOnBoard(){

		printCurrentStateOfBoard(game.getBoard(), game.getBoardSize());

		game.dropTokenOnBoard('X', 2);
		game.dropTokenOnBoard('X', 5);
		game.dropTokenOnBoard('X', 7);

		int actualNumberOfDroppedTokens = 3;

		printCurrentStateOfBoard(game.getBoard(), game.getBoardSize());

		int number = checkNumberOfTokens(game.getBoard(), game.getBoardSize(), "X");

		Assert.assertEquals(actualNumberOfDroppedTokens, number);

	}



	private void printCurrentStateOfBoard(String[][] board, int boardSize)
	{

		System.out.println();
		for (int row = 0; row < boardSize; row ++)
		{
			for (int col = 0; col < boardSize ; col++)
			{
				System.out.print(board[row][col]);
			}
			System.out.println();
		}
		System.out.println();
	}

	private int checkNumberOfTokens(String[][] board, int boardSize, String c)
	{
		int numberOfTokens = 0;

		for (int row = 0; row < boardSize; row ++)
		{
			for (int col = 0; col < boardSize ; col++)
			{
				if(board[row][col].contains(c)){
					numberOfTokens++;
				}
			}
		}
		return numberOfTokens;
	}

}
