package server;

import util.Action;
import java.io.IOException;


public interface GameBoardHandler
{

	Action enterMove(final PlayerHandler playerToken, final String input);

	String[][] getBoard();

	void setBoard(Board board);

	void setCurrentPlayer(PlayerHandler playerHandler);

	PlayerHandler getCurrentPlayer();

	PlayerHandler getOtherPlayer();

	void addPlayerToGame(PlayerHandler player);

	void notifyOpponent(PlayerHandler player, Object message)  throws IOException;

	boolean isOpponentConnected();

	int getBoardSize();



}
