package jotto.jotto.ui;

import javax.swing.SwingUtilities;

import jotto.jotto.model.JottoModel;
import jotto.jotto.model.TableModel;

public class JottoGUI {

    public JottoGUI() {
        JottoModel model = new JottoModel();
        View view = new View();
        TableModel tableModel = new TableModel();
        Controller controller = new Controller(view, model, tableModel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { 
            new JottoGUI();
        });
    }
}