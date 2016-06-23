package jotto.jotto.ui;

import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;

import jotto.jotto.model.TableModel;

@SuppressWarnings("serial")
public class View extends JFrame {
    
    private JButton newPuzzleButton;
    private JTextField newPuzzleNumber;
    private JLabel puzzleNumber;
    private JLabel typeAGuess;
    private JTextField guess;
    private JTable guessTable;
    
    private static final int DEFAULT_SIZE = 500;
        
    /**
     * Make a view.
     */
    public View() {
        initFields();
        initGroup();
        this.setSize(DEFAULT_SIZE, DEFAULT_SIZE);
        this.setVisible(true);
    }
    
    /**
     * Initialise all components.
     */
    private void initFields() {
        newPuzzleButton = new JButton("New Puzzle");
        newPuzzleButton.setName("newPuzzleButton");
        
        newPuzzleNumber = new JTextField();
        newPuzzleNumber.setName("newPuzzleNumber");
        puzzleNumber = new JLabel();
        puzzleNumber.setName("puzzleNumber");
        typeAGuess = new JLabel("Type a guess here:");
        typeAGuess.setName("typeAGuess");

        guess = new JTextField();
        guess.setName("guess");
        TableModel model = new TableModel();
        guessTable = new JTable(model);
        guessTable.setName("guessTable");
    }
    
    /**
     * Group the components in a single layout.
     */
    private void initGroup() {
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
    
    /**
     * Attach a listener to newPuzzleButton.
     * @param listener
     */
    public void addNewPuzzleButtonListener(ActionListener listener) {
        newPuzzleButton.addActionListener(listener);
    }
    
    /**
     * Attach a listener to guess text field.
     * @param listener
     */
    public void addGuessFieldListener(ActionListener listener) {
        guess.addActionListener(listener);
    }
    
    public String getGuess() {
        return guess.getText();
    }
    
    public void setGuessText(String text) {
        guess.setText(text);
    }
    
    public String getPuzzleNumber() {
        return newPuzzleNumber.getText();
    }

    // Set JTextField
    public void setPuzzleNumTextField(String s) {
        newPuzzleNumber.setText(s);
    }
       
    // Set JLabel
    public void setPuzzleNum(String puzzleNum) {
        puzzleNumber.setText(puzzleNum);
    }
    
    
    public void setTableModel(TableModel model) {
        this.guessTable.setModel(model);
    }
}