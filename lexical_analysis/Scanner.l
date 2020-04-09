import java.util.*;
import java.io.*;
%%

%class Scanner
%line
%integer

%{
  
  public static final int AUTO = 1;
  public static final int BREAK = 2;
  public static final int CASE = 3;
  public static final int CHAR = 4;
  public static final int CONST = 5;
  public static final int CONTINUE = 6;
  public static final int DEFAULT =  7;
  public static final int DO =  8;
  public static final int DOUBLE = 9;
  public static final int ELSE  = 10;
  public static final int ENUM =  11;
  public static final int EXTERN = 12;
  public static final int FLOAT = 13;
  public static final int FOR = 14;
  public static final int GOTO = 15;
  public static final int IF = 16;
  public static final int INT = 17;
  public static final int LONG = 18;
  public static final int REGISTER = 19;
  public static final int RETURN = 20;
  public static final int SHORT  = 21;
  public static final int SIGNED  = 22;
  public static final int SIZEOF  = 23;
  public static final int STATIC = 24;
  public static final int STRUCT = 25;
  public static final int SWITCH  = 26;
  public static final int TYPEDEF = 27;
  public static final int UNION  = 28;  
  public static final int UNSIGNED =  29;
  public static final int VOID  = 30;
  public static final int VOLATILE = 31;
  public static final int WHILE =  32;

  public static final int IDENTIFIER =  33;
  public static final int SLC  = 34;
  public static final int MLCS = 35;
  public static final int MLCE = 36;

  public static final int LEQ = 37;
  public static final int GEQ = 38;
  public static final int EQEQ =  39;
  public static final int NEQ = 40;
  public static final int LOR = 41;
  public static final int LAND = 42;
  public static final int ASSIGN = 43;
  public static final int PLUS = 44;
  public static final int SUB =  45;
  public static final int MULT = 46;
  public static final int DIV = 47;
  public static final int MOD = 48;
  public static final int LESSER = 49;
  public static final int GREATER = 50;
  public static final int INCR = 51;
  public static final int DECR = 52;

  public static final int COMMA = 53;
  public static final int SEMI  = 54;

  public static final int HEADER = 55;
  public static final int MAIN  = 56;

  public static final int PRINTF = 57;
  public static final int SCANF  = 58;
  public static final int DEFINE = 59;

  public static final int INT_CONST = 60;
  public static final int FLOAT_CONST = 61;

  public static final int TYPE_SPEC = 62;

  public static final int DQ = 63;

  public static final int OBO = 64;
  public static final int OBC = 65;
  public static final int CBO = 66;
  public static final int CBC = 67;
  public static final int HASH = 68;

  public static final int ARR = 69;
  public static final int FUNC =70;

  public static final int NUM_ERR = 71;
  public static final int UNKNOWN = 72;

  public static final int CHAR_CONST  = 73;
  public static final int SIGNED_CONST = 74;
  public static final int STRING_CONST = 75;

  public static int lineno = 1;
  public static StringBuffer string = new StringBuffer();

  static class Node {
      public int lineno;
      public String token;
      public String type;

      Node(int lineno, String type, String token) {
        this.lineno = lineno;
        this.type = type;
        this.token = token;
      }

  }

  public static boolean present(String type, String token) {
    for (int i = 0; i < count; i++)
      if(symbolTable[i].type.equals(type) && symbolTable[i].token.equals(token))
        return true;
      return false;
  }

  public static void insertToken(int lineno, String type, String token) {
    if( !present(type, token) ) {
      symbolTable[count++] = new Node(lineno, type, token);
    } 
  }

  public static void display() {
    for( int i = 0; i < count; i++ ){
      System.out.println(" line: " + symbolTable[i].lineno + "\ttype: " + symbolTable[i].type + "\ttoken: " + symbolTable[i].token );
    }
  }
  
  static Node[] symbolTable = new Node[1024];
  static int count = 0;

  
  public static void main(String[] args) throws IOException 
  {
      //InputStream stream = new FileInputStream(args[0]);
      InputStreamReader reader = new InputStreamReader(System.in);
      Scanner lexer = new Scanner(reader);

      int scan, slcline = 0, mlc = 0, mlcline = 0, dq = 0, dqline = 0; 
      
      System.out.println(" Scanning the file\n");
      scan = lexer.yylex();
      while(scan != YYEOF) {

        if( lineno == slcline ) {
          scan = lexer.yylex();
          continue;
        }
        if( lineno != dqline && dqline != 0 ) {
          if( dq % 2 != 0)
            System.out.println("\n******** ERROR!! INCOMPLETE STRING at Line "+dqline+" ********\n\n");
          dq=0;
        }

        if(( scan >= 1 && scan <= 32) && mlc == 0) {
            System.out.println(lexer.yytext() + "\t\t\tKEYWORD\t\t\t\tLine " + lineno);
            insertToken(lexer.yyline,"KEYWORD", lexer.yytext());
        }
        if( scan == 33 && mlc == 0) {
            System.out.println(lexer.yytext() + "\t\t\tIDENTIFIER\t\t\tLine " + lineno);
            insertToken(lexer.yyline,"IDENTIFIER", lexer.yytext());
        }
        if( scan == 34 ) {
            System.out.println(lexer.yytext() + "\t\t\tSingleLine Comment\t\tLine " + lineno);
            slcline = lineno;
        }
        if( scan == 35 && mlc == 0) {
            System.out.println(lexer.yytext() + "\t\t\tMultiLine Comment Start\t\tLine " + lineno);
            mlcline = lineno;
            mlc = 1;
        }
        if( scan == 36 && mlc == 0) {
            System.out.println("\n******** ERROR!! UNMATCHED MULTILINE COMMENT " + lexer.yytext() +" at Line "+lineno+" *******\n");
        }
        if( scan == 36 && mlc == 1) {
          mlc = 0;
          System.out.println(lexer.yytext() + "\t\t\tMultiLine Comment End\t\tLine " + lineno);
        }
        if( (scan >= 37 && scan<= 52) && mlc == 0){
            System.out.println(lexer.yytext() + "\t\t\tOPERATOR\t\t\tLine " + lineno);
            insertToken(lexer.yyline, "OPERATOR", lexer.yytext());
        }
        if((scan == 53 || scan == 54 || scan == 63 || (scan>=64 && scan<=68)) && mlc == 0) {
            System.out.println(lexer.yytext() + "\t\t\tSPECIAL SYMBOL\t\t\tLine " + lineno);
            if( scan == 63 ) {
              dq++;
              dqline = lineno;
            }
            insertToken(lexer.yyline, "SPECIAL SYMBOL", lexer.yytext());
        }
        if( scan == 55 && mlc == 0) {
          System.out.println(lexer.yytext() + "\tHEADER\t\t\t\tLine " + lineno);
        }
        if( scan == 56 && mlc == 0) {
          System.out.println(lexer.yytext() + "\t\t\tMAIN FUNCTION\t\t\tLine " + lineno);
          insertToken(lexer.yyline, "IDENTIFIER", lexer.yytext());
        }
        if((scan == 57 || scan == 58) && mlc == 0) {
          System.out.println(lexer.yytext() + "\t\t\tPRE DEFINED FUNCTION\t\tLine " + lineno);
          insertToken(lexer.yyline, "PRE DEFINED FUNCTION", lexer.yytext());
        }
        if( scan == 59 && mlc == 0 ) {
          System.out.println(lexer.yytext() + "\t\t\tPRE PROCESSOR DIRECTIVE\t\tLine " + lineno);
        }
        if( scan == 60 && mlc == 0) {
          System.out.println(lexer.yytext() + "\t\t\tINTEGER CONSTANT\t\tLine " + lineno);
          insertToken(lexer.yyline, "INTGER CONSTANT", lexer.yytext());
        }
        if( scan == 61 && mlc == 0) {
          System.out.println(lexer.yytext() + "\t\t\tFLOATING POINT CONSTANT\t\tLine " + lineno);
          insertToken(lexer.yyline, "FLOATING POINT CONSTANT", lexer.yytext());
        }
        if( scan == 62 && mlc == 0) {
          System.out.println(lexer.yytext() + "\t\t\tTYPE SPECIFIER\t\t\t\tLine " + lineno);
        }
        if( scan == 69 && mlc == 0 ) {
          System.out.println(lexer.yytext() + "\t\t\tARRAY\t\t\t\tLine " + lineno);
          insertToken(lexer.yyline, "ARRAY", lexer.yytext());
        }
        if( scan == 70 && mlc == 0) {
          System.out.println(lexer.yytext() + "\t\t\tUSER DEFINED FUNCTION\t\tLine " + lineno);
          insertToken(lexer.yyline, "USER DEFINED FUNCTION", lexer.yytext());
        }
        if( scan == 71 && mlc == 0) {
          System.out.println("\n******** ERROR!! CONSTANT ERROR " + lexer.yytext() +" at Line "+lineno+" *******\n");
        }
        if( scan == 72 && mlc == 0) {
          System.out.println("\n******** ERROR!! UNKNOWN TOKEN " + lexer.yytext() +" at Line "+lineno+" ********\n");
        }
        if( scan == 73 && mlc == 0) {
          System.out.println(lexer.yytext() + "\t\t\tCHARACTER CONSTANT\t\t\tLine " + lineno);
          insertToken(lexer.yyline, "CHARACTER CONSTANT", lexer.yytext());
        }
        if( scan == 74 && mlc == 0) {
          System.out.println(lexer.yytext() + "\t\t\tSIGNED CONSTANT\t\t\tLine " + lineno);
          insertToken(lexer.yyline, "SIGNED CONSTANT", lexer.yytext());
        }
        if( scan == 75 && mlc == 0 ) {
          System.out.println(lexer.yytext() + "\t\t\tSTRING CONSTANT\t\t\tLine " + lineno);
          insertToken(lexer.yyline, "STRING CONSTANT", lexer.yytext());
        }

        scan = lexer.yylex();
      }
      if(mlc == 1)
        System.out.println("\n******** ERROR!! UNMATCHED MULTILINE COMMENT " + lexer.yytext() +" at Line "+lineno+" *******\n");

      System.out.println("\n\t******** SYMBOL TABLE ********\t\t\n");
      display();

    }

%}

