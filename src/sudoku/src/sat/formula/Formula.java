/**
 * Author: dnj, Hank Huang
 * Date: March 7, 2009
 * 6.005 Elements of Software Construction
 * (c) 2007-2009, MIT 6.005 Staff
 */
package sudoku.src.sat.formula;

import sudoku.src.immutable.EmptyImList;
import sudoku.src.immutable.ImList;
import sudoku.src.immutable.NonEmptyImList;

import java.util.Iterator;

import sudoku.src.sat.env.Variable;

/**
 * Formula represents an immutable boolean formula in
 * conjunctive normal form, intended to be solved by a
 * SAT solver.
 */
public class Formula {
    private final ImList<Clause> clauses;
    // Rep invariant:
    //      clauses != null
    //      clauses contains no null elements (ensured by spec of ImList)
    //
    // Note: although a formula is intended to be a set,  
    // the list may include duplicate clauses without any problems. 
    // The cost of ensuring that the list has no duplicates is not worth paying.
    //
    //    
    //    Abstraction function:
    //        The list of clauses c1,c2,...,cn represents 
    //        the boolean formula (c1 and c2 and ... and cn)
    //        
    //        For example, if the list contains the two clauses (a,b) and (!c,d), then the
    //        corresponding formula is (a or b) and (!c or d).
    //
    // Datatype definition:
    //      Formula = ImList<Clause> a list of clauses ANDed together
    //      Clause = ImList<Literal> a list of literals ORed together
    //      Literal = PosLiteral(v: Variable) + NegLiteral(v: Variable) either a variable P or its negation ~P
    //      Variable = String

    void checkRep() {
        assert this.clauses != null : "SATProblem, Rep invariant: clauses non-null";
    }

    /**
     * Create a new problem for solving that contains no clauses (that is the
     * vacuously true problem)
     * 
     * @return the true problem
     */
    public Formula() {
        this.clauses = new EmptyImList<Clause>();
    }

    /**
     * Create a new problem for solving that contains a single clause with a
     * single literal
     * 
     * @return the problem with a single clause containing the literal l
     */
    public Formula(Variable l) {
        Literal literal = PosLiteral.make(l);
        Clause c = new Clause(literal);
        ImList<Clause> cl = new NonEmptyImList<Clause>(c);
        this.clauses = cl;
    }

    /**
     * Create a new problem for solving that contains a single clause
     * 
     * @return the problem with a single clause c
     */
    public Formula(Clause c) {
        ImList<Clause> clauses = new NonEmptyImList<Clause>(c);
        this.clauses = clauses;
    }
    
    /**
     * Create a new problem for solving that contains a list of clauses.
     * 
     * @param clauses The list of clauses.
     */
    private Formula(ImList<Clause> clauses) {
        this.clauses = clauses;
    }

    /**
     * Add a clause to this problem
     * 
     * @return a new problem with the clauses of this, but c added
     */
    public Formula addClause(Clause c) {
        ImList<Clause> consClause = this.clauses.add(c);
        return new Formula(consClause);
    }

    /**
     * Get the clauses of the formula.
     * 
     * @return list of clauses
     */
    public ImList<Clause> getClauses() {
        return clauses;
    }

    /**
     * Iterator over clauses
     * 
     * @return an iterator that yields each clause of this in some arbitrary
     *         order
     */
    public Iterator<Clause> iterator() {
        return clauses.iterator();
    }

    /**
     * @return a new problem corresponding to the conjunction of this and p
     */
    public Formula and(Formula p) {
        ImList<Clause> fullClauseList = this.clauses;
        for (Clause c : p.getClauses())
            fullClauseList = fullClauseList.add(c);
        
        return new Formula(fullClauseList);
    }

    /**
     * @return a new problem corresponding to the disjunction of this and p
     */
    public Formula or(Formula other) {
        // (a & b) .or (c & d) == (a | c) & (a | d) & (b | c) & (b | d)
        if (other.clauses.size() == 0)
            return this;
        if (this.clauses.size() == 0)
            return other;
        
        ImList<Clause> fullClauseList = new EmptyImList<Clause>();
        
        // Extract each literal from each clause of this and combine with every
        // literal from every clause of other.
        for (Clause thisClause : this.clauses) {
            for (Iterator<Literal> thisIter = thisClause.iterator(); thisIter.hasNext();) {
                Literal thisLiteral = thisIter.next();
                Clause initialClause = new Clause(thisLiteral);
            
                for (Clause otherClause : other.getClauses()) {
                    for (Iterator<Literal> otherIter = otherClause.iterator(); otherIter.hasNext();) {
                        Literal thatLiteral = otherIter.next();
                        Clause pairClause = initialClause.add(thatLiteral);
                        fullClauseList = fullClauseList.add(pairClause);
                    }
                }
            }
        }
        return new Formula(fullClauseList);        
    }

    /**
     * @return a new problem corresponding to the negation of this
     */
    public Formula not() {
        // Hint: you'll need to apply DeMorgan's Laws (http://en.wikipedia.org/wiki/De_Morgan's_laws)
        // to move the negation down to the literals, and the distributive law to preserve 
        // conjunctive normal form, i.e.:
        //   if you start with (a | b) & c,
        //   you'll need to make !((a | b) & c) 
        //                       => (!a & !b) | !c            (moving negation down to the literals)
        //                       => (!a | !c) & (!b | !c)    (conjunctive normal form)
                
        Formula result = new Formula();
        for (Clause c: clauses){
            Clause neg = new Clause();
            for(Literal l: c){
                neg = neg.add(l.getNegation());
            }
            
            result = result.or(new Formula(neg));
        }
        return result;
    }
    
    /**
     * 
     * @return number of clauses in this
     */
    public int getSize() {
        return clauses.size();
    }

    /**
     * @return string representation of this formula
     */
    public String toString() {
        String result = "Problem[";
        for (Clause c : clauses)
            result += "\n" + c;
        return result + "]";
    }
}