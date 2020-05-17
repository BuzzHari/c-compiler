//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "parser.y"
    import java.util.*;
    import java.io.*;
//#line 20 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short INT=257;
public final static short FLOAT=258;
public final static short VOID=259;
public final static short ID=260;
public final static short NUM=261;
public final static short REAL=262;
public final static short WHILE=263;
public final static short IF=264;
public final static short RETURN=265;
public final static short PREPROC=266;
public final static short LE=267;
public final static short STRING=268;
public final static short PRINT=269;
public final static short FUNCTION=270;
public final static short DO=271;
public final static short ARRAY=272;
public final static short ELSE=273;
public final static short STRUCT=274;
public final static short STRUCT_VAR=275;
public final static short FOR=276;
public final static short GE=277;
public final static short EQ=278;
public final static short NE=279;
public final static short INC=280;
public final static short DEC=281;
public final static short AND=282;
public final static short OR=283;
public final static short NEQ=284;
public final static short UMINUS=285;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    8,    8,   11,   11,   12,    7,
    7,    7,   10,   13,   13,   14,   14,   14,   14,   14,
   14,   14,   14,   14,   14,   14,   14,   19,   20,   18,
   21,   22,   23,   16,   24,   26,   15,   25,   25,   27,
   28,   17,    1,    1,    1,    1,    1,    1,   29,   30,
   31,    2,    2,    2,    2,    2,    3,    3,   32,   33,
   34,    9,    9,    9,    9,    9,    9,    9,   36,   35,
   37,    4,   38,    4,    4,   39,   40,    4,   41,   42,
    4,   43,   44,    4,   45,   46,    4,   47,   48,    4,
   49,   50,    4,   51,   52,    4,   53,   54,    4,   55,
   56,    4,    4,   57,    5,   58,    5,    5,    6,   59,
    6,    6,    6,
};
final static short yylen[] = {                            2,
    2,    2,    2,    0,    5,    6,    3,    1,    2,    1,
    1,    1,    3,    2,    0,    1,    1,    4,    1,    1,
    1,    3,    2,    3,    1,    5,    1,    0,    0,    9,
    0,    0,    0,   12,    0,    0,    8,    2,    0,    0,
    0,    7,    3,    3,    3,    3,    1,    1,    0,    0,
    0,    6,    3,    3,    1,    1,    1,    1,    0,    0,
    0,    8,    2,    6,    5,    6,    4,    1,    0,    5,
    0,    4,    0,    4,    1,    0,    0,    5,    0,    0,
    5,    0,    0,    5,    0,    0,    5,    0,    0,    5,
    0,    0,    5,    0,    0,    5,    0,    0,    5,    0,
    0,    5,    1,    0,    4,    0,    4,    1,    3,    0,
    3,    1,    1,
};
final static short yydefred[] = {                         0,
   68,   10,   11,   12,    0,   57,   58,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    2,    0,
   63,    0,    0,    1,    3,    0,   53,    0,   50,    0,
    0,   54,    0,    0,    0,    0,    0,   67,    0,    0,
    0,    0,    0,    8,    0,    0,    0,   60,   65,    0,
  110,    0,  113,    0,    0,  108,  103,    0,    0,   15,
    5,    9,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   71,   73,   52,  104,  106,   66,    0,    6,
    7,   43,   44,   45,   64,   46,    0,    0,   77,   80,
   83,   86,   89,   92,   95,   98,  101,  112,  111,  109,
    0,    0,    0,    0,    0,   40,    0,    0,    0,   28,
    0,   13,   25,   16,   27,   14,   17,   19,   20,   21,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  105,  107,    0,    0,    0,    0,   23,
    0,    0,    0,    0,   62,   70,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   24,   22,
    0,    0,    0,   18,    0,   35,    0,    0,    0,   41,
    0,   26,    0,    0,    0,   36,    0,    0,   42,    0,
   29,    0,    0,   37,    0,    0,   38,   30,    0,    0,
    0,   34,
};
final static short yydgoto[] = {                         10,
   46,   11,   53,   54,   55,   56,   13,   14,   15,   61,
   43,   44,   89,  126,  127,  128,  129,  130,  153,  195,
  179,  192,  200,  181,  194,  190,  147,  185,   18,   37,
   85,   35,   70,  131,   57,   71,  111,  112,   72,  133,
   73,  134,   74,  135,   75,  136,   76,  137,   77,  138,
   78,  139,   79,  140,   80,  141,  113,  114,   81,
};
final static short yysindex[] = {                       -87,
    0,    0,    0,    0,   -1,    0,    0,  -87, -207,    0,
    2,   20, -182,  -87,  -87,  -84,  -84,   18,    0, -114,
    0,  -84,  -14,    0,    0,   58,    0,   29,    0,   70,
  -76,    0,  -27,  -22,   83,   75,  -37,    0, -112,   38,
   32, -108,   19,    0,    1,  101,  172,    0,    0,    0,
    0,  -37,    0,   95,  -25,    0,    0,  105,  195,    0,
    0,    0,   32,  -24, -227,  -22,  -22,  196,  -22,  -37,
  165,   -9,  -16,  -13,  -21,  -11,  -23,  206,  205,  207,
  -33,  104,    0,    0,    0,    0,    0,    0,  -54,    0,
    0,    0,    0,    0,    0,    0,   95,  -37,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -33,  -33,  -33,  -33,   77,    0,  229,  -48,  230,    0,
  232,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  214,   22,  -37,  -37,  -37,  -37,  -37,  -37,  -37,  -37,
  -37,  -25,  -25,    0,    0,  233,  235,  -37,  217,    0,
  218,   10,   32,  -37,    0,    0,   95,   95,   95,   95,
   95,   95,   95,   95,   95,  220,  -37,  147,    0,    0,
  239,   21,   95,    0,  148,    0,  222,  242,  224,    0,
   32,    0,  -37,  -37,   32,    0,  154,   95,    0,   12,
    0,  227,   32,    0,  228,  -37,    0,    0,   95,  247,
   32,    0,
};
final static short yyrindex[] = {                       289,
    0,    0,    0,    0,  106,    0,    0,  289,    0,    0,
    0,  -18,    0,  289,  289,    0,    0,    0,    0,    0,
    0,    0,  231,    0,    0,   57,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  197,    0,  198,    0,    0,  -41,
    0,    0,    0,   30,   -5,    0,    0,  231,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  234,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  106,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   14,   27,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -17,   -8,    7,   46,
   60,   67,   68,   69,   73,    0,    0,    0,    0,    0,
    0,    0,  236,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  237,    0,  -12,
    0,    0,    0,    0,    0,    0,    0,    0,  253,    0,
    0,    0,
};
final static short yygindex[] = {                       122,
  152,  142,   15,  -42,   89,   43,   -6,    0,  -15,  -50,
    0,  238,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static int YYTABLESIZE=302;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        112,
  112,  112,   52,  112,  123,  112,   52,   51,   31,   82,
  150,   51,   90,   41,   12,   40,   86,  112,   94,  100,
   97,   87,   12,   78,   39,   33,   42,   97,   12,   12,
   12,   12,   81,    6,    7,   75,   12,   75,  125,   75,
   56,   78,   16,   66,   67,   12,   39,   84,   47,   69,
   81,  112,   20,   75,   72,  132,   72,   42,   72,   63,
   21,   65,   64,   22,   83,   84,   84,   74,   60,   74,
  122,   74,   72,  124,   56,   78,   34,   23,   29,   92,
   47,   47,   39,   47,   81,   74,   87,   75,   51,   17,
  157,  158,  159,  160,  161,  162,  163,  164,  165,   84,
   90,   16,  172,   12,   87,  168,   72,   93,   96,   99,
   39,  173,   39,  102,  156,   55,  146,   49,   90,   74,
   16,   36,   51,  109,  175,   93,   96,   99,   38,   19,
  186,  102,  151,   49,  189,   24,   25,   83,   87,   84,
  187,  188,  197,   48,  110,   30,   83,   58,   84,   55,
  202,   62,   90,  199,   60,  144,  145,   27,   28,   93,
   96,   99,   59,   32,   55,  102,   49,   17,    1,    2,
    3,    4,    5,    6,    7,   26,    6,    7,    8,    1,
    2,    3,    4,    5,    6,    7,    9,  176,  180,   83,
   83,   84,   84,   68,  191,   34,   83,    9,   84,  142,
  143,    1,    2,    3,    4,  115,    6,    7,  116,  117,
  118,  149,    6,    7,  119,   69,  120,   93,   94,    9,
   96,  121,   50,    6,    7,   76,  108,    6,    7,    2,
    3,    4,    2,    3,    4,   79,   82,   45,    6,    7,
   88,   91,   85,   39,   39,   39,   39,   39,   39,   39,
   39,   39,   39,   88,   95,   98,   39,   99,   39,  104,
  100,   39,  102,   39,  101,  105,  106,  107,  148,  152,
  103,  154,  155,  166,  167,  169,  170,  171,  174,  177,
  182,  183,  184,  178,  193,  196,  198,  201,    4,   47,
   48,   59,   61,   33,   31,   32,    0,    0,    0,    0,
    0,   91,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   40,   45,   59,   47,   40,   45,  123,   52,
   59,   45,   63,   41,    0,   31,   42,   59,   60,   61,
   62,   47,    8,   41,   31,   40,   33,   70,   14,   15,
   16,   17,   41,  261,  262,   41,   22,   43,   89,   45,
   59,   59,   44,   43,   44,   31,   59,   41,   34,   91,
   59,   93,  260,   59,   41,   98,   43,   64,   45,   41,
   59,   61,   44,   44,   43,   59,   45,   41,  123,   43,
  125,   45,   59,   89,   93,   93,   91,  260,   61,   65,
   66,   67,   89,   69,   93,   59,   41,   93,   59,   91,
  133,  134,  135,  136,  137,  138,  139,  140,  141,   93,
   41,   44,  153,   89,   59,  148,   93,   41,   41,   41,
  123,  154,  125,   41,   93,   59,   40,   61,   59,   93,
   44,   93,   93,   81,  167,   59,   59,   59,   59,    8,
  181,   59,  118,   59,  185,   14,   15,   43,   93,   45,
  183,  184,  193,   61,   41,  260,   43,  260,   45,   93,
  201,  260,   93,  196,  123,  113,  114,   16,   17,   93,
   93,   93,  125,   22,   59,   93,   61,   91,  256,  257,
  258,  259,  260,  261,  262,  260,  261,  262,  266,  256,
  257,  258,  259,  260,  261,  262,  274,   41,   41,   43,
   43,   45,   45,   93,   41,   91,   43,  274,   45,  111,
  112,  256,  257,  258,  259,  260,  261,  262,  263,  264,
  265,  260,  261,  262,  269,   44,  271,   66,   67,  274,
   69,  276,  260,  261,  262,  267,  260,  261,  262,  257,
  258,  259,  257,  258,  259,  277,  278,  260,  261,  262,
  282,  283,  284,  256,  257,  258,  259,  260,  261,  262,
  263,  264,  265,   59,   59,   91,  269,  267,  271,  283,
  277,  274,  284,  276,  278,   60,   62,   61,   40,   40,
  282,   40,   59,   41,   40,   59,   59,  268,   59,   41,
   59,   40,   59,  263,  273,   59,   59,   41,    0,   93,
   93,   61,   59,   41,   59,   59,   -1,   -1,   -1,   -1,
   -1,   64,
};
}
final static short YYFINAL=10;
final static short YYMAXTOKEN=285;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,"INT","FLOAT","VOID","ID","NUM","REAL",
"WHILE","IF","RETURN","PREPROC","LE","STRING","PRINT","FUNCTION","DO","ARRAY",
"ELSE","STRUCT","STRUCT_VAR","FOR","GE","EQ","NE","INC","DEC","AND","OR","NEQ",
"UMINUS",
};
final static String yyrule[] = {
"$accept : start",
"start : Function start",
"start : PREPROC start",
"start : Declaration start",
"start :",
"Function : Type ID '(' ')' CompoundStmt",
"Function : Type ID '(' parameter_list ')' CompoundStmt",
"parameter_list : parameter_list ',' parameter",
"parameter_list : parameter",
"parameter : Type ID",
"Type : INT",
"Type : FLOAT",
"Type : VOID",
"CompoundStmt : '{' StmtList '}'",
"StmtList : StmtList stmt",
"StmtList :",
"stmt : Declaration",
"stmt : if",
"stmt : ID '(' ')' ';'",
"stmt : for",
"stmt : while",
"stmt : dowhile",
"stmt : RETURN consttype ';'",
"stmt : RETURN ';'",
"stmt : RETURN ID ';'",
"stmt : ';'",
"stmt : PRINT '(' STRING ')' ';'",
"stmt : CompoundStmt",
"$$1 :",
"$$2 :",
"dowhile : DO $$1 CompoundStmt WHILE '(' E ')' $$2 ';'",
"$$3 :",
"$$4 :",
"$$5 :",
"for : FOR '(' E $$3 ';' E $$4 ';' E $$5 ')' CompoundStmt",
"$$6 :",
"$$7 :",
"if : IF '(' E ')' $$6 CompoundStmt $$7 else",
"else : ELSE CompoundStmt",
"else :",
"$$8 :",
"$$9 :",
"while : WHILE $$8 '(' E ')' $$9 CompoundStmt",
"assignment : ID '=' consttype",
"assignment : ID '+' assignment",
"assignment : ID ',' assignment",
"assignment : consttype ',' assignment",
"assignment : ID",
"assignment : consttype",
"$$10 :",
"$$11 :",
"$$12 :",
"assignment1 : ID $$10 '=' $$11 E $$12",
"assignment1 : ID ',' assignment1",
"assignment1 : consttype ',' assignment1",
"assignment1 : ID",
"assignment1 : consttype",
"consttype : NUM",
"consttype : REAL",
"$$13 :",
"$$14 :",
"$$15 :",
"Declaration : Type ID $$13 '=' $$14 E $$15 ';'",
"Declaration : assignment1 ';'",
"Declaration : Type ID '[' assignment ']' ';'",
"Declaration : ID '[' assignment1 ']' ';'",
"Declaration : STRUCT ID '{' Declaration '}' ';'",
"Declaration : STRUCT ID ID ';'",
"Declaration : error",
"$$16 :",
"array : ID $$16 '[' E ']'",
"$$17 :",
"E : E '+' $$17 T",
"$$18 :",
"E : E '-' $$18 T",
"E : T",
"$$19 :",
"$$20 :",
"E : ID $$19 LE $$20 E",
"$$21 :",
"$$22 :",
"E : ID $$21 GE $$22 E",
"$$23 :",
"$$24 :",
"E : ID $$23 EQ $$24 E",
"$$25 :",
"$$26 :",
"E : ID $$25 NEQ $$26 E",
"$$27 :",
"$$28 :",
"E : ID $$27 AND $$28 E",
"$$29 :",
"$$30 :",
"E : ID $$29 OR $$30 E",
"$$31 :",
"$$32 :",
"E : ID $$31 '<' $$32 E",
"$$33 :",
"$$34 :",
"E : ID $$33 '>' $$34 E",
"$$35 :",
"$$36 :",
"E : ID $$35 '=' $$36 E",
"E : array",
"$$37 :",
"T : T '*' $$37 F",
"$$38 :",
"T : T '/' $$38 F",
"T : F",
"F : '(' E ')'",
"$$39 :",
"F : '-' $$39 F",
"F : ID",
"F : consttype",
};

