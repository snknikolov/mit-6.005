package minesweeper;

public final class Board {
    private static final int DEFAULT_BOARD_SIZE = 10;
    private static final double IS_BOMB_PROBABILITY = 0.25;
    private static final int BOMB_FLAG = 1;
    private static final int NO_BOMB_FLAG = 0;
    
    private final Square[][] board;
    
    // Thread safety argument:
    //  all access to Square objects happen within Board methods,
    //  which are all guarder by Board's lock.
    
    /**
     * Get a board instance.
     * @param args
     * @return
     */
    public static Board getBoard(String... args) {
        if (args.length == 1) {
            return new Board(DEFAULT_BOARD_SIZE);
        } else if (args[1].equals("-s")) {
            int size;
            try {
                size = Integer.parseInt(args[2]);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
                throw new IllegalArgumentException("Usage: <DEBUG> -s <size>.");
            }
            return new Board(size);
        } else if (args[1].equals("-f")) {
            return null;
        } else {
            return null;
        }
    }
    
    /**
     * Make a board with dimensions size*size
     * @param size The size of the board's length and width.
     */
    private Board(int size) {
        int[][] initBoard = constructBoard(size);
        this.board = new Square[size][size];
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int numBombNeighbours = countBombNeighbours(i, j, initBoard);
                if (initBoard[i][j] == NO_BOMB_FLAG) {
                    board[i][j] = new Square(false, numBombNeighbours);
                } else {
                    board[i][j] = new Square(true, numBombNeighbours);
                }
            }
        }
    }
                    
    /**
     * Dig a square on the board.
     * Only takes effect if 0 <= i < board size
     *                  AND 0 <= j < board size
     *                  AND state is UNTOUCHED.
     * @param i Row
     * @param j Column 
     */
    public synchronized void dig(int i, int j) {
        if (i < board.length && j < board.length
                && i >= 0 && j >= 0
                && board[i][j].getState() == State.UNTOUCHED) {
            board[i][j] = board[i][j].changeState(State.DUG);
        }
    }
    
    /**
     * Flag a square on the board. 
     * Only takes effect if 0 <= i < board size
     *                  AND 0 <= j < board size.
     *                  
     * @param i Row
     * @param j Column
     */
    public synchronized void flag(int i, int j) {
        if (i < board.length && j < board.length
                && i >= 0 && j >= 0
                && board[i][j].getState() == State.UNTOUCHED) {
            board[i][j] = board[i][j].changeState(State.FLAGGED);
        }
    }
    
    /**
     * Remove the flag from a square on the board.
     * Only takes effect if 0 <= i < board size
     *                  AND 0 <= j < board size
     *                  AND current square state is FLAGGED.
     * 
     * @param i Row
     * @param j Column
     */
    public synchronized void deflag(int i, int j) {
        if (i < board.length && j < board.length
                && i >= 0 && j >= 0
                && board[i][j].getState() == State.FLAGGED) {
            board[i][j] = board[i][j].changeState(State.UNTOUCHED);
        }
    }
    
    /**
     * Get square at position [i][j] on the board.
     * @param i Row
     * @param j Column
     * @return
     */
    public synchronized Square getSquare(int i, int j) {
        return board[i][j];
    }
    
    @Override
    public synchronized String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                State state = board[i][j].getState();
                switch(state) {
                case DUG:
                    if (board[i][j].isBomb())
                        sb.append("*");
                    else if (board[i][j].getNumBombNeighbours() == 0)
                        sb.append(" ");
                    else
                        sb.append(board[i][j].getNumBombNeighbours());
                    break;
                case UNTOUCHED:
                    sb.append("-");
                    break;
                case FLAGGED:
                    sb.append("F");
                    break;
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    
    /**
     * Initialise an N x N minesweeper board.
     * 
     * @param size The length/width of the board.
     * @return int[][] representation of a minesweeper board.
     */
    private int[][] constructBoard(int size) {
        int[][] board = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (Math.random() <= IS_BOMB_PROBABILITY)
                    board[i][j] = BOMB_FLAG;
                else
                    board[i][j] = NO_BOMB_FLAG;
            }
        }
        return board;
    }
    
    /**
     * Count the number of bomb neighbours around a square in row i, column j.
     * 
     * @param i Row
     * @param j Column
     * @param initBoard int[][] representation of minesweeper board.
     * @return The number of neighbours that are bombs.
     */
    private int countBombNeighbours(int i, int j, int[][] initBoard) {
        int numBombNeighbours = 0;
        int up = i - 1;
        int down = i + 1;
        int left = j - 1;
        int right = j + 1;
        int max = initBoard.length;
        
        // up, up left, up right
        if (up >= 0) {
            if (initBoard[up][j] == BOMB_FLAG)
                ++numBombNeighbours;
            if (left >= 0 && initBoard[up][left] == BOMB_FLAG)
                ++numBombNeighbours;
            if (right < max && initBoard[up][right] == BOMB_FLAG)
                ++numBombNeighbours;
        }
        // left and right.
        if (left >= 0 && initBoard[i][left] == BOMB_FLAG)
            ++numBombNeighbours;
        if (right < max && initBoard[i][right] == BOMB_FLAG)
            ++numBombNeighbours;
        // down, down left, down right
        if (down < max) {
            if (initBoard[down][j] == BOMB_FLAG)
                ++numBombNeighbours;
            if (left >= 0 && initBoard[down][left] == BOMB_FLAG)
                ++numBombNeighbours;
            if (right < max && initBoard[down][right] == BOMB_FLAG)
                ++numBombNeighbours;
        }
        return numBombNeighbours;
    }    
}