alpha = [A-Za-z]
digit = [0-9]
und   = [_]
space  = [ ]
line   = [\n]
tab    = [\t]
char   = \'.\'
at     = [@]

%state STRING
%%
<YYINITIAL> {
  {space}*  {/* Ignore */}
  {tab}*    {/* Ignore */}
  \"        { string.setLength(0); yybegin(STRING); } 
  {char}    { return CHAR_CONST; }
  {line}    { lineno++; }
  auto      { return AUTO; }
  break     { return BREAK; }
  case      { return CASE; }
  char      { return CHAR; }
  const     { return CONST; }
  continue  { return CONTINUE; }
  default   { return DEFAULT; }
  do        { return DO; }
  double    { return DOUBLE; }
  else      { return ELSE; }
  enum      { return ENUM; }
  extern    { return EXTERN; }
  float     { return FLOAT; }
  for       { return FOR; }
  goto      { return GOTO; }
  if        { return IF; }
  int       { return INT; }
  long      { return LONG; }
  register  { return REGISTER; }
  return    { return RETURN; }
  short     { return SHORT; }
  signed    { return SIGNED; }
  sizeof    { return SIZEOF; }
  static    { return STATIC; }
  struct    { return STRUCT; } 
  switch    { return SWITCH; }
  typedef   { return TYPEDEF; }
  union     { return UNION; }
  unsigned  { return UNSIGNED; }
  void      { return VOID; }
  volatile  { return VOLATILE; } 
  while     { return WHILE; }

  printf    {return PRINTF; }
  scanf     {return SCANF; }

  {alpha}({alpha}|{digit}|{und})* { return IDENTIFIER; }
  [+-][0-9]{digit}*(\.{digit}+)?  { return SIGNED_CONST; }

  "//"    { return SLC; }
  "/*"    { return MLCS; }
  "*/"    { return MLCE; }

  "<="    { return LEQ; }
  ">="    { return GEQ; }
  "=="    { return EQEQ;}
  "!="    { return NEQ; }
  "||"    { return LOR; }
  "&&"    { return LAND; }
  "="     { return ASSIGN; }
  "+"     { return PLUS; }
  "-"     { return SUB; }
  "*"     { return MULT; }
  "/"     { return DIV; }
  "%"     { return MOD; }
  "<"     { return LESSER; }
  ">"     { return GREATER; }
  "++"    { return INCR; }
  "--"    { return DECR; }

  ","     { return COMMA; }
  ";"     { return SEMI; }

  "#include <stdio.h>"    { return HEADER; }
  "main()"                { return MAIN; }

  {digit}+                { return INT_CONST; }
  ({digit}+)\.({digit}+)  { return FLOAT_CONST; }

  "%d"|"%f"|"%u"|"%s"     { return TYPE_SPEC; }
  "\""                    { return DQ; }
  "("                     { return OBO; }
  ")"                     { return OBC; }
  "{"                     { return CBO; }
  "}"                     { return CBC; }
  "#"                     { return HASH; }

  {alpha}({alpha}|{digit}|{und})*\[{digit}*\]
  {alpha}({alpha}|{digit}|{und})*\(({alpha}|{digit}|{und}|{space})*\) { return FUNC; }
  ({digit}+)\.({digit}+)\.({digit}|\.)* { return NUM_ERR; }
  ({digit}|}{at})+({alpha}|{digit}|{und}|{at})*   { return UNKNOWN; }
  [^]       { return UNKNOWN;}
}
<STRING> {
  \"            {
                  yybegin(YYINITIAL);
                  return STRING_CONST; 
                }
  [^\n\r\"\\]+  { string.append( yytext());}
  {tab}         { string.append('\t');}
  {line}        { string.append('\n');}
  \\r           { string.append('\r');}
  \\\"          { string.append('\"');}
  \\            { string.append('\\');}
}