//#line 311 "parser.y"


private Yylex lexer;


public static int g_addr = 100;
public static int i=1, k=-1, j=0;
public static int[] stack = new int[100];
public static int index1 = 1;
public static int[] end = new int[100];
public static int[] arr = new int[10];
public static int ct=0,c=0,b;
public static int fl;
public static int top=0;
public static int[] label = new int[20];
public static int label_num = 0;
public static int ltop = 0;
public static String[] st1 = new String[100];
public static char[] temp_count = new char[2];
public static int[] plist = new int[100];
public static int[] flist = new int[100];
public static int errc=0;
public static int lnum1 = 0;
public static String temp = "t";


void scope_start()
{
	stack[index1]=i;
	i++;
	index1++;
	return;
}

void scope_end()
{
	index1--;
	end[stack[index1]]=1;
	stack[index1]=0;
	return;
}

void if1()
{
	label_num++;
	temp = "t";
	temp = temp + temp_count[0];
	System.out.print("\n"+temp+" = not "+st1[top]+"\n");
	System.out.print("if "+temp+" goto L"+label_num+"\n");
	temp_count[0]++;
	label[++ltop]=label_num;

}

void if2()
{
	label_num++;
	System.out.print("\ngoto L"+label_num+"\n");
	System.out.print("L"+label[ltop--]);
	label[++ltop]=label_num;
}

