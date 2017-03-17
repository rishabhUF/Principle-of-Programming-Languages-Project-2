package cop5556sp17;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.HashMap;

import cop5556sp17.Scanner.Kind;
import cop5556sp17.Scanner.Token;

public class Scanner {
	/**
	 * Kind enum
	 */
	public enum State
	{
		START,	
		AFTER_EQ,
		IDENT,
		IN_DIGIT,
		IN_IDENT,
		AFTER_NOT,
		LESS_THAN,
		MINUS,
		GREATER_THAN,
		OR,EOR,AFTER_DIVISION,COMMENTS;

	}
	HashMap<String, Kind> hmap = new HashMap<String, Kind>();
	ArrayList<Integer> line_position = new ArrayList<Integer>();

	public static enum Kind {
		IDENT(""), INT_LIT(""), KW_INTEGER("integer"), KW_BOOLEAN("boolean"), 
		KW_IMAGE("image"), KW_URL("url"), KW_FILE("file"), KW_FRAME("frame"), 
		KW_WHILE("while"), KW_IF("if"), KW_TRUE("true"), KW_FALSE("false"), 
		SEMI(";"), COMMA(","), LPAREN("("), RPAREN(")"), LBRACE("{"), 
		RBRACE("}"), ARROW("->"), BARARROW("|->"), OR("|"), AND("&"), 
		EQUAL("=="), NOTEQUAL("!="), LT("<"), GT(">"), LE("<="), GE(">="), 
		PLUS("+"), MINUS("-"), TIMES("*"), DIV("/"), MOD("%"), NOT("!"), 
		ASSIGN("<-"), OP_BLUR("blur"), OP_GRAY("gray"), OP_CONVOLVE("convolve"), 
		KW_SCREENHEIGHT("screenheight"), KW_SCREENWIDTH("screenwidth"), 
		OP_WIDTH("width"), OP_HEIGHT("height"), KW_XLOC("xloc"), KW_YLOC("yloc"), 
		KW_HIDE("hide"), KW_SHOW("show"), KW_MOVE("move"), OP_SLEEP("sleep"), 
		KW_SCALE("scale"), EOF("eof");

		Kind(String text) {
			this.text = text;
		}

		final String text;

		String getText() {
			return text;
		}
	}
	/**
	 * Thrown by Scanner when an illegal character is encountered
	 */
	@SuppressWarnings("serial")
	public static class IllegalCharException extends Exception {
		public IllegalCharException(String message) {
			super(message);
		}
	}

	/**
	 * Thrown by Scanner when an int literal is not a value that can be represented by an int.
	 */
	@SuppressWarnings("serial")
	public static class IllegalNumberException extends Exception {
		public IllegalNumberException(String message){
			super(message);
		}
	}


	/**
	 * Holds the line and position in the line of a token.
	 */
	static class LinePos {
		public final int line;
		public final int posInLine;

		public LinePos(int line, int posInLine) {
			super();
			this.line = line;
			this.posInLine = posInLine;
		}

		@Override
		public String toString() {
			return "LinePos [line=" + line + ", posInLine=" + posInLine + "]";
		}
	}
	public class Token {
		public final Kind kind;
		public final int pos;  //position in input array
		public final int length;  

		//returns the text of this Token
		public String getText() {
			//TODO IMPLEMENT THIS
			//TODO Error
			if(chars == "") {
				return null;
			}
			else {
				return chars.substring(pos, pos+length);
			}
		}
		//returns a LinePos object representing the line and column of this Token
		LinePos getLinePos(){
			int line1=0;
			int pos_line=0;
			int i;
			if(line_position.size() >= 2)
			{
				for(i=1;i<line_position.size()-1;i++)
				{
					if(pos >= line_position.get(i) && pos < line_position.get(i+1))
					{
						line1 = i;
						pos_line = pos - line_position.get(i)-1;
					}
					else if(pos >= line_position.get(line_position.size()-1))
					{
						line1 = line_position.size()-1;
						pos_line = pos - line_position.get(line_position.size()-1)-1;
						
					}
				}
			}
				else
				{
					line1=0;
					pos_line=pos;
				}
			LinePos lp = new LinePos(line1,pos_line);
			return lp;
			}
		
