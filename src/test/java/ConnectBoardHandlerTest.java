import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import server.Board;
import server.ConnectBoardHandler;
import server.PlayerHandler;
import util.Action;
import java.io.IOException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ConnectBoardHandlerTest
{

	private ConnectBoardHandler boardHandler;

	private PlayerHandler currentPlayer;
	private PlayerHandler otherPlayer;

	@Before
	public void setup()
	{
		boardHandler = new ConnectBoardHandler();

		currentPlayer = mock(PlayerHandler.class);
		otherPlayer = mock(PlayerHandler.class);

		boardHandler.addPlayerToGame(currentPlayer);
		boardHandler.addPlayerToGame(otherPlayer);

	}

	@Test
	public void addPlayerToGameTest()
	{
		Assert.assertEquals(currentPlayer, boardHandler.getCurrentPlayer());
		Assert.assertEquals(otherPlayer, boardHandler.getOtherPlayer());
	}

	@Test
	public void isOpponentConnectedTest_YES()
	{
		boolean result =  boardHandler.isOpponentConnected();
		Assert.assertTrue(result);
	}


	@Test
	public void notifyOpponentTest() throws IOException
	{
		boardHandler.notifyOpponent(currentPlayer, "Message");
		verify(otherPlayer, times(1)).writeToClient(any());
	}

	@Test
	public void enterMoveTest_NotNumber()
	{
		Action actionResult = boardHandler.enterMove(currentPlayer, "a");
		Assert.assertEquals(actionResult, Action.NOT_NUMBER);
	}

	@Test
	public void enterMoveTest_NotCurrentPlayer()
	{
		Action actionResult = boardHandler.enterMove(otherPlayer, "2");
		Assert.assertEquals(actionResult, Action.NOT_YOUR_TURN);
	}


	@Test
	public void enterMoveTest_IllegalNumber()
	{
		Action actionResult = boardHandler.enterMove(currentPlayer, "23");
		Assert.assertEquals(actionResult, Action.ILLEGAL_MOVE);
	}

	@Test
	public void enterMoveTest_SuccessfulDrop()
	{
		Action actionResult = boardHandler.enterMove(currentPlayer, "8");
		Assert.assertEquals(actionResult, Action.SUCCESS);

		Assert.assertEquals(boardHandler.getCurrentPlayer(), otherPlayer);
		Assert.assertEquals(boardHandler.getOtherPlayer(), currentPlayer);
	}


	@Test
	public void enterMoveTest_Win()
	{
		Board gameBoard = mock(Board.class);
		when(gameBoard.hasWon(anyChar())).thenReturn(true);
		when(gameBoard.getBoardSize()).thenReturn(9);

		boardHandler.setBoard(gameBoard);

		Action actionResult = boardHandler.enterMove(currentPlayer, "8");
		Assert.assertEquals(actionResult, Action.WINNER);

	}

}
