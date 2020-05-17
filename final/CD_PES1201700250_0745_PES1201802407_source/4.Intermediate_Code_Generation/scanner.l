import java.util.*;

%%

%byaccj
%integer
%line

alpha = [A-Za-z]
digit = [0-9]
und = [_]

%{
    private Parser yyparser;
    public Yylex(java.io.Reader r, Parser yyparser) {
        this(r);
        this.yyparser = yyparser;
    }
    
    public int getLine(){
	    //System.out.println(" line: " + yyline + "\tDatatype: " + datatype + "\ttoken: " + yytext());
        return yyline + 1;
    }

    public static StringBuffer string = new StringBuffer();
%}

%eofval{
    return 0;
%eofval}

%states STRING
%%

<YYINITIAL> {
    [ \t]   { /*Ignore*/ }
    \n      { /*Ignore*/ }
    "{"	    {yyparser.scope_start(); return '{';}
    "}"	    {yyparser.scope_end(); return '}';}
    ";"	    {   return(';'); }
    ","	    {   return(','); }
    ":"	    {   return(':'); }
    "="	    {   return('='); }
    "("	    {   return('('); }
    ")"	    {   return(')'); }
    ("[") 	{   return('['); }
    ("]")   {   return(']'); }
    "."	    {   return('.'); }
    "&"	    {   return('&'); }
    "!"	    {   return('!'); }
    "~"	    {   return('~'); }
    "-"	    {   return('-'); }
    "+"	    {   return('+'); }
    "*"	    {   return('*'); }
    "/"	    {   return('/'); }
    "%"	    {   return('%'); }
    "<"	    {   return('<'); }
    ">"	    {   return('>'); }
    "^"	    {   return('^'); }
    "|"	    {   return('|'); }
    "?"	    {   return('?'); }


    int	    {   yyparser.yylval = new ParserVal(Parser.INT); return Parser.INT; }
    float	{   yyparser.yylval = new ParserVal(Parser.FLOAT); return Parser.FLOAT; }
    void	{   yyparser.yylval = new ParserVal(Parser.VOID); return Parser.VOID; }

    else    {   return Parser.ELSE; }
    do	    {   return Parser.DO; }
    if	    {   return Parser.IF; }
    for     {   return Parser.FOR; }
    struct 	{   return Parser.STRUCT; }
    ^"#include ".+	{   return Parser.PREPROC; }
    while	        {   return Parser.WHILE; }
    return      	{   return Parser.RETURN; }
    printf	        {   return Parser.PRINT; }

    {alpha}({alpha}|{digit}|{und})* {   yyparser.yylval = new ParserVal(yytext()); return Parser.ID; }
    {digit}+	                    {   yyparser.yylval = new ParserVal(yytext()); return Parser.NUM; } 
    {digit}+\.{digit}+ 	            {   yyparser.yylval = new ParserVal(yytext()); return Parser.REAL;} 
    
    "<="		   {    return Parser.LE; }
    ">="           {    return Parser.GE; }
    "=="           {    return Parser.EQ; }
    "!="           {    return Parser.NEQ; }
    "&&"           {    return Parser.AND; }
    "||"           {    return Parser.OR; }
    "++"           {    return Parser.INC; }
    "--"           {    return Parser.DEC; }
    
    \/\/.*              {/*Ignore*/} 
    \/\*(.*\n)*.*\*\/   {/*Ignore*/} 
    
    \"	           { string.setLength(0); yybegin(STRING); }
    .	           { return -1; } // WTF?
}
<STRING> {
     \"             {
                        yybegin(YYINITIAL);
                        return Parser.STRING;
                    }

    [^\n\r\"\\]+    { string.append( yytext());}
    \t              { string.append('\t');}
    \n              { string.append('\n');}
    \\r             { string.append('\r');}
    \\\"            { string.append('\"');}
    \\              { string.append('\\');}
}
