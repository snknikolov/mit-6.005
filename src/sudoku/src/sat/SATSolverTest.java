package sudoku.src.sat;

import static org.junit.Assert.*;

import org.junit.Test;

import sudoku.src.sat.formula.Clause;
import sudoku.src.sat.formula.Formula;
import sudoku.src.sat.formula.Literal;
import sudoku.src.sat.formula.PosLiteral;
import sudoku.src.sat.env.Bool;
import sudoku.src.sat.env.Environment;

public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();


    // (a v ~b) and (a v b) should return a: True, b: anything
    @Test
    public void testSimple() {
        Clause first = make(a, nb);
        Clause second = make(a, b);
        Formula f = make(first, second);
        
        Environment env = SATSolver.solve(f);
        assertEquals(Bool.TRUE, env.get(a.getVariable()));
    }
    
    // (a and b) and (a and ~b) should return null
    @Test
    public void testNoSolution() {
        Clause aClause = make(a);
        Clause bClause = make(b);
        Clause notbClause = make(nb);
        
        Formula fOne = make(aClause);
        Formula fTwo = make(bClause);
        Formula fThree = make(notbClause);
        Formula completeFormula = (fOne.and(fTwo)).and((fOne.and(fThree)));
        
        Environment env = SATSolver.solve(completeFormula);
        assertEquals(null, env);
    }
    
    // (a and b) and (~b v c) should return a: True, b: True, c: True
    @Test
    public void testOneSolution() {
        Clause aClause = make(a);
        Clause bClause = make(b);
        Clause secondClause = make(nb, c);
        
        Formula fOne = make(aClause);
        Formula fTwo = make(bClause);
        Formula completeFirst = fOne.and(fTwo);
        Formula completeFormula = completeFirst.and(make(secondClause));
        
        Environment env = SATSolver.solve(completeFormula);
        
        assertEquals(Bool.TRUE, env.get(a.getVariable()));
        assertEquals(Bool.TRUE, env.get(b.getVariable()));
        assertEquals(Bool.TRUE, env.get(c.getVariable()));
    }
    
    // a implies b
    // ~a v b
    @Test
    public void testImplies() {
        Formula fa = make(make(a));
        Formula fb = make(make(b));
        Formula complete = (fa.not()).or(fb);
        
        Environment env = SATSolver.solve(complete);
        assertTrue(env.get(a.getVariable()) == Bool.TRUE || env.get(b.getVariable()) == Bool.TRUE);
    }
        
    private Clause make(Literal... e) {
        Clause c = new Clause();
        for (int i = 0; i < e.length; ++i) {
            c = c.add(e[i]);
        }
        return c;
    }
    
    private Formula make(Clause... e) {
        Formula f = new Formula();
        for (Clause c : e) {
            f = f.addClause(c);
        }
        return f;
    }
}