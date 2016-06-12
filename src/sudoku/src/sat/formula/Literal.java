/**
 * Author: dnj
 * Date: Mar 5, 2008, 9:47:18 PM
 * 6.005 Elements of Software Construction
 * (c) 2008, MIT and Daniel Jackson
 */
package sudoku.src.sat.formula;

import sudoku.src.sat.env.Variable;

/**
 * A class to represent literals used in clausal form.
 * The main feature is a factory pattern that ensures
 * that literals are interned, so that they can be 
 * compared with == for efficiency.
 */
public abstract class Literal {
    
    protected Variable var;

    // not private, so it can be set in PosLiteral's factory method
    Literal negation;

    /* Rep invariant:
     *   this.negation.negation == this
     *   this.name != null (part of rep of superclass)
     *   this.negation.name.equals (this.name)
     * Invariant is established only when factory method in PosLiteral has completed,
     * so checkRep is called there rather than in constructor here.
     * 
     * Abstraction function:
     *    if this is an instance of PosLiteral, then represents the literal var 
     *    if this is an instance of NegLiteral, then represents the literal !var
     */

    void checkRep () {
        assert this.getNegation().getNegation() == this : "Variable, Rep invariant: negation of negation";
        assert this.getNegation().var.getName().equals(var.getName()) : "Variable, Rep invariant: names match";
    }

    Literal (String name) {
        this(new Variable(name));
    }
    
    Literal (Variable var) {
        this.var = var;
    }

    /**
     * @return the variable associated with this literal
     */
    public Variable getVariable () {
        return var;
    }

    /**
     * @return the literal that corresponds to the negation of this
     */
    public Literal getNegation () {
        return negation;
    }

    /**
     * @return true iff literal corresponds to the negation of this
     */
    public boolean negates (Literal literal) {
        return this.negation == literal;
    }

    // same as Object.equals, but must override bool.Variable.equals
    @Override
    public boolean equals (Object o) {
        return this == o;
    }
}