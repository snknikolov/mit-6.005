package jotto.jotto.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;


public class JottoModel {
	private static final String URL_PART_ONE = 
	        "http://courses.csail.mit.edu/6.005/jotto.py?puzzle=";
	private static final String URL_PART_TWO = 
	        "&guess=";
	private static final String INVALID_GUESS = "Invalid guess.";
	
	private int numPuzzle;
	
	/**
	 * Make a JottoModel.
	 */	
	public JottoModel() { 	
	    
	}
	
	/**
     * Send a guess to server.
     * @param guess The user's guess.
     * @return String Server's response, where:
     *         charAt(0) is the number of letters in common
     *         charAt(1) is the number of letters in correct position.
     *         INVALID_GUESS if the number of puzzle or guess was invalid.
     */
	public String makeGuess(String guess) {
	    URLConnection urlc = null;
	    try {
	        URL requestUrl = new URL(URL_PART_ONE + numPuzzle + URL_PART_TWO + guess);
	        urlc = requestUrl.openConnection();
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (IOException ioe) {
	        ioe.printStackTrace();
	    }

	    String fromServer = "";
	    try (
	        BufferedReader in = new BufferedReader(
	                new InputStreamReader(urlc.getInputStream()));
	    ) {
	        fromServer = in.readLine();
	    } catch (IOException ioe) {
	        ioe.printStackTrace();
	    }
	    
	    if (fromServer.indexOf("error") != -1) {
	        return INVALID_GUESS;
	    } else {
	        fromServer = fromServer.replaceAll("\\D", "");
	    }
	    return fromServer;
	}
	
	/**
	 * Set the number of the puzzle and make a new List of guesses.
	 * @param number the new puzzle number;
	 */
	public void setPuzzleNumber(String number) {
	    if (isValidPuzzleNum(number)) {
	        this.numPuzzle = Integer.parseInt(number);
	    }
	    else {
	        this.numPuzzle = generatePuzzleNum();
	    }
	}
	
	/**
	 * Return String representation of the puzzle number.
	 * @return
	 */
	public String getPuzzleNum() {
	    return String.valueOf(numPuzzle);
	}
	
	/**
	 * Generate a random 5-digit puzzle number.
	 * @return int 5-digit random int.
	 */
	private static int generatePuzzleNum() {
	    Random rand = new Random();
	    return rand.nextInt((99999 - 10000) + 1) + 10000;
	}	
	
	/**
	 * Verify user's input validity.
	 * @param in
	 * @return
	 */
	private static boolean isValidPuzzleNum(String in) {
	    return in.matches("\\d{5}");
	}	
	
	/**
	 * Check if the response from server is valid.
	 * @param fromServer
	 * @return
	 */
	public static boolean isValidResponse(String fromServer) {
	    return fromServer.matches("\\d\\d");
	}
	
	/**
	 * Check if user's guess was correct.
	 * @param guess User's guess.
	 * @param fromServer Response from server, as returned by makeGuess()
	 * @return true if the match was correct, false otherwise.
	 */
	public String guessedString(String guess, String fromServer) {
	    // This is the server's response for a perfect match.
	    // 5 matching characters, 5 characters in correct position.
	    if (fromServer.equals("55"))
	        return "You win! The secret word was " + guess;
	    else
	        return "";
	}
}