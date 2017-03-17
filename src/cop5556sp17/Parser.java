package cop5556sp17;

import cop5556sp17.Scanner.Kind;
import static cop5556sp17.Scanner.Kind.*;

import java.awt.List;
import java.util.ArrayList;

import cop5556sp17.Scanner.Token;
import cop5556sp17.AST.AssignmentStatement;
import cop5556sp17.AST.BinaryChain;
import cop5556sp17.AST.BinaryExpression;
import cop5556sp17.AST.Block;
import cop5556sp17.AST.BooleanLitExpression;
import cop5556sp17.AST.Chain;
import cop5556sp17.AST.ChainElem;
import cop5556sp17.AST.ConstantExpression;
import cop5556sp17.AST.Dec;
import cop5556sp17.AST.Expression;
import cop5556sp17.AST.FilterOpChain;
import cop5556sp17.AST.FrameOpChain;
import cop5556sp17.AST.IdentChain;
import cop5556sp17.AST.IdentExpression;
import cop5556sp17.AST.IdentLValue;
import cop5556sp17.AST.IfStatement;
import cop5556sp17.AST.ImageOpChain;
import cop5556sp17.AST.IntLitExpression;
import cop5556sp17.AST.ParamDec;
import cop5556sp17.AST.Program;
import cop5556sp17.AST.SleepStatement;
import cop5556sp17.AST.Statement;
import cop5556sp17.AST.Tuple;
import cop5556sp17.AST.WhileStatement;

public class Parser {

	/**
	 * Exception to be thrown if a syntax error is detected in the input.
	 * You will want to provide a useful error message.
	 *
	 */
	@SuppressWarnings("serial")
	public static class SyntaxException extends Exception {
		public SyntaxException(String message) {
			super(message);
		}
	}
	
	/**
	 * Useful during development to ensure unimplemented routines are
	 * not accidentally called during development.  Delete it when 
	 * the Parser is finished.
	 *
	 */
	@SuppressWarnings("serial")	
	public static class UnimplementedFeatureException extends RuntimeException {
		public UnimplementedFeatureException() {
			super();
		}
	}

	Scanner scanner;
	Token t;

	public Parser(Scanner scanner) {
		this.scanner = scanner;
		t = scanner.nextToken();
	}

	/**
	 * parse the input using tokens from the scanner.
	 * Check for EOF (i.e. no trailing junk) when finished
	 * 
	 * @throws SyntaxException
	 */
	void parse() throws SyntaxException {
		program();
		match(EOF);
		return;
	}

	public Expression expression() throws SyntaxException 
	{
		Expression ex1=null;
		Expression ex2=null;
		Token t1 = t;
		Token t2=null;
		
		ex1 = term();
		while(t.kind.equals(LT) || t.kind.equals(LE) || t.kind.equals(GT) || t.kind.equals(GE) || t.kind.equals(EQUAL) || t.kind.equals(NOTEQUAL))
		{
			t2 = t;
			consume();
			ex2 = term();
			ex1 = new BinaryExpression(t1,ex1,t2,ex2);
		}
		return ex1;
	}

	Expression term() throws SyntaxException 
	{
		Expression ex1=null;
		Expression ex2=null;
		Token t1 = t;
		Token t2=null;
		
		ex1 = elem();
		while(t.kind.equals(PLUS) || t.kind.equals(MINUS) || t.kind.equals(OR))
		{
			t2=t;
			consume();
			ex2 = elem();
			ex1 = new BinaryExpression(t1,ex1,t2,ex2);
		}
		return ex1;
	}

	Expression elem() throws SyntaxException 
	{
		Expression ex1=null;
		Expression ex2=null;
		Token t1 = t;
		Token t2=null;
		
		ex1 = factor();
		while(t.kind.equals(TIMES) || t.kind.equals(DIV) || t.kind.equals(AND) || t.kind.equals(MOD))
		{
		 t2 = t;
		 consume();
		 ex2 =  factor();
		 ex1 = new BinaryExpression(t1,ex1,t2,ex2);
		}
		return ex1;
	}

