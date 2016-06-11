package calculator;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import calculator.Type;

/**
 * Calculator lexical analyser.
 */
public class Lexer {
    
    /**
     * Pattern constants.
     */
    private static final String digitsPattern = "[0-9]+\\.?[0-9]*";
    private static final String inchPattern = "in?";
    private static final String pointPattern = "pt?";

    /**
     * Store the String input as a list of tokens.
     */
    private final List<Token> tokens;

    /**
     * Takes input string and turns it into a list of tokens.
     * 
     * @param input The input String to be tokenised.
     * @throws TokensMismatchException If the input string contains erroneous input.
     */
    public Lexer(String input) throws TokenMismatchException {
        input = input.replaceAll(" ", "");
        tokens = readAll(input, "", new LinkedList<Token>());
    }
    
    /**
     * 
     * @return the list of tokens
     */
    public List<Token> getTokens() {
        return Collections.unmodifiableList(tokens);
    }
    
    /**
     * Read and tokenize input string.
     * 
     * @param input The string to be tokenized.
     * @param carry Substring of input that didn't match any type.
     * @param tokens The list of tokens.
     * 
     * @return The list of tokens collected during the reading.
     * @throws TokenMismatchException If the input string contains erroneous input.
     */
    private List<Token> readAll(String input, String carry, List<Token> tokens) 
                throws TokenMismatchException {
        if (input.length() == 0) {
            if (carry.length() != 0 && checkType(carry) != null)
                tokens.add(new Token(checkType(carry), carry));
            return tokens;
        }
        
        String currentIn = input.substring(0, 1);
        Type result = tokenMap.get(currentIn);
        
        if (result != null && carry.length() == 0) {
            tokens.add(new Token(result, currentIn));
            return readAll(input.substring(1), "", tokens);
        } else if (result != null && carry.length() != 0) {
            Type carryType = checkType(carry);
            if (carryType != null) {
                tokens.add(new Token(carryType, carry));
                tokens.add(new Token(result, currentIn));
                return readAll(input.substring(1), "", tokens);
            } else {
                throw new TokenMismatchException(carry + " is not a valid input");
            }
        }
        
        // Check if the concatenated carry and current input matches any type.
        Type concatType = checkType(carry + currentIn);
        if (concatType != null) {
            return readAll(input.substring(1), carry+currentIn, tokens);
        }
        // Check if the carry alone matches any type.
        Type carryType = checkType(carry);
        if (carryType != null) {
            tokens.add(new Token(carryType, carry));
            return readAll(input.substring(1), currentIn, tokens);
        }

        // All possible legal states have been exhausted.
        throw new TokenMismatchException(input+carry + " is not a valid input.");
    }
    
    /**
     * Match a string to a type.
     * @param s The string to be matched
     * @return the input string's type, null if it doesn't match any pattern.
     */
    private Type checkType(String s) {
        if (s.matches(digitsPattern)) {
            return Type.SCALAR;
        } else if (s.matches(inchPattern)) {
            return Type.INCH;
        } else if (s.matches(pointPattern)) {
            return Type.POINT;
        } else {
            return null;
        }
    }
    
    /**
     * Token in the stream.
     */
    public static class Token {
        private final Type type;
        private final String text;

        Token(Type type, String text) {
            this.type = type;
            this.text = text;
        }

        Token(Type type) {
            this(type, null);
        }
        
        public Type getType() {
            return type;
        }
        
        public String getText() {
            return text;
        }
    }
    
    /**
     * Mapping from String value to Type.
     */
    private static final Map<String, Type> tokenMap =
            new HashMap<String, Type>();
    static {
        for (Type t : Type.values()) {
            tokenMap.put(t.value, t);
        }
    }

    static class TokenMismatchException extends Exception {
        private static final long serialVersionUID = 1L;

        public TokenMismatchException(String s) {
            super(s);
        }
    }
}