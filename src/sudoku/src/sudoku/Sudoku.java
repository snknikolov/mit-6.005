/**
 * Author: dnj, Hank Huang
 * Date: March 7, 2009
 * 6.005 Elements of Software Construction
 * (c) 2007-2009, MIT 6.005 Staff
 */
package sudoku.src.sudoku;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import sudoku.src.sat.env.Bool;
import sudoku.src.sat.env.Environment;
import sudoku.src.sat.env.Variable;
import sudoku.src.sat.formula.Clause;
import sudoku.src.sat.formula.Formula;
import sudoku.src.sat.formula.NegLiteral;
import sudoku.src.sat.formula.PosLiteral;

/**
 * Sudoku is an immutable abstract datatype representing instances of Sudoku.
 * Each object is a partially completed Sudoku puzzle.
 */
public class Sudoku {
    // The value of a missing sudoku square.
    private static final int NOT_PRESENT = -1;
    
    private final int dim;
    private final int size;
    
    // known values: square[i][j] represents the square in the ith row and jth
    // column,
    // contains -1 if the digit is not present, else i>=0 to represent the digit
    // i+1
    // (digits are indexed from 0 and not 1 so that we can take the number k
    // from square[i][j] and
    // use it to index into occupies[i][j][k])
    private final int[][] square;
    // occupies [i,j,k] means that kth symbol occupies entry in row i, column j
    private final Variable[][][] occupies;

