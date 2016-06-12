/**
 * Author: dnj
 * Date: Mar 5, 2008, 9:58:43 PM
 * 6.005 Elements of Software Construction
 * (c) 2008, MIT and Daniel Jackson
 */
package sudoku.src.sat.formula;
import sudoku.src.sat.env.Bool;
import sudoku.src.sat.env.Environment;
import sudoku.src.sat.env.Variable;

/**
 * Class representing negative literals.
 * Works with PosLiteral to ensure interning of literals.
 * NegLiteral objects are immutable.
 */
public class NegLiteral extends Literal {

    // should NOT be used by clients
    NegLiteral(String name) {
        super(name);
    }

    public static NegLiteral make (Variable var) {
        return make(var.getName());
    }
    
    public static NegLiteral make (String name) {
        Literal posLiteral = PosLiteral.make(name);
        return (NegLiteral) posLiteral.getNegation();
    }

    public Bool eval (Environment e) {
        return e.get(this.var).not();
    }

    public String toString() {
        return "~" + var;
    }

}
