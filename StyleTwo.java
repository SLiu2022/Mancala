import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Creates mancala board and visualizes it
 * @author TeamCat
 * @version 12/4/2020
 */
public class StyleTwo implements BoardStyle {
	private static Model model;
	private JButton[] pits;
	private JFrame frame;
	private static JTextArea score;
	private static JTextArea undosLeftRemain;
	
	/**
	 * Creates a new board
	 * @param stoneNum - number of stones
	 * @param undo - undo button
	 * @param pits - array of pits
	 * @param model - game model
	 */
	@Override
	public void makeBoard(int stoneNum, final JButton undo, JButton[] pits, Model model) {
		StyleTwo.model = model;
		this.pits = pits;
		Dimension pitDimension = new Dimension(105, 100);
		Dimension mancalaDimension = new Dimension(105, 200);
		//Set pit and Mancala dimensions
		for (int i = 0; i < 14; i++) {
			if (i != Model.PLAYER_A && i != Model.PLAYER_B) {
				pits[i].setPreferredSize(pitDimension);
				setIcons(i, stoneNum);
			} else {
				pits[i].setPreferredSize(mancalaDimension);
				setActive(pits[i]);
			}
		}

		//Populate pits
		JPanel playerBPits = new JPanel();
		playerBPits.setLayout(new GridLayout(1, 6));
		JPanel playerAPits = new JPanel();
		playerAPits.setLayout(new GridLayout(1, 6));
		for (int i = 12; i > Model.PLAYER_A; i--) {
			playerBPits.add(pits[i]);
		}
		for (int i = 0; i < Model.PLAYER_A; i++) {
			playerAPits.add(pits[i]);
		}

		//Undo panel contains undo button and undo counter
		JPanel undoPanel = new JPanel();
		undoPanel.setLayout(new FlowLayout());
		undoPanel.add(undo);
		undosLeftRemain = new JTextArea(1, 8);
		undosLeftRemain.setEditable(false);
		undoPanel.add(undosLeftRemain);
		
		//Label each pit
		JLabel labelBPits = new JLabel("         B6                B5                B4                B3                B2               B1");
		labelBPits.setFont(new Font("Times New Roman", Font.BOLD, 20));
		JLabel labelAPits = new JLabel("         A1                A2                A3                A4                A5               A6");
		labelAPits.setFont(new Font("Times New Roman", Font.BOLD, 20));
		
		//Add pits to one panel
		JPanel pitPanel = new JPanel();
		pitPanel.setLayout(new GridLayout(4, 1));
		pitPanel.add(labelBPits);
		pitPanel.add(playerBPits);
		pitPanel.add(playerAPits);
		pitPanel.add(labelAPits);

		//Player A Mancala
		JPanel playerAPanel = new JPanel();
		JLabel mancalaALabel = new JLabel(" A ");
		mancalaALabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
		playerAPanel.add(pits[Model.PLAYER_A]);
		playerAPanel.add(mancalaALabel);

		//Player B Mancala
		JPanel playerBPanel = new JPanel();
		JLabel mancalaBLabel = new JLabel(" B ");
		mancalaBLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
		playerBPanel.add(mancalaBLabel);
		playerBPanel.add(pits[Model.PLAYER_B]);
		
		//Combine 
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new FlowLayout());
		centerPanel.add(playerBPanel);
		centerPanel.add(pitPanel);
		centerPanel.add(playerAPanel);
		
		//Score panel displays current scores and end game message
		score = new JTextArea(4,10);
		score.setEditable(false);
	
		//Finalize frame
		frame = new JFrame("Mancala");
		frame.setLayout(new BorderLayout());
		frame.add(centerPanel, BorderLayout.CENTER);
		frame.add(undoPanel, BorderLayout.NORTH);
		frame.add(score, BorderLayout.SOUTH);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
	}

	/**
	 * When switching turns, set the color of the other player's row of pits to inactive
	 * @param pit - player pits
	 */
	@Override
	public void setInactive(JButton pit) {
		pit.setBackground(Color.gray);
	}
	
	/**
	 * When switching turns, set the color of the current player's row of pits to inactive
	 * @param pit - player pits
	 */
	@Override
	public void setActive(JButton pit) {
		pit.setBackground(Color.white);
	}

	/**
	 * Sets the icon for the stones
	 * @param pitIndex - stone pit
	 * @param stoneNum - number of stones should be in the pit
	 */
	@Override
	public void setIcons(int pitIndex, int stoneNum) {
		pits[pitIndex].setIcon(new IconTypeOne(stoneNum));
		pits[pitIndex].setDisabledIcon(new IconTypeOne(stoneNum));
	}

	/**
	 * Pack Frame
	 */
	@Override
	public void pack() {
		frame.pack();
	}

	/**
	 * Repaint Frame
	 */
	@Override
	public void repaint() {
		frame.repaint();
	}
	
	/**
	 * Gets current board scores
	 * @return combine - scores
	 */
	public static String getText() {
		String player_B = "";
		String player_A = "     ";
		
		for(int x = 0 ; x <= Model.PLAYER_A; x++){
			if(x == Model.PLAYER_A){
				player_A += "  ";
			}
			player_A += Model.getStoneAtIndex(x) + " ";
		}
		for(int x = 13; x > Model.PLAYER_A; x--){
			player_B += Model.getStoneAtIndex(x) + " ";
			if(x == Model.PLAYER_B) {
				player_B += "  ";
			}
		}
		String combine = player_B + "\n" + player_A;
		return combine;
	} 
	
	/**
	 * Undo counter
	 * @return undosLeft - undos left for this turn
	 */
	public static String undosLeft() {
		String undosLeft = "";
		int counter = model.getundoCounter();
		undosLeft = "Undos Left:  " + counter;
		return undosLeft;
	}
	
	/**
	 * Update score and undo counter
	 */
	public static void update() {
		score.setText(getText());
		undosLeftRemain.setText(undosLeft());
	}

	//Display results in score panel
	public static void endGame() {
		score.setText(model.getResults());
	}
}
