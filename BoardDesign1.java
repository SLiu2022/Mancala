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

public class BoardDesign1 implements BoardStyle {
	private static Model model;
	private JButton[] pits;
	private JFrame frame;
	private static JTextArea score;
	private static JTextArea undosLeftRemain;
	
	
	@Override
	public void makeBoard(int gameSize, final JButton undo, JButton[] pits, Model model) {
		BoardDesign1.model = model;
		this.pits = pits;
		Dimension pitDimension = new Dimension(105, 100);
		Dimension mancalaDimension = new Dimension(105, 200);
		//Set pit and Mancala dimensions
		for (int i = 0; i < 14; i++) {
			if (i != Model.PLAYER_A && i != Model.PLAYER_B) {
				pits[i].setPreferredSize(pitDimension);
				setIcons(i, gameSize);
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
		
		//Add pits to one panel
		JPanel pitPanel = new JPanel();
		pitPanel.setLayout(new GridLayout(2, 1));
		pitPanel.add(playerBPits);
		pitPanel.add(playerAPits);

		//Player A Mancala
		JPanel playerAPanel = new JPanel();
		JLabel aText = new JLabel(" A ");
		aText.setFont(new Font("Times New Roman", Font.BOLD, 30));
		playerAPanel.add(pits[Model.PLAYER_A]);
		playerAPanel.add(aText);

		//Player B Mancala
		JPanel playerBPanel = new JPanel();
		JLabel bText = new JLabel(" B ");
		bText.setFont(new Font("Times New Roman", Font.BOLD, 30));
		playerBPanel.add(bText);
		playerBPanel.add(pits[Model.PLAYER_B]);
		
		score = new JTextArea(2,10);
		score.setEditable(false);

		
		//Finalize frame
		frame = new JFrame("Mancala");
		frame.setLayout(new BorderLayout());
		frame.add(playerBPanel, BorderLayout.WEST);
		frame.add(pitPanel, BorderLayout.CENTER);
		frame.add(playerAPanel, BorderLayout.EAST);
		frame.add(undoPanel, BorderLayout.NORTH);
		frame.add(score, BorderLayout.SOUTH);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
	}

	/**
	 * Set color to inactive
	 */
	@Override
	public void setInactive(JButton house) {
		house.setBackground(Color.gray);
	}
	
	/**
	 * Set color to active
	 */
	@Override
	public void setActive(JButton house) {
		house.setBackground(Color.white);
	}

	/**
	 * Set icons for stones
	 */
	@Override
	public void setIcons(int pitIndex, int stones) {
		pits[pitIndex].setIcon(new IconTypeOne(stones));
		pits[pitIndex].setDisabledIcon(new IconTypeOne(stones));
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
}