void if3()
{
	System.out.print("\nL"+label[ltop--]);
}

void while1()
{
	label_num++;
	label[++ltop]=label_num;
	System.out.println("\nL"+label_num);
}


void while2()
{
	label_num++;
	temp = "t";
	temp = temp + temp_count[0];
	System.out.print("\n"+temp+" = not "+st1[top--]+"\n");
	System.out.print("if "+temp+" goto L"+label_num+"\n");
	temp_count[0]++;
	label[++ltop]=label_num;
}


void while3()
{
	int y=label[ltop--];
	System.out.println("\ngoto L"+label[ltop--]);
	System.out.print("L"+y+":\n");
}

void dowhile1()
{
	label_num++;
	label[++ltop]=label_num;
	System.out.print("\nL"+label_num+"\n");
}

void dowhile2()
{
	System.out.print("\nif "+st1[ltop--]+" goto L"+label[ltop--]+"\n");
}

void for1()
{
	label_num++;
	label[++ltop]=label_num;
	System.out.print("\nL"+label_num+":\n");
}

void for2()
{
	label_num++;
	temp = "t";
	temp = temp + temp_count[0];
	System.out.print("\n"+temp+" = not "+st1[top--]+"\n");
	System.out.print("if "+temp+" goto L"+label_num+"\n");
	temp_count[0]++;
	label[++ltop]=label_num;
	label_num++;
	System.out.print("goto L"+label_num+"\n");
	label[++ltop]=label_num;
	label_num++;
	System.out.print("L"+label_num+"\n");
	label[++ltop]=label_num;
}


