import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Visualizes the game
 * @author TeamCat
 * @version 12/04/2020
 */
public class View implements ChangeListener {
	private Model model;
	private BoardStyle style;
	private int[] pitIndex;
	private JButton[] pits;
	private JButton undo;

	/**
	 * Initialize instance variables
	 * @param model - game model
	 */
	public View(Model model) {
		this.model = model;
		pits = new JButton[14];
		undo = new JButton("Undo");
		undo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.undo();
				undo.setEnabled(false);
			}
		});
		undo.setEnabled(false);
	}

	/**
	 * Creates a style two game board
	 * Attach action listeners to pits
	 * @param stone - number of initial stones in each pit
	 */
	public void startGame(int stone) {
		model.setStoneNum(stone);
		pitIndex = model.getcurBoard();
		for (int i = 0; i < 14; i++) {
			JButton b = new JButton();
			final int position = i;
			b.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					model.move(position);
				}
			});
			pits[i] = b;
		}
		pits[Model.PLAYER_A].setEnabled(false);
		pits[Model.PLAYER_B].setEnabled(false);
		style = new StyleTwo();
		style.makeBoard(stone, undo, pits, model);
		exchangeTurn();
	}

	/**
	 * When switching turn, set the pits of the current player to active
	 * and the other player to inactive
	 */
	private void exchangeTurn() {
		if (model.getTurn() == 0) {
			for (int i = 0; i < Model.PLAYER_A; i++) {
				if (pitIndex[i] == 0) {
					pits[i].setEnabled(false);
					style.setInactive(pits[i]);
				} else {
					pits[i].setEnabled(true);
					style.setActive(pits[i]);
				}
			}
			for (int i = 7; i < Model.PLAYER_B; i++) {
				pits[i].setEnabled(false);
				style.setInactive(pits[i]);
			}
		} else {
			for (int i = 7; i < Model.PLAYER_B; i++) {
				if (pitIndex[i] == 0) {
					pits[i].setEnabled(false);
					style.setInactive(pits[i]);
				} else {
					pits[i].setEnabled(true);
					style.setActive(pits[i]);
				}
			}
			for (int i = 0; i < Model.PLAYER_A; i++) {
				pits[i].setEnabled(false);
				style.setInactive(pits[i]);
			}
		}
	}

	/**
	 * Update the view of the board after each move
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		StyleTwo.update();
		if (model.gameOver()) {
			StyleTwo.endGame();
		}
		pitIndex = model.getcurBoard();
		for (int i = 0; i < 14; i++) {
			style.setIcons(i, pitIndex[i]);
		}
		if (model.undoable()) {
			undo.setEnabled(true);
		}
		exchangeTurn();
		style.pack();
		style.repaint();
	}
} 
