package ubc.cosc322;

import java.util.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;
import ygraph.ai.smartfox.games.amazons.HumanPlayer;
import java.util.Timer;
import java.util.TimerTask;



/**
 * An example illustrating how to implement a GamePlayer
 * @author Yong Gao (yong.gao@ubc.ca)
 * Jan 5, 2021
 *
 */
public class COSC322Test extends GamePlayer{

    private GameClient gameClient = null; 
    private BaseGameGUI gamegui = null;
	
    private String userName = "Ricky" ;
    private String passwd = "hello";
 
	
    /**
     * The main method
     * @param args for name and passwd (current, any string would work)
     */
    public static void main(String[] args) {				 
    	COSC322Test player = new COSC322Test(args[0], args[1]);
    	//HumanPlayer player = new HumanPlayer();
    	
    	if(player.getGameGUI() == null) {
    		player.Go();
    	}
    	else {
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
     * @param userName
      * @param passwd
     */
    public COSC322Test(String userName, String passwd) {
    	this.userName = userName;
    	this.passwd = passwd;
    	
    	//To make a GUI-based player, create an instance of BaseGameGUI
    	//and implement the method getGameGUI() accordingly
    	this.gamegui = new BaseGameGUI(this);
    }
 


    @Override
    public void onLogin() {
    	/*System.out.println("Congratualations!!! "
    			+ "I am called because the server indicated that the login is successfully");
    	System.out.println("The next step is to find a room and join it: "
    			+ "the gameClient instance created in my constructor knows how!"); 
    	List<Room> rooms = this.gameClient.getRoomList();
    	for (Room room: rooms) {
    		System.out.println(room);
    	}
    	this.gameClient.joinRoom(rooms.get(2).getName()); */
    	this.userName = gameClient.getUserName();
    	if(gamegui != null) {
    	gamegui.setRoomInformation(gameClient.getRoomList());
    	}
    }

    @Override
    public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
    	//This method will be called by the GameClient when it receives a game-related message
    	//from the server.
	
    	//For a detailed description of the message types and format, 
    	//see the method GamePlayer.handleGameMessage() in the game-client-api document. 
    	    	System.out.println(messageType);
    	    	System.out.println(msgDetails);
    	switch(messageType) {
    		case GameMessage.GAME_STATE_BOARD:
    			//System.out.println("case state board");
    			//System.out.println(msgDetails.toString());
    			//System.out.println(msgDetails.get("game-state"));
    			this.gamegui.setGameState((ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.GAME_STATE));
    	
    			break;
    		case GameMessage.GAME_ACTION_MOVE:
    			
    			HashMap<ArrayList<Integer>, ArrayList<Integer>> map = this.makeHashTable();
    			
    			
    			
    				this.gamegui.updateGameState(msgDetails);
    				//System.out.println("case action move");
    				//System.out.println(msgDetails.toString()); 
    				
    				//System.out.println("Sending a move");
    				
    				
    				int[][] boardInit = { { 0, 0, 0, 2, 0, 0, 2, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 2, 0, 0, 0, 0, 0, 0, 0, 0, 2 },
    						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    						{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 1, 0, 0, 0 } };
    			
    				
    				ArrayList<Integer> QueenPosCurEnemey = (ArrayList<Integer>) msgDetails
    						.get(AmazonsGameMessage.QUEEN_POS_CURR);
    				ArrayList<Integer> QueenPosNewEnemey = (ArrayList<Integer>) msgDetails
    						.get(AmazonsGameMessage.Queen_POS_NEXT);
    				ArrayList<Integer> ArrowPosEnemey = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);
    				
    				printMatrix(boardInit);
    				closestQueen(boardInit);
    					
    					
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
    				closestQueen(boardInit);

    				// Sending the positions
    				gameClient.sendMoveMessage(QueenPosCurSend, QueenPosNewSend, ArrowPosSend);

    				
    				
    				
    				this.gamegui.updateGameState(QueenPosCurSend, QueenPosNewSend, ArrowPosSend);    				t =30;
    				Timer timer = new Timer();
    				timer.schedule(new countDown(), 0, 5000);
    				break;
    				
    		case GameMessage.GAME_ACTION_START:
    			//System.out.println("case action start");
    		//	System.out.println(msgDetails.toString());
    			//System.out.println((ArrayList<Integer>)msgDetails.get(AmazonsGameMessage.GAME_STATE));
    			//System.out.println(AmazonsGameMessage.PLAYER_BLACK);
    			//System.out.println(AmazonsGameMessage.PLAYER_WHITE);
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
		return  this.gamegui;
	}

	@Override
	public void connect() {
		// TODO Auto-generated method stub
    	gameClient = new GameClient(userName, passwd, this);			
	}
	int t =30;
	class countDown extends TimerTask{
		public void run() {
			if(t>0) {
			System.out.println(t + " seconds left");
			t-=5;
			}
			
				
		}
		

		
	}
	
	
	public ArrayList<int[]> WhiteQueenLocations(int[][] board) {
		ArrayList<int[]> WhiteQueensLocations = new ArrayList<>(4);
		for(int i=0; i<board.length; i++) {
			for(int j =0; j<board[i].length; j++) {
				if(board[i][j] ==1 ) {
					for(int q =0; q<4; q++) {
						int[] value = new int[2];
						value[0] = i;
						value[1] =j;
						WhiteQueensLocations.add(value);
					}
					}
			}
		}
		return WhiteQueensLocations;
	}
	
	public ArrayList<int[]> BlackQueenLocations(int[][] board) {
		ArrayList<int[]> BlackQueensLocations = new ArrayList<>(4);
		for(int i=0; i<board.length; i++) {
			for(int j =0; j<board[i].length; j++) {
				if(board[i][j] ==2 ) {
					for(int q =0; q<4; q++) {
						int[] value = new int[2];
						value[0] = i;
						value[1] =j;
						BlackQueensLocations.add(value);
					}
					}
			}
		}
		return BlackQueensLocations;
	}
	
	public int[][] closestQueen(int[][] board) {
		int[] white, black;
		
		double dw = 1000;
		double db = 1000;
		int[][] owned = new int[10][10];
		
		
		ArrayList<int[]> WqueensLocations = WhiteQueenLocations(board);
		ArrayList<int[]> BqueensLocations = BlackQueenLocations(board);
		for(int i=0; i<board.length; i++) {
			for(int j =0; j<board[i].length; j++) {
				
				if(board[i][j] == 0) {
					white = WqueensLocations.get(0);
					dw = Math.sqrt(Math.pow(white[0]-i, 2) + Math.pow(white[1]-j, 2));
					black = BqueensLocations.get(0);
					db = Math.sqrt(Math.pow(black[0]-i, 2) + Math.pow(black[1]-j, 2));
				
				
					for (int q =1; q< 4; q++) {
						
						white = WqueensLocations.get(q);
						double z = Math.sqrt(Math.pow(white[0]-i, 2) + Math.pow(white[1]-j, 2));
						if(z<dw) {
							dw =z;
					
					}
				}
					for (int q =1; q< 4; q++) {
						
						black = BqueensLocations.get(q);
						double z = Math.sqrt(Math.pow(black[0]-i, 2) + Math.pow(black[1]-j, 2));
						if(z<db) {
							db =z;
						}
					}
				if(db<dw) {
					owned[i][j] = 2; // square at i,j is owned by black
				}
				else if(db>dw) {
					owned[i][j] = 1; // square at i,j is owned by white
				}
				else {
					owned[i][j] = 0; // square at i,j is neutral
				}
				}
				else if(board[i][j] == 3) {
					
					owned[i][j] =3;
				}
			}
		}
		printMatrix(owned);
		return owned;
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
}//end of class
