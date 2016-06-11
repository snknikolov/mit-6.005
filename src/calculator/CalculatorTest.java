package calculator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class CalculatorTest {
    private MultiUnitCalculator muc;
    private static final String ERROR_MESSAGE = "> Invalid character detected...";
    
    @Test
    public void simple() {
        String test = "1+2";
        muc = new MultiUnitCalculator();
        
        String evaluated = muc.evaluate(test);
        String expectedValue = test + " = 3.0";
        
        assertEquals(expectedValue, evaluated);
    }
    
    @Test
    public void simpleSecond() {
        String test = "1-2.4";
        muc = new MultiUnitCalculator();
        
        String evaluated = muc.evaluate(test);
        String expectedValue = test + " = -1.4";
        
        assertEquals(expectedValue, evaluated);
    }
    
    @Test
    public void testParentheses() {
        String test = "(3 + 4)";
        muc = new MultiUnitCalculator();
        
        String evaluated = muc.evaluate(test);
        String expectedValue = test + " = 7.0";
        
        assertEquals(expectedValue, evaluated);
    }
    
    @Test
    public void scalarInch() {
        String test = "3 + 2.4in";
        muc = new MultiUnitCalculator();
        
        String evaluated = muc.evaluate(test);
        String expectedValue = test + " = 5.4 in";
        
        assertEquals(expectedValue, evaluated);
    }

    @Test
    public void pointInch() {
        String test = "3pt * 2.4in";
        muc = new MultiUnitCalculator();
        
        String evaluated = muc.evaluate(test);
        String expectedValue = test + " = 518.4 pt";
        
        assertEquals(expectedValue, evaluated);
    }
    
    @Test
    public void inchScalar() {
        String test = "3 in * 2.4";
        muc = new MultiUnitCalculator();
        
        String evaluated = muc.evaluate(test);
        String expectedValue = test + " = 7.2 in";
        
        assertEquals(expectedValue, evaluated);
    }
    
    @Test
    public void pointInchScalar() {
        String test = "4pt + (3 in * 2.4)";
        muc = new MultiUnitCalculator();
        
        String evaluated = muc.evaluate(test);
        String expectedValue = test + " = 522.4 pt";
        
        assertEquals(expectedValue, evaluated);
    }
    
    @Test
    public void pointInchScalarNestedParentheses() {
        String test = "4pt + ((3 in * 2.4))";
        muc = new MultiUnitCalculator();
        
        String evaluated = muc.evaluate(test);
        String expectedValue = test + " = 522.4 pt";
        
        assertEquals(expectedValue, evaluated);
    }
    
    @Test
    public void scalarsConvertToInches() {
        String test = "(3 + 2.4)in";
        muc = new MultiUnitCalculator();
        
        String evaluated = muc.evaluate(test);
        String expectedValue = test + " = 5.4 in";
        
        assertEquals(expectedValue, evaluated);
    }
    
    @Test
    public void inchScalarParenthesisedConversion() {
        String test = "(3in * 2.4)pt";
        muc = new MultiUnitCalculator();
        
        String evaluated = muc.evaluate(test);
        String expectedValue = test + " = 518.4 pt";
        
        assertEquals(expectedValue, evaluated);
    }
    
    @Test
    public void illegalInput() {
        String test = "3++";
        muc = new MultiUnitCalculator();
        
        String evaluated = muc.evaluate(test);
        
        assertEquals(ERROR_MESSAGE, evaluated);
    }
    
    @Test
    public void illegalInputTwo() {
        String test = "7++4.2";
        muc = new MultiUnitCalculator();
        
        String evaluated = muc.evaluate(test);
        
        assertEquals(ERROR_MESSAGE, evaluated);
    }
    
    @Test
    public void illegalInputThree() {
        String test = "7+in";
        muc = new MultiUnitCalculator();
        
        String evaluated = muc.evaluate(test);
        
        assertEquals(ERROR_MESSAGE, evaluated);
    }
    
    @Test
    public void illegalInputFour() {
        String test = "in+pt";
        muc = new MultiUnitCalculator();
        
        String evaluated = muc.evaluate(test);
        
        assertEquals(ERROR_MESSAGE, evaluated);
    }
    
    @Test
    public void illegalInputFive() {
        String test = "pt + 12 + 3";
        muc = new MultiUnitCalculator();
        
        String evaluated = muc.evaluate(test);
        
        assertEquals(ERROR_MESSAGE, evaluated);
    }
    
    @Test
    public void illegalInputParentheses() {
        String test = "()1+2)";
        muc = new MultiUnitCalculator();
        
        String evaluated = muc.evaluate(test);

        assertEquals(ERROR_MESSAGE, evaluated);
    }
    
    @Test
    public void illegalInputParenthesesTwo() {
        String test = "(1+2";
        muc = new MultiUnitCalculator();
        
        String evaluated = muc.evaluate(test);

        assertEquals(ERROR_MESSAGE, evaluated);
    }
}