	Expression factor() throws SyntaxException {
		Expression ex1=null;
		Token t1 = t;
		Kind kind = t.kind;
		switch (kind) {
		case IDENT: {
			ex1 = new IdentExpression(t1);
			consume();
		}
			break;
		case INT_LIT: {
			ex1 = new IntLitExpression(t1);
			consume();
		}
			break;
		case KW_TRUE:
		case KW_FALSE: {
			ex1 = new BooleanLitExpression(t1);
			consume();
		}
			break;
		case KW_SCREENWIDTH:
		case KW_SCREENHEIGHT: {
			ex1 = new ConstantExpression(t1);
			consume();
		} 
			break;
		case LPAREN: {
			consume();
			ex1 = expression();
			match(RPAREN);
		}
			break;
		default:
			throw new SyntaxException("illegal factor");
		}
		return ex1;
	}
	
	WhileStatement whileStatement() throws SyntaxException
	{
		Token k=t;
		Expression expr=null;
		Block blo=null;
		if(t.kind.equals(KW_WHILE))
		{
			consume();
		}
		match(LPAREN);
		expr=expression();
		match(RPAREN);
		blo=block();
		return new WhileStatement(k, expr, blo);
		
	}//end whileStatement
	
	IfStatement ifStatement() throws SyntaxException
	{
		Token k=t;
		Expression expr=null;
		Block blo=null;
		if(t.kind.equals(KW_IF))
		{
			consume();
		}
		match(LPAREN);
		expr=expression();
		match(RPAREN);
		blo=block();
		return new IfStatement(k, expr, blo);
		
	}//end ifStatement
	
	Token arrowOp() throws SyntaxException
	{
		Kind kind = t.kind;
		switch(kind)
		{
		case ARROW:
		{
			Token t1=t;
			consume();
			return t1;
		}//end arrow
		case BARARROW:
		{
			Token t1 = t;
			consume();
			return t1;
		} //end bararrow
		default:
	    	throw new SyntaxException("illegal arrowOp");
		}//end switch 
	} //end arrowOp

	Block block() throws SyntaxException 
	{ 
		ArrayList<Dec> dec = new ArrayList<Dec>();
		ArrayList<Statement>  stlst = new ArrayList<Statement>();
		Token t1 = t;
		if(t.kind.equals(LBRACE))
		{
			consume();
		
			while(t.kind.equals(KW_INTEGER) || t.kind.equals(KW_BOOLEAN) || t.kind.equals(KW_IMAGE) || t.kind.equals(KW_FRAME) || //dec
					t.kind.equals(OP_SLEEP) || t.kind.equals(KW_WHILE) || t.kind.equals(KW_IF) || t.kind.equals(IDENT) || //statement
					t.kind.equals(OP_BLUR) || t.kind.equals(OP_GRAY) || t.kind.equals(OP_CONVOLVE) || t.kind.equals(KW_SHOW) || t.kind.equals(KW_HIDE) || t.kind.equals(KW_MOVE) || t.kind.equals(KW_XLOC) || 
					t.kind.equals(KW_YLOC) || t.kind.equals(OP_WIDTH) || t.kind.equals(OP_HEIGHT) || t.kind.equals(KW_SCALE))
			{
				
				if(t.kind.equals(KW_INTEGER) || t.kind.equals(KW_BOOLEAN) || t.kind.equals(KW_IMAGE) || t.kind.equals(KW_FRAME) )
				{
					dec.add(dec());
				}
				else
				{ 
					stlst.add(statement());
					
				}
				
			}
			
				match(RBRACE);
				return new Block(t1,dec,stlst);

		}
		else
		{
		throw new SyntaxException("illegal block");
		}
	}

