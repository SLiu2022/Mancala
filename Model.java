import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Model holds the mancala game data
 * @author TeamCat
 * @version 12/04/2020
 */
public class Model {
	private static int[] curBoard;
	private int[] prevcurBoard;
	private int turn;
	private int prevTurn;
	private int undoCounter;
	private int prevPlayer;
	private ArrayList<ChangeListener> listeners;
	public static final int PLAYER_A = 6;
	public static final int PLAYER_B = 13;
	public static final int UNDOS_PER_TURN = 3;

	/**
	 * Initialize instance variables
	 */
	public Model() {
		curBoard = new int[14];
		turn = 0;
		undoCounter = UNDOS_PER_TURN;
		prevPlayer = 0;
		listeners = new ArrayList<ChangeListener>();
	}

	/**
	 * Sets stones in each pit
	 * @param stones - user selected game size
	 */
	public void setStoneNum(int stones) {
		for (int i = 0; i < 14; i++) {
			if (i != PLAYER_A && i != PLAYER_B) {
				curBoard[i] = stones;
			}
		}
	}
	
	/**
	 * Gets the number of stones at the specific pit
	 * @param n - pit index
	 * @return number of stones in the pit
	 */
	public static int getStoneAtIndex(int n) {
		return curBoard[n];
	}

	/**
	 * Undo last move if undoable
	 */
	public void undo() {
		curBoard = prevcurBoard.clone();
		turn = prevTurn;
		prevPlayer = getTurn();
		undoCounter--;
		update();
	}

	/**
	 * Determine if the undo counter is greater that 0
	 * @return true if counter is greater 0, false if counter = 0
	 */
	public boolean undoable() {
		return undoCounter > 0;
	}
	
	/**
	 * Gets the undo counter
	 * @return undoCounter - number of undos left
	 */
	public int getundoCounter() {
		return undoCounter;
	}

	/**
	 * Get a copy for the current board for undo
	 * @return back up of the current board
	 */
	public int[] getcurBoard() {
		return curBoard.clone();
	}

	/**
	 * Player A = 0
	 * Player B = 1
	 * @return 0 or 1
	 */
	public int getTurn() {
		return turn % 2;
	}
	
	/**
	 * Attach listeners
	 * @param l - listeners
	 */
	public void attach(ChangeListener l) {
		listeners.add(l);
	}
	
	/**
	 * Update changes
	 */
	public void update() {
		for (ChangeListener l : listeners) {
			l.stateChanged(new ChangeEvent(this));
		}
	}
	
	/**
	 * Defines how the game should be played
	 * @param position - player selected pit index
	 */
	public void move(int position) {
		//Save previous turn for undo
		prevcurBoard = curBoard.clone();
		prevTurn = turn;
		//If new player takes turn
		//Reset undo counter
		if (getTurn() != prevPlayer) {
			undoCounter = UNDOS_PER_TURN;
		}
		//Number of stones in the selected pit
		int stonesInPit = curBoard[position];
		//Empty selected pit
		curBoard[position] = 0;
		//Distribute stones into pits
		for (int i = position + 1; stonesInPit > 0; i++, stonesInPit--) {
			//Player A
			if (getTurn() == 0) {
				if (i == PLAYER_B) {
					i = 0;
				}
				if (stonesInPit == 1) {
					if (i >= 0 && i < PLAYER_A) {
						if (curBoard[i] == 0 && curBoard[12 - i] != 0) {
							curBoard[PLAYER_A]++;
							curBoard[i] = -1;
							curBoard[PLAYER_A] += curBoard[12 - i];
							curBoard[12 - i] = 0;
						}
					}
					//Player gains extra turn if last stone lands in player mancala
					else if (i == PLAYER_A) {
						turn++;
					}
				}
			}
			//Player B
			else {
				if (i == PLAYER_A) {
					i++;
				} else if (i > PLAYER_B) {
					i = 0;
				}
				if (stonesInPit == 1) {
					if (i > PLAYER_A && i < PLAYER_B) {
						if (curBoard[i] == 0 && curBoard[12 - i] != 0) {
							curBoard[PLAYER_B]++;
							curBoard[i] = -1;
							curBoard[PLAYER_B] += curBoard[12 - i];
							curBoard[12 - i] = 0;
						}
					}
					//Player gains extra turn if last stone lands in player mancala
					else if (i == PLAYER_B) {
						turn++;
					}
				}
			} 
			curBoard[i]++;
		}
		turn++;
		update();
	}

	/**
	 * check if the game is over
	 * @return gameOver - whether one row of pits is empty
	 */
	public boolean gameOver() {
		boolean gameOver = false;
		int AScore = 0;
		int BScore = 0;
		//Check if stones > 0
		for (int i = 0; i < PLAYER_A; i++) {
			AScore += curBoard[i];
		}
		//Check if stones > 0
		for (int i = 7; i < PLAYER_B; i++) {
			BScore += curBoard[i];
		}
		//End the game if one of the rows is empty
		if (AScore == 0 || BScore == 0) {
			gameOver = true;
		}
		return gameOver;
	}

	/**
	 * Gets end game message
	 * @return winner - result message
	 */
	public String getResults() {
		String winner = "";
		//Calculate Player A score
		for (int i = 0; i < PLAYER_A; i++) {
			curBoard[PLAYER_A] += curBoard[i];
			curBoard[i] = 0;
		}
		//Calculate Player B score
		for (int i = 7; i < PLAYER_B; i++) {
			curBoard[PLAYER_B] += curBoard[i];
			curBoard[i] = 0;
		}
		//Compare scores
		if (curBoard[PLAYER_A] > curBoard[PLAYER_B]) {
			winner = "Player A Wins\n\n";
		} else if (curBoard[PLAYER_B] > curBoard[PLAYER_A]) {
			winner = "Player B Wins\n\n";
		}
		return winner + "Player A: " + curBoard[PLAYER_A] + "\nPlayer B: " + curBoard[PLAYER_B];
	}
}