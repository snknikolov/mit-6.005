package minesweeper;

/**
 * An immutable class representing a square on a minesweeper board.
 *
 */
public final class Square {
    private final State state;
    private final boolean isBomb;
    private final int numBombNeighbours;
        
    /**
     * Default constructor, initialises state to UNTOUCHED.
     * @param isBomb is this square a bomb?
     * @param numBombNeighbours Number of neighbour squares that are bombs.
     */
    public Square(boolean isBomb, int numBombNeighbours) {
        state = State.UNTOUCHED;
        this.isBomb = isBomb;
        this.numBombNeighbours = numBombNeighbours;
    }    
    
    /**
     * Private constructor for changing Square state.
     * 
     * @param state The new square's state.
     * @param isBomb is this square a bomb?
     * @param numBombNeighbours Number of neigbour squares that are bombs.
     */
    private Square(State state, boolean isBomb, int numBombNeighbours) {
        this.state = state;
        this.isBomb = isBomb;
        this.numBombNeighbours = numBombNeighbours;
    }
    
    public State getState() {
        return state;
    }
    
    /**
     * Return 
     * @return
     */
    public boolean isBomb() {
        return isBomb;
    }
    
    public int getNumBombNeighbours() {
        return numBombNeighbours;
    }
    
    /**
     * "Change" Square state by returning a new Square with queried state.
     * @param state The new Square's state.
     * @return The new Square object with changed state.
     */
    Square changeState(State state) {
        return new Square(state, isBomb, numBombNeighbours);
    }
}