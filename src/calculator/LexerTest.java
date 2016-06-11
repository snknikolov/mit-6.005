package calculator;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import calculator.Lexer.Token;

public class LexerTest {
    private Lexer lex;
    
    @Test
    public void simple() {
        String test = "1+2";
        try {
            lex = new Lexer(test);
        } catch (Exception e) {}
        
        List<Token> tokens = lex.getTokens();
        
        List<Type> expectedTypes = new ArrayList<Type>();
        expectedTypes.add(Type.SCALAR);
        expectedTypes.add(Type.PLUS);
        expectedTypes.add(Type.SCALAR);
                
        for (int i = 0; i < tokens.size(); i++) {

            assertEquals(test.substring(i, i+1), tokens.get(i).getText());
            assertEquals(expectedTypes.get(i), tokens.get(i).getType());
        }
    }
    
    @Test
    public void simpleSecond() {
        String test = "1-2.4";
        try {
            lex = new Lexer(test);
        } catch (Exception e) {}
        List<Token> tokens = lex.getTokens();
        
        List<Type> expectedTypes = new ArrayList<Type>();
        expectedTypes.add(Type.SCALAR);
        expectedTypes.add(Type.MINUS);
        expectedTypes.add(Type.SCALAR);
        List<String> expectedString = new ArrayList<String>();
        expectedString.add("1");
        expectedString.add("-");
        expectedString.add("2.4");
        
        for (int i = 0; i < tokens.size(); i++) {
            assertEquals(expectedString.get(i), tokens.get(i).getText());
            assertEquals(expectedTypes.get(i), tokens.get(i).getType());
        }
    }
    
    @Test
    public void testParentheses() {
        String test = "(3+4)";
        try {
            lex = new Lexer(test);
        } catch (Exception e) {}
        List<Token> tokens = lex.getTokens();
        
        List<Type> expectedTypes = new ArrayList<Type>();
        expectedTypes.add(Type.OPEN_P);
        expectedTypes.add(Type.SCALAR);
        expectedTypes.add(Type.PLUS);
        expectedTypes.add(Type.SCALAR);
        expectedTypes.add(Type.CLOSE_P);
        
        for (int i = 0; i < tokens.size(); i++) {
            assertEquals(test.substring(i, i+1), tokens.get(i).getText());
            assertEquals(expectedTypes.get(i), tokens.get(i).getType());
        }
    }
    
    @Test
    public void testInches() {
        String test = "3+2.4 in";
        try {
            lex = new Lexer(test);
        } catch (Exception e) {}
        List<Token> tokens = lex.getTokens();
        // Lexer should replace all white spaces.
        test = test.replace(" ", "");
        
        List<Type> expectedTypes = new ArrayList<Type>();
        expectedTypes.add(Type.SCALAR);
        expectedTypes.add(Type.PLUS);
        expectedTypes.add(Type.SCALAR);
        expectedTypes.add(Type.INCH);
        List<String> expectedString = new ArrayList<String>();
        expectedString.add("3");
        expectedString.add("+");
        expectedString.add("2.4");
        expectedString.add("in");

        
        
        for (int i = 0; i < tokens.size(); i++) {
            assertEquals(expectedString.get(i), tokens.get(i).getText());
            assertEquals(expectedTypes.get(i), tokens.get(i).getType());
        }
    }
    
    @Test
    public void testPoints() {
        String test = "3+2.4 pt";
        try {
            lex = new Lexer(test);
        } catch (Exception e) {}
        List<Token> tokens = lex.getTokens();
        // Lexer should replace all white spaces.
        test = test.replace(" ", "");
        
        List<Type> expectedTypes = new ArrayList<Type>();
        expectedTypes.add(Type.SCALAR);
        expectedTypes.add(Type.PLUS);
        expectedTypes.add(Type.SCALAR);
        expectedTypes.add(Type.POINT);
        List<String> expectedString = new ArrayList<String>();
        expectedString.add("3");
        expectedString.add("+");
        expectedString.add("2.4");
        expectedString.add("pt");

        
        for (int i = 0; i < tokens.size(); i++) {
            assertEquals(expectedString.get(i), tokens.get(i).getText());
            assertEquals(expectedTypes.get(i), tokens.get(i).getType());
        }
    }
    
    @Test
    public void testNestedParentheses() {
        String test = "4 pt + ((3 in * 2.4))";
        try {
            lex = new Lexer(test);
        } catch (Exception e) {}

        List<Token> tokens = lex.getTokens();
        // Lexer should replace all white spaces.
        test = test.replace(" ", "");
        
        List<Type> expectedTypes = new ArrayList<Type>();
        expectedTypes.add(Type.SCALAR);
        expectedTypes.add(Type.POINT);
        expectedTypes.add(Type.PLUS);
        expectedTypes.add(Type.OPEN_P);
        expectedTypes.add(Type.OPEN_P);
        expectedTypes.add(Type.SCALAR);
        expectedTypes.add(Type.INCH);
        expectedTypes.add(Type.MUL);
        expectedTypes.add(Type.SCALAR);
        expectedTypes.add(Type.CLOSE_P);
        expectedTypes.add(Type.CLOSE_P);
        List<String> expectedString = new ArrayList<String>();
        expectedString.add("4");
        expectedString.add("pt");
        expectedString.add("+");
        expectedString.add("(");
        expectedString.add("(");
        expectedString.add("3");
        expectedString.add("in");
        expectedString.add("*");
        expectedString.add("2.4");
        expectedString.add(")");
        expectedString.add(")");

        
        for (int i = 0; i < tokens.size(); i++) {
            assertEquals(expectedString.get(i), tokens.get(i).getText());
            assertEquals(expectedTypes.get(i), tokens.get(i).getType());
        }
    }
    
    @Test
    public void invalidInputBeginning() {
        String test = "h1+2";
        try {
            lex = new Lexer(test);
            fail("Exception not thrown");
        } catch (Exception e) {}
    }
    
    @Test
    public void invalidInputMiddle() {
        String test = "1+(3h*2)";
        try {
            lex = new Lexer(test);
            fail("Exception not thrown");
        } catch (Exception e) {}
    }    
}