void for3()
{
	System.out.print("\ngoto L"+label[ltop-3]+"\n");
	System.out.print("L"+label[ltop-1]+"\n");
}

void for4()
{
	System.out.print("\ngoto L"+label[ltop]+"\n");
	System.out.print("L"+label[ltop-2]+"\n");
	ltop-=4;
}


void push(String a)
{
	st1[++top] = a;
}

void array1()
{
	temp = "t";
	temp = temp + temp_count[0];
	System.out.print("\n"+temp+" = "+st1[top]+"\n");
	st1[top] = temp;
	temp_count[0]++;
	temp = "t";
	temp = temp + temp_count[0];
	System.out.print(temp+" = "+st1[top-1]+" [ "+st1[top]+" ] \n");
	top--;
	st1[top] = temp;
	temp_count[0]++;
}


void codegen()
{
	temp = "t";
	temp = temp + temp_count[0];
	System.out.println("\n"+temp+" = "+st1[top-2]+" "+st1[top-1]+" "+st1[top]);
	top-=2;
	st1[top] = temp;
	temp_count[0]++;
}

void codegen_umin()
{
	temp = "t";
	temp = temp + temp_count[0];
	System.out.print("\n"+temp+" = -"+st1[top]+"\n");
	top--;
	st1[top] = temp;
	temp_count[0]++;
}

void codegen_assign()
{
	System.out.print("\n"+st1[top-2]+" = "+st1[top]+"\n");
	top-=2;
}

class Sym{
    public int sno;
    public int[] type = new int[100];
    public int[] paratype = new int[100];
    public int tn;
    public int pn;
    public int index;
    public int scope;
    public String token;
    public float fvalue;


    public Sym(int sno, int tn, int pn,
         int index, int scope, String token, float fvalue) {
            this.sno = sno;
            this.tn = tn;
            this.pn = pn;
            this.index = index;
            this.scope = scope;
            this.token = "";
            this.fvalue = fvalue;
        }
}

public static Sym[] st = new Sym[100];
public static int n=0;
public static int tnp;


public int returntype_func(int ct)
{
	return arr[ct-1];
}
public void storereturn( int ct, int returntype )
{
	arr[ct] = returntype;
	return;
}
public void insertscope(String a,int s)
{
	int i;
	for(i=0;i<n;i++)
	{
		if(st[i].token.equals(a))
		{
			st[i].scope=s;
			break;
		}
	}
}
public int returnscope(String a,int cs)
{
	int i;
	int max = 0;
	for(i=0;i<=n;i++)
	{
		if(st[i].token.equals(a) && cs>=st[i].scope)
		{
			if(st[i].scope>=max)
				max = st[i].scope;
		}
	}
	return max;
}
public int lookup(String a)
{
	int i;
	for(i=0;i<n;i++)
	{
		if(st[i].token.equals(a)) {
			return 0;
        }
	}
	return 1;
}
int returntype(String a,int sct)
{
	int i;
	for(i=0;i<n;i++)
	{
		if(st[i].token.equals(a) && st[i].scope==sct){
			return st[i].type[0];
        }
	}
    return 0;
}

