
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnitRunner;
import server.GameBoardHandler;
import server.PlayerHandler;
import util.Action;
import util.GameState;
import java.io.*;
import java.util.Scanner;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class PlayerHandlerTest
{

	private PlayerHandler playerHandler;
	private GameBoardHandler gameBoardHandler;
	private Scanner inputStream;
	private ObjectOutputStream outputStream;

	@Before
	public void setup()
	{
		gameBoardHandler = mock(GameBoardHandler.class);
	}

	/*
	 * Test that the opponent is notified when the current player has quit
	 * */
	@Test
	public void playerHasQuit_test() throws IOException
	{
		String message = "quit";

		duplicateMockSetup(message);

		playerHandler.play();

		ArgumentCaptor<GameState> captor = ArgumentCaptor.forClass(GameState.class);
		verify(gameBoardHandler).notifyOpponent(any(), captor.capture());
		Assert.assertEquals(captor.getValue().getCurrentAction(), Action.QUIT);
		Assert.assertEquals(playerHandler.isAlive(), false);
	}

	/*
	 * Test the return message when the opponent is not connected yet
	 * */
	@Test
	public void opponentIsNotConnected_test() throws IOException
	{
		String message = "Opponent is not connected. Please Wait..";

		duplicateMockSetup(message);
		when(gameBoardHandler.isOpponentConnected()).thenReturn(Boolean.FALSE);

		playerHandler.play();

		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(outputStream).writeObject(captor.capture());
		Assert.assertEquals(captor.getValue(), message);
	}


	/*
	* Test that when player has made a successful move that the appropriate messages have been sent
	* */
	@Test
	public void successfulMove_test() throws IOException
	{
		String command = "5";
		duplicateMockSetup(command);

		when(gameBoardHandler.enterMove(playerHandler, command )).thenReturn(Action.SUCCESS);

		playerHandler.play();

		//Capture message to current player
		ArgumentCaptor<GameState> captor = ArgumentCaptor.forClass(GameState.class);
		verify(outputStream).writeObject(captor.capture());
		Assert.assertEquals(captor.getValue().getCurrentAction(), Action.SUCCESS);
		Assert.assertEquals(captor.getValue().getMessage(), "Now wait for other player to make move.");

		//Capture message sent to other player
		ArgumentCaptor<GameState> secondCaptor = ArgumentCaptor.forClass(GameState.class);
		verify(gameBoardHandler).notifyOpponent(any(), secondCaptor.capture());
		Assert.assertTrue(secondCaptor.getValue().getMessage().contains("It's your turn"));

	}

	/*
	 * Test that the player has made a winning move and that the appropriate messages has been sent
	 * */
	@Test
	public void winningMove_test() throws IOException
	{
		String command = "5";

		duplicateMockSetup(command);
		when(gameBoardHandler.enterMove(playerHandler, command)).thenReturn(Action.WINNER);

		playerHandler.play();

		ArgumentCaptor<GameState> captor = ArgumentCaptor.forClass(GameState.class);
		verify(outputStream).writeObject(captor.capture());
		Assert.assertEquals(captor.getValue().getCurrentAction(), Action.WINNER);
		Assert.assertEquals(captor.getValue().getMessage(), "You've Won.\nGame Ended");
		Assert.assertEquals(playerHandler.isAlive(), false);


		//Capture message sent to other player
		ArgumentCaptor<GameState> secondCaptor = ArgumentCaptor.forClass(GameState.class);
		verify(gameBoardHandler).notifyOpponent(any(), secondCaptor.capture());
		Assert.assertTrue(secondCaptor.getValue().getMessage().contains("Game Ended\nPress any button to end session"));

	}

	/*
	 * Test that the appropriate message has been returned
	 * */
	@Test
	public void IllegalMove_test() throws IOException
	{

		String command = "10";
		duplicateMockSetup(command);
		when(gameBoardHandler.enterMove(playerHandler, command)).thenReturn(Action.ILLEGAL_MOVE);

		playerHandler.play();

		wrongMoveTest("Please enter an integer between 1 and 9");
	}

	/*
	 * Test that the appropriate message has been returned
	 * */
	@Test
	public void NotYourTurn_test() throws IOException
	{
		String command = "5";
		duplicateMockSetup(command);
		when(gameBoardHandler.enterMove(playerHandler, command)).thenReturn(Action.NOT_YOUR_TURN);

		playerHandler.play();

		wrongMoveTest("It is not your turn, please wait until notified");
	}

	/*
	 * Test that the appropriate message has been returned
	 * */
	@Test
	public void NotNumber_test() throws IOException
	{
		String command = "a";
		duplicateMockSetup(command);
		when(gameBoardHandler.enterMove(playerHandler, command)).thenReturn(Action.NOT_NUMBER);

		playerHandler.play();

		wrongMoveTest("Illegal character entered, Please enter a number between 1 and 9");
	}


	/*
	 * Test that the appropriate message has been sent when the first player has connected.
	 * */
	@Test
	public void player1SettingUp_test() throws IOException
	{
		String command = "name Here";
		duplicateMockSetup(command);
		playerHandler.setAlive(false); // do doesnt run play() method

		when(gameBoardHandler.isOpponentConnected()).thenReturn(Boolean.FALSE);
		doNothing().when(outputStream).close();

		playerHandler.run();


		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(outputStream, times(4)).writeObject(captor.capture());
		Assert.assertTrue(captor.getValue().contains("Opponent is not connected. Please Wait.."));

	}

	/*
	 * Test that the appropriate message has been sent when the second player has connected.
	 * */
	@Test
	public void player2SettingUp_test() throws IOException
	{
		String command = "name Here";
		duplicateMockSetup(command);
		playerHandler.setAlive(false); // do doesnt run play() method

		when(gameBoardHandler.isOpponentConnected()).thenReturn(Boolean.TRUE);
		doNothing().when(outputStream).close();

		playerHandler.run();

		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(outputStream, times(4)).writeObject(captor.capture());
		Assert.assertTrue(captor.getValue().contains("Game has begun. Please wait for opponent to make move."));

		ArgumentCaptor<GameState> secondCaptur = ArgumentCaptor.forClass(GameState.class);
		verify(gameBoardHandler).notifyOpponent(any(), secondCaptur.capture());
		Assert.assertEquals(secondCaptur.getValue().getCurrentAction(), Action.SUCCESS);
		Assert.assertTrue(secondCaptur.getValue().getMessage().contains("Other player has connected"));

	}


	private  void wrongMoveTest( String result) throws IOException
	{
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(outputStream).writeObject(captor.capture());
		Assert.assertEquals(captor.getValue(), result);
	}

	private void duplicateMockSetup(String command) throws IOException
	{
		inputStream = new Scanner(command);
		outputStream =  spy(ObjectOutputStream.class);
		playerHandler = new PlayerHandler(inputStream, outputStream, gameBoardHandler);
		playerHandler.setPlayerName("Peter");
		playerHandler.setToken('X');

		doNothing().when(outputStream).reset();
		when(gameBoardHandler.isOpponentConnected()).thenReturn(Boolean.TRUE);
		when(gameBoardHandler.getCurrentPlayer()).thenReturn(playerHandler);
	}



}