	Program program() throws SyntaxException 
	{
		ArrayList<ParamDec> prmdec = new ArrayList<ParamDec>();
		Token t1 = t;
		Block block = null;
		if(t.kind.equals(IDENT))
		{
			consume();
			if(t.kind.equals(LBRACE))
			{
				block = block();
				
			}
			else
			{
				prmdec.add(paramDec());
				while(t.kind.equals(COMMA))
				{
					consume();
					prmdec.add(paramDec());
				}
				block = block();
			}
			return new Program(t1,prmdec,block);
		}
		else
		{
		throw new SyntaxException("illegal program");
		}
	}
	AssignmentStatement assign() throws SyntaxException
	{   Token tok=t;
		IdentLValue idlve=new IdentLValue(t);
		Expression expr=null;
		match(IDENT);
		match(ASSIGN);
		expr=expression();
		return new AssignmentStatement(tok, idlve, expr);
	}// end assign
	ParamDec paramDec() throws SyntaxException 
	{
		Token t1 = null;
		Token t2 = null;
		t1 = t;
		if(t.kind.equals(KW_URL) || t.kind.equals(KW_FILE) || t.kind.equals(KW_INTEGER) || t.kind.equals(KW_BOOLEAN))
		{
			consume();
			t2 = t;
			match(IDENT);
			return new ParamDec(t1,t2);
		}
		else
		{
		throw new SyntaxException("illegal Statement");
		}
	}

	Dec dec() throws SyntaxException 
	{
		Token t1 = null;
		Token t2 = null;
		t1 = t;
		if(t.kind.equals(KW_INTEGER) || t.kind.equals(KW_BOOLEAN) || t.kind.equals(KW_IMAGE) || t.kind.equals(KW_FRAME))
		{
			consume();
			t2 = t;
			match(IDENT);
			return new Dec(t1,t2);
		}
		else
		{
		throw new SyntaxException("illegal dec");
		}
	}//end dec

	Statement statement() throws SyntaxException 
	{
		Token t1 = t;
		Expression expr = null;
		Kind kind = t.kind;
		switch(kind)
		{
		case OP_SLEEP:
		{
			consume();
			expr = expression();
			match(SEMI);
			return new SleepStatement(t1,expr);
		}
		case KW_WHILE:
		{
			return whileStatement();
		}
		
		case KW_IF:
		{
			return ifStatement();
		}//end ifStatement
		
		case IDENT:
		{
			if(scanner.peek().kind.equals(ASSIGN))
			{
				AssignmentStatement as=null;
				as=assign();
				match(SEMI);
				return as;
			}
			else if(scanner.peek().kind.equals(ARROW)||scanner.peek().kind.equals(BARARROW))
			{
				Statement s=null;
				s=chain();
				match(SEMI);
				return s;
			}
			else
				throw new SyntaxException("");

		}
		case OP_BLUR:
		case OP_GRAY:
		case OP_CONVOLVE:
		{
			Statement s=chain();
			match(SEMI);
			return s;
		}
		case KW_SHOW:
		case KW_HIDE:
		case KW_MOVE:
		case KW_XLOC:
		case KW_YLOC:
		{
			Statement s=chain();
			match(SEMI);
			return s;
		}
		case OP_WIDTH:
		case OP_HEIGHT:
		case KW_SCALE:
		{
			Statement s=chain();
			match(SEMI);
			return s;
		}
		default:
			throw new SyntaxException("illegal Statement");
		}//end switch
	}

	Chain chain() throws SyntaxException 
	{
		Token t1 = t;
		Chain chain = null;
		chain = chainElem();
		Token t2 = arrowOp();
		ChainElem chainElem =  chainElem();
		Chain chain1 = new BinaryChain(t1,chain,t2,chainElem);
		while(t.kind.equals(ARROW) || t.kind.equals(BARARROW))	
		{
			Token t3 = arrowOp();
			ChainElem chainElem2 = chainElem();
			chain1 = new BinaryChain(t1,chain1,t3,chainElem2);
		}
		return chain1;
	}

