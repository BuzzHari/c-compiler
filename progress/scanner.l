import java.util.*;
%%

%byaccj
%line

%{
    private Parser yyparser;
    public Yylex(java.io.Reader r, Parser yyparser){
        this(r);
        this.yyparser = yyparser;
    }
%}

alpha =		[A-Za-z_]
fl	=	(f|F|l|L)
ul	=	(u|U|l|L)*
digit 	=	[0-9]
space 	=	[ ]
hex		=[a-fA-F0-9]
exp		=[Ee][+-]?{digit}+


%{
	public void printToken(String type){
		System.out.println(" line: " + yyline + "\ttype: " + type + "\ttoken: " + yytext());
	}
	
    public boolean isPresent(String type, String token){
        for( int i = 0; i < count; i++ ){
            if( symbolTable[i].type.equals(type) && symbolTable[i].token.equals(token) ){
                return true;
            }
        }
        return false;
    }

    public void insertTokenUnique(String type, String token){
        if( !isPresent(type, token) ){
            symbolTable[count] = new node(yyline, type, token);
            count++;
        }
    }

    public void insertToken(String type, String token){
        symbolTable[count] = new node(yyline, type, token);
        count++;
    }

    class node{
        public int lineno;
        public String type;
        public String token;
        node(int lineno, String type, String token){
            this.lineno = lineno;
            this.type = type;
            this.token = token;
        }
    }

    node[] symbolTable = new node[1024];
    int count = 0;
    String datatype = "";

%}

%eof{
    for( int i = 0; i < count; i++ ){
		System.out.println(" line: " + symbolTable[i].lineno + "\ttype: " + symbolTable[i].type + "\ttoken: " + symbolTable[i].token );
    }
%eof}

%xstates comment
%%

"#".+                           {}

"//".*				            {}
"/*"				            { yybegin(comment); }
"#include<"({alpha})*".h>" {}

"#define"({space})""({alpha})""({alpha}|{digit})*""({space})""({digit})+"" 			{ return Parser.DEFINE;}
"#define"({space})""({alpha}({alpha}|{digit})*)""({space})""(({digit}+)\.({digit}+))"" 		{ return Parser.DEFINE;}
"#define"({space})""({alpha}({alpha}|{digit})*)""({space})""({alpha}({alpha}|{digit})*)"" 	{ return Parser.DEFINE;}

{digit}+    				{  return Parser.CONSTANT; }
({digit}+)\.({digit}+) 			{  return Parser.CONSTANT; }
0[xX]{hex}+{ul}?			{  return Parser.CONSTANT; }
{digit}+{ul}?				{  return Parser.CONSTANT; }
'(\\.|[^\\'])+'				{  return Parser.CONSTANT; }
{digit}+{exp}{fl}?			{  return Parser.CONSTANT; }
{digit}*"."{digit}+({exp})?{fl}?	{ return Parser.CONSTANT; }
{digit}+"."{digit}*({exp})?{fl}?	{  return Parser.CONSTANT; }

/* {alpha}?\"(\\.|[^\\"])*\"		{  return Parser.STRING_LITERAL; }  */

"->"			{  return Parser.PTR_OP; }
"++"			{  return Parser.INC_OP; }
"--"			{  return Parser.DEC_OP; }
"<<"			{  return Parser.LEFT_OP; }
">>"			{  return Parser.RIGHT_OP; }
"<="			{  return Parser.LE_OP; }
">="			{  return Parser.GE_OP; }
"=="			{  return Parser.EQ_OP; }
"!="			{  return Parser.NE_OP; }
"&&"			{  return Parser.AND_OP; }
"||"			{  return Parser.OR_OP; }
"*="			{  return Parser.MUL_ASSIGN; }
"/="			{  return Parser.DIV_ASSIGN; }
"%="			{  return Parser.MOD_ASSIGN; }
"+="			{  return Parser.ADD_ASSIGN; }
"-="			{  return Parser.SUB_ASSIGN; }
"<<="			{  return Parser.LEFT_ASSIGN; }
">>="			{  return Parser.RIGHT_ASSIGN; }
"&="			{  return Parser.AND_ASSIGN; }
"^="			{  return Parser.XOR_ASSIGN; }
"|="			{  return Parser.OR_ASSIGN; }

"auto"			{ return Parser.AUTO; }
"break"			{ return Parser.BREAK; }
"case"			{ return Parser.CASE; }
"char"			{ return Parser.CHAR; }
"const"			{ return Parser.CONST; }
"continue"		{ return Parser.CONTINUE; }
"default"		{ return Parser.DEFAULT; }
"do"			{ return Parser.DO; }
"double"		{ return Parser.DOUBLE; }
"else"			{ return Parser.ELSE; }
"enum"			{ return Parser.ENUM; }
"extern"		{ return Parser.EXTERN; }
"float"			{ datatype="FLOAT"; return Parser.FLOAT; }
"for"			{ return Parser.FOR; }
"goto"			{ return Parser.GOTO; }
"if"			{ return Parser.IF; }
"int"			{ datatype="INT"; return Parser.INT; }
"long"			{ return Parser.LONG; }
"register"		{ return Parser.REGISTER; }
"return"		{ return Parser.RETURN; }
"short"			{ return Parser.SHORT; }
"signed"		{ return Parser.SIGNED; }
"sizeof" 		{ return Parser.SIZEOF; }
"static"		{ return Parser.STATIC; }
"struct"		{ return Parser.STRUCT; }
"switch"		{ return Parser.SWITCH; }
"typedef"		{ return Parser.TYPEDEF; }
"union"			{ return Parser.UNION; }
"unsigned"		{ return Parser.UNSIGNED; }
"void"			{ return Parser.VOID; }
"volatile"		{ return Parser.VOLATILE; }
"while"			{ return Parser.WHILE; }


";"			{  datatype=""; return(';'); }
("{"|"<%")		{  return('{'); }
("}"|"%>")		{  return('}'); }
","			{  return(','); }
":"			{  return(':'); }
"="			{  return('='); }
"("			{  return('('); }
")"			{  return(')'); }
("["|"<:")		{  return('['); }
("]"|":>")		{  return(']'); }
"."			{  return('.'); }
"&"			{  return('&'); }
"!"			{  return('!'); }
"~"			{  return('~'); }
"-"			{  return('-'); }
"+"			{  return('+'); }
"*"			{  return('*'); }
"/"			{  return('/'); }
"%"			{  return('%'); }
"<"			{  return('<'); }
">"			{  return('>'); }
"^"			{  return('^'); }
"|"			{  return('|'); }
"?"			{  return('?'); }
"printf"|"scanf"	{ insertTokenUnique("FUNCTION", yytext()); return Parser.IDENTIFIER; }
"main" 			{  insertTokenUnique("FUNCTION", yytext()); return Parser.IDENTIFIER; }
{alpha}({alpha}|{digit})*    		{  
						if( datatype == "" )
							return Parser.IDENTIFIER;
						else 
						{
                            insertTokenUnique(datatype, yytext());
							return Parser.IDENTIFIER; 
						}
					}
[ \t\v\n\f]		{  }
.               {}
<comment>{
	"*/"			{ yybegin(YYINITIAL); }
	\n			    {}
	.			    {}
}

