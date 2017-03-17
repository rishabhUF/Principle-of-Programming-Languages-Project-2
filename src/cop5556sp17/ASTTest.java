package cop5556sp17;

import static cop5556sp17.Scanner.Kind.PLUS;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556sp17.Parser.SyntaxException;
import cop5556sp17.Scanner.IllegalCharException;
import cop5556sp17.Scanner.IllegalNumberException;
import cop5556sp17.Scanner.Kind;
import cop5556sp17.Scanner.Token;
import cop5556sp17.AST.ASTNode;
import cop5556sp17.AST.AssignmentStatement;
import cop5556sp17.AST.BinaryExpression;
import cop5556sp17.AST.Block;
import cop5556sp17.AST.BooleanLitExpression;
import cop5556sp17.AST.ConstantExpression;
import cop5556sp17.AST.Dec;
import cop5556sp17.AST.FilterOpChain;
import cop5556sp17.AST.IdentChain;
import cop5556sp17.AST.IdentExpression;
import cop5556sp17.AST.IdentLValue;
import cop5556sp17.AST.IntLitExpression;
import cop5556sp17.AST.ParamDec;
import cop5556sp17.AST.Program;
import cop5556sp17.AST.Statement;
import cop5556sp17.AST.Tuple;

public class ASTTest {

	static final boolean doPrint = true;
	static void show(Object s){
		if(doPrint){System.out.println(s);}
	}
	

