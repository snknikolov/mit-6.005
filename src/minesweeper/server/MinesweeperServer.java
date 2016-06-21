package minesweeper.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import minesweeper.Board;

/**
 * 
 * User-to-Server Message Protocol:
 * 
 * MESSAGE := ( LOOK | DIG | FLAG | DEFLAG | HELP_REQ | BYE ) NEWLINE
 * LOOK := "look"
 * DIG := "dig" SPACE X SPACE Y
 * FLAG := "flag" SPACE X SPACE Y
 * DEFLAG := "deflag" SPACE X SPACE Y
 * HELP_REQ := "help"
 * BYE := "bye"
 * NEWLINE := "\n"
 * X := INT
 * Y := INT
 * SPACE := " "
 * INT := [0-9]+
 * 
 * Server-to-User Message Protocol:
 * 
 * MESSAGE := BOARD | BOOM | HELP | HELLO
 * BOARD := LINE+
 * LINE := (SQUARE SPACE)* SQUARE NEWLINE
 * SQUARE := "-" | "F" | COUNT | SPACE
 * SPACE := " "
 * NEWLINE := "\n"
 * COUNT := [1-8]
 * BOOM := "BOOM!" NEWLINE
 * HELP := [^NewLine]+ NEWLINE
 * HELLO := "Welcome to Minesweeper. " N " people are playing including you.
 *          Type 'help' for help." NEWLINE
 * N := INT
 * INT := [0-9]+
 * 
 */

public class MinesweeperServer {
    // Default port
	private final static int PORT = 4444;
	// Server to user Strings
	private static final String BYE = "And a very good day to you too, sir!";
	private static final String BOOM = "BOOM";
	private static final String HELP = "help";
	
	// Board instance used by MinesweeperServer
	private static Board board;
	// Don't terminate user connection on BOOM message in DEBUG mode.
	private static boolean debug;
	
	private ServerSocket serverSocket;
	
    /**
     * Make a MinesweeperServer that listens for connections on port.
     * @param port port number, requires 0 <= port <= 65535.
     */
    public MinesweeperServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }
    
    /**
     * Run the server, listening for client connections and handling them.  
     * Never returns unless an exception is thrown.
     * @throws IOException if the main server socket is broken
     * (IOExceptions from individual clients do *not* terminate serve()).
     */
    public void serve() throws IOException {
        while (true) {
            // block until a client connects
            final Socket socket = serverSocket.accept();
            
            // handle the client
            Thread handler = new Thread(() -> {
              try {
                  try {
                      handleConnection(socket);
                  } finally {
                      socket.close();
                  }
              } catch (IOException ioe) {
                  ioe.printStackTrace();
              }
            });
            handler.start();
        }
    }
    
    /**
     * Handle a single client connection.  Returns when client disconnects.
     * @param socket  socket where client is connected
     * @throws IOException if connection has an error or terminates unexpectedly
     */
    private void handleConnection(Socket socket) throws IOException {
        try (
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));
            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);
        ) {
            int numActivePlayers = Thread.activeCount() - 1;
            out.println("Welcome to Minesweeper. "+ numActivePlayers 
                    +  " people are playing including you. Type 'help' for help.");
            
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                String output = handleRequest(line);
                if (output != null) {
                    out.println(output);
                    // Terminate user connection.
                    if (output.equals(BYE) || 
                        (output.equals(BOOM) && !debug))
                        return;
                }
            }
        }
    }

	/**
	 * handler for client input
	 * 
	 * make requested mutations on game state if applicable, then return appropriate message to the user
	 * 
	 * @param input
	 * @return
	 */
	private static String handleRequest(String input) {

		String regex = "(look)|(dig \\d+ \\d+)|(flag \\d+ \\d+)|(deflag \\d+ \\d+)|(help)|(bye)";
		if(!input.matches(regex)) {
			//invalid input
			return null;
		}
		String[] tokens = input.split(" ");
		// x y arguments for dig/flag/deflag
		int x = 0, y = 0;
		if (tokens.length > 1) {
		    x = Integer.parseInt(tokens[1]);
		    y = Integer.parseInt(tokens[2]);
		}

		switch (tokens[0]) {
		case "look":
		    return board.toString();
		case "help":
		    return HELP;
		case "bye":
		    return BYE;
		case "dig":
		    board.dig(y, x);
		    if (board.getSquare(y, x).isBomb())
		        return BOOM;
		    else
		        return board.toString();
		case "flag":
		    board.flag(y, x);
		    return board.toString();
		case "deflag":
		    board.deflag(y, x);
		    return board.toString();
		default:
		    // Should never get here.
		    return "";
		}
	}
	
	/**
	 * Convert a text file to int[][] board.
	 * 
	 * @param filePath
	 * @return int[][] representation of a board,
	 *         null if the file could not be converted.
	 */
	private static int[][] fileToBoard(String fileName) {
	    try (
	            BufferedReader br = new BufferedReader(
	                    new FileReader(fileName));
	    ) {
	        String line = br.readLine();
	        int size = line.length();
	        int[][] board = new int[size][size];
	        int i = 0;
	        while (line != null) {
	            // File contains illegal character or has invalid shape.
	            if (!line.matches("\\d+") || line.length() != size)
	                return null;
	            
	            for (int j = 0; j < size; j++)
	                board[i][j] = line.charAt(j);
	            ++i;
	            line = br.readLine();
	        }
	        return board;
	    } catch (FileNotFoundException fnfe) {
	        fnfe.printStackTrace();
	        return null;
	    } catch (IOException ioe) {
	        ioe.printStackTrace();
	        return null;
	    }
	}
    
    /**
     * Start a MinesweeperServer running on the default port.
     * 
     * Command line arguments protocol:
     * 
     * ARGS := DEBUG SPACE ( SIZE | FILE)?
     * DEBUG := "true" | "false"
     * SIZE := SIZE_FLAG SPACE X
     * SIZE_FLAG := "-s"
     * X := INT
     * FILE := FILE_FLAG SPACE PATH
     * FILE_FLAG := "-f"
     * PATH := .+
     * INT := [0-9]+
     * SPACE := " "
     */
    public static void main(String[] args) {
        try {
            MinesweeperServer server = new MinesweeperServer(PORT);
            debug = args[0].equals("true");
            board = Board.getBoard(args);
            server.serve();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}