import javax.swing.JButton;

/**
 * Style interface for board style two
 * @author TeamCat
 * @version 12/04/2020
 */
public interface BoardStyle {
	
	/**
	 * Creates a new board
	 * @param stoneNum - number of stones
	 * @param undo - undo button
	 * @param pits - array of pits
	 * @param m - game model
	 */
	public void makeBoard(int stoneNum, final JButton undo, JButton[] pits, Model m);
	
	/**
	 * When switching turns, set the other player's row of pits to inactive
	 * @param pit - player pits
	 */
	public void setInactive(JButton pit);
	
	/**
	 * When switching turns, set the current player's row of pits to inactive
	 * @param pit - player pits
	 */
	public void setActive(JButton pit);
	
	/**
	 * Sets the icon for the stones
	 * @param pitIndex - stone pit
	 * @param stoneNum - number of stones should be in the pit
	 */
	public void setIcons(int pitIndex, int stoneNum);
	
	/**
	 * Override pack method
	 */
	public void pack();
	
	/**
	 * Override repaint method
	 */
	public void repaint();
} 