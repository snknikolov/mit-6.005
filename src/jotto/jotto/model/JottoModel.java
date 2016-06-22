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
	
	private final int numPuzzle;
	
	/**
	 * Make a JottoModel with a puzzle number numPuzzle.
	 * 
	 * @param numPuzzle The number of this puzzle.
	 */
	public JottoModel(int numPuzzle) {
	    this.numPuzzle = numPuzzle;
	}
	
	/**
	 * Send a guess to server.
	 * @param guess The user's guess.
	 * @return String Server's response, where:
	 *         charAt(0) is the number of letters in common
	 *         charAt(1) is the number of letters in correct position.
	 *         INVALID_GUESS if the number of puzzle or guess was invalid.
	 */
	public String makeGuess(String guess) throws IOException {
	    URLConnection urlc = null;
	    try {
	        URL requestUrl = new URL(URL_PART_ONE + numPuzzle + URL_PART_TWO + guess);
	        urlc = requestUrl.openConnection();
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	        return "Error occured while connecting to server";
	    } catch (IOException ioe) {
	        ioe.printStackTrace();
            return "Error occured while connecting to server";
	    }
	    BufferedReader in = new BufferedReader(
	            new InputStreamReader(urlc.getInputStream()));
	    
	    String inputLine = in.readLine();
	    in.close();
	    
	    if (inputLine.indexOf("error") != -1)
	        return INVALID_GUESS;
	    else
	        return inputLine.replaceAll("\\D", "");
	}
	
	/**
	 * Generate a random 5-digit puzzle number.
	 * @return int 5-digit random int.
	 */
	public static int generatePuzzleNum() {
	    Random rand = new Random();
	    return rand.nextInt((99999 - 10000) + 1) + 10000;
	}	
}