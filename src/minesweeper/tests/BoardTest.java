package minesweeper.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import minesweeper.Board;
import minesweeper.Square;
import minesweeper.State;

public class BoardTest {
    private static final String[] args = new String[] { "10" };
    private static final int SIZE = 10;
        
    @Test
    public void untouchedBoard() {
        Board board = Board.getBoard(args);        

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Square s = board.getSquare(i, j);
                assertEquals(State.UNTOUCHED, s.getState());
            }
        }
    }
    
    @Test
    public void dugBoard() {
        Board board = Board.getBoard(args);        
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                board.dig(i, j);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Square s = board.getSquare(i, j);
                assertEquals(State.DUG, s.getState());
            }
        }
    }
    
    @Test
    public void flagBoard() {
        Board board = Board.getBoard(args);        
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                board.flag(i, j);
        

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Square s = board.getSquare(i, j);
                assertEquals(State.FLAGGED, s.getState());
            }
        }
    }
    
    @Test
    public void testBoard() {
        Board board = Board.getBoard(args);        
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                board.dig(i, j);
        
        String[] boardString = board.toString().split("\\n");
        
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Square square = board.getSquare(i, j);
                String boardRepr = boardString[i].substring(j, j+1);
                
                if (square.isBomb()) {
                    // Bomb squares should be marked as "*"
                    assertEquals("*", boardRepr);
                } else if (square.getNumBombNeighbours() == 0) {
                    // Squares with no neighbours should be marked " ".
                    assertEquals(" ", boardRepr);
                } else {
                    // Squares with bomb neigbours should display the number of bomb neighbours.
                    assertEquals(String.valueOf(square.getNumBombNeighbours()),
                                    boardRepr);
                }
            }
        }
    }
    
    @Test
    public void digFlaggedNotAllowed() {
        Board board = Board.getBoard(args);
        board.flag(0, 0);
        board.dig(0, 0);
        
        assertEquals(State.FLAGGED, board.getSquare(0, 0).getState());
    }
    
    @Test
    public void flagDugNotAllowed() {
        Board board = Board.getBoard(args);
        board.dig(0, 0);
        board.flag(0, 0);

        assertEquals(State.DUG, board.getSquare(0, 0).getState());
    }
    
    @Test
    public void deflagDugNotAllowed() {
        Board board = Board.getBoard(args);
        board.dig(0, 0);
        board.deflag(0, 0);
        
        assertEquals(State.DUG, board.getSquare(0, 0).getState());
    }
    
    @Test
    public void outOfRangeActions() {
        Board board = Board.getBoard(args);
        State expectedState = State.UNTOUCHED;
        
        // Just 1 out of range.
        board.flag(10, 10);
        assertExpectedBoardState(board, expectedState);
        board.dig(10, 10);
        assertExpectedBoardState(board, expectedState);
        board.deflag(10, 10);
        assertExpectedBoardState(board, expectedState);
        
        // Below and above range.
        board.flag(-1, 2);
        assertExpectedBoardState(board, expectedState);
        board.dig(-10, 3);
        assertExpectedBoardState(board, expectedState);
        board.deflag(-42, 13);
        assertExpectedBoardState(board, expectedState);
    }
    
    @Test
    public void fromFile() {
        String[] args = { "true", "-f", "src/minesweeper/boards/board1.txt" };
        @SuppressWarnings("unused")
        Board board = Board.getBoard(args);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void fromFileInvalidSize() {
        String[] args = { "true", "-f", "src/minesweeper/boards/board-invalid-5x4.txt" };
        @SuppressWarnings("unused")
        Board board = Board.getBoard(args);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void fromFileIllegalNum() {
        String[] args = { "true", "-f", "src/minesweeper/boards/board-badnum.txt" };
        @SuppressWarnings("unused")
        Board board = Board.getBoard(args);
    }
    
    @Test(expected=IllegalArgumentException.class) 
    public void fromFileIllegalChar() {
        String[] args = { "true", "-f", "src/minesweeper/boards/board-badchar.txt" };
        @SuppressWarnings("unused")
        Board board = Board.getBoard(args);
    }
    
    private void assertExpectedBoardState(Board board, State expectedState) {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                assertEquals(expectedState, board.getSquare(i, j).getState());
    }
}