	@Rule
	public ExpectedException thrown = ExpectedException.none();
/*
	@Test
	public void testFactor0() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(IdentExpression.class, ast.getClass());
	}

	@Test
	public void testFactor1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "123";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(IntLitExpression.class, ast.getClass());
	}



	@Test
	public void testBinaryExpr0() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "1+abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BinaryExpression.class, ast.getClass());
		BinaryExpression be = (BinaryExpression) ast;
		assertEquals(IntLitExpression.class, be.getE0().getClass());
		assertEquals(IdentExpression.class, be.getE1().getClass());
		assertEquals(PLUS, be.getOp().kind);
	}


	@Test
	public void testProgram1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc {}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.program();
		assertEquals(Program.class,ast.getClass());
		Program be = (Program) ast;
		assertEquals(Kind.IDENT,be.getFirstToken().kind);
		assertEquals(java.util.ArrayList.class, be.getParams().getClass());
		assertEquals(Block.class, be.getB().getClass());
		Block b = be.getB();
		assertEquals("{",b.getFirstToken().getText());
		assertEquals(new ArrayList<Statement>(),b.getStatements());
		
	}
	
	@Test
	public void testProgram2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "file abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.program();
	}
	
	@Test
	public void testProgram3() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "mnv {integer nn}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.program();
		assertEquals(Program.class,ast.getClass());
		Program be = (Program) ast;
		assertEquals(Kind.IDENT,be.getFirstToken().kind);
		assertEquals(java.util.ArrayList.class, be.getParams().getClass());
		assertEquals(Block.class, be.getB().getClass());
		Block b = be.getB();
		assertEquals("{",b.getFirstToken().getText());
		Dec a = b.getDecs().get(0);
		assertEquals(Kind.KW_INTEGER,a.getFirstToken().kind);
		assertEquals("nn",a.getIdent().getText());
		
	}
	
	@Test
	public void testProgram4() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "mnv {grayish <-  998|(abcgg*77%true+9<=false<98>=abc>nncd8==(a!=b));}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.program();
		assertEquals(Program.class,ast.getClass());
		Program be = (Program) ast;
		assertEquals(Kind.IDENT,be.getFirstToken().kind);
		assertEquals(java.util.ArrayList.class, be.getParams().getClass());
		assertEquals(Block.class, be.getB().getClass());
		Block b = be.getB();
		assertEquals("{",b.getFirstToken().getText());
		AssignmentStatement s = (AssignmentStatement) b.getStatements().get(0);
		assertEquals(Kind.IDENT,be.getFirstToken().kind);
		assertEquals(IdentLValue.class, s.getVar().getClass());
		assertEquals("grayish", s.getVar().getText());
		assertEquals(BinaryExpression.class, s.getE().getClass());
		
	}
	
	@Test
	public void testProgram5() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "mnddv {integer nn grayish <-  998|(abcgg*77%true+9<=false<98>=abc>nncd8==(a!=b));}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.program();
		assertEquals(Program.class,ast.getClass());
		Program be = (Program) ast;
		assertEquals(Kind.IDENT,be.getFirstToken().kind);
		assertEquals("mnddv",be.getFirstToken().getText());
		assertEquals(java.util.ArrayList.class, be.getParams().getClass());
		assertEquals(Block.class, be.getB().getClass());
		Block b = be.getB();
		assertEquals("{",b.getFirstToken().getText());
		Dec a = b.getDecs().get(0);
		assertEquals(Kind.KW_INTEGER,a.getFirstToken().kind);
		assertEquals("nn",a.getIdent().getText());
		AssignmentStatement s = (AssignmentStatement) b.getStatements().get(0);
		assertEquals(Kind.IDENT,be.getFirstToken().kind);
		assertEquals(IdentLValue.class, s.getVar().getClass());
		assertEquals("grayish", s.getVar().getText());
		assertEquals(BinaryExpression.class, s.getE().getClass());
	}
	
	@Test
	public void testProgram6() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "mnv url fcv {}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.program();
		assertEquals(Program.class,ast.getClass());
		Program be = (Program) ast;
		assertEquals(Kind.IDENT,be.getFirstToken().kind);
		assertEquals("mnv",be.getFirstToken().getText());
		assertEquals(java.util.ArrayList.class, be.getParams().getClass());
		assertEquals(Block.class, be.getB().getClass());
		Block b = be.getB();
		ParamDec p = be.getParams().get(0);
		assertEquals(Kind.KW_URL,p.getFirstToken().kind);
		assertEquals("fcv",p.getIdent().getText());
		assertEquals("{",b.getFirstToken().getText());
	}
	

	@Test
	public void testProgram7() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "mnddv integer kkk,file vfrh {image hello}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.program();
		assertEquals(Program.class,ast.getClass());
		Program be = (Program) ast;
		assertEquals(Kind.IDENT,be.getFirstToken().kind);
		assertEquals("mnddv",be.getFirstToken().getText());
		assertEquals(java.util.ArrayList.class, be.getParams().getClass());
		assertEquals(Block.class, be.getB().getClass());
		Block b = be.getB();
		ParamDec p = be.getParams().get(0);
		assertEquals(Kind.KW_INTEGER,p.getFirstToken().kind);
		assertEquals("kkk",p.getIdent().getText());
		p = be.getParams().get(1);
		assertEquals(Kind.KW_FILE,p.getFirstToken().kind);
		assertEquals("vfrh",p.getIdent().getText());
		
		assertEquals("{",b.getFirstToken().getText());
		Dec a = b.getDecs().get(0);
		assertEquals(Kind.KW_IMAGE,a.getFirstToken().kind);
		assertEquals("hello",a.getIdent().getText());
	}
	
	@Test
	public void testProgram8() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "url fcv {}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.program();
	}

	@Test
	public void testProgram9() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "mnddv ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.program();
	}
	
	@Test
	public void testProgram10() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "mnv url fcv ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.program();
	}
	

	@Test
	public void testProgram11() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "mnddv {hello}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.program();
	}
	
	@Test
	public void testProgram12() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "mnv url fcv, {}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.program();
	}
	
	@Test
	public void testBinaryExpr3() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "   (1+ab) * 3";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BinaryExpression.class, ast.getClass());
		BinaryExpression be = (BinaryExpression) ast;
		assertEquals(Kind.LPAREN, be.getFirstToken().kind);
		assertEquals(BinaryExpression.class, be.getE0().getClass());
		assertEquals(IntLitExpression.class, be.getE1().getClass());
		assertEquals(Kind.TIMES, be.getOp().kind);
		BinaryExpression be1 = (BinaryExpression) be.getE0();
		assertEquals(IntLitExpression.class, be1.getE0().getClass());
		assertEquals(IdentExpression.class, be1.getE1().getClass());
		assertEquals(PLUS, be1.getOp().kind);
		assertEquals(1, be1.getFirstToken().intVal());
	}
	
	@Test
	public void testBinaryExpr4() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "   1+ab * 3";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BinaryExpression.class, ast.getClass());
		BinaryExpression be = (BinaryExpression) ast;
		assertEquals(1, be.getFirstToken().intVal());
		assertEquals(IntLitExpression.class, be.getE0().getClass());
		assertEquals(BinaryExpression.class, be.getE1().getClass());
		assertEquals(PLUS, be.getOp().kind);
		BinaryExpression be1 = (BinaryExpression) be.getE1();
		assertEquals(IdentExpression.class, be1.getE0().getClass());
		assertEquals(IntLitExpression.class, be1.getE1().getClass());
		assertEquals(Kind.TIMES, be1.getOp().kind);
		assertEquals("ab", be1.getFirstToken().getText());
	}
*/	
	