int returntype2(String a,int sct)
{
	int i;
	for(i=0;i<n;i++)
	{
		if(st[i].token.equals(a) && st[i].scope==sct)
			{ return st[i].type[1];}
	}
    return 0;
}

int returntypef(String a)
{
	int i;
	for(i=0;i<n;i++)
	{
		if(st[i].token.equals(a) ) {
            return st[i].type[1];
        }
	}
    return 0;
}


void check_scope_update(String a, String b,int sc)
{
	int i,j,k;
	int max=0;
	for(i=0;i<=n;i++)
	{
		if(st[i].token.equals(a) && sc>=st[i].scope)
		{
			if(st[i].scope>=max)
				max=st[i].scope;
		}
	}
	for(i=0;i<=n;i++)
	{
		if(st[i].token.equals(a) && max==st[i].scope)
		{
            float temp;
            try {
			    temp=Float.parseFloat(b);
            } catch (NumberFormatException nve){
                temp=0; 
            }
			for(k=0;k<st[i].tn;k++)
			{
				if(st[i].type[k]==INT)
					st[i].fvalue=(int)temp;
				else
					st[i].fvalue=temp;
			}
		}
	}
}
void storevalue(String a, String b, int s_c)
{
	int i;
	for(i=0;i<=n;i++)
	{
		if(st[i].token.equals(a) && s_c==st[i].scope)
		{
			st[i].fvalue=Float.parseFloat(b);
		}
	}
}

void insert(String name, int type)
{
	int i;
	if(lookup(name) != 0)
	{
		st[n].token = name;
		st[n].tn=1;
        st[n].pn=0;
		st[n].type[st[n].tn-1]=type;
		st[n].sno=n+1;
		n++;
	}
	else
	{
		for(i=0;i<n;i++)
		{
			if(st[i].token.equals(name))
			{
				st[i].tn++;
				st[i].type[st[i].tn-1]=type;
				break;
			}
		}
	}

	return;
}

void insert_dup(String name, int type, int s_c)
{
	st[n].token = name;
	st[n].tn=1;
    st[n].pn=0;
	st[n].type[st[n].tn-1]=type;
	st[n].sno=n+1;
	st[n].scope=s_c;
	n++;
	return;
}

void insert_by_scope(String name, int type, int s_c)
{
 	int i;
	for(i=0;i<n;i++)
 	{
  		if(st[i].token.equals(name) && st[i].scope==s_c)
  		{
   			st[i].tn++;
   			st[i].type[st[i].tn-1]=type;
  		}
 	}
}

void insertp(String name,int type)
{
 	int i;
 	for(i=0;i<n;i++)
 	{
  		if(st[i].token.equals(name))
  		{
   			st[i].pn++;
   			st[i].paratype[st[i].pn-1]=type;
   			break;
  		}
 	}
}

void insert_index(String name, String ind)
{
 	int i;
 	for(i=0;i<n;i++)
 	{
  		if( st[i].token.equals(name) && st[i].type[0]==ARRAY)
  		{

   			st[i].index = Integer.parseInt(ind);
  		}
	}
}

int checkp(String name,int flist,int c)
{
 	int i,j;
 	for(i=0;i<n;i++)
 	{
  		if(st[i].token.equals(name))
  		{
    			if(st[i].paratype[c]!=flist)
    			return 1;
  		}
 	}
 	return 0;
}

public static void display()
{
	int i,j;
	System.out.print("\n");
	System.out.print("-------------------------------------------------------Symbol Table-----------------------------------------------------------\n");
	System.out.print("------------------------------------------------------------------------------------------------------------------------------\n");
	System.out.print("\nSNo\tIdentifier\tScope\t\tValue\t\tType\t\t\tParameter type(for functions)\n");
	System.out.print("------------------------------------------------------------------------------------------------------------------------------\n\n");
	for(i=0;i<n;i++)
	{
		if(st[i].type[0]== INT || st[i].type[1] == INT || st[i].type[1] == VOID)
			System.out.print(st[i].sno+"\t"+st[i].token+"\t\t"+st[i].scope+"\t\t"+(int)st[i].fvalue+"\t");
		else
			System.out.print(st[i].sno+"\t"+st[i].token+"\t\t"+st[i].scope+"\t\t"+st[i].fvalue+"\t");
                System.out.print("\t");
		for(j=0;j<st[i].tn;j++)
		{
			if(st[i].type[j]== INT)
				System.out.print("INT");
			else if(st[i].type[j]== FLOAT)
				System.out.print("FLOAT");
			else if(st[i].type[j]==FUNCTION)
				System.out.print("FUNCTION");
			else if(st[i].type[j]==ARRAY)
				System.out.print("ARRAY");
			else if(st[i].type[j]==VOID)
				System.out.print("VOID");
                        if(st[i].tn>1 && j<(st[i].tn-1))System.out.print(" - ");
		}
                System.out.print("\t\t");
		for(j=0;j<st[i].pn;j++)
		{
			if(st[i].paratype[j]==INT)
				System.out.print("INT");
			else if(st[i].paratype[j]==FLOAT)
				System.out.print("FLOAT");
			if(st[i].pn>1 && j<(st[i].pn-1))System.out.print(", ");
		}
		System.out.print("\n");
	}
	System.out.print("------------------------------------------------------------------------------------------------------------------------------\n\n");
	return;
}


