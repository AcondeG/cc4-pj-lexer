/*
 *  The scanner definition for COOL.
 */

import java_cup.runtime.Symbol;

%%

%{

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */

    // Max size of string constants
    static int MAX_STR_CONST = 1025;

    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();

    private int curr_lineno = 1;
    int get_curr_lineno() {
	return curr_lineno;
    }

    private AbstractSymbol filename;

    void set_filename(String fname) {
	filename = AbstractTable.stringtable.addString(fname);
    }

    AbstractSymbol curr_filename() {
	return filename;
    }

    String currentString = "";
    int comentarios = 0;
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    // empty for now
%init}

%eofval{

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */

    switch(yy_lexical_state) {
    case YYINITIAL:
	/* nothing special to do in the initial state */
	break;

	/* If necessary, add code for other states here, e.g:
	   case COMMENT:
	   ...
	   break;
    */
   case INSIDE_STRING:
		{ 
		  	yybegin(YYINITIAL); 
		  	return new Symbol(TokenConstants.ERROR, "EOF in string constant"); 
		}
	case COMMENT:
		{ 
			yybegin(YYINITIAL); 
			return new Symbol(TokenConstants.ERROR, "EOF in comment");
		}
    }
    return new Symbol(TokenConstants.EOF);
%eofval}

%class CoolLexer
%cup
%state INSIDE_STRING, ERROR_STRING, COMMENT, INLINE_COMMENT


%%


<YYINITIAL>[cC][lL][aA][sS][sS]				{ /* Palabras reservadas */
											  return new Symbol(TokenConstants.CLASS); }
<YYINITIAL>[iI][nN][hH][eE][rR][iI][tT][sS]	{ return new Symbol(TokenConstants.INHERITS); }
<YYINITIAL>[iI][fF] 						{ return new Symbol(TokenConstants.IF); }
<YYINITIAL>[tT][hH][eE][nN] 				{ return new Symbol(TokenConstants.THEN); }
<YYINITIAL>[eE][lL][sS][eE] 				{ return new Symbol(TokenConstants.ELSE); }
<YYINITIAL>[fF][iI]							{ return new Symbol(TokenConstants.FI); }
<YYINITIAL>[wW][hH][iI][lL][eE] 			{ return new Symbol(TokenConstants.WHILE); }
<YYINITIAL>[lL][oO][oO][pP] 				{ return new Symbol(TokenConstants.LOOP); }
<YYINITIAL>[pP][oO][oO][lL] 				{ return new Symbol(TokenConstants.POOL); }
<YYINITIAL>[lL][eE][tT] 		 			{ return new Symbol(TokenConstants.LET); }
<YYINITIAL>[iI][nN] 						{ return new Symbol(TokenConstants.IN);	}
<YYINITIAL>[cC][aA][sS][eE] 				{ return new Symbol(TokenConstants.CASE); }
<YYINITIAL>[oO][fF] 						{ return new Symbol(TokenConstants.OF); }
<YYINITIAL>[eE][sS][aA][cC]					{ return new Symbol(TokenConstants.ESAC); }
<YYINITIAL>[nN][eE][wW] 					{ return new Symbol(TokenConstants.NEW); }
<YYINITIAL>[iI][sS][vV][oO][iI][dD]			{ return new Symbol(TokenConstants.ISVOID); }
<YYINITIAL>[nN][oO][tT] 					{ return new Symbol(TokenConstants.NOT); }

<YYINITIAL>t[rR][uU][eE]	  		 		{ /* Booleans */
											  return new Symbol(TokenConstants.BOOL_CONST, "true"); }
<YYINITIAL>f[aA][lL][sS][eE] 			 	{ return new Symbol(TokenConstants.BOOL_CONST, "false"); }


<YYINITIAL>"{"				 				{ /* Simbolos y operadores */
											  return new Symbol(TokenConstants.LBRACE); }
<YYINITIAL>"}"				 				{ return new Symbol(TokenConstants.RBRACE); }
<YYINITIAL>"("				 				{ return new Symbol(TokenConstants.LPAREN); }
<YYINITIAL>")"				 				{ return new Symbol(TokenConstants.RPAREN); }
<YYINITIAL>":"				 				{ return new Symbol(TokenConstants.COLON);  }
<YYINITIAL>","				 				{ return new Symbol(TokenConstants.COMMA);  }
<YYINITIAL>";"				 				{ return new Symbol(TokenConstants.SEMI);   }

<YYINITIAL>"@"				 				{ return new Symbol(TokenConstants.AT);     }
<YYINITIAL>"<-"		     					{ return new Symbol(TokenConstants.ASSIGN); }
<YYINITIAL>"=>"								{ /* Sample lexical rule for "=>" arrow.
					                                     Further lexical rules should be defined
					                                     here, after the last %% separator */
					                                  return new Symbol(TokenConstants.DARROW); }					                          
<YYINITIAL>"+"								{ return new Symbol(TokenConstants.PLUS);}
<YYINITIAL>"-"				 				{ return new Symbol(TokenConstants.MINUS);}
<YYINITIAL>"*"				 				{ return new Symbol(TokenConstants.MULT);}
<YYINITIAL>"/"								{ return new Symbol(TokenConstants.DIV);}
<YYINITIAL>"~"				 				{ return new Symbol(TokenConstants.NEG);}
<YYINITIAL>"<"				 				{ return new Symbol(TokenConstants.LT);}
<YYINITIAL>"<="			 					{ return new Symbol(TokenConstants.LE);}  
<YYINITIAL>"="				 				{ return new Symbol(TokenConstants.EQ);}
<YYINITIAL>"."								{ return new Symbol(TokenConstants.DOT);}

