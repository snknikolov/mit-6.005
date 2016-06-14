package sudoku.src.sat.formula;

import static org.junit.Assert.*;
import org.junit.Test;

public class ClauseTest {
    // helpful values for test cases
    Clause empty = make();
    Literal p = PosLiteral.make("P");
    Literal q = PosLiteral.make("Q");
    Literal r = PosLiteral.make("R");
    Literal np = p.getNegation();
    Literal nq = q.getNegation();
    Literal nr = r.getNegation();
    Clause cp = make(p);
    Clause cq = make(q);
    Clause cr = make(r);
    Clause cnp = make(np);
    Clause cnq = make(nq);
    Clause cpq = make(p, q);
    Clause cpqr = make(p, q, r);
    Clause cpnq = make(p, nq);
        
    @Test
    public void testChooseLiteral() {
        Clause c = cpqr;
        while (!(c.isEmpty())) {
            Literal l = c.chooseLiteral();
            assertTrue(c.contains(l));
            c = c.reduce(l.getNegation());
        }
    }

    private Clause make(Literal... e) {
        Clause c = new Clause();
        for (int i = 0; i < e.length; ++i) {
            c = c.add(e[i]);
        }
        return c;
    }
}