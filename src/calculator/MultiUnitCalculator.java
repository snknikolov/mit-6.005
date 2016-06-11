package calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import calculator.Parser.Value;

/**
 * Multi-unit calculator.
 */
public class MultiUnitCalculator {

    /**
     * @param expression
     *            a String representing a multi-unit expression, as defined in
     *            the problem set
     * @return the value of the expression, as a number possibly followed by
     *         units, e.g. "72pt", "3", or "4.882in"
     */
    public String evaluate(String expression) {
        Lexer lex = null;
        Parser parse = null;
        Value val = null;
        String result = "";
        
        try {
            lex = new Lexer(expression);
        } catch (Exception e) {
            result += "> Invalid character detected...";
        }
        
        try {
            parse = new Parser(lex);
            val = parse.evaluate();
            result += expression + " = " + val.toString();
        } catch (Exception e) {
            result += "> Invalid character detected...";
        }
        
        return result;
    }

    /**
     * Repeatedly reads expressions from the console, and outputs the results of
     * evaluating them. Inputting an empty line will terminate the program.
     * 
     * @param args
     *            unused
     */
    public static void main(String[] args) throws IOException {
        MultiUnitCalculator calculator;
        String result;

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String expression;
        while (true) {
            // display prompt
            System.out.print("> ");
            // read input
            expression = in.readLine();
            // terminate if input empty
            if (expression.equals(""))
                break;

            // evaluate
            calculator = new MultiUnitCalculator();
            result = calculator.evaluate(expression);
            // display result
            System.out.println(result);
        }
    }
}
