/**
 * Author: dnj
 * Date: Mar 5, 2008, 8:15:32 PM
 * 6.005 Elements of Software Construction
 * (c) 2008, MIT and Daniel Jackson
 */
package sudoku.src.sat.env;


/**
 * A Variable is a logical propositional variable
 * This datatype is immutable.
 */
public class Variable {
    /*
     * Rep invariant
     *     name != null
     */
    private final String name;

    public Variable (String name) {
        this.name = name;
    }

    public Bool eval (Environment e) {
        return e.get(this);
    }

    public String toString () {
        return name;
    }
    public String getName () {
        return name;
    }
    
    /**
     * @return true iff this and o represent the same literal
     * (that is, they have the same string name)
     */
    public boolean equals (Object o) {
        if (o == this) return true;
        if (!(o instanceof Variable)) return false;
        Variable v = (Variable) o;
        return v.name.equals(name); 
    }
}
