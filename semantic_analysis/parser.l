import java.util.*;

%%

alpha = [A-Za-z]
digit = [0-9]
und = [_]

%{
    public void printToken(){
	System.out.println(" line: " + yyline + "\tDatatype: " + datatype + "\ttoken: " + yytext());
    }
%}

%%

[ \t]        { }
\n           {yylineno++;}
"{"	     {push(); return '{';}
"}"	     {pop(); return '}';}
";"	     {   return(';'); }
","	     {   return(','); }
":"	     {   return(':'); }
"="	     {   return('='); }
"("	     {   return('('); }
")"	     {   return(')'); }
("[") 	     {   return('['); }
("]")        {   return(']'); }
"."	     {   return('.'); }
"&"	     {   return('&'); }
"!"	     {   return('!'); }
"~"	     {   return('~'); }
"-"	     {   return('-'); }
"+"	     {   return('+'); }
"*"	     {   return('*'); }
"/"	     {   return('/'); }
"%"	     {   return('%'); }
"<"	     {   return('<'); }
">"	     {   return('>'); }
"^"	     {   return('^'); }
"|"	     {   return('|'); }
"?"	     {   return('?'); }
int	{yylval.ival = INT; return Parser.INT;}
float	{yylval.ival = FLOAT; return Parser.FLOAT;}
void	{yylval.ival = VOID; return Parser.VOID;}
else	{return Parser.ELSE;}
do	{return Parser.DO;}
if	{return Parser.IF;}
for     {return Parser.FOR;}
struct 	{return Parser.STRUCT;}
^"#include ".+	return Parser.PREPROC;
while	return Parser.WHILE;
return Parser.	return RETURN;
printf	return Parser.PRINT;
{alpha}({alpha}|{digit}|{und})* {yylval.str=strdup(yytext); return Parser.ID;} //TODO
{digit}+	{yylval.str=strdup(yytext);return Parser.NUM;} //TODO
{digit}+\.{digit}+ 	{yylval.str=strdup(yytext); return Parser.REAL;} //TODO
"<="		return Parser.LE;
">="            return Parser.GE;
"=="            return Parser.EQ;
"!="            return Parser.NE;
"++"            return Parser.INC;
"--"            return Parser.DEC;
\/\/.* ;
\/\*(.*\n)*.*\*\/ ;
\".*\"	return Parser.STRING;
.	return yytext[0];
%%
