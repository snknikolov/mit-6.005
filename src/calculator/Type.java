package calculator;

/** 
 * Symbols:
 * +, -, *, / 
 * scalar, in (inches), pt (point)
 * ( )
 * 
 * 
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
 * Token type.
 */
enum Type {
    OPEN_P("("), CLOSE_P(")"),
	SCALAR(null), INCH("in"), POINT("pt"),
	PLUS("+"), MINUS("-"), MUL("*"), DIV("/");
	
	public final String value;
    Type(String value) {
        this.value = value;
    }
}