/*
 *public int lexer.getLine()
 *{
 *    return yylineno;
 *}
 */
public static void push()
{
	stack[top]=i;
	i++;
	top++;
	return;
}
public static void pop()
{
	top--;
	end[stack[top]]=1;
	stack[top]=0;
	return;
}

private int yylex(){
    int yyl_return = -1;
    try{
        yylval = new ParserVal();
        yyl_return = lexer.yylex();
        //if(yyl_return == -1)
        //    yyerror("Syntax Error");
    }
    catch (IOException e){
        System.err.println("IO error: "+e);
    }
    return yyl_return;
}

public void yyerror(String error) {
    System.err.println("Error: "+ error + " at line " + lexer.getLine()); 
    errc+=1;
}

public Parser(Reader r){
    lexer = new Yylex(r, this);
}

public void init() {
    for(int i = 0; i < 100; i++)
        st[i] = new Sym(0,0,0,-1,0,"",(float)0.0);
    temp_count[0] = '0';
    temp_count[0] = '0';
}

public static void main(String args[]) throws IOException{

    Parser yyparser;
    if(args.length == 0)
        System.exit(1);

    yyparser = new Parser(new FileReader(args[0]));

    yyparser.init();
    int par = yyparser.yyparse();
	if(par == 0 && errc <= 0)
        System.out.println("\nParsing Complete and OK!");
    else
        System.out.println("\nParsing Failed!");
   //display();
   //Yylex.display();
}

//#line 1004 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 5:
//#line 28 "parser.y"
{
            if( !val_peek(3).sval.equals("main")) {
                System.out.print("goto F"+lnum1+"\n");
            }
            if (val_peek(4).ival!=returntype_func(ct)) {
                System.out.print("\nError : Type mismatch : Line "+lexer.getLine()+"\n");
                errc++;
            }
            if (val_peek(3).sval.equals("printf") || val_peek(3).sval.equals("scanf") || val_peek(3).sval.equals("getc") || val_peek(3).sval.equals("gets") || val_peek(3).sval.equals("getchar") || val_peek(3).sval.equals("puts") || val_peek(3).sval.equals("putchar") || val_peek(3).sval.equals("clearerr") || val_peek(3).sval.equals("getw") || val_peek(3).sval.equals("putw") || val_peek(3).sval.equals("putc") || val_peek(3).sval.equals("rewind") || val_peek(3).sval.equals("sprint") || val_peek(3).sval.equals("sscanf") || val_peek(3).sval.equals("remove") || val_peek(3).sval.equals("fflush"))
                {System.out.print("Error : RRedeclaration of "+val_peek(3).sval+" : Line "+lexer.getLine()+"\n"); errc++;}
            else
            {
                insert(val_peek(3).sval,FUNCTION);
                insert(val_peek(3).sval,val_peek(4).ival);
                g_addr+=4;
            }
	    }
break;
case 6:
//#line 45 "parser.y"
{
            if (val_peek(5).ival!=returntype_func(ct)) {
                System.out.print("\nError : Type mismatch : Line "+lexer.getLine()+"\n"); 
                errc++;
            }
                if (val_peek(4).sval.equals("printf") || val_peek(4).sval.equals("scanf") || val_peek(4).sval.equals("getc") || val_peek(4).sval.equals("gets") || val_peek(4).sval.equals("getchar") || val_peek(4).sval.equals("puts") || val_peek(4).sval.equals("putchar") || val_peek(4).sval.equals("clearerr") || val_peek(4).sval.equals("getw") || val_peek(4).sval.equals("putw") || val_peek(4).sval.equals("putc") || val_peek(4).sval.equals("rewind") || val_peek(4).sval.equals("sprint") || val_peek(4).sval.equals("sscanf") || val_peek(4).sval.equals("remove") || val_peek(4).sval.equals("fflush"))
                {System.out.print("Error : rRedeclaration of "+val_peek(4).sval+" : Line "+lexer.getLine()+"\n");errc++;}
                else {
                    insert(val_peek(4).sval,FUNCTION);
                    insert(val_peek(4).sval,val_peek(5).ival);
                    for(j=0;j<=k;j++) {
                      insertp(val_peek(4).sval,plist[j]);
                    }
                    k=-1;
                }
	    }
break;
case 9:
//#line 67 "parser.y"
{plist[++k]=val_peek(1).ival;insert(val_peek(0).sval,val_peek(1).ival);insertscope(val_peek(0).sval,i);}
break;
case 22:
//#line 88 "parser.y"
{
            try {
                int i =  Integer.parseInt(val_peek(1).sval);
                storereturn(ct,INT);
            } catch (NumberFormatException nfe) {
                float i = Float.parseFloat(val_peek(1).sval);
                storereturn(ct,FLOAT);
            }
            ct++;
	 }
