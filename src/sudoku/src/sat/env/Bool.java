/**
 * Author: dnj
 * Date: Feb 27, 2008
 * Time: 3:16:01 PM
 * (c) 2008, Daniel Jackson and MIT
 */
package sudoku.src.sat.env;

/**
 * Boolean type for use in evaluating formulas.
 * Undefined value is used to evaluate a variable that is not bound in an Environment.
 */
public enum Bool {
    TRUE, FALSE, UNDEFINED;
    
    public Bool and (Bool b) {
        if (this==FALSE || b==FALSE) return FALSE;
        if (this==TRUE && b==TRUE) return TRUE;
        return UNDEFINED;
    }

    public Bool or (Bool b) {
        if (this==FALSE && b==FALSE) return FALSE;
        if (this==TRUE || b==TRUE) return TRUE;
        return UNDEFINED;
    }

    public Bool not () {
        if (this==FALSE) return TRUE;
        if (this==TRUE) return FALSE;
        return UNDEFINED;
    }
}