	@Test
	public void testFactor0() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(IdentExpression.class, ast.getClass());
	}

	@Test
	public void testFactor1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "123";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(IntLitExpression.class, ast.getClass());
	}



	@Test
	public void testBinaryExpr0() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "1+abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BinaryExpression.class, ast.getClass());
		BinaryExpression be = (BinaryExpression) ast;
		assertEquals(IntLitExpression.class, be.getE0().getClass());
		assertEquals(IdentExpression.class, be.getE1().getClass());
		assertEquals(PLUS, be.getOp().kind);
	}
	
	@Test
	public void testArg0() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "  (3,5) ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.arg();
		assertEquals(Tuple.class, ast.getClass());
		Tuple tu = (Tuple) ast;
		assertEquals(IntLitExpression.class, tu.getExprList().get(0).getClass());
		assertEquals(IntLitExpression.class, tu.getExprList().get(1).getClass());
	}
	
	@Test
	public void testArg1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "  (3,) ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		ASTNode ast = parser.arg();
	}

	@Test
	public void testProgram0() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "prog0 {}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.program();
		assertEquals(Program.class, ast.getClass());
		Program pr = (Program) ast;
		assertEquals(Kind.IDENT, pr.getFirstToken().kind);
		assertEquals(Block.class, pr.getB().getClass());
	}
	
	@Test
	public void testFactor() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "()";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		ASTNode ast = parser.factor();	
	}
	
	@Test
	public void testFactor2() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "screenheight";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.factor();
		assertEquals(ConstantExpression.class, ast.getClass());
	}
	
	@Test
	public void testFactor3() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "screenheight1234";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.factor();
		assertEquals(IdentExpression.class, ast.getClass());
	}
	
	@Test
	public void testFactor4() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "screenheight 1234"; //Note that 1234 will not be parsed with this method call
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.factor();
		assertEquals(ConstantExpression.class, ast.getClass());
	}
	
	@Test
	public void testElem() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "(abc*1234%5/def&true)";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.elem();
		assertEquals(BinaryExpression.class, ast.getClass());
		BinaryExpression binExp = (BinaryExpression) ast; //abc, *, 1234%5/def&true
		assertEquals(Kind.AND, binExp.getOp().kind); // This is an expression hence right associative
		assertEquals(BinaryExpression.class, binExp.getE0().getClass());
		assertEquals(BooleanLitExpression.class, binExp.getE1().getClass());
		BinaryExpression binExp1 = (BinaryExpression) binExp.getE0(); //1234, %, 5/def&true
		assertEquals(IdentExpression.class, binExp1.getE1().getClass());
		assertEquals(Kind.DIV, binExp1.getOp().kind);
		BinaryExpression binExp2 = (BinaryExpression) binExp1.getE0(); //1234, %, 5/def&true
		assertEquals(IntLitExpression.class, binExp2.getE1().getClass());
		assertEquals(Kind.MOD, binExp2.getOp().kind);
		BinaryExpression binExp3 = (BinaryExpression) binExp2.getE0(); //1234, %, 5/def&true
		assertEquals(IdentExpression.class, binExp3.getE0().getClass());
		assertEquals(IntLitExpression.class, binExp3.getE1().getClass());
		assertEquals(Kind.TIMES, binExp3.getOp().kind);
		
	}
	
	@Test
	public void testElem1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "(abc*1234%5/def&&true)"; // strongOp not followed by factor
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		ASTNode ast = parser.elem();
	}
	
	@Test
	public void testTerm() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "(abc*1234%5/def&true)";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.term();
		assertEquals(BinaryExpression.class, ast.getClass());
		BinaryExpression binExp = (BinaryExpression) ast;
		assertEquals(Kind.AND, binExp.getOp().kind);
		assertEquals(BooleanLitExpression.class, binExp.getE1().getClass());
		BinaryExpression binExp1 = (BinaryExpression) binExp.getE0();
		assertEquals(IdentExpression.class, binExp1.getE1().getClass());
		assertEquals(Kind.DIV, binExp1.getOp().kind);
		BinaryExpression binExp2 = (BinaryExpression) binExp1.getE0();
		assertEquals(IntLitExpression.class, binExp2.getE1().getClass());
		assertEquals(Kind.MOD, binExp2.getOp().kind);
		BinaryExpression binExp3 = (BinaryExpression) binExp2.getE0();
		assertEquals(IntLitExpression.class, binExp3.getE1().getClass());
		assertEquals(IdentExpression.class, binExp3.getE0().getClass());
		assertEquals(Kind.TIMES, binExp3.getOp().kind);
		
	}
	
	@Test
	public void testTerm1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc*1234%5/def&true|false&21+121-something";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.term(); //(abc*1234%5/def&true, |, false&21)+121-something
		assertEquals(BinaryExpression.class, ast.getClass());
		BinaryExpression binExp = (BinaryExpression) ast;
		assertEquals(Kind.MINUS, binExp.getOp().kind);
		assertEquals(BinaryExpression.class, binExp.getE0().getClass());
		BinaryExpression binExp1 = (BinaryExpression) binExp.getE0();
		assertEquals(BinaryExpression.class, binExp1.getE0().getClass());
		assertEquals(IntLitExpression.class, binExp1.getE1().getClass());
		assertEquals(Kind.PLUS, binExp1.getOp().kind);
		BinaryExpression binExp2 = (BinaryExpression) binExp1.getE0();
		assertEquals(BinaryExpression.class, binExp2.getE0().getClass());
		assertEquals(Kind.OR, binExp2.getOp().kind);
	}
	
	@Test
	public void testTerm2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc*1234%5/def&true|false&21+121-something/";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		ASTNode ast = parser.term();
	}
	
	@Test
	public void testTerm3() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc*1234%5/def&true|false&21+121-something and someMorething";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.term(); //abc*1234%5/def&true, |, false&21+121-something  // will stop at abc*1234%5/def&true|false&21+121-something 
		assertEquals(BinaryExpression.class, ast.getClass());
		BinaryExpression binExp = (BinaryExpression) ast;
		assertEquals(Kind.MINUS, binExp.getOp().kind);
		assertEquals(BinaryExpression.class, binExp.getE0().getClass());
		assertEquals(IdentExpression.class, binExp.getE1().getClass());
		BinaryExpression binExp1 = (BinaryExpression) binExp.getE0();
		assertEquals(BinaryExpression.class, binExp1.getE0().getClass());
		assertEquals(IntLitExpression.class, binExp1.getE1().getClass());
		assertEquals(Kind.PLUS, binExp1.getOp().kind);
		BinaryExpression binExp2 = (BinaryExpression) binExp1.getE0();
		assertEquals(BinaryExpression.class, binExp2.getE0().getClass());
		assertEquals(Kind.OR, binExp2.getOp().kind);
	}
	
	@Test
	public void testExpression() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "(something)<=somethingMore>somethingLess!=nothing==void";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		BinaryExpression binExp = (BinaryExpression) ast;
		assertEquals(Kind.EQUAL, binExp.getOp().kind);
		assertEquals(BinaryExpression.class, binExp.getE0().getClass());
		assertEquals(IdentExpression.class, binExp.getE1().getClass());
		BinaryExpression binExp1 = (BinaryExpression) binExp.getE0();
		assertEquals(BinaryExpression.class, binExp1.getE0().getClass());
		assertEquals(IdentExpression.class, binExp1.getE1().getClass());
		assertEquals(Kind.NOTEQUAL, binExp1.getOp().kind);
		BinaryExpression binExp2 = (BinaryExpression) binExp1.getE0();
		assertEquals(BinaryExpression.class, binExp2.getE0().getClass());
		assertEquals(Kind.GT, binExp2.getOp().kind);
		BinaryExpression binExp3 = (BinaryExpression) binExp2.getE0();
		assertEquals(IdentExpression.class, binExp3.getE1().getClass());
		assertEquals(Kind.LE, binExp3.getOp().kind);
		assertEquals(IdentExpression.class, binExp3.getE0().getClass());
	}
	
	@Test
	public void testArg2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.arg();
		assertEquals(Tuple.class, ast.getClass());
	}
	
	@Test
	public void testArg3() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "(something)<=somethingMore>somethingLess!=nothing==void, (), 1234, a*b, ,";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.arg(); //(something) and stops here
		assertEquals(Tuple.class, ast.getClass());
		Tuple tuple = (Tuple) ast;
		assertEquals(IdentExpression.class, tuple.getExprList().get(0).getClass());				
	}
	
	@Test
	public void testArg4() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "(something)<=somethingMore>somethingLess!=nothing==void";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.arg(); //(something) and stops here
		assertEquals(Tuple.class, ast.getClass());
		Tuple tuple = (Tuple) ast;
		assertEquals(IdentExpression.class, tuple.getExprList().get(0).getClass());					
	}
	
	@Test
	public void testChainElem() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc blur";//blur will be ignored
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.chainElem(); //stops as abc
		assertEquals(IdentChain.class, ast.getClass());
	}
	
	@Test
	public void testChainElem1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "blur";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.chainElem();
		assertEquals(FilterOpChain.class, ast.getClass());
	}
	
	@Test
	public void testChainElem2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "blur123";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.chainElem();
		assertEquals(IdentChain.class, ast.getClass());	
	}
	
	/*@Test
	public void testIf1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "if";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		ASTNode ast = parser.ifStatement();
	}
	
	@Test
	public void testIf2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "if(){";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		ASTNode ast = parser.ifStatement();
	}
	
	@Test
	public void testIf3() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "if(){}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		ASTNode ast = parser.ifStatement();		
	}
	
	@Test
	public void testIf5() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "if(abc>=123){integer def while(1){def -> blur;}}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.ifStatement();
		assertEquals(IfStatement.class, ast.getClass());
		IfStatement ifStatement = (IfStatement) ast;
		assertEquals(BinaryExpression.class, ifStatement.getE().getClass());
		BinaryExpression binExp = (BinaryExpression) ifStatement.getE();
		assertEquals(Kind.GE, binExp.getOp().kind);
		assertEquals(Block.class, ifStatement.getB().getClass());
	}
	
	@Test
	public void testIf6() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "if(abc>=123){integer def while(1){def -> blur somethingToCauseError}}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		ASTNode ast = parser.ifStatement();	
	}
	*/
	@Test
	public void testProgram111() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "value file value1 integer value2 {}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		ASTNode ast = parser.program();			
	}
	
	@Test
	public void testProgram21() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "value file value1, integer value2 {}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.program();
		assertEquals(Program.class, ast.getClass());
	}
	
	@Test
	public void testProgram31() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input2 = "value file value1, image value2 {}";
		Parser parser2 = new Parser(new Scanner(input2).scan());
		thrown.expect(Parser.SyntaxException.class);
		parser2.parse();
	}
	
	@Test
	public void testNested() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "value file value1, integer value2 {if((something)>=(SomethingElse)){sleep sleepExp; \r\n gray(somethings, again, and, again1)->screenwidth345;}}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		
		for(Token t:scanner.tokens)
		{
			System.out.println(t.kind);
		}
		ASTNode ast = parser.program();
		assertEquals(Program.class, ast.getClass());
	}
	
	@Test
	public void testNested1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input1 = "value file value1, integer value2 {if((something)>=(SomethingElse)){sleep sleepExp; \r\n gray(somethings, again, and, again1)->screenwidth345;}}}";
		Parser parser1 = new Parser(new Scanner(input1).scan());
		thrown.expect(Parser.SyntaxException.class);
		parser1.parse();		
	}
	@Test
	public void testProgram1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc {}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.program();
		assertEquals(Program.class,ast.getClass());
		Program be = (Program) ast;
		assertEquals(Kind.IDENT,be.getFirstToken().kind);
		assertEquals(java.util.ArrayList.class, be.getParams().getClass());
		assertEquals(Block.class, be.getB().getClass());
		Block b = be.getB();
		assertEquals("{",b.getFirstToken().getText());
		assertEquals(new ArrayList<Statement>(),b.getStatements());
		
	}
	
	@Test
	public void testProgram2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "file abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.program();
	}
	
	@Test
	public void testProgram3() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "mnv {integer nn}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.program();
		assertEquals(Program.class,ast.getClass());
		Program be = (Program) ast;
		assertEquals(Kind.IDENT,be.getFirstToken().kind);
		assertEquals(java.util.ArrayList.class, be.getParams().getClass());
		assertEquals(Block.class, be.getB().getClass());
		Block b = be.getB();
		assertEquals("{",b.getFirstToken().getText());
		Dec a = b.getDecs().get(0);
		assertEquals(Kind.KW_INTEGER,a.getFirstToken().kind);
		assertEquals("nn",a.getIdent().getText());
		
	}
	
	@Test
	public void testProgram4() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "mnv {grayish <-  998|(abcgg*77%true+9<=false<98>=abc>nncd8==(a!=b));}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.program();
		assertEquals(Program.class,ast.getClass());
		Program be = (Program) ast;
		assertEquals(Kind.IDENT,be.getFirstToken().kind);
		assertEquals(java.util.ArrayList.class, be.getParams().getClass());
		assertEquals(Block.class, be.getB().getClass());
		Block b = be.getB();
		assertEquals("{",b.getFirstToken().getText());
		AssignmentStatement s = (AssignmentStatement) b.getStatements().get(0);
		assertEquals(Kind.IDENT,be.getFirstToken().kind);
		assertEquals(IdentLValue.class, s.getVar().getClass());
		assertEquals("grayish", s.getVar().getText());
		assertEquals(BinaryExpression.class, s.getE().getClass());
		
	}
	
	@Test
	public void testProgram5() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "mnddv {integer nn grayish <-  998|(abcgg*77%true+9<=false<98>=abc>nncd8==(a!=b));}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.program();
		assertEquals(Program.class,ast.getClass());
		Program be = (Program) ast;
		assertEquals(Kind.IDENT,be.getFirstToken().kind);
		assertEquals("mnddv",be.getFirstToken().getText());
		assertEquals(java.util.ArrayList.class, be.getParams().getClass());
		assertEquals(Block.class, be.getB().getClass());
		Block b = be.getB();
		assertEquals("{",b.getFirstToken().getText());
		Dec a = b.getDecs().get(0);
		assertEquals(Kind.KW_INTEGER,a.getFirstToken().kind);
		assertEquals("nn",a.getIdent().getText());
		AssignmentStatement s = (AssignmentStatement) b.getStatements().get(0);
		assertEquals(Kind.IDENT,be.getFirstToken().kind);
		assertEquals(IdentLValue.class, s.getVar().getClass());
		assertEquals("grayish", s.getVar().getText());
		assertEquals(BinaryExpression.class, s.getE().getClass());
	}
	
	@Test
	public void testProgram6() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "mnv url fcv {}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.program();
		assertEquals(Program.class,ast.getClass());
		Program be = (Program) ast;
		assertEquals(Kind.IDENT,be.getFirstToken().kind);
		assertEquals("mnv",be.getFirstToken().getText());
		assertEquals(java.util.ArrayList.class, be.getParams().getClass());
		assertEquals(Block.class, be.getB().getClass());
		Block b = be.getB();
		ParamDec p = be.getParams().get(0);
		assertEquals(Kind.KW_URL,p.getFirstToken().kind);
		assertEquals("fcv",p.getIdent().getText());
		assertEquals("{",b.getFirstToken().getText());
	}
	

	@Test
	public void testProgram7() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "mnddv integer kkk,file vfrh {image hello}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.program();
		assertEquals(Program.class,ast.getClass());
		Program be = (Program) ast;
		assertEquals(Kind.IDENT,be.getFirstToken().kind);
		assertEquals("mnddv",be.getFirstToken().getText());
		assertEquals(java.util.ArrayList.class, be.getParams().getClass());
		assertEquals(Block.class, be.getB().getClass());
		Block b = be.getB();
		ParamDec p = be.getParams().get(0);
		assertEquals(Kind.KW_INTEGER,p.getFirstToken().kind);
		assertEquals("kkk",p.getIdent().getText());
		p = be.getParams().get(1);
		assertEquals(Kind.KW_FILE,p.getFirstToken().kind);
		assertEquals("vfrh",p.getIdent().getText());
		
		assertEquals("{",b.getFirstToken().getText());
		Dec a = b.getDecs().get(0);
		assertEquals(Kind.KW_IMAGE,a.getFirstToken().kind);
		assertEquals("hello",a.getIdent().getText());
	}
	
	@Test
	public void testProgram8() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "url fcv {}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.program();
	}

	@Test
	public void testProgram9() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "mnddv ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.program();
	}
	
	@Test
	public void testProgram10() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "mnv url fcv ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.program();
	}
	

	@Test
	public void testProgram11() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "mnddv {hello}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.program();
	}
	
	@Test
	public void testProgram12() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "mnv url fcv, {}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.program();
	}
	

	@Test
	public void testParse1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input2 = "prog2 {integer a };";
		Parser parser2 = new Parser(new Scanner(input2).scan());
		thrown.expect(Parser.SyntaxException.class);
		parser2.parse();
	}
	
	@Test
	public void testParse2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input2 = "123(";
		Parser parser2 = new Parser(new Scanner(input2).scan());
		thrown.expect(Parser.SyntaxException.class);
		parser2.chainElem();
	}
}

	
