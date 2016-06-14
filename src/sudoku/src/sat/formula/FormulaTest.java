package sudoku.src.sat.formula;

import static org.junit.Assert.*;
import org.junit.Test;

import sudoku.src.immutable.EmptyImList;
import sudoku.src.immutable.ImList;
import sudoku.src.sat.SATSolver;
import sudoku.src.sat.env.Bool;
import sudoku.src.sat.env.Environment;

public class FormulaTest {    
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal d = PosLiteral.make("d");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();

    
    @Test
    public void noClausesFormula() {
        Formula f = new Formula();
        ImList<Clause> clauses = f.getClauses();
        ImList<Clause> expected = new EmptyImList<>();
        assertEquals(expected, clauses);
    }
    
    @Test
    public void singleClauseFormula() {
        Clause c = make(a);
        Formula f = new Formula(c);
        
        for (Clause cl : f.getClauses())
            assertEquals(c, cl);
    }
    
    @Test
    public void testAnd() {
        Formula p = make(make(a), make(b));
        Formula q = make(make(nc), make(d));
        Formula expected = make(make(a), make(b), make(d), make(nc));
        Formula test = p.and(q);
        
        assertEquals(expected.getClauses(), test.getClauses());
    }
    
    //  (a & b) .or (c & d) == (a | c) & (a | d) & (b | c) & (b | d)      
    @Test
    public void testOr() {
        // (a & b) . or (c & d)
        Clause ab = make(a, b);
        Clause cd = make(c, d);
        
        Formula first = make(ab);
        Formula second = make(cd);        
        Formula test = first.or(second);
        
        // Construct expected with clauses ac, ad, bc, bd
        Clause ac = make(a,c);
        Clause ad = make(a,d);
        Clause bc = make(b,c);
        Clause bd = make(b,d);

        Formula expected = make(bd, bc, ad, ac);
        
        assertEquals(expected.getClauses(), test.getClauses());
    }
    
    //   if you start with (a | b) & c,
    //   you'll need to make !((a | b) & c) 
    //                       => (!a & !b) | !c            (moving negation down to the literals)
    //                       => (!a | !c) & (!b | !c)    (conjunctive normal form)
    @Test
    public void testNot() {
        Clause abClause = make(a, b);
        Clause cClause = make(c);
        Formula test = make(abClause, cClause);
        test = test.not();
        
        Clause notANotC = make(nc, na);
        Clause notBNotC = make(nc, nb);
        Formula expected = make(notANotC, notBNotC);
        
        assertEquals(expected.getClauses(), test.getClauses());
    }
    
    // a implies b
    // ~a v b
    @Test
    public void testImplies() {
        Formula fa = make(make(a));
        Formula fb = make(make(b));
        Formula complete = (fa.not()).or(fb);
        
        Environment env = SATSolver.solve(complete);
        assertEquals(Bool.TRUE, env.get(b.getVariable()));
    }

    private Clause make(Literal... e) {
        Clause c = new Clause();
        for (int i = 0; i < e.length; ++i) {
            c = c.add(e[i]);
        }
        return c;
    }
    
    private Formula make(Clause... c) {
        Formula f = new Formula();
        for (Clause cl : c) {
            f = f.addClause(cl);
        }
        return f;
    }
}