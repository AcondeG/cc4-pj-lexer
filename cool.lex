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
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in string constant");
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
NUMBER = [0-9]+
FALSE = ([f])([Aa])([Ll])([Ss])([Ee])
TRUE = ([t])([Rr])([Uu])([Ee])
NEWLINE = [\n]
TYPEID = [A-Z][0-9a-zA-Z_]*
OBJECTID = [a-z][0-9a-zA-Z_]*
NOT = [Nn][Oo][Tt]
ELSE = [Ee][Ll][Ss][Ee]
FI = [Ff][Ii]
IF = [Ii][Ff]
IN = [Ii][Nn]
INHERITS = [Ii][Nn][Hh][Ee][Rr][Ii][Tt][Ss]
LET = [Ll][Ee][Tt]
LOOP = [Ll][Oo][Oo][Pp]
POOL = [Pp][Oo][Oo][Ll]
THEN = [Tt][Hh][Ee][Nn]
WHILE = [Ww][Hh][Ii][Ll][Ee]
CASE = [Cc][Aa][Ss][Ee]
ESAC = [Ee][Ss][Aa][Cc]
OF = [Oo][Ff]
NEW = [Nn][Ee][Ww]
ISVOID = [Ii][Ss][Vv][Oo][Ii][Dd]
CLASS = [Cc][Ll][Aa][Ss][Ss]

%%

<YYINITIAL>"=>"  { /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }

<YYINITIAL>{NEWLINE} {
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

<YYINITIAL>{ELSE} {
    return new Symbol(TokenConstants.ELSE);
}

<YYINITIAL>{IF} {
    return new Symbol(TokenConstants.IF);
}

<YYINITIAL>{FI} {
    return new Symbol(TokenConstants.FI);
}

<YYINITIAL>{IN} {
    return new Symbol(TokenConstants.IN);
}

<YYINITIAL>{INHERITS} {
    return new Symbol(TokenConstants.INHERITS);
}

<YYINITIAL>{LET} {
    return new Symbol(TokenConstants.LET);
}

<YYINITIAL>{LOOP} {
    return new Symbol(TokenConstants.LOOP);
}

<YYINITIAL>{POOL} {
    return new Symbol(TokenConstants.POOL);
}

<YYINITIAL>{THEN} {
    return new Symbol(TokenConstants.THEN);
}

<YYINITIAL>{WHILE} {
    return new Symbol(TokenConstants.WHILE);
}

<YYINITIAL>{CASE} {
    return new Symbol(TokenConstants.CASE);
}

<YYINITIAL>{ESAC} {
    return new Symbol(TokenConstants.ESAC);
}

<YYINITIAL>{OF} {
    return new Symbol(TokenConstants.OF);
}

<YYINITIAL>{NEW} {
    return new Symbol(TokenConstants.NEW);
}

<YYINITIAL>{ISVOID} {
    return new Symbol(TokenConstants.ISVOID);
}

<YYINITIAL>{CLASS} {
    return new Symbol(TokenConstants.CLASS);
}

<YYINITIAL>{FALSE} {
    return new Symbol(TokenConstants.BOOL_CONST, false);
}

<YYINITIAL>{TRUE} {
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


<YYINITIAL>{NUMBER} {
    AbstractSymbol numero = AbstractTable.inttable.addString(yytext());
    return new Symbol(TokenConstants.INT_CONST, numero);
}

<YYINITIAL>"\"" {
    currentString = "";
    yybegin(STRING);
}

<STRING>"\0" {
    yybegin(YYINITIAL);
    return new Symbol(TokenConstants.ERROR, "String contains null character");
}

<STRING>{NEWLINE} {
    yybegin(YYINITIAL);
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
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "String constant too long");
    }
}

.                               { /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
