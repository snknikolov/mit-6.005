package sudoku.src.sudoku;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;


public class SudokuTest {
    

    // make sure assertions are turned on!  
    // we don't want to run test cases without assertions too.
    // see the handout to find out how to turn them on.
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false;
    }

    @Test(expected=IllegalArgumentException.class)
    public void emptyBoardNegativeDim() {
        int dim = -1;
        Sudoku test = new Sudoku(dim);
    }
    
    @Test
    public void readerTest() {
        String fileName = "src/sudoku/samples/sudoku_easy.txt";
        int dim = 3;
        try {
            Sudoku sudoku = Sudoku.fromFile(dim, fileName);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test(expected=Sudoku.ParseException.class)
    public void readerIllegalDimension() 
            throws IOException, Sudoku.ParseException {
        String fileName = "src/sudoku/samples/sudoku_easy.txt";
        int dim = 4;
        
        Sudoku sudoku = Sudoku.fromFile(dim, fileName);
    }
    
    @Test(expected=AssertionError.class)
    public void readerIllegalFormat() 
            throws IOException, Sudoku.ParseException {
        String fileName = "src/sudoku/samples/sudoku_badformat.txt";
        int dim = 3;
        
        Sudoku sudoku = Sudoku.fromFile(dim, fileName);
    }
    
    @Test(expected=Sudoku.ParseException.class) 
    public void readerNonNumericalValue() 
            throws IOException, Sudoku.ParseException {
        String fileName = "src/sudoku/samples/sudoku_badchar.txt";
        int dim = 3;
        
        Sudoku sudoku = Sudoku.fromFile(dim, fileName);
    }
}