import java.util.*;
%%

%standalone
%line

D  =	[0-9]
NZ =	[1-9]
L  =	[a-zA-Z_]
A  =	[a-zA-Z_0-9]
WS =	[ \t\v\n\f]

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

"if"				            { insertTokenUnique("IF", yytext()); }
"else"				            { insertTokenUnique("ELSE", yytext());}
"while"				            { insertTokenUnique("WHILE", yytext());}
"for"				            { insertTokenUnique("FOR", yytext());}
"return"			            { insertTokenUnique("RETURN", yytext());}
"int"|"float"|"double"|"long"   { datatype = yytext().toUpperCase(); }
"main"				            { insertTokenUnique("FUNCTION", yytext());}



"++"				{ insertTokenUnique("INC_OP", yytext());} 
"--"				{ insertTokenUnique("DEC_OP", yytext());} 
"!"				    { insertTokenUnique("NOT_OP", yytext());}
"+"				    { insertTokenUnique("PLUS", yytext());}     
"-"				    { insertTokenUnique("MINUS", yytext());}     
"*"				    { insertTokenUnique("STAR", yytext());}     
"/"				    { insertTokenUnique("SLASH", yytext());}     
"<"				    { insertTokenUnique("LT", yytext());}     
"<="				{ insertTokenUnique("LTEQ", yytext());} 
">"				    { insertTokenUnique("GT", yytext());}     
">="				{ insertTokenUnique("GTEQ", yytext());} 
"=="				{ insertTokenUnique("EQ", yytext());} 
"!="				{ insertTokenUnique("NEQ", yytext());} 
"="				    { insertTokenUnique("ASSIGN", yytext());}     

("[")				{ insertTokenUnique("LSQUAR", yytext());}
("]")				{ insertTokenUnique("RSQUAR", yytext());}
("{")				{ insertTokenUnique("LBRACE", yytext());}
("}")				{ insertTokenUnique("RBRACE", yytext());}
//";"				{ insertTokenUnique("SEMI", yytext());}
","				    { insertTokenUnique("COMMA", yytext());}
"("				    { insertTokenUnique("LPAREN", yytext());}
")"				    { insertTokenUnique("RPAREN", yytext());}



{L}{A}*				{ insertTokenUnique(datatype, yytext()); }
{D}+				{ insertTokenUnique("C_INT", yytext()); }
{D}+"."{D}+         { insertTokenUnique("C_DOUBLE", yytext()); }
{WS}+				{}
.				    {}


<comment>{
	"*/"			{ yybegin(YYINITIAL); }
	\n			    {}
	.			    {}
}