    // Rep invariant:
    //      square != null
    //      dim > 0
    //      size == dim^2
    //      0 <= square[i][j] <= size
    private void checkRep() {
        assert this.square != null;
        assert this.dim > 0;
        assert this.size == (int) Math.pow(dim, 2);
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                assert square[i][j] >= NOT_PRESENT;
            }
        }
    }

    /**
     * create an empty Sudoku puzzle of dimension dim.
     * 
     * @param dim
     *            size of one block of the puzzle. For example, new Sudoku(3)
     *            makes a standard Sudoku puzzle with a 9x9 grid.
     */
    public Sudoku(int dim) {
        this(dim, new int[dim*dim][dim*dim]);
    }

    /**
     * create Sudoku puzzle
     * 
     * @param square
     *            digits or blanks of the Sudoku grid. square[i][j] represents
     *            the square in the ith row and jth column, contains 0 for a
     *            blank, else i to represent the digit i. So { { 0, 0, 0, 1 }, {
     *            2, 3, 0, 4 }, { 0, 0, 0, 3 }, { 4, 1, 0, 2 } } represents the
     *            dimension-2 Sudoku grid: 
     *            
     *            ...1 
     *            23.4 
     *            ...3
     *            41.2
     * 
     * @param dim
     *            dimension of puzzle Requires that dim*dim == square.length ==
     *            square[i].length for 0<=i<dim.
     */
    public Sudoku(int dim, int[][] square) {
        assert dim > 0;
        assert dim * dim == square.length;
        
        this.dim = dim;
        this.size = dim * dim;
        this.square = new int[size][size];
        this.occupies = new Variable[size][size][size];
        
        for (int row = 0; row < size; row++) {
            assert square[row].length == dim * dim;
            for (int col = 0; col < size; col++) {
                this.square[row][col] = square[row][col] - 1;
                for (int k = 0; k < size; k++) 
                    this.occupies[row][col][k] = new Variable(String.format("%d%d%d", row, col, k));
            }
        }
        checkRep();
    }

    /**
     * Reads in a file containing a Sudoku puzzle.
     * 
     * @param dim
     *            Dimension of puzzle. Requires: at most dim of 3, because
     *            otherwise need different file format
     * @param filename
     *            of file containing puzzle. The file should contain one line
     *            per row, with each square in the row represented by a digit,
     *            if known, and a period otherwise. With dimension dim, the file
     *            should contain dim*dim rows, and each row should contain
     *            dim*dim characters.
     * @return Sudoku object corresponding to file contents
     * @throws IOException
     *             if file reading encounters an error
     * @throws ParseException
     *             if file has error in its format
     */
    public static Sudoku fromFile(int dim, String filename) throws IOException,
            ParseException {
        assert dim > 0;
        Sudoku sud = new Sudoku(dim);
        
        BufferedReader br = new BufferedReader(new FileReader(filename));
        try {
            String line = br.readLine();
            int row = 0;
            
            while (line != null) {
                assert line.length() == sud.size : "Number of columns in file inconsistent with size";
                
                for (int col = 0; col < line.length(); col++) {
                    if (line.charAt(col) == '.')
                        sud.square[row][col] = NOT_PRESENT;
                    else {
                        try {
                            int colValue = Integer.parseInt(line.substring(col, col+1));                            
                            sud.square[row][col] = colValue - 1;
                        } catch (NumberFormatException nfe) {
                            throw new ParseException("Expected integer, found " + line.substring(col,col+1));
                        }
                    }
                }
                line = br.readLine();
                ++row;
            }
            assert row == sud.size : "Number of rows in file inconsistent with size.";
            
        } finally {
            br.close();
        }
        return sud;
    }

    /**
     * Exception used for signaling grammatical errors in Sudoku puzzle files
     */
    @SuppressWarnings("serial")
    public static class ParseException extends Exception {
        public ParseException(String msg) {
            super(msg);
        }
    }

    /**
     * Produce readable string representation of this Sukoku grid, e.g. for a 4
     * x 4 sudoku problem: 
     *   12.4 
     *   3412 
     *   2.43 
     *   4321
     * 
     * @return a string corresponding to this grid
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int current = square[row][col];
                if (current == NOT_PRESENT)
                    sb.append('.');
                else
                    sb.append(current + 1);
                if ((col+1) % dim == 0)
                    sb.append("|");
            }
            if ((row+1) % dim == 0) {
                sb.append("\n");
                for (int col = 0; col < size; col++)
                    sb.append("-"); 
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * @return a SAT problem corresponding to the puzzle, using variables with
     *         names of the form occupies(i,j,k) to indicate that the kth symbol
     *         occupies the entry in row i, column j
     */
    public Formula getProblem() {
        Formula initial = new Formula();
        
        // Only one digit per square.
        // A digit can appear exactly once per row and exactly once per column.
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Clause atLeastOncePerRow = new Clause();
                Clause atLeastOncePerCol = new Clause();

                if (square[i][j] != NOT_PRESENT)
                    initial = initial.and(new Formula(occupies[i][j][square[i][j]]));
                
                for (int k = 0; k < size; k++) {
                    Clause oneDigitPerSquare = new Clause(NegLiteral.make(occupies[i][j][k]));
                    Clause atMostOncePerRow = new Clause(NegLiteral.make(occupies[i][k][j]));
                    Clause atMostOncePerCol = new Clause(NegLiteral.make(occupies[k][i][j]));
                    atLeastOncePerRow = atLeastOncePerRow.add(PosLiteral.make(occupies[i][k][j]));
                    atLeastOncePerCol = atLeastOncePerCol.add(PosLiteral.make(occupies[k][i][j]));
                    
                    for (int prime = k+1; prime < size; prime++) {
                        initial = initial.addClause(oneDigitPerSquare.add(NegLiteral.make(occupies[i][j][prime])));
                        initial = initial.addClause(atMostOncePerRow.add(NegLiteral.make(occupies[i][prime][j])));
                        initial = initial.addClause(atMostOncePerCol.add(NegLiteral.make(occupies[prime][i][j])));
                    }
                }
                initial = initial.addClause(atLeastOncePerRow);
                initial = initial.addClause(atLeastOncePerCol);
            }            
        }
                
        return initial;
    }

    /**
     * Interpret the solved SAT problem as a filled-in grid.
     * 
     * @param e
     *            Assignment of variables to values that solves this puzzle.
     *            Requires that e came from a solution to this.getProblem().
     * @return a new Sudoku grid containing the solution to the puzzle, with no
     *         blank entries.
     */
    public Sudoku interpretSolution(Environment e) {
        Sudoku sud = new Sudoku(dim);

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                for (int k = 0; k < size; k++)
                    if (this.occupies[i][j][k].eval(e) == Bool.TRUE)
                        sud.square[i][j] = k;
        
        return sud;
    }    
}