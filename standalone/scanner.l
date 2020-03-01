%%

%standalone
%line

D  =		[0-9]
NZ =		[1-9]
L  =		[a-zA-Z_]
A  =		[a-zA-Z_0-9]
WS =		[ \t\v\n\f]

%{
	public void printToken(String type){
		System.out.println(" line: " + yyline + "\ttype: " + type + "\ttoken: " + yytext());
	}
	String datatype = "";
%}

%xstates comment
%%

"//".*				{}
"/*"				{ yybegin(comment); }

"if"				{ printToken("IF"); }
"else"				{printToken("ELSE");}
"while"				{printToken("WHILE");}
"return"			{printToken("RETURN");}
"void"				{printToken("VOID");}
"int"				{printToken("INT");}
"float"				{printToken("FLOAT");}
"main"				{printToken("MAIN");}

"++"				{printToken("INC_OP");} 
"--"				{printToken("DEC_OP");} 
"!"				{printToken("NOT_OP");}
"+"				{printToken("PLUS");}     
"-"				{printToken("MINUS");}     
"*"				{printToken("STAR");}     
"/"				{printToken("SLASH");}     
"<"				{printToken("LT");}     
"<="				{printToken("LTEQ");} 
">"				{printToken("GT");}     
">="				{printToken("GTEQ");} 
"=="				{printToken("EQ");} 
"!="				{printToken("NEQ");} 
"="				{printToken("ASSIGN");}     

("[")				{printToken("LSQUAR");}
("]")				{printToken("RSQUAR");}
("{")				{printToken("LBRACE");}
("}")				{printToken("RBRACE");}
//";"				{printToken("SEMI");}
","				{printToken("COMMA");}
"("				{printToken("LPAREN");}
")"				{printToken("RPAREN");}



{L}{A}*				{ printToken("ID"); }
{D}+				{ printToken("NUMBER"); }
{WS}+				{}
.				{}


<comment>{
	"*/"			{ yybegin(YYINITIAL); }
	\n			{}
	.			{}
}