		Token(Kind kind, int pos, int length) {
			this.kind = kind;
			this.pos = pos;
			this.length = length;
		}
		/** 
		 * Precondition:  kind = Kind.INT_LIT,  the text can be represented with a Java int.
		 * Note that the validity of the input should have been checked when the Token was created.
		 * So the exception should never be thrown.
		 * 
		 * @return  int value of this token, which should represent an INT_LIT
		 * @throws NumberFormatException
		 * @throws IllegalNumberException 
		 */
		public int intVal() throws NumberFormatException
		{
			return(Integer.parseInt(chars.substring(pos, pos+length)));

		}	
		@Override
		  public int hashCode() {
		   final int prime = 31;
		   int result = 1;
		   result = prime * result + getOuterType().hashCode();
		   result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		   result = prime * result + length;
		   result = prime * result + pos;
		   return result;
		  }

		  @Override
		  public boolean equals(Object obj) {
		   if (this == obj) {
		    return true;
		   }
		   if (obj == null) {
		    return false;
		   }
		   if (!(obj instanceof Token)) {
		    return false;
		   }
		   Token other = (Token) obj;
		   if (!getOuterType().equals(other.getOuterType())) {
		    return false;
		   }
		   if (kind != other.kind) {
		    return false;
		   }
		   if (length != other.length) {
		    return false;
		   }
		   if (pos != other.pos) {
		    return false;
		   }
		   return true;
		  }

		 

		  private Scanner getOuterType() {
		   return Scanner.this;
		  }
	}
	public Scanner(String chars) {
		this.chars = chars;
		tokens = new ArrayList<Token>();
		hmap.put("integer", Kind.KW_INTEGER);
		hmap.put("boolean",Kind.KW_BOOLEAN);
		hmap.put("image",Kind.KW_IMAGE);
		hmap.put("url",Kind.KW_URL);
		hmap.put("file",Kind.KW_FILE);
		hmap.put("frame",Kind.KW_FRAME);
		hmap.put("while",Kind.KW_WHILE);
		hmap.put("if",Kind.KW_IF);
		hmap.put("screenheight",Kind.KW_SCREENHEIGHT);
		hmap.put("screenwidth",Kind.KW_SCREENWIDTH);
		hmap.put("sleep",Kind.OP_SLEEP);
		hmap.put("gray",Kind.OP_GRAY);
		hmap.put("convolve",Kind.OP_CONVOLVE);
		hmap.put("blur",Kind.OP_BLUR);
		hmap.put("scale",Kind.KW_SCALE);
		hmap.put("width",Kind.OP_WIDTH);
		hmap.put("height",Kind.OP_HEIGHT);
		hmap.put("xloc",Kind.KW_XLOC);
		hmap.put("yloc",Kind.KW_YLOC);
		hmap.put("hide",Kind.KW_HIDE);
		hmap.put("move",Kind.KW_MOVE);
		hmap.put("true",Kind.KW_TRUE);
		hmap.put("false",Kind.KW_FALSE);
		hmap.put("show", Kind.KW_SHOW);
		line_position.add(0);

	}
	/**
	 * Initializes Scanner object by traversing chars and adding tokens to tokens list.
	 * 
	 * @return this scanner
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 */

