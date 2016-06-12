/**
 * Author: dnj
 * Date: Mar 5, 2008, 9:57:47 PM
 * 6.005 Elements of Software Construction
 * (c) 2008, MIT and Daniel Jackson
 */
package sudoku.src.sat.formula;

import sudoku.src.sat.env.Variable;
import sudoku.src.immutable.ImListMap;
import sudoku.src.immutable.ImMap;

/**
 * Class representing positive literals.
 * Works with NegLiteral to ensure interning of literals.
 * PosLiteral objects are immutable.
 */
public class PosLiteral extends Literal {
    /* 
     * Mapping of positive literals that have already been allocated, keyed on their names
     * Invariant: non null, and no key or value is null
     */
    static ImMap<String,PosLiteral> allocatedPosLiterals = new ImListMap<String,PosLiteral>();

    private PosLiteral (String name) {
        super (name);
    }    
    
    public static PosLiteral make (Variable var) {
        return make(var.getName());
    }
        
    /**
     * Factory method. Preserves the invariant that only one object
     * will exist to represent a literal of a given name. 
     * @return the positive literal with the given name
     */
    public static PosLiteral make (String name) {
        PosLiteral literal = allocatedPosLiterals.get(name);
        if (literal==null) {
            literal = new PosLiteral(name);
            NegLiteral negated = new NegLiteral(name);
            literal.negation = negated;
            negated.negation = literal;
            allocatedPosLiterals = allocatedPosLiterals.put(name, literal);
        }
        literal.checkRep(); 
        return literal;
    }

    public String toString () {
        return var.toString();
    }
}
