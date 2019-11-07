package server;

public interface Board
{

	String[][] initialize();

	void dropTokenOnBoard(final char token, final int column);

	boolean hasWon(final char token);

	int getBoardSize();

	String[][] getBoard();
}