break;
case 23:
//#line 98 "parser.y"
{storereturn(ct,VOID); ct++;}
break;
case 24:
//#line 99 "parser.y"
{
          int sct=returnscope(val_peek(1).sval,stack[top-1]);	/*stack[top-1] - current scope*/
          int type=returntype(val_peek(1).sval,sct);
          if (type == FLOAT)
            storereturn(ct,FLOAT);
          else 
            storereturn(ct,INT);
          ct++;
     }
break;
case 28:
//#line 114 "parser.y"
{dowhile1();}
break;
case 29:
//#line 114 "parser.y"
{dowhile2();}
break;
case 31:
//#line 117 "parser.y"
{for1();}
break;
case 32:
//#line 117 "parser.y"
{for2();}
break;
case 33:
//#line 117 "parser.y"
{for3();}
break;
case 34:
//#line 117 "parser.y"
{for4();}
break;
case 35:
//#line 120 "parser.y"
{if1();}
break;
case 36:
//#line 120 "parser.y"
{if2();}
break;
case 38:
//#line 123 "parser.y"
{if3();}
break;
case 40:
//#line 127 "parser.y"
{while1();}
break;
case 41:
//#line 127 "parser.y"
{while2();}
break;
case 42:
//#line 127 "parser.y"
{while3();}
break;
case 49:
//#line 140 "parser.y"
{push(val_peek(0).sval);}
break;
case 50:
//#line 140 "parser.y"
{st1[++top] = "=";}
break;
case 51:
//#line 140 "parser.y"
{codegen_assign();}
break;
case 52:
//#line 141 "parser.y"
{
		int sct=returnscope(val_peek(5).sval,stack[index1-1]);
		int type=returntype(val_peek(5).sval,sct);
        try {
            int i =  Integer.parseInt(val_peek(1).sval);
        } catch (NumberFormatException nfe) {
            if(type != INT && fl == 0) {
                System.out.print("\nError : Type lMismatch : Line "+lexer.getLine()+"\n");
                errc++;
            }
        }
        if(lookup(val_peek(5).sval) == 0)
		{
			int currscope=stack[index1-1];
			int scope=returnscope(val_peek(5).sval,currscope);
			if((scope<=currscope && end[scope]==0) && !(scope==0))
				check_scope_update(val_peek(5).sval,val_peek(1).sval,currscope);
		}
	}
break;
case 53:
//#line 161 "parser.y"
{
		if(lookup(val_peek(2).sval) != 0)
			System.out.print("\nUUndeclared Variable "+val_peek(2).sval+" : Line "+lexer.getLine()+"\n"); errc++;
	}
break;
case 55:
//#line 166 "parser.y"
{
		if(lookup(val_peek(0).sval) != 0)
			{ System.out.print("\nnUndeclared Variable "+val_peek(0).sval+" : Line "+lexer.getLine()+"\n"); errc++; }
	}
break;
case 59:
//#line 177 "parser.y"
{push(val_peek(0).sval);}
break;
case 60:
//#line 177 "parser.y"
{st1[++top] = "="; }
break;
case 61:
//#line 177 "parser.y"
{codegen_assign();}
break;
case 62:
//#line 177 "parser.y"
{
        try {
            int i =  Integer.parseInt(val_peek(2).sval);
        } catch (NumberFormatException nfe) {
            if(val_peek(7).ival == INT && fl == 0) {
                System.out.print("\nError : Type Mmmismatch : Line "+lexer.getLine()+"\n");
                errc++;
                fl=1;
            }
        }
		if(lookup(val_peek(6).sval) == 0) {
			int currscope=stack[index1-1];
			int previous_scope=returnscope(val_peek(6).sval,currscope);
			if(currscope==previous_scope) {
                System.out.println("\nError : Redeclaration of "+val_peek(6).sval+" : Line "+lexer.getLine());
                errc++;
            }
			else {
				insert_dup(val_peek(6).sval,val_peek(7).ival,currscope);
				check_scope_update(val_peek(6).sval,val_peek(2).sval,stack[index1-1]);
				int sg=returnscope(val_peek(6).sval,stack[index1-1]);
				g_addr+=4;
			}
		}
		else {
			int scope=stack[index1-1];
			insert(val_peek(6).sval,val_peek(7).ival);
			insertscope(val_peek(6).sval,scope);
			check_scope_update(val_peek(6).sval,val_peek(2).sval,stack[index1-1]);
			g_addr+=4;
		}
	}
break;
case 63:
//#line 209 "parser.y"
{
		if(lookup(val_peek(1).sval) == 0)
		{
			int currscope=stack[index1-1];
			int scope=returnscope(val_peek(1).sval,currscope);
			if(!(scope<=currscope && end[scope]==0) || scope==0)
				{System.out.print("\nError : Variable "+val_peek(1).sval+" out of scope : Line "+lexer.getLine()+"\n");errc++;}
		}
		else
			{System.out.print("\nError : uUndeclared Variable "+val_peek(1).sval+" : Line "+lexer.getLine()+"\n");errc++;}
	}
