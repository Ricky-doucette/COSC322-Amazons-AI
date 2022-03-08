package ubc.cosc322;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;
import ygraph.ai.smartfox.games.amazons.HumanPlayer;

/**
 * An example illustrating how to implement a GamePlayer
 * 
 * @author Yong Gao (yong.gao@ubc.ca) Jan 5, 2021
 *
 */
public class COSC322Test extends GamePlayer {

	private GameClient gameClient = null;
	private BaseGameGUI gamegui = null;

	private String userName = null;
	private String passwd = null;

	/**
	 * The main method
	 * 
	 * @param args for name and passwd (current, any string would work)
	 */
	public static void main(String[] args) {
	// COSC322Test player = new COSC322Test(args[0], args[1]);
	HumanPlayer player = new HumanPlayer();

		if (player.getGameGUI() == null) {
			player.Go();
		} else {
			BaseGameGUI.sys_setup();
			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run() {
					player.Go();
				}
			});
		}
	}

	/**
	 * Any name and passwd
	 * 
	 * @param userName
	 * @param passwd
	 */
	public COSC322Test(String userName, String passwd) {
		this.userName = userName;
		this.passwd = passwd;

		// To make a GUI-based player, create an instance of BaseGameGUI
		// and implement the method getGameGUI() accordingly
		this.gamegui = new BaseGameGUI(this);
	}

	@Override
	public void onLogin() {
		System.out.println(
				"Congratualations!!! " + "I am called because the server indicated that the login is successfully");
		System.out.println("The next step is to find a room and join it: "
				+ "the gameClient instance created in my constructor knows how!");
		/*
		 * List<Room> rooms = this.gameClient.getRoomList(); for (Room room : rooms) {
		 * System.out.println(room); } this.gameClient.joinRoom(rooms.get(2).getName());
		 */
		this.userName = gameClient.getUserName();
		if (gamegui != null) {
			gamegui.setRoomInformation(gameClient.getRoomList());
		}

	}

	@Override
	public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
		// This method will be called by the GameClient when it receives a game-related
		// message
		// from the server.

		// For a detailed description of the message types and format,
		// see the method GamePlayer.handleGameMessage() in the game-client-api
		// document.

		// System.out.println("Before the swith starts");
		// System.out.println(messageType);
		// System.out.println(msgDetails);
		switch (messageType) {
		case GameMessage.GAME_STATE_BOARD:
			System.out.println("case state board");
			System.out.println(msgDetails.toString());

			// System.out.println("Game_State_board");
			ArrayList<Integer> board = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.GAME_STATE);

			// System.out.println("This is the board I got:\n"+board.toString());

			this.gamegui.setGameState(board);
			break;

		case GameMessage.GAME_ACTION_MOVE:
			// Get map for coordinate conversions
			HashMap<ArrayList<Integer>, ArrayList<Integer>> map = this.makeHashTable();

			// Update before AI's move
			this.gamegui.updateGameState(msgDetails);

			System.out.println("Game_Action_Move");

			// In this hashmap is the details of the opponents move
			// System.out.println(msgDetails.toString());

			// Calculate possible states

			// Generate initial Game Board - 2's are black, 1's are whites, 3's are arrows
			int[][] boardInit = { { 0, 0, 0, 2, 0, 0, 2, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 2, 0, 0, 0, 0, 0, 0, 0, 0, 2 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 1, 0, 0, 0 } };

			// Read in sent position
			ArrayList<Integer> QueenPosCurEnemey = (ArrayList<Integer>) msgDetails
					.get(AmazonsGameMessage.QUEEN_POS_CURR);
			ArrayList<Integer> QueenPosNewEnemey = (ArrayList<Integer>) msgDetails
					.get(AmazonsGameMessage.Queen_POS_NEXT);
			ArrayList<Integer> ArrowPosEnemey = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);

			// Print the board before we make all the moves
			printMatrix(boardInit);

			// Update the board for the enemy's move
			// Get if the moving piece is white or black
			int WorB = boardInit[map.get(QueenPosCurEnemey).get(0)][map.get(QueenPosCurEnemey).get(1)];

			// replace old location of piece with 0
			boardInit[map.get(QueenPosCurEnemey).get(0)][map.get(QueenPosCurEnemey).get(1)] = 0;

			// if moving piece is white put 1 at coord else put 2
			boardInit[map.get(QueenPosNewEnemey).get(0)][map.get(QueenPosNewEnemey).get(1)] = (WorB == 1) ? 1 : 2;

			// Update arrow location
			boardInit[map.get(ArrowPosEnemey).get(0)][map.get(ArrowPosEnemey).get(1)] = 3;

			// Our positions to send

			ArrayList<Integer> QueenPosCurSend = new ArrayList<>();
			ArrayList<Integer> QueenPosNewSend = new ArrayList<>();
			ArrayList<Integer> ArrowPosSend = new ArrayList<>();

			QueenPosCurSend.add(4); // row [4,1]
			QueenPosCurSend.add(1); // col

			QueenPosNewSend.add(5);
			QueenPosNewSend.add(2); // [5,2]

			ArrowPosSend.add(5);
			ArrowPosSend.add(3);

			// Update the board for your moves
			// Get if the moving piece is white or black
			WorB = boardInit[map.get(QueenPosCurSend).get(0)][map.get(QueenPosCurSend).get(1)];

			// replace old location of piece with 0
			boardInit[map.get(QueenPosCurSend).get(0)][map.get(QueenPosCurSend).get(1)] = 0;

			// if moving piece is white put 1 at coord else put 2
			boardInit[map.get(QueenPosNewSend).get(0)][map.get(QueenPosNewSend).get(1)] = (WorB == 1) ? 1 : 2;

			// Update arrow location
			boardInit[map.get(ArrowPosSend).get(0)][map.get(ArrowPosSend).get(1)] = 3;

			/*boardInit[map.get(QueenPosCurSend).get(0)][map.get(QueenPosCurSend).get(1)] = 0;
			boardInit[map.get(QueenPosNewSend).get(0)][map.get(QueenPosNewSend).get(1)] = 2;
			boardInit[map.get(ArrowPosSend).get(0)][map.get(ArrowPosSend).get(1)] = 3;*/

			// print the board after all the moves have been made
			printMatrix(boardInit);

			// Sending the positions
			gameClient.sendMoveMessage(QueenPosCurSend, QueenPosNewSend, ArrowPosSend);

			// Update the game gui after our move
			this.gamegui.updateGameState(QueenPosCurSend, QueenPosNewSend, ArrowPosSend);
			break;

		case GameMessage.GAME_ACTION_START:
			System.out.println("case action start");
			// The board printed in this line is null
			// System.out.println((ArrayList<Integer>)msgDetails.get(AmazonsGameMessage.GAME_STATE));

			break;

		default:
			break;
		}
		return true;
	}

	@Override
	public String userName() {
		return userName;
	}

	@Override
	public GameClient getGameClient() {
		// TODO Auto-generated method stub
		return this.gameClient;
	}

	@Override
	public BaseGameGUI getGameGUI() {
		// TODO Auto-generated method stub
		return this.gamegui;
	}

	@Override
	public void connect() {
		// TODO Auto-generated method stub
		gameClient = new GameClient(userName, passwd, this);
	}

	public static ArrayList<Integer> convertCoords(ArrayList<Integer> coords) {
		// Convert ArrayList to 2d matrix coords

		return coords;

	}

	public static void printMatrix(int[][] matrix) {
		System.out.println("**********");
		int counter = 0;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				counter++;
				System.out.print(matrix[i][j]);
				if (counter == 10) {
					System.out.println();

					counter = 0;
				}

			}
		}
		System.out.println("**********");
	}

	public HashMap<ArrayList<Integer>, ArrayList<Integer>> makeHashTable() {
		HashMap<ArrayList<Integer>, ArrayList<Integer>> boardConversion = new HashMap<>();

		ArrayList<Integer> keys = new ArrayList<>();
		for (int row = 10; row > 0; row--) {
			for (int col = 1; col < 11; col++) {
				keys.add(row);
				keys.add(col);

			}
		}

		ArrayList<Integer> values = new ArrayList<>();
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				values.add(row);
				values.add(col);

			}
		}

		int counter = 0, keyIndex = -1, valueIndex = -1;
		boolean done = false;
		while (!done) {
			ArrayList<Integer> keyTemp = new ArrayList<>();
			ArrayList<Integer> valueTemp = new ArrayList<>();
			keyTemp.add(keys.get(++keyIndex));
			keyTemp.add(keys.get(++keyIndex));

			valueTemp.add(values.get(++valueIndex));
			valueTemp.add(values.get(++valueIndex));

			boardConversion.put(keyTemp, valueTemp);
			counter++;
			if (counter == 100) {
				done = true;
			}

		}
		return boardConversion;
	}

}// end of class