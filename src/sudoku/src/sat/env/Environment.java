/**
 * Author: dnj
 * Date: Mar 5, 2008, 5:02:48 PM
 * 6.005 Elements of Software Construction
 * (c) 2008, MIT and Daniel Jackson
 */
package sudoku.src.sat.env;
import sudoku.src.immutable.ImListMap;
import sudoku.src.immutable.ImMap;

/**
 * An environment is an immutable mapping from variables to boolean values.
 * A special 3-valued Bool type is used to handle the case
 * in which a variable is to be evaluated that has no binding.
 * 
 * Typically, clients are expected to bind variables explicitly only
 * to Bool.TRUE and Bool.FALSE, and Bool.UNDEFINED is used only
 * to return a boolean value for an unbound variable. But this
 * implementation does not prevent a variable from being explicitly
 * bound to UNDEFINED.
 */
public class Environment {
    /*
     * Rep invariant
     *     bindings != null
     */
    private ImMap <Variable, Bool> bindings;

    private Environment(ImMap <Variable, Bool> bindings) {
        this.bindings = bindings;
    }

    public Environment() {
        this (new ImListMap<Variable, Bool> ());
    }

    /**
     * @return a new environment in which l has the value b
     * if a binding for l already exists, overwrites it
     */
    public Environment put(Variable v, Bool b) {
        return new Environment (bindings.put (v, b));
    }

    /**
     * @return a new environment in which l has the value Bool.TRUE
     * if a binding for l already exists, overwrites it
     */
    public Environment putTrue(Variable v) {
        return new Environment (bindings.put (v, Bool.TRUE));
    }

    /**
     * @return a new environment in which l has the value Bool.FALSE
     * if a binding for l already exists, overwrites it
     */
    public Environment putFalse(Variable v) {
        return new Environment (bindings.put (v, Bool.FALSE));
    }

    /**
     * @return the boolean value that l is bound to, or
     * the special UNDEFINED value of it is not bound
     */
    public Bool get(Variable v){
        Bool b = bindings.get(v);
        if (b==null) return Bool.UNDEFINED;
        else return b;
    }

    @Override
    public String toString () {
        return "Environment:" + bindings;
    }
}
