import javax.swing.JOptionPane;

public class MancalaTester {
	public static void main(String[] args) {
		String[] stoness = {"3", "4"};
		int stones = Integer.parseInt((String)JOptionPane.showInputDialog(null, "Choose the initial stones", "stones selection", JOptionPane.QUESTION_MESSAGE, null, stoness, "3"));
		
		Model model = new Model();
		model.setStoneNum(stones);
		View initiate = new View(model);
		model.attach(initiate);
		initiate.startGame(stones);
	}
} 