break;
case 64:
//#line 220 "parser.y"
{
            int itype;
            try {
                int i =  Integer.parseInt(val_peek(2).sval);
                itype = INT;
            } catch (NumberFormatException nfe) {
                float i = Float.parseFloat(val_peek(2).sval);
                itype = FLOAT;
            }

             if(itype != INT) {
                System.out.print("\nError : Array index must be of type int : Line "+lexer.getLine()+"\n");errc++;
            }

            try {
                if(Integer.parseInt(val_peek(2).sval)<=0) {
                    System.out.print("\nError : Array index must be of type int > 0 : Line "+lexer.getLine()+"\n");
                    errc++;
                }
            } catch (NumberFormatException nfe) {
                System.out.print("\nError : Array index must be of type int  : Line "+lexer.getLine()+"\n");
                errc++;
            }

            if(lookup(val_peek(4).sval) == 0) {
                int currscope=stack[top-1];
                int previous_scope=returnscope(val_peek(4).sval,currscope);
                if(currscope==previous_scope) {
                    System.out.print("\nError : Redeclaration of "+val_peek(4).sval+" : Line "+lexer.getLine()+"\n");errc++;
                }
                else {
                    insert_dup(val_peek(4).sval,ARRAY,currscope);
                    insert_by_scope(val_peek(4).sval,val_peek(5).ival,currscope);	/*to insert type to the correct identifier in case of multiple entries of the identifier by using scope*/
                    if (itype==INT) {
                        insert_index(val_peek(4).sval,val_peek(2).sval);
                    }
                }
            }
            else {
                int scope=stack[top-1];
                insert(val_peek(4).sval,ARRAY);
                insert(val_peek(4).sval,val_peek(5).ival);
                insertscope(val_peek(4).sval,scope);
                if (itype==INT) {
                    insert_index(val_peek(4).sval,val_peek(2).sval);
                }
            }
    }
break;
case 66:
//#line 269 "parser.y"
{
	    insert(val_peek(4).sval,STRUCT);
	    g_addr+=4;
    }
break;
case 67:
//#line 273 "parser.y"
{
        insert(val_peek(1).sval,STRUCT_VAR);
	    g_addr+=4;
    }
break;
case 69:
//#line 281 "parser.y"
{push(val_peek(0).sval);}
break;
case 71:
//#line 284 "parser.y"
{st1[++top] = "+";}
break;
case 72:
//#line 284 "parser.y"
{codegen();}
break;
case 73:
//#line 285 "parser.y"
{st1[++top] = "-";}
break;
case 74:
//#line 285 "parser.y"
{codegen();}
break;
case 76:
//#line 287 "parser.y"
{push(val_peek(0).sval);}
break;
case 77:
//#line 287 "parser.y"
{st1[++top] = "<=";}
break;
case 78:
//#line 287 "parser.y"
{codegen();}
break;
case 79:
//#line 288 "parser.y"
{push(val_peek(0).sval);}
break;
case 80:
//#line 288 "parser.y"
{st1[++top] = ">=";}
break;
case 81:
//#line 288 "parser.y"
{codegen();}
break;
case 82:
//#line 289 "parser.y"
{push(val_peek(0).sval);}
break;
case 83:
//#line 289 "parser.y"
{st1[++top] = "==";}
break;
case 84:
//#line 289 "parser.y"
{codegen();}
break;
case 85:
//#line 290 "parser.y"
{push(val_peek(0).sval);}
break;
case 86:
//#line 290 "parser.y"
{st1[++top] = "!=";}
break;
case 87:
//#line 290 "parser.y"
{codegen();}
break;
case 88:
//#line 291 "parser.y"
{push(val_peek(0).sval);}
break;
case 89:
//#line 291 "parser.y"
{st1[++top] = "&&";}
break;
case 90:
//#line 291 "parser.y"
{codegen();}
break;
case 91:
//#line 292 "parser.y"
{push(val_peek(0).sval);}
break;
case 92:
//#line 292 "parser.y"
{st1[++top] = "||";}
break;
case 93:
//#line 292 "parser.y"
{codegen();}
break;
case 94:
//#line 293 "parser.y"
{push(val_peek(0).sval);}
break;
case 95:
//#line 293 "parser.y"
{st1[++top] = "<";}
break;
case 96:
//#line 293 "parser.y"
{codegen();}
break;
case 97:
//#line 294 "parser.y"
{push(val_peek(0).sval);}
break;
case 98:
//#line 294 "parser.y"
{st1[++top] = ">";}
break;
case 99:
//#line 294 "parser.y"
{codegen();}
break;
case 100:
//#line 295 "parser.y"
{push(val_peek(0).sval);}
break;
case 101:
//#line 295 "parser.y"
{st1[++top] = "=";}
break;
case 102:
//#line 295 "parser.y"
{codegen_assign();}
break;
case 103:
//#line 296 "parser.y"
{array1();}
break;
case 104:
//#line 298 "parser.y"
{st1[++top] = "*";}
break;
case 105:
//#line 298 "parser.y"
{codegen();}
break;
case 106:
//#line 299 "parser.y"
{st1[++top] = "/";}
break;
case 107:
//#line 299 "parser.y"
{codegen();}
break;
case 109:
//#line 302 "parser.y"
{yyval.sval=val_peek(1).sval;}
break;
case 110:
//#line 303 "parser.y"
{st1[++top] = "-";}
break;
case 111:
//#line 303 "parser.y"
{codegen_umin();}
break;
case 112:
//#line 304 "parser.y"
{push(val_peek(0).sval);fl=1;}
break;
case 113:
//#line 305 "parser.y"
{push(val_peek(0).sval);}
break;
//#line 1615 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