<YYINITIAL>[A-Z][a-zA-Z0-9_]* {
	/*identificadores */
	return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
<YYINITIAL>[a-z][a-zA-Z0-9_]* {
	return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); 
}

<YYINITIAL>[0-9]+ {
	/* Constante entera */
	return new Symbol(TokenConstants.INT_CONST, AbstractTable.inttable.addString(yytext())); 
}

<YYINITIAL>" "|\b|\t|\f|\u000b|\xB|\r {/* Espacio en blanco*/ }

<YYINITIAL>\n|\n[\t]* {
	curr_lineno++; 
}

<YYINITIAL>\" {
	/* Limpiamos nuestro buffer y cambiamos de estado */
	string_buf.setLength(0); 
	yybegin(INSIDE_STRING); 
}

<YYINITIAL>"*)" {
	/*Comentario extra, mostrar error */
	return new Symbol(TokenConstants.ERROR, "Unmatched *)"); 
}

<YYINITIAL>"(*" { 
	/* Comentario multilenea*/
	comentarios++;
	yybegin(COMMENT);	
}

<YYINITIAL>"--" {
	yybegin(INLINE_COMMENT); 
}

<INSIDE_STRING>\" {
	/*Se regresa al estado principal*/
	yybegin(YYINITIAL);
	return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(string_buf.toString()));
}

<INSIDE_STRING>[^\n\\\"\0] {
	/*Se agrega cualquier texto*/
	if (string_buf.length() < MAX_STR_CONST-1 )
	{
		string_buf.append(yytext()); 
	} 	else { 
			yybegin(ERROR_STRING); 
			return new Symbol(TokenConstants.ERROR, "String constant too long"); 
	}
}
<INSIDE_STRING>\'							{	
	if ( string_buf.length() < MAX_STR_CONST-1 )
	{
		string_buf.append("\'");
	} else {
		yybegin(ERROR_STRING);
		return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
}
<INSIDE_STRING>"\""							{ 	
	if ( string_buf.length() < MAX_STR_CONST-1 )
	{
		string_buf.append("\"");
	} else {
		yybegin(ERROR_STRING);
		return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
}
<INSIDE_STRING>"\b"							{	
	if ( string_buf.length() < MAX_STR_CONST-1 )
	{
		string_buf.append("\b");
	} else {
		yybegin(ERROR_STRING);
		return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
}
<INSIDE_STRING>"\t"							{ 	
	if ( string_buf.length() < MAX_STR_CONST-1 )
	{
		string_buf.append("\t");
	} else {
		yybegin(ERROR_STRING);
		return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
}
<INSIDE_STRING>"\n"							{	
	if ( string_buf.length() < MAX_STR_CONST-1 )
	{
		string_buf.append("\n");
	} else {
		yybegin(ERROR_STRING);
		return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
}
<INSIDE_STRING>"\f"							{	
	if ( string_buf.length() < MAX_STR_CONST-1 )
	{
		string_buf.append("\f");
	} else {
		yybegin(ERROR_STRING);
		return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
}
<INSIDE_STRING>null|\0						{ 
	yybegin(ERROR_STRING);
	return new Symbol(TokenConstants.ERROR, "String contains null character.");
}
<INSIDE_STRING>\\							{ /* no hacer nada*/ }	
<INSIDE_STRING>\\[\n]						{	
	curr_lineno++;
	if( string_buf.length() < MAX_STR_CONST-1 )
	 {
		 string_buf.append('\n');
	 } else {
		yybegin(ERROR_STRING);
		return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
}
<INSIDE_STRING>\n							{
	  curr_lineno++;
	  yybegin(YYINITIAL);
	  return new Symbol(TokenConstants.ERROR, "Unterminated string constant"); 
  }
<INSIDE_STRING>\\[^\b\t\n\f]				{	
	if ( string_buf.length() < MAX_STR_CONST-1 )
	{
		string_buf.append(yytext().charAt(1));
	} else {
		yybegin(ERROR_STRING);
		return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
}

<ERROR_STRING>.*\" {
	 yybegin(YYINITIAL); 
	}

	<ERROR_STRING>\\[\n] { 
	curr_lineno++; 
}

<ERROR_STRING>\n {	
	curr_lineno++;
	yybegin(YYINITIAL);
}

<ERROR_STRING>.	{ }


<COMMENT>"*)"						  		{ 	/* Estado para comentarios multilinea */
	comentarios--;
	if (comentarios == 0)  
	{   
		yybegin(YYINITIAL); 
	}
}
<COMMENT>"(*"					  			{ comentarios++; }
<COMMENT>\n  								{ curr_lineno++; }
<COMMENT>" "|\b|\t|\f|\u000b|\xB|\r			{ /* Espacios en blanco*/ }
<COMMENT>.									{ /* No hacer nada */ }

<INLINE_COMMENT>.*[\n]	  					{ /* Estado para comentarios de linea */
  curr_lineno++; 
  yybegin(YYINITIAL);
}
<INLINE_COMMENT>.							{ /* No hacer nada */ }
.                               { /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  yybegin(YYINITIAL);
                                  return new Symbol(TokenConstants.ERROR, yytext());
                                  }