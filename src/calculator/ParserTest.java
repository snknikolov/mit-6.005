package calculator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import calculator.Parser.Value;

public class ParserTest {
    private Lexer lex;
    
    @Test
    public void simple() {
        String test = "1+2";
        try {
            lex = new Lexer(test);
        } catch (Exception e) {}
        
        Parser parser = new Parser(lex);
        Value value = parser.evaluate();
        String expectedValue = "3.0";
        
        assertEquals(expectedValue, value.toString());
    }
    
    @Test
    public void simpleSecond() {
        String test = "1-2.4";
        try {
            lex = new Lexer(test);
        } catch (Exception e) {}

        Parser parser = new Parser(lex);
        Value value = parser.evaluate();
        String expectedValue = "-1.4";
        
        assertEquals(expectedValue, value.toString());
    }
    
    @Test
    public void testParentheses() {
        String test = "(3 + 4)";
        try {
            lex = new Lexer(test);
        } catch (Exception e) {}

        Parser parser = new Parser(lex);
        Value value = parser.evaluate();
        String expectedValue = "7.0";
        
        assertEquals(expectedValue, value.toString());
    }
    
    @Test
    public void scalarInch() {
        String test = "3 + 2.4in";
        try {
            lex = new Lexer(test);
        } catch (Exception e) {}

        Parser parser = new Parser(lex);
        Value value = parser.evaluate();
        String expectedValue = "5.4 in";
        
        assertEquals(expectedValue, value.toString());
    }

    @Test
    public void pointInch() {
        String test = "3pt * 2.4in";
        try {
            lex = new Lexer(test);
        } catch (Exception e) {}

        Parser parser = new Parser(lex);
        Value value = parser.evaluate();
        String expectedValue = "518.4 pt";
        
        assertEquals(expectedValue, value.toString());
    }
    
    @Test
    public void inchScalar() {
        String test = "3 in * 2.4";
        try {
            lex = new Lexer(test);
        } catch (Exception e) {}

        Parser parser = new Parser(lex);
        Value value = parser.evaluate();
        String expectedValue = "7.2 in";

        assertEquals(expectedValue, value.toString());
    }
    
    @Test
    public void pointInchScalar() {
        String test = "4pt + (3 in * 2.4)";
        try {
            lex = new Lexer(test);
        } catch (Exception e) {}

        Parser parser = new Parser(lex);
        Value value = parser.evaluate();
        String expectedValue = "522.4 pt";
        
        assertEquals(expectedValue, value.toString());
    }
    
    @Test
    public void pointInchScalarNestedParentheses() {
        String test = "4pt + ((3 in * 2.4))";
        try {
            lex = new Lexer(test);
        } catch (Exception e) {}

        Parser parser = new Parser(lex);
        Value value = parser.evaluate();
        String expectedValue = "522.4 pt";
        
        assertEquals(expectedValue, value.toString());
    }
    
    @Test
    public void nestedParentheses() {
        String test = "(1 + 2 + (4 + 5))";
        try {
            lex = new Lexer(test);
        } catch (Exception e) {}

        Parser parser = new Parser(lex);
        Value value = parser.evaluate();
        String expectedValue = "12.0";
        
        assertEquals(expectedValue, value.toString());
    }
    
    @Test
    public void scalarsConvertToInches() {
        String test = "(3 + 2.4)in";
        try {
            lex = new Lexer(test);
        } catch (Exception e) {}

        Parser parser = new Parser(lex);
        Value value = parser.evaluate();
        String expectedValue = "5.4 in";
        
        assertEquals(expectedValue, value.toString());
    }
    
    @Test
    public void inchScalarParenthesisedConversion() {
        String test = "(3in * 2.4)pt";
        try {
            lex = new Lexer(test);
        } catch (Exception e) {}

        Parser parser = new Parser(lex);
        Value value = parser.evaluate();
        String expectedValue = "518.4 pt";
        
        assertEquals(expectedValue, value.toString());
    }
    
    @Test
    public void pointsPointsDivision() {
        String test = "4 pt / 4 pt";
        try {
            lex = new Lexer(test);
        } catch (Exception e) {}

        Parser parser = new Parser(lex);
        Value value = parser.evaluate();
        String expectedValue = "1.0";
        
        assertEquals(expectedValue, value.toString());
    }
    
    @Test
    public void inchInchDivision() {
        String test = "4 in / 4 in";
        try {
            lex = new Lexer(test);
        } catch (Exception e) {}

        Parser parser = new Parser(lex);
        Value value = parser.evaluate();
        String expectedValue = "1.0";
        
        assertEquals(expectedValue, value.toString());
    }
}