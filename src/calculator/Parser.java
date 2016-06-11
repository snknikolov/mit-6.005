package calculator;

import java.util.List;
import java.util.Stack;

import calculator.Lexer;
import calculator.Lexer.Token;

/**
 * Grammar:
 * 
 * calculator ::= (regular | parenthesised)+ 
 * parenthesised = OPEN_P (regular | parenthesised)+ CLOSE_P
 * regular ::= (unit operator unit)+
 * unit ::= scalar | IN | PT
 * operator ::= PLUS | MINUS | MUL | DIV
 * scalar ::= [\d]
 */

/**
 * Calculator parser. All values are measured in pt.
 */
class Parser {

    private static final double PT_PER_IN = 72;
    private final List<Token> tokens;

    /**
     * Try to parse input from lexer.
     * 
     * @param lexer
     * @throws ParserException If the input lexer cannot be parsed.
     */
    Parser (Lexer lexer) throws ParserException {
        try {
            this.tokens = lexer.getTokens();
        } catch (Exception e) {
            throw new ParserException();
        }
    }

    /**
     * Evaluate input text from lexer.
     * @return The final value of the whole expression.
     */
    public Value evaluate() throws ParserException {        
        Stack<Value> unitStack = new Stack<Value>();
        Stack<Token> operationsStack = new Stack<Token>();
        int parenthesesBallance = 0;
        Value evaluated = null;
        
        for (Token token : tokens) {
            Type tokenType = token.getType();
            
            switch (tokenType) {
            case SCALAR:
                unitStack.push(new Value(Double.parseDouble(token.getText()), ValueType.SCALAR));
                break;
            case INCH:
                evaluated = new Value(unitStack.pop().value * PT_PER_IN, ValueType.INCHES);
                unitStack.push(evaluated);
                break;
            case POINT:
                evaluated = new Value(unitStack.pop().value, ValueType.POINTS);
                unitStack.push(evaluated);
                break;
            case PLUS:
            case MINUS:
            case MUL:
            case DIV:
                if (parenthesesBallance == 0 && unitStack.size() > 1) {
                    evaluated = partialEval(unitStack, operationsStack);
                    unitStack.push(evaluated);
                } else 
                    operationsStack.push(token); 
                break;
            case OPEN_P:
                ++parenthesesBallance;
                break;
            case CLOSE_P:
                --parenthesesBallance;
                evaluated = partialEval(unitStack, operationsStack);
                unitStack.push(evaluated);
                break;
            }
        }
        
        if (parenthesesBallance != 0)
            throw new ParserException();
        
        while (!operationsStack.isEmpty()) {
            evaluated = partialEval(unitStack, operationsStack);
            unitStack.push(evaluated);
        }
        
        return evaluated;
    }
    
    /**
     * Evaluate a signle expression using stack of units and operations.
     * 
     * @param unitsStack Stack containing the units (SCALAR, IN, etc.)
     * @param operationsStack Stack containing the operations (MUL, DIV, etc.)
     * @return The evaluated expression.
     */
    private Value partialEval(Stack<Value> unitsStack, Stack<Token> operationsStack)
                    throws ParserException {
        // Too many operations, no units.
        if (unitsStack.size() < 2) {
            throw new ParserException();
        }
        
        Value second = unitsStack.pop();
        Value first = unitsStack.pop();
        Type operationType = operationsStack.pop().getType();
        
        Value evaluated = evalResult(first, second, operationType);
        return evaluated;
    }
    
    /**
     * Evaluate the result of an expression.
     *  
     * @param first First value.
     * @param second Second value.
     * @param operationType The type of expression.
     * @return The result of the operation as Value.
     */
    private Value evalResult(Value first, Value second, Type operationType) {
        ValueType typeResult = evalResultType(first.type, second.type, operationType);
        double valueResult = 0;
                        
        switch (operationType) {
        case PLUS:
            valueResult = first.value + second.value;
            break;
        case MINUS:
            valueResult = first.value - second.value;
            break;
        case MUL:
            valueResult = first.value * second.value;
            break;
        case DIV:
            if (second.value == 0) 
                throw new IllegalArgumentException("Division by zero.");
            valueResult = first.value / second.value;
            break;
        default:
            throw new ParserException();
        }
        
        return new Value(valueResult, typeResult);
    }
    
    /**
     * Evaluate the result type of an operation between two types.
     * The resulting types are as follows:
     * 
     * in +-/* scalar = in
     * in / in = scalar
     * in / pt = scalar
     * 
     * pt +-/* scalar = pt
     * pt / in = scalar
     * pt / pt = scalar
     * 
     * scalar as first operand, always takes the second operand's type.
     * 
     * in + pt = in (use units of the first operand).
     * 
     * @param first The first operand.
     * @param second The second operand.
     * @param operationType The type of operation between the operands.
     * @return The result type of the operation between the operands.
     */
    private ValueType evalResultType(ValueType first, ValueType second, Type operationType) {
        ValueType type = null;
        
        switch (first) {
        case INCHES:
            if (shouldConvertType(first, second, operationType))
                type = ValueType.SCALAR;
            else
                type = first;
            break;
        case POINTS:
            if (shouldConvertType(first, second, operationType))
                type = ValueType.SCALAR;
            else
                type = first;
            break;
        case SCALAR:
            type = second;
            break;
        default:
            throw new IllegalArgumentException();
        }
        return type;
    }
    
    /**
     * Check if the value type should be converted.
     * 
     * @param first First operand.
     * @param second Second operand.
     * @param operationType The type of operation between the operands.
     * @return true if the type should be converted, false otherwise.
     */
    private boolean shouldConvertType(ValueType first, ValueType second, Type operationType) {
        return (second == ValueType.POINTS || second == ValueType.INCHES)
                && operationType == Type.DIV;
    }
    
    static class ParserException extends RuntimeException {
        private static final long serialVersionUID = 2L;

        public ParserException() {
            super();
        }
    }

    /**
     * Type of values.
     */
    private enum ValueType {
        POINTS, INCHES, SCALAR
    };

    /**
     * Internal value is always in points.
     */
    public class Value {
        final double value;
        final ValueType type;

        Value(double value, ValueType type) {
            this.value = value;
            this.type = type;
        }

        @Override
        public String toString() {
            switch (type) {
            case INCHES:
                return value / PT_PER_IN + " in";
            case POINTS:
                return value + " pt";
            default:
                return "" + value;
            }
        }
    }
}