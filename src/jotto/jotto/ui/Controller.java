package jotto.jotto.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import jotto.jotto.model.JottoModel;
import jotto.jotto.model.TableModel;

public class Controller {
    private JottoModel model;
    private TableModel tableModel;
    private View view;
    
    public Controller(View view, JottoModel model, TableModel tableModel) {
        this.view = view;
        this.model = model;
        this.tableModel = tableModel;
        
        this.view.addNewPuzzleButtonListener(new NewPuzzleButtonListener());
        this.view.addGuessFieldListener(new GuessFieldListener());
        
        // Initialise with some default values.
        String puzzleNumber = "16952";
        model.setPuzzleNumber(puzzleNumber);
        tableModel.resetTableModel();
        view.setPuzzleNum("Puzzle #" + model.getPuzzleNum());
    }
    
    /**
     * Action listener for view's new puzzle button.
     */
    class NewPuzzleButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
             String puzzleNumber = view.getPuzzleNumber();
             
             model.setPuzzleNumber(puzzleNumber);
             tableModel.resetTableModel();
             
             view.setPuzzleNum("Puzzle #" + model.getPuzzleNum());
             view.setPuzzleNumTextField("");
             view.setGuessText("");
        }
    }
    
    /**
     * Action listener for view's guess field.
     */
    class GuessFieldListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String guess = view.getGuess();
            String response = model.makeGuess(guess);
            String shortResponse = model.guessedString(guess, response);
            tableModel.add(guess, response);
            view.setGuessText(shortResponse);
            view.setTableModel(tableModel);
        }
    }
}