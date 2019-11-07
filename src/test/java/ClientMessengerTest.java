import client.ClientMessenger;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.Action;
import util.GameState;
import java.io.*;
import static org.junit.Assert.*;



public class ClientMessengerTest
{
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;


	@Before
	public void setup(){

		System.setOut(new PrintStream(outContent));
	}


	/*
	* Tests that the system output contains the message ""You're a winner"
	* and does not print the board
	* */

	@Test
	public void ClientMessengerOutputTest() throws IOException, ClassNotFoundException
	{
		String message = "You're a winner";

		GameState gameState = new GameState(Action.WINNER, message);
		ByteArrayInputStream byteArraySteam = new ByteArrayInputStream(convertToByteArray(gameState));

		ObjectInputStream obj = new ObjectInputStream(byteArraySteam);

		ClientMessenger clientMessenger = new ClientMessenger(obj);
		clientMessenger.run();

		assertTrue(outContent.toString().contains(message));
		assertFalse(outContent.toString().contains("X"));
		assertFalse(outContent.toString().contains("O"));
		assertEquals(clientMessenger.isAlive(), false);

	}

	/*
	 * Tests that the system output contains the message ""You're a winner"
	 * and prints the board
	 * */

	@Test
	public void ClientMessengerOutputTest2() throws IOException
	{

		String[][] board = new String[][]{
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[X]", "[]", "[]", "[]", "[X]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[]", "[X]", "[X]", "[X]", "[X]", "[X]", "[]", "[]", "[]"},
				{ "[]", "[X]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},
				{ "[X]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"},

		};


		String message = "You're a winner";

		GameState gameState = new GameState(board, board.length ,Action.WINNER, message);
		ByteArrayInputStream byteArraySteam = new ByteArrayInputStream(convertToByteArray(gameState));

		ObjectInputStream obj = new ObjectInputStream(byteArraySteam);

		ClientMessenger clientMessenger = new ClientMessenger(obj);
		clientMessenger.run();

		int numberOfXs  = StringUtils.countMatches(outContent.toString(), "X");
		int numberOfOs = StringUtils.countMatches(outContent.toString(), "O");

		assertEquals(numberOfXs, 9);
		assertEquals(numberOfOs, 0);

		assertTrue(outContent.toString().contains(message));
		assertEquals(clientMessenger.isAlive(), false);
	}

	/*
	 * Tests the client closes safely when the input stream closes
	 * */

	@Test
	public void HandleNullInputStreamTest() throws IOException
	{
		String message = "illegal move";

		GameState gameState = new GameState(Action.ILLEGAL_MOVE, message);
		ByteArrayInputStream byteArraySteam = new ByteArrayInputStream(convertToByteArray(gameState));

		ObjectInputStream obj = new ObjectInputStream(byteArraySteam);

		ClientMessenger clientMessenger = new ClientMessenger(obj);
		clientMessenger.run();

		assertTrue(outContent.toString().contains(message));
		assertTrue(outContent.toString().contains("Connection Closed"));
		assertEquals(clientMessenger.isAlive(), false);

	}


	@After
	public void restoreStreams() {
		System.setOut(originalOut);
	}

	private byte[] convertToByteArray(GameState obj)
	{
		return SerializationUtils.serialize(obj);
	}
}