	ChainElem chainElem() throws SyntaxException 
	{
		Token t1 = null;
		ChainElem chainElem = null;
		Kind kind = t.kind;
		Tuple arg = null;
		switch(kind)
		{
		case IDENT:
		{
			chainElem = new IdentChain(t1);
			consume();
			return chainElem;
			
		}
		case OP_BLUR:
		{
			Token t2=t;
			consume();
			arg=arg();
			chainElem=new FilterOpChain(t2,arg);
			return chainElem;
		}//end OP_BLUR
		case OP_GRAY:
		{
			Token t2 = t;
			consume();
			arg = arg();
			chainElem=new FilterOpChain(t2,arg);
			return chainElem;
		}//end OP_GRAY
		case OP_CONVOLVE:
		{
			Token t2 = t;
			consume();
			arg = arg();
			chainElem=new FilterOpChain(t2,arg);
			return chainElem;
		}
	//end op_convolve
		case KW_SHOW:
		{
			Token t2 = t;
			consume();
			arg = arg();
			return new FrameOpChain(t2,arg);
		}//end KW_SHOW
		case KW_HIDE:
		{
			Token t2 = t;
			consume();
			arg = arg();
			return new FrameOpChain(t2,arg);
		}//end KW_HIDE
		case KW_MOVE:
		{
			Token t2 = t;
			consume();
			arg = arg();
			return new FrameOpChain(t2,arg);
		}//end KW_MOVE
		case KW_XLOC:
		{
			Token t2 = t;
			consume();
			arg = arg();
			return new FrameOpChain(t2,arg);
		}//end KW_XLOC
		case KW_YLOC:
		{
			Token t2 = t;
			consume();
			arg = arg();
			return new FrameOpChain(t2,arg);
		}//end KW_YLOC
		case OP_WIDTH:
		{
			Token t2 = t;
			consume();
			arg = arg();
			return new ImageOpChain(t2,arg);
		}//end Op_width
		case OP_HEIGHT:
		{
			Token t2 = t;
			consume();
			arg = arg();
			return new ImageOpChain(t2,arg);
		}//end Op_height
		case KW_SCALE:
		{
			Token t2 = t;
			consume();
			arg = arg();
			return new ImageOpChain(t2,arg);
		}//end KW_scale
		default:
			throw new SyntaxException("illegal Statement");
		}
	}//end chainElem

	Tuple arg() throws SyntaxException 
	{
		Token t1 = null;
		ArrayList<Expression> exprlist = new ArrayList<Expression>();
		Kind kind = t.kind;
		t1 = t;
		if(!t.kind.equals(LPAREN))
		{
			return new Tuple(t1,exprlist);
		}
		else if(t.kind.equals(LPAREN))
		{
			consume();
		    exprlist.add(expression());
		
			while(t.kind.equals(COMMA))
			{
				consume();
				exprlist.add(expression());
			}
			match(RPAREN);
			return new Tuple(t1,exprlist);
		}
		else
		{
			throw new SyntaxException("");
		}
	
		}
	
	Token strongOp() throws SyntaxException 
	{
		Token t1 = null;
		Kind kind = t.kind;
		switch(kind)
		{
		case TIMES:
		{
			t1=t;
			consume();
			return t1;
		}//end Times
		case DIV:
		{
			t1 = t;
			consume();
			return t1;
			
		}//end Div
		case AND:
		{
			t1 = t;
			consume();
			return t1;
		}//end And
		case MOD:
		{
			t1 = t;
			consume();
			return t1;
		}//end Mod
		default:
			throw new SyntaxException("illegal Statement");
		} // end switch 
	}//end strongOp
	
	Token weakOp() throws SyntaxException
	{
		Token t1 = null;
		Kind kind = t.kind;
		switch(kind)
		{
		case PLUS:
		{
			t1 = t;
			consume();
			return t1;
		}//end Plus
		case MINUS:
		{
			t1 = t;
			consume();
			return t1;
		}//end Minus
		case OR:
		{
			t1 = t;
			consume();
			return t1;
		}//end Or
		default:
			throw new SyntaxException("illegal Statement");

		}//end switch
	}//end weakOp
	
