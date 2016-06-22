package jotto.jotto.ui;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 * // TODO Write specifications for your JottoGUI class.
 */
public class JottoGUI extends JFrame {

	private JButton newPuzzleButton;
	private JTextField newPuzzleNumber;
	private JLabel puzzleNumber;
	private JLabel typeAGuess;
	private JTextField guess;
	private JTable guessTable;

	public JottoGUI() {
		newPuzzleButton = new JButton("New Puzzle");
		newPuzzleButton.setName("newPuzzleButton");
		newPuzzleNumber = new JTextField();
		newPuzzleNumber.setName("newPuzzleNumber");
		puzzleNumber = new JLabel("Puzzle #12345");
		puzzleNumber.setName("puzzleNumber");
		typeAGuess = new JLabel("Type a guess here:");
		typeAGuess.setName("typeAGuess");
		guess = new JTextField();
		guess.setName("guess");
		guessTable = new JTable(new DefaultTableModel(new Object[]{"Column1", "Column2", "Column 3"}, 30));
		guessTable.setName("guessTable");
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		

		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		        .addComponent(guessTable)
		        .addGroup(layout.createSequentialGroup()
		                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		                        .addComponent(puzzleNumber)
		                        .addComponent(typeAGuess))
		                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		                        .addGroup(layout.createSequentialGroup()
		                                .addComponent(newPuzzleButton)
		                                .addComponent(newPuzzleNumber))
		                        .addComponent(guess)))
		        );
		
		layout.setVerticalGroup(layout.createSequentialGroup()
		        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		                .addComponent(puzzleNumber)
		                .addComponent(newPuzzleButton)
		                .addComponent(newPuzzleNumber))
		        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		                .addComponent(typeAGuess)
		                .addComponent(guess))
		        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		                .addComponent(guessTable))
		        );
	}

	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JottoGUI main = new JottoGUI();
				
				main.setVisible(true);
			}
		});
	}
}