	State state;
	public Scanner scan() throws IllegalCharException, IllegalNumberException
	{
		int pos = 0; 
		int length = chars.length();
		state = State.START;
		int startPos = 0;


		int ch;
		StringBuilder temp = new StringBuilder();
		while(pos<=length)
		{
			ch = pos < length ? chars.charAt(pos):-1;
			switch(state)
			{

			case START:
			{
				pos = skipWhiteSpace(pos);
				ch = pos < length ? chars.charAt(pos):-1;
				startPos = pos;
				switch(ch)
				{
				case -1: 
				{
					tokens.add(new Token(Kind.EOF,pos,0));
					pos++;
				}break;

				case '+':
				{
					tokens.add(new Token(Kind.PLUS,startPos,1));
					pos++;
				}break;

				case '*':
				{
					tokens.add(new Token(Kind.TIMES,startPos,1));
					pos++;
				}break;

				case '/': 
				{
					state = State.AFTER_DIVISION;
					pos++;
				} break;

				case '-': 
				{
					state=State.MINUS;
					pos++;
				} break;

				case '%': 
				{
					tokens.add(new Token(Kind.MOD, startPos, 1));
					pos++;
				} break;

				case '=':
				{
					state = State.AFTER_EQ;
					pos++;
				}break;

				case '!':
				{
					state = State.AFTER_NOT;
					pos++;
				}break;

				case '&':
				{
					tokens.add(new Token(Kind.AND,startPos,1));
					pos++;
				}break;

				case '>':
				{
					state = State.GREATER_THAN;
					pos++;
				}break;

				case '<':
				{
					state = State.LESS_THAN;
					pos++;
				}break;

				case '|':
				{
					state = State.OR;
					pos++;
				}break;

				case ';':	
				{
					tokens.add(new Token(Kind.SEMI,startPos,1));
					pos++;
				}break;				
				case ',':
				{
					tokens.add(new Token(Kind.COMMA,startPos,1));
					pos++;
				}break;
				case '(':
				{
					tokens.add(new Token(Kind.LPAREN,startPos,1));
					pos++;
				}break;
				case ')':
				{
					tokens.add(new Token(Kind.RPAREN,startPos,1));
					pos++;
				}break;

				case '{':
				{
					tokens.add(new Token(Kind.LBRACE,startPos,1));
					pos++;
				}break;

				case '}':
				{
					tokens.add(new Token(Kind.RBRACE,startPos,1));
					pos++;
				}break;

				default :
				{
					if (Character.isDigit(ch))
					{
						if (ch=='0')
						{
							tokens.add(new Token(Kind.INT_LIT,startPos,1));
							state = State.START;
							pos++;
						}
						else
						{
							state = State.IN_DIGIT;
							pos++;
						} 
					}

					else if (Character.isJavaIdentifierStart(ch))
					{
						state = State.IN_IDENT;
					} 
					else {throw new IllegalCharException(String.valueOf(Character.toChars(ch))+"Illegal char found");
					}
				}
				}
			}//end start
			break;

			case IN_IDENT: 
			{
				if (Character.isJavaIdentifierPart(ch)) 
				{
					temp.append((char)ch); 
					pos++;
				} 
				else 
				{
					if(hmap.containsKey(temp.toString()))
					{
						tokens.add(new Token(hmap.get(temp.toString()),startPos,pos-startPos));
						temp.setLength(0);
						state = State.START;

					}
					else
					{
						tokens.add(new Token(Kind.IDENT, startPos, pos - startPos));
						temp.setLength(0);
						state = State.START;
					}

				}

			} // end IN_IDENT
			break;

			case IN_DIGIT:
			{
				if(Character.isDigit(ch))
				{
					pos++;
				}
				else
				{

					float max_value_check = Float.parseFloat(chars.substring(startPos, pos));
					if(max_value_check > Integer.MAX_VALUE)
					{
						throw new IllegalNumberException("");
					}
					else
					{
						tokens.add(new Token(Kind.INT_LIT,startPos,pos - startPos));
						state = State.START;
					}
					state=State.START;
				}
			} // end IN_DIGIT
			break;

			case OR:
			{
				ch = pos < length ? chars.charAt(pos):-1;
				switch(ch)
				{
				case '-':
				{
					pos++;
					state = State.EOR;

				}break;
				default:
				{
					tokens.add(new Token(Kind.OR,startPos,1));
					state = State.START;
				}
				}
			} // end OR
			break;	

			case EOR:
			{
				switch(ch)
				{
				case '>':
				{
					tokens.add(new Token(Kind.BARARROW,startPos,3));
					pos++;
					state = State.START;
				}break;

				default:
				{
					tokens.add(new Token(Kind.OR,startPos,1));
					pos--;
					state = State.START;
				}
				}
			} // end EOR
			break;

			case AFTER_NOT:
			{
				ch = pos < length ? chars.charAt(pos):-1;
				switch(ch)
				{
				case '=':
				{
					tokens.add(new Token(Kind.NOTEQUAL, startPos,2));
					state = State.START;
					pos++;
				}
				break;

				default :
				{
					tokens.add(new Token(Kind.NOT,startPos,1));
					state = State.START;
					
				}break;
				}
			} // AFTER_NOT
			break;

			case LESS_THAN:
			{
				ch = pos < length ? chars.charAt(pos):-1;

				switch(ch)
				{
				case '=':
				{
					tokens.add(new Token(Kind.LE,startPos,2));
					pos++;
					state = State.START;
				}break;
				case '-':
				{
					tokens.add(new Token(Kind.ASSIGN,startPos,2));
					pos++;
					state = State.START;
				}break;
				default:
				{
					tokens.add(new Token(Kind.LT,startPos,1));
					state = State.START;
				}
				}
			} // end LESS_THAN
			break;

			case GREATER_THAN:
			{

				ch=pos<length?chars.charAt(pos):-1;
				switch(ch)
				{
				case '=':
				{
					tokens.add(new Token(Kind.GE, startPos, 2));
					pos++;
					state=State.START;
				}break;
				default:
				{
					tokens.add(new Token(Kind.GT, startPos, 1));
					state=State.START;
				}
				}
			} // end GREATER_THAN
			break;
			case MINUS:
			{
				switch(ch)
				{
				case '>':
				{
					tokens.add(new Token(Kind.ARROW, startPos, 2));
					pos++;
					state=State.START;
				}break;
				default:
				{
					tokens.add(new Token(Kind.MINUS, startPos, 1));
					state=State.START;
				}
				}
			} // end MINUS
			break;

			case AFTER_EQ:
			{

				switch(ch)
				{
				case '=':
				{
					tokens.add(new Token(Kind.EQUAL,startPos,2));
					pos++;
					state = State.START;
				}break;
				default:
				{
					throw new IllegalCharException( "illegal char " +ch+" at pos "+pos);	
				}

				}	
			} // end AFTER_EQ
			break;

			case AFTER_DIVISION:
			{
				
				switch(ch)
				{
				case '*':
				{
					state = State.COMMENTS;
					pos++;
				}break;
				default:
				{
					tokens.add(new Token(Kind.DIV,startPos,1));
					state= State.START;
				}
				}
			} //end AFTER_DIVISION
			break;

			case COMMENTS:
			{
				ch=pos<length?chars.charAt(pos):-1;
				switch(ch)
				{
				case -1:
				{
					state=State.START;
					
				}
				case '*':
				{
					if(pos+1<length)
					{
						if(chars.charAt(pos+1)=='/')
						{
							state = State.START;
							pos = pos+2;
						}
						else
						{
							state = State.COMMENTS;
							pos++;
						}
					}
					else
					{
						state=State.START;
						pos++;
					}
				}break;
				case '\n':
				{
					System.out.println("RAM");
					line_position.add(pos);
					pos++;
				}
				default:
					pos++;
				}
			} // end COMMENTS
			break;


			default: assert false;
			}
		}
		//tokens.add(new Token(Kind.EOF,pos,0));
		return this;  
	}
	private int skipWhiteSpace(int pos)
	{
		int ch;
		boolean flag = true;
		while(flag)
		{
			ch = pos < chars.length() ? chars.charAt(pos):-1;	
			if(Character.isWhitespace(ch))
			{
				if(ch=='\n')
				{
					line_position.add(pos);
					//pos++;
				}
				pos++;
			}
			
			else
			{
				flag=false;
				state=State.START;
			}
		}
		return pos;
	}
	final ArrayList<Token> tokens;
	final String chars;
	int tokenNum;

	/*
	 * Return the next token in the token list and update the state so that
	 * the next call will return the Token..  
	 */
	public Token nextToken() {
		if (tokenNum >= tokens.size())
			return null;
		return tokens.get(tokenNum++);
	}

	/*
	 * Return the next token in the token list without updating the state.
	 * (So the following call to next will return the same token.)
	 */
	public Token peek(){
		if (tokenNum >= tokens.size())
			return null;
		return tokens.get(tokenNum);		
	}
	/**
	 * Returns a LinePos object containing the line and position in line of the 
	 * given token.  
	 * 
	 * Line numbers start counting at 0
	 * 
	 * @param t
	 * @return
	 */
	public LinePos getLinePos(Token t) 
	{
		return t.getLinePos();
	}
}
