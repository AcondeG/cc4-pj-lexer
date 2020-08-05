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
    case STRING:
        return new Symbol(TokenConstants.ERROR, "EOF in string constant");
    break;
	/* If necessary, add code for other states here, e.g:
	   case COMMENT:
	   ...
	   break;
	*/
    }
    return new Symbol(TokenConstants.EOF);
%eofval}

%class CoolLexer
%cup
%state STRING
min_letters = [a-z]
mayus_letters = [A-Z]
number = [0-9]+
false = ([f])([Aa])([Ll])([Ss])([Ee])
true = ([t])([Rr])([Uu])([Ee])
newline = [\n]
TYPEID = [A-Z][0-9a-zA-Z_]*
OBJECTID = [a-z][0-9a-zA-Z_]*
NOT = [Nn][Oo][Tt]

%%

<YYINITIAL>"=>"			{ /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }

<YYINITIAL>{newline} {
    curr_lineno++;
}

<YYINITIAL>[\t] {
    
}

<YYINITIAL>"<-" {
    return new Symbol(TokenConstants.ASSIGN);
}


<YYINITIAL>" " {
    
}

<YYINITIAL>"(" {
    return new Symbol(TokenConstants.LPAREN);
}

<YYINITIAL>")" {
    return new Symbol(TokenConstants.RPAREN);
}

<YYINITIAL>"{" {
    return new Symbol(TokenConstants.LBRACE);
}

<YYINITIAL>"}" {
    return new Symbol(TokenConstants.RBRACE);
}

<YYINITIAL>":" {
    return new Symbol(TokenConstants.COLON);
}

<YYINITIAL>"+" {
    return new Symbol(TokenConstants.PLUS);
}

<YYINITIAL>"-" {
    return new Symbol(TokenConstants.MINUS);
}

<YYINITIAL>";" {
    return new Symbol(TokenConstants.SEMI);
}

<YYINITIAL>"." {
    return new Symbol(TokenConstants.DOT);
}

<YYINITIAL>"," {
    return new Symbol(TokenConstants.COMMA);
}

<YYINITIAL>"~" {
    return new Symbol(TokenConstants.NEG);
}

<YYINITIAL>"*" {
    return new Symbol(TokenConstants.MULT);
}

<YYINITIAL>"<=" {
    return new Symbol(TokenConstants.LE);
}

<YYINITIAL>"=" {
    return new Symbol(TokenConstants.EQ);
}

<YYINITIAL>"<" {
    return new Symbol(TokenConstants.LT);
}

<YYINITIAL>"/" {
    return new Symbol(TokenConstants.DIV);
}

<YYINITIAL>{NOT} {
    return new Symbol(TokenConstants.NOT);
}

<YYINITIAL>{false} {
    return new Symbol(TokenConstants.BOOL_CONST, false);
}

<YYINITIAL>{true} {
    return new Symbol(TokenConstants.BOOL_CONST, true);
}

<YYINITIAL>{TYPEID} {
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, obj_id);
}

<YYINITIAL>{OBJECTID} {
    AbstractSymbol obj_id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, obj_id);
}


<YYINITIAL>{number} {
    AbstractSymbol numero = AbstractTable.inttable.addString(yytext());
    return new Symbol(TokenConstants.INT_CONST, numero);
}

<YYINITIAL>"\"" {
    currentString = "";
    yybegin(STRING);
}

<STRING>"\0" {
    return new Symbol(TokenConstants.ERROR, "String contains null character");
}

<STRING>{newline} {
    return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}

<STRING>"\"" {
    AbstractSymbol str =AbstractTable.stringtable.addString(currentString);
    yybegin(YYINITIAL);
    return new Symbol(TokenConstants.STR_CONST, str);
}

<STRING>. {
    currentString += yytext();
    if(currentString.length() > MAX_STR_CONST){
        return new Symbol(TokenConstants.ERROR, "String constant too long");
    }
}

.                               { /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