	Token relOp() throws SyntaxException
	{
		Token t1 = null;
		Kind kind = t.kind;
		switch(kind)
		{
		case LT:
		{
			t1 = t;
			consume();
			return t1;
		}//end LT
		case LE:
		{
			t1 = t;
			consume();
			return t1;
		}// end LE
		case GT:
		{
			t1 = t;
			consume();
			return t1;
		}//end GT
		case GE:
		{
			t1 = t;
			consume();
			return t1;
		}//end GE
		case EQUAL:
		{
			t1 = t;
			consume();
			return t1;
		}//end Equal
		case NOTEQUAL:
		{
			t1 = t;
			consume();
			return t1;
		}//end NotEqual
		default:
			throw new SyntaxException("illegal Statement");
		}//end switch			
	} //end relOp
	
	Token imageOp() throws SyntaxException
	{
		Token t1 = null;
		Kind kind = t.kind;
		switch(kind)
		{
		case OP_WIDTH:
		{
			t1 = t;
			consume();
			return t1;
		}//end Op_width
		case OP_HEIGHT:
		{
			t1 = t;
			consume();
			return t1;
		}//end Op_height
		case KW_SCALE:
		{
			t1 = t;
			consume();
			return t1;
		}//end KW_scale
		default:
			throw new SyntaxException("illegal Statement");
		}//end switch
	}//end imageOP
	
	Token frameOp() throws SyntaxException
	{
		Token t1 = null;
		Kind kind = t.kind;
		switch(kind)
		{
		case KW_SHOW:
		{
			t1 = t;
			consume();
			return t1;
		}//end KW_SHOW
		case KW_HIDE:
		{
			t1 = t;
			consume();
			return t1;
		}//end KW_HIDE
		case KW_MOVE:
		{
			t1 = t;
			consume();
			return t1;
		}//end KW_MOVE
		case KW_XLOC:
		{
			t1 = t;
			consume();
			return t1;
		}//end KW_XLOC
		case KW_YLOC:
		{
			t1 = t;
			consume();
			return t1;
		}//end KW_YLOC
		default:
			throw new SyntaxException("illegal Statement");
		}//end switch
	}//end frameOp
	
	Token filterOp() throws SyntaxException
	{
		Token t1 = null;
		Kind kind = t.kind;
		switch(kind)
		{
		case OP_BLUR:
		{
			t1 = t;
			consume();
			return t1;
		}//end OP_BLUR
		case OP_GRAY:
		{
			t1 = t;
			consume();
			return t1;
		}//end OP_GRAY
		case OP_CONVOLVE:
		{
			t1 = t;
			consume();
			return t1;
		}//end op_convolve
		default:
			throw new SyntaxException("illegal Statement");
		}//end switch
	}// end filterOp

	/**
	 * Checks whether the current token is the EOF token. If not, a
	 * SyntaxException is thrown.
	 * 
	 * @return
	 * @throws SyntaxException
	 */
	private Token matchEOF() throws SyntaxException {
		if (t.kind.equals(EOF)) {
			return t;
		}
		throw new SyntaxException("expected EOF");
	}

	/**
	 * Checks if the current token has the given kind. If so, the current token
	 * is consumed and returned. If not, a SyntaxException is thrown.
	 * 
	 * Precondition: kind != EOF
	 * 
	 * @param kind
	 * @return
	 * @throws SyntaxException
	 */
	private Token match(Kind kind) throws SyntaxException {
		if (t.kind.equals(kind)) {
			return consume();
		}
		throw new SyntaxException("saw " + t.kind + "expected " + kind);
	}

	/**
	 * Checks if the current token has one of the given kinds. If so, the
	 * current token is consumed and returned. If not, a SyntaxException is
	 * thrown.
	 * 
	 * * Precondition: for all given kinds, kind != EOF
	 * 
	 * @param kinds
	 *            list of kinds, matches any one
	 * @return
	 * @throws SyntaxException
	 */
	private Token match(Kind... kinds) throws SyntaxException {
		// TODO. Optional but handy
		return null; //replace this statement
	}

	/**
	 * Gets the next token and returns the consumed token.
	 * 
	 * Precondition: t.kind != EOF
	 * 
	 * @return
	 * 
	 */
	private Token consume() throws SyntaxException {
		Token tmp = t;
		//System.out.println(t.kind);
		t = scanner.nextToken();
		return tmp;
	}

}
