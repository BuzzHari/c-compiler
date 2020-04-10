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
public final static short NEQ=282;
public final static short AND=283;
public final static short OR=284;
public final static short UMINUS=285;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    8,    8,   11,   11,   12,    7,
    7,    7,   10,   13,   13,   13,   14,   14,   14,   14,
   14,   14,   14,   14,   14,   14,   14,   14,   19,   20,
   18,   21,   22,   23,   16,   24,   26,   15,   25,   25,
   27,   28,   17,    1,    1,    1,    1,    1,    1,   29,
   30,   31,    2,    2,    2,    2,    2,   32,   32,   33,
   33,   34,    3,    3,   35,   36,   37,    9,    9,    9,
    9,    9,    9,    9,   39,   38,   40,    4,   41,    4,
    4,   42,   43,    4,   44,   45,    4,   46,   47,    4,
   48,   49,    4,   50,   51,    4,   52,   53,    4,   54,
   55,    4,   56,   57,    4,   58,   59,    4,    4,   60,
    5,   61,    5,    5,    6,   62,    6,    6,    6,
};
final static short yylen[] = {                            2,
    2,    2,    2,    0,    5,    6,    3,    1,    2,    1,
    1,    1,    3,    2,    1,    0,    1,    1,    4,    1,
    1,    1,    3,    2,    3,    1,    5,    1,    0,    0,
    9,    0,    0,    0,   12,    0,    0,    8,    2,    0,
    0,    0,    7,    3,    3,    3,    3,    1,    1,    0,
    0,    0,    6,    3,    3,    1,    1,    6,    4,    3,
    1,    1,    1,    1,    0,    0,    0,    8,    2,    6,
    5,    6,    4,    1,    0,    5,    0,    4,    0,    4,
    1,    0,    0,    5,    0,    0,    5,    0,    0,    5,
    0,    0,    5,    0,    0,    5,    0,    0,    5,    0,
    0,    5,    0,    0,    5,    0,    0,    5,    1,    0,
    4,    0,    4,    1,    3,    0,    3,    1,    1,
};
final static short yydefred[] = {                         0,
   74,   10,   11,   12,    0,   63,   64,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    2,    0,
   69,    0,    0,    1,    3,    0,   54,    0,   51,    0,
    0,   55,    0,    0,    0,    0,    0,   73,    0,    0,
    0,    0,    0,    8,    0,    0,    0,   66,   71,    0,
    0,  116,  119,    0,    0,  114,  109,    0,    0,    0,
    5,    9,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   77,   79,   53,  110,  112,   72,   15,    0,
    6,    7,   44,   46,   45,   70,   47,    0,    0,   83,
   86,   89,   92,   95,   98,  101,  104,  107,  115,  118,
  117,    0,    0,    0,    0,    0,   41,    0,    0,    0,
   29,    0,   13,   26,   17,   28,   14,   18,   20,   21,
   22,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  111,  113,    0,    0,    0,    0,
   24,    0,    0,    0,    0,   68,   76,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   25,
   23,    0,    0,    0,   19,    0,   36,    0,    0,    0,
   42,    0,   27,    0,    0,    0,   37,    0,    0,   43,
    0,   30,    0,    0,   38,    0,    0,   39,   31,    0,
    0,    0,   35,
};
final static short yydgoto[] = {                         10,
   46,   11,   53,   54,   55,   56,   13,   14,   15,   61,
   43,   44,   90,  127,  128,  129,  130,  131,  154,  196,
  180,  193,  201,  182,  195,  191,  148,  186,   18,   37,
   85,    0,    0,    0,   35,   70,  132,   57,   71,  112,
  113,   72,  134,   73,  135,   74,  136,   75,  137,   76,
  138,   77,  139,   78,  140,   79,  141,   80,  142,  114,
  115,   82,
};
final static short yysindex[] = {                       -78,
    0,    0,    0,    0,  -34,    0,    0,  -78, -234,    0,
  -11,  -16, -193,  -78,  -78, -124, -124,   12,    0, -114,
    0, -124,   18,    0,    0,   42,    0,   64,    0,   57,
   30,    0,  -27,  -63,   20,  102,  -37,    0,  -88,   50,
   53,  -74,  126,    0,  -19,  107,  151,    0,    0,    0,
  -37,    0,    0,  148,   23,    0,    0,  110,  160,   53,
    0,    0,   53,  -24, -207,  -63,  -63,  162,  -63,  -37,
  147,  -28,  -23,  -38,  -26,  -25,  -29,  200,  199,  202,
  111,  -33,    0,    0,    0,    0,    0,    0,    0,  -54,
    0,    0,    0,    0,    0,    0,    0,  148,  -37,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -33,  -33,  -33,  -33,  -17,    0,  235,  -48,  236,
    0,  237,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  220,   17,  -37,  -37,  -37,  -37,  -37,  -37,  -37,
  -37,  -37,   23,   23,    0,    0,  240,  242,  -37,  225,
    0,  234,   27,   53,  -37,    0,    0,  148,  148,  148,
  148,  148,  148,  148,  148,  148,  238,  -37,  123,    0,
    0,  253,   35,  148,    0,  144,    0,  241,  256,  243,
    0,   53,    0,  -37,  -37,   53,    0,  149,  148,    0,
   28,    0,  244,   53,    0,  246,  -37,    0,    0,  148,
  258,   53,    0,
};
final static short yyrindex[] = {                       306,
    0,    0,    0,    0,  157,    0,    0,  306,    0,    0,
    0,   39,    0,  306,  306,    0,    0,    0,    0,    0,
    0,    0,  247,    0,    0,   65,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  214,    0,  216,    0,    0,  -41,
    0,    0,    0,   60,   34,    0,    0,  247,    0,    9,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  251,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  157,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   37,   49,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -10,   -8,    2,
    4,   25,   76,   80,   81,   84,    0,    0,    0,    0,
    0,    0,    0,  252,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  254,    0,
  -12,    0,    0,    0,    0,    0,    0,    0,    0,  271,
    0,    0,    0,
};
final static short yygindex[] = {                        26,
   62,  143,   22,  -35,   32,   33,  -18,    0,  -14,  -31,
    0,  250,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static int YYTABLESIZE=314;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        118,
  118,  118,   51,  118,  124,  118,   51,   52,   31,   16,
  151,   52,   39,   41,   42,   81,   40,  118,  100,  106,
  103,   12,  147,   67,   66,   20,   16,   22,   89,   12,
   84,   91,   87,   19,   98,   12,   12,   12,   12,   24,
   25,   65,   90,   12,   93,   42,   40,   21,   84,   75,
   87,  118,   12,    6,    7,   47,   17,   33,  126,   83,
   90,   84,   93,  133,   86,   96,   23,   16,   60,   87,
  123,   39,   29,   17,   81,  125,   81,   78,   81,   78,
   48,   78,   84,   96,   87,   16,   93,   47,   47,   80,
   47,   80,   81,   80,   90,   78,   93,   57,  158,  159,
  160,  161,  162,  163,  164,  165,  166,   80,   34,  157,
   40,   12,   40,  169,  111,   38,   99,   96,   52,  174,
  102,  105,  173,   56,  108,   50,   81,   94,   95,   78,
   97,   57,  176,   16,   99,   26,    6,    7,  102,  105,
  152,   80,  108,  143,  144,   30,  145,  146,  188,  189,
  187,  109,   52,   83,  190,   84,   36,   56,   27,   28,
   49,  200,  198,  177,   32,   83,   63,   84,   99,   64,
  203,   58,  102,  105,   59,   60,  108,    1,    2,    3,
    4,    5,    6,    7,  181,   62,   83,    8,   84,  192,
   83,   83,   84,   84,   69,    9,   45,    6,    7,   68,
   34,    1,    2,    3,    4,  116,    6,    7,  117,  118,
  119,  150,    6,    7,  120,   56,  121,   50,   88,    9,
   96,  122,   50,    6,    7,   82,  110,    6,    7,    2,
    3,    4,    2,    3,    4,   85,   88,   99,  100,  102,
   91,   94,   97,   40,   40,   40,   40,   40,   40,   40,
   40,   40,   40,  101,  105,  103,   40,  104,   40,  106,
  107,   40,  108,   40,   16,   16,   16,   16,   16,   16,
   16,   16,   16,   16,  149,  153,  155,   16,  156,   16,
  167,  168,   16,  170,   16,    1,    2,    3,    4,    5,
    6,    7,  171,  178,  172,  184,  175,  179,  202,  183,
  194,  185,  197,    9,  199,    4,   48,   65,   49,   67,
   32,   34,   33,   92,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   40,   45,   59,   47,   40,   45,  123,   44,
   59,   45,   31,   41,   33,   51,   31,   59,   60,   61,
   62,    0,   40,   43,   44,  260,   44,   44,   60,    8,
   41,   63,   41,    8,   70,   14,   15,   16,   17,   14,
   15,   61,   41,   22,   41,   64,   59,   59,   59,   91,
   59,   93,   31,  261,  262,   34,   91,   40,   90,   43,
   59,   45,   59,   99,   42,   41,  260,   59,  123,   47,
  125,   90,   61,   91,   41,   90,   43,   41,   45,   43,
   61,   45,   93,   59,   93,   44,   65,   66,   67,   41,
   69,   43,   59,   45,   93,   59,   93,   59,  134,  135,
  136,  137,  138,  139,  140,  141,  142,   59,   91,   93,
  123,   90,  125,  149,   82,   59,   41,   93,   59,  155,
   41,   41,  154,   59,   41,   61,   93,   66,   67,   93,
   69,   93,  168,  125,   59,  260,  261,  262,   59,   59,
  119,   93,   59,  112,  113,  260,  114,  115,  184,  185,
  182,   41,   93,   43,  186,   45,   93,   93,   16,   17,
   59,  197,  194,   41,   22,   43,   41,   45,   93,   44,
  202,  260,   93,   93,  125,  123,   93,  256,  257,  258,
  259,  260,  261,  262,   41,  260,   43,  266,   45,   41,
   43,   43,   45,   45,   44,  274,  260,  261,  262,   93,
   91,  256,  257,  258,  259,  260,  261,  262,  263,  264,
  265,  260,  261,  262,  269,   59,  271,   61,   59,  274,
   59,  276,  260,  261,  262,  267,  260,  261,  262,  257,
  258,  259,  257,  258,  259,  277,  278,   91,  267,  278,
  282,  283,  284,  256,  257,  258,  259,  260,  261,  262,
  263,  264,  265,  277,  284,  282,  269,  283,  271,   60,
   62,  274,   61,  276,  256,  257,  258,  259,  260,  261,
  262,  263,  264,  265,   40,   40,   40,  269,   59,  271,
   41,   40,  274,   59,  276,  256,  257,  258,  259,  260,
  261,  262,   59,   41,  268,   40,   59,  263,   41,   59,
  273,   59,   59,  274,   59,    0,   93,   61,   93,   59,
   59,   41,   59,   64,
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
"ELSE","STRUCT","STRUCT_VAR","FOR","GE","EQ","NE","INC","DEC","NEQ","AND","OR",
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
"StmtList : CompoundStmt",
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
"function_call : ID '=' E '(' paralist ')'",
"function_call : ID '(' paralist ')'",
"paralist : paralist ',' param",
"paralist : param",
"param : ID",
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

//#line 466 "parser.y"


private Yylex lexer;
public static int i=1,k=-1,l=-1;
public static int j=0;
public static String curfunc;
public static int[] stack = new int[100];
public static int top=0;
public static int fl;
public static int[] plist = new int[100];
public static int[] flist = new int[100];
public static int[] end = new int[100];
public static int[] arr = new int[10];
public static int ct=0,c=0,b;
public static int loop = 0;
public static int errc=0;
public static int type=0;
public static int g_addr = 100;
public static int lnum1 = 0;
public static int index1 = 1;
public static int f1;
public static int[] label = new int[20];
public static int label_num = 0;
public static int ltop = 0;
public static String[] st1 = new String[10];
public static char[] temp_count = new char[2];
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
	/* strcpy(temp,"t"); */
	temp = "t";
	/* strcat(temp,temp_count); */
	temp = temp + temp_count;
	/* printf("\n%s = not %s\n",temp,st1[top]); */
	System.out.print("\n"+temp+" = not "+st1[top]+"\n");
 	/* printf("if %s goto L%d\n",temp,label_num); */
	System.out.print("if "+temp+" goto L"+label_num+"\n");
	temp_count[0]++;
	label[++ltop]=label_num;

}
void if2()
{
	label_num++;
	/* printf("\ngoto L%d\n",label_num); */
	System.out.print("\ngoto L"+label_num+"\n");
	/* printf("L%d: \n",label[ltop--]); */
	System.out.print("L"+label[ltop--]);
	label[++ltop]=label_num;
}
void if3()
{
	/* printf("\nL%d:\n",label[ltop--]); */
	System.out.print("\nL"+label[ltop--]);
}
void while1()
{
	label_num++;
	label[++ltop]=label_num;
	/* printf("\nL%d:\n",label_num); */
	System.out.println("\nL"+label_num);
}
void while2()
{
	label_num++;
	/* strcpy(temp,"t"); */
	temp = "t";
	/* strcat(temp,temp_count); */
	temp = temp + temp_count;
	/* printf("\n%s = not %s\n",temp,st1[top--]); */
	System.out.print("\n"+temp+" = not "+st1[top--]+"\n");
 	/* printf("if %s goto L%d\n",temp,label_num); */
	System.out.print("if "+temp+" goto L"+label_num+"\n");
	temp_count[0]++;
	label[++ltop]=label_num;
}
void while3()
{
	int y=label[ltop--];
	/* printf("\ngoto L%d\n",label[ltop--]); */
	System.out.print("\ngoto L"+label[ltop--]);
	/* printf("L%d:\n",y); */
	System.out.print("L"+y+":\n");
}
void dowhile1()
{
	label_num++;
	label[++ltop]=label_num;
	/* printf("\nL%d:\n",label_num); */
	System.out.print("\nL"+label_num+"\n");
}
void dowhile2()
{
 	/* printf("\nif %s goto L%d\n",st1[top--],label[ltop--]); */
	System.out.print("\nif "+st1[ltop--]+" goto L"+label[ltop--]+"\n");
}
void for1()
{
	label_num++;
	label[++ltop]=label_num;
	/* printf("\nL%d:\n",label_num); */
	System.out.print("\nL"+label_num+":\n");
}
void for2()
{
	label_num++;
	/* strcpy(temp,"t"); */
	temp = "t";
	/* strcat(temp,temp_count); */
	temp = temp + temp_count;
	/* printf("\n%s = not %s\n",temp,st1[top--]); */
	System.out.print("\n"+temp+" = not "+st1[top--]+"\n");
 	/* printf("if %s goto L%d\n",temp,label_num); */
	System.out.print("if "+temp+" goto L"+label_num+"\n");
	temp_count[0]++;
	label[++ltop]=label_num;
	label_num++;
	/* printf("goto L%d\n",label_num); */
	System.out.print("goto L"+label_num+"\n");
	label[++ltop]=label_num;
	label_num++;
	/* printf("L%d:\n",label_num); */
	System.out.print("L"+label_num+"\n");
	label[++ltop]=label_num;
}
void for3()
{
	/* printf("\ngoto L%d\n",label[ltop-3]); */
	System.out.print("\ngoto L"+label[ltop-3]+"\n");
	/* printf("L%d:\n",label[ltop-1]); */
	System.out.print("L"+label[ltop-1]+"\n");
}
void for4()
{
	/* printf("\ngoto L%d\n",label[ltop]); */
	System.out.print("\ngoto L"+label[ltop]+"\n");
	/* printf("L%d:\n",label[ltop-2]); */
	System.out.print("L"+label[ltop-2]+"\n");
	ltop-=4;
}
void push(String a)
{
	/* strcpy(st1[++top],a); */
	st1[++top] = a;
}
void array1()
{
	/* strcpy(temp,"t"); */
	temp = "t";
	/* strcat(temp,temp_count); */
	temp = temp + temp_count;
	/* printf("\n%s = %s\n",temp,st1[top]); */
	System.out.print("\n"+temp+" = not "+st1[top]+"\n");
	/* strcpy(st1[top],temp); */
	st1[top] = temp;
	temp_count[0]++;
	/* strcpy(temp,"t"); */
	temp = "t";
	/* strcat(temp,temp_count); */
	temp = temp + temp_count;
	/* printf("%s = %s [ %s ] \n",temp,st1[top-1],st1[top]); */
	System.out.print(temp+" = "+st1[top-1]+" [ "+st1[top]+" ] \n");
	top--;
	/* strcpy(st1[top],temp); */
	st1[top] = temp;
	temp_count[0]++;
}
void codegen()
{
	/* strcpy(temp,"t"); */
	temp = "t";
	/* strcat(temp,temp_count); */
	temp = temp + temp_count;
	/* printf("\n%s = %s %s %s\n",temp,st1[top-2],st1[top-1],st1[top]); */
	System.out.println("\n"+temp+" = "+st1[top-2]+" "+st1[top-1]+" "+st1[top]);
	top-=2;
	/* strcpy(st1[top],temp); */
	st1[top] = temp;
	temp_count[0]++;
}
void codegen_umin()
{
	/* strcpy(temp,"t"); */
	temp = "t";
	/* strcat(temp,temp_count); */
	temp = temp + temp_count;
	/* printf("\n%s = -%s\n",temp,st1[top]); */
	System.out.print("\n"+temp+" = -"+st1[top]+"\n");
	top--;
	/* strcpy(st1[top],temp); */
	st1[top] = temp;
	temp_count[0]++;
}
void codegen_assign()
{
	/* printf("\n%s = %s\n",st1[top-2],st1[top]); */
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
//int[] arr = new int[10];
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
			float temp=Float.parseFloat(b);
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
    /* System.err.println("Error: "+ error + " at line " + lexer.yyline); */
    System.out.print("Error at: ");
    lexer.getLine();
}

public Parser(Reader r){
    lexer = new Yylex(r, this);
}

public void init() {
    for(int i = 0; i < 100; i++)
        st[i] = new Sym(0,0,0,-1,0,"",(float)0.0);
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
    display();
   //Yylex.display();
}

//#line 1033 "Parser.java"
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
//#line 25 "parser.y"
{
	 if( !val_peek(3).sval.equals("main"))
	 {
	 	System.out.print("goto F"+lnum1+"\n");
	 }
        if (val_peek(4).ival!=returntype_func(ct))
        {
            System.out.print("\nError : Type mismatch : Line "+lexer.getLine()+"\n");
            errc++;
        }

        if (!val_peek(3).sval.equals("printf") && !val_peek(3).sval.equals("scanf") && !val_peek(3).sval.equals("getc") && !val_peek(3).sval.equals("gets") && !val_peek(3).sval.equals("getchar") && !val_peek(3).sval.equals("puts") && !val_peek(3).sval.equals("putchar") && !val_peek(3).sval.equals("clearerr") && !val_peek(3).sval.equals("getw") && !val_peek(3).sval.equals("putw") && !val_peek(3).sval.equals("putc") && !val_peek(3).sval.equals("rewind") && !val_peek(3).sval.equals("sprint") && !val_peek(3).sval.equals("sscanf") && !val_peek(3).sval.equals("remove") && !val_peek(3).sval.equals("fflush"))
            {System.out.print("Error : Redeclaration of "+val_peek(3).sval+" : Line "+lexer.getLine()+"\n"); errc++;}
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
        if (val_peek(5).ival!=returntype_func(ct))
        {
            System.out.print("\nError : Type mismatch : Line "+lexer.getLine()+"\n"); errc++;
        }

        if (!val_peek(4).sval.equals("printf") && !val_peek(4).sval.equals("scanf") && !val_peek(4).sval.equals("getc") && !val_peek(4).sval.equals("gets") && !val_peek(4).sval.equals("getchar") && !val_peek(4).sval.equals("puts") && !val_peek(4).sval.equals("putchar") && !val_peek(4).sval.equals("clearerr") && !val_peek(4).sval.equals("getw") && !val_peek(4).sval.equals("putw") && !val_peek(4).sval.equals("putc") && !val_peek(4).sval.equals("rewind") && !val_peek(4).sval.equals("sprint") && !val_peek(4).sval.equals("sscanf") && !val_peek(4).sval.equals("remove") && !val_peek(4).sval.equals("fflush"))
            {System.out.print("Error : Redeclaration of "+val_peek(4).sval+" : Line "+lexer.getLine()+"\n");errc++;}
        else
        {
            insert(val_peek(4).sval,FUNCTION);
            insert(val_peek(4).sval,val_peek(5).ival);
	    for(j=0;j<=k;j++)
		    {insertp(val_peek(4).sval,plist[j]);}
	    k=-1;
        }
	}
break;
case 9:
//#line 68 "parser.y"
{plist[++k]=val_peek(1).ival;insert(val_peek(0).sval,val_peek(1).ival);insertscope(val_peek(0).sval,i);}
break;
case 23:
//#line 90 "parser.y"
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
case 24:
//#line 100 "parser.y"
{storereturn(ct,VOID); ct++;}
break;
case 25:
//#line 101 "parser.y"
{
          int sct=returnscope(val_peek(1).sval,stack[top-1]);	/*stack[top-1] - current scope*/
          int type=returntype(val_peek(1).sval,sct);
          if (type == FLOAT)
            storereturn(ct,FLOAT);
          else storereturn(ct,INT);
          ct++;
     }
break;
case 29:
//#line 115 "parser.y"
{dowhile1();}
break;
case 30:
//#line 115 "parser.y"
{dowhile2();}
break;
case 32:
//#line 118 "parser.y"
{for1();}
break;
case 33:
//#line 118 "parser.y"
{for2();}
break;
case 34:
//#line 118 "parser.y"
{for3();}
break;
case 35:
//#line 118 "parser.y"
{for4();}
break;
case 36:
//#line 121 "parser.y"
{if1();}
break;
case 37:
//#line 121 "parser.y"
{if2();}
break;
case 39:
//#line 124 "parser.y"
{if3();}
break;
case 41:
//#line 128 "parser.y"
{while1();}
break;
case 42:
//#line 128 "parser.y"
{while2();}
break;
case 43:
//#line 128 "parser.y"
{while3();}
break;
case 50:
//#line 141 "parser.y"
{push(val_peek(0).sval);}
break;
case 51:
//#line 141 "parser.y"
{st1[++top] = "=";}
break;
case 52:
//#line 141 "parser.y"
{codegen_assign();}
break;
case 53:
//#line 142 "parser.y"
{
		int sct=returnscope(val_peek(5).sval,stack[index1-1]);
		int type=returntype(val_peek(5).sval,sct);
        try {
            int i =  Integer.parseInt(val_peek(1).sval);
            if(type != INT) {
                System.out.print("\nError : Type lMismatch : Line "+lexer.getLine()+"\n");
                errc++;
            }
        } catch (NumberFormatException nfe) {
            float i = Float.parseFloat(val_peek(1).sval);
            if(type != FLOAT) {
                System.out.print("\nError : Type pMismatch : Line "+lexer.getLine()+"\n");
                errc++;
            }
        }
        if (type == ARRAY) {
            System.out.print("\nError : Type xMismatch : Line "+lexer.getLine()+"\n");
            errc++;
        }

        if(lookup(val_peek(5).sval) == 0)
		{
			int currscope=stack[top-1];
			int scope=returnscope(val_peek(5).sval,currscope);
			if((scope<=currscope && end[scope]==0) && !(scope==0))
				check_scope_update(val_peek(5).sval,val_peek(1).sval,currscope);
		}
	}
break;
case 54:
//#line 172 "parser.y"
{
		if(lookup(val_peek(2).sval) != 0)
			System.out.print("\nUndeclared Variable "+val_peek(2).sval+" : Line "+lexer.getLine()+"\n"); errc++;
	}
break;
case 56:
//#line 178 "parser.y"
{
		if(lookup(val_peek(0).sval) != 0)
			{ System.out.print("\nUndeclared Variable "+val_peek(0).sval+" : Line "+lexer.getLine()+"\n"); errc++; }
	}
break;
case 58:
//#line 218 "parser.y"
{
							int sct=returnscope(val_peek(5).sval,stack[top-1]);
							int type=returntype(val_peek(5).sval,sct);
							/*printf("%s",$3);*/
							int rtype;
							rtype=returntypef(val_peek(3).sval); int ch=0;
							/*printf("%d",rtype);*/
							if(rtype!=type)
							{System.out.print("\nError : Type Mismatch : Line "+lexer.getLine()); errc++;}
							/* { printf("\nError : Type Mismatch : Line %d\n",printline()); errc++;} */
								if(lookup(val_peek(5).sval)!=0)
								{
									for(j=0;j<=l;j++)
									{ch = ch+checkp(val_peek(3).sval,flist[j],j);}
									if(ch>0)
									{System.out.print("\nError : Parameter Type Mistake or Function undeclared Line "+lexer.getLine()); errc++;}
									/* { printf("\nError : Parameter Type Mistake or Function undeclared : Line %d\n",printline()); errc++;} */
									l=-1;
								}
			}
break;
case 59:
//#line 239 "parser.y"
{
							int sct=returnscope(val_peek(3).sval,stack[top-1]);
							int type=returntype(val_peek(3).sval,sct); int ch=0;
							if(lookup(val_peek(3).sval)!=0)
							{
								for(j=0;j<=l;j++)
								{ch = ch+checkp(val_peek(3).sval,flist[j],j);}
								if(ch>0)
								{System.out.print("\nError : Parameter Type Mistake or Function undeclared Line "+lexer.getLine()); errc++;}
								/* { printf("\nError : Parameter Type Mistake or Required Function undeclared : Line %d\n",printline()); errc++;} */
								l=-1;
							}
							else
							{System.out.print("\nUndeclared Function "+val_peek(3).sval+" : Line "+lexer.getLine()+"\n");errc++;}
							/* {printf("\nUndeclared Function %s : Line %d\n",$1,printline());errc++;} */
			}
break;
case 62:
//#line 263 "parser.y"
{
            if(lookup(val_peek(0).sval) != 0) {
                System.out.print("\nUndeclared Variable "+val_peek(0).sval+" : Line "+lexer.getLine()+"\n");errc++;
            }
            else {
                int sct=returnscope(val_peek(0).sval,stack[top-1]);
                flist[++l]=returntype(val_peek(0).sval,sct);
            }
	  }
break;
case 65:
//#line 305 "parser.y"
{push(val_peek(0).sval);}
break;
case 66:
//#line 305 "parser.y"
{st1[++top] = "="; }
break;
case 67:
//#line 305 "parser.y"
{codegen_assign();}
break;
case 68:
//#line 305 "parser.y"
{
        try {
            int i =  Integer.parseInt(val_peek(2).sval);
            if(val_peek(7).ival != INT) {
                System.out.print("\nError : Type Mmismatch : Line "+lexer.getLine()+"\n"); errc++;
            }
        } catch (NumberFormatException nfe) {
            float i = Float.parseFloat(val_peek(2).sval);
            if(val_peek(7).ival != FLOAT) {
                System.out.print("\nError : Type mMismatch : Line "+lexer.getLine()+"\n"); errc++;
            }
        }
        if (val_peek(7).ival == ARRAY) {
            System.out.print("\nError : Type Mmmismatch : Line "+lexer.getLine()+"\n");errc++;
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
case 69:
//#line 342 "parser.y"
{
		if(lookup(val_peek(1).sval) == 0)
		{
			int currscope=stack[index1-1];
			int scope=returnscope(val_peek(1).sval,currscope);
			/* int type=returntype($1,scope); */
			if(!(scope<=currscope && end[scope]==0) || scope==0)
				{System.out.print("\nError : Variable "+val_peek(1).sval+" out of scope : Line "+lexer.getLine()+"\n");errc++;}
		}
		else
			{System.out.print("\nError : Undeclared Variable "+val_peek(1).sval+" : Line "+lexer.getLine()+"\n");errc++;}
	}
break;
case 70:
//#line 374 "parser.y"
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
case 72:
//#line 424 "parser.y"
{
	    insert(val_peek(4).sval,STRUCT);
	    g_addr+=4;
    }
break;
case 73:
//#line 428 "parser.y"
{
        insert(val_peek(1).sval,STRUCT_VAR);
	g_addr+=4;
    }
break;
case 75:
//#line 436 "parser.y"
{push(val_peek(0).sval);}
break;
case 77:
//#line 439 "parser.y"
{st1[++top] = "+";}
break;
case 78:
//#line 439 "parser.y"
{codegen();}
break;
case 79:
//#line 440 "parser.y"
{st1[++top] = "-";}
break;
case 80:
//#line 440 "parser.y"
{codegen();}
break;
case 82:
//#line 442 "parser.y"
{push(val_peek(0).sval);}
break;
case 83:
//#line 442 "parser.y"
{st1[++top] = "<=";}
break;
case 84:
//#line 442 "parser.y"
{codegen();}
break;
case 85:
//#line 443 "parser.y"
{push(val_peek(0).sval);}
break;
case 86:
//#line 443 "parser.y"
{st1[++top] = ">=";}
break;
case 87:
//#line 443 "parser.y"
{codegen();}
break;
case 88:
//#line 444 "parser.y"
{push(val_peek(0).sval);}
break;
case 89:
//#line 444 "parser.y"
{st1[++top] = "==";}
break;
case 90:
//#line 444 "parser.y"
{codegen();}
break;
case 91:
//#line 445 "parser.y"
{push(val_peek(0).sval);}
break;
case 92:
//#line 445 "parser.y"
{st1[++top] = "!=";}
break;
case 93:
//#line 445 "parser.y"
{codegen();}
break;
case 94:
//#line 446 "parser.y"
{push(val_peek(0).sval);}
break;
case 95:
//#line 446 "parser.y"
{st1[++top] = "&&";}
break;
case 96:
//#line 446 "parser.y"
{codegen();}
break;
case 97:
//#line 447 "parser.y"
{push(val_peek(0).sval);}
break;
case 98:
//#line 447 "parser.y"
{st1[++top] = "||";}
break;
case 99:
//#line 447 "parser.y"
{codegen();}
break;
case 100:
//#line 448 "parser.y"
{push(val_peek(0).sval);}
break;
case 101:
//#line 448 "parser.y"
{st1[++top] = "<";}
break;
case 102:
//#line 448 "parser.y"
{codegen();}
break;
case 103:
//#line 449 "parser.y"
{push(val_peek(0).sval);}
break;
case 104:
//#line 449 "parser.y"
{st1[++top] = ">";}
break;
case 105:
//#line 449 "parser.y"
{codegen();}
break;
case 106:
//#line 450 "parser.y"
{push(val_peek(0).sval);}
break;
case 107:
//#line 450 "parser.y"
{st1[++top] = "=";}
break;
case 108:
//#line 450 "parser.y"
{codegen_assign();}
break;
case 109:
//#line 451 "parser.y"
{array1();}
break;
case 110:
//#line 453 "parser.y"
{st1[++top] = "*";}
break;
case 111:
//#line 453 "parser.y"
{codegen();}
break;
case 112:
//#line 454 "parser.y"
{st1[++top] = "/";}
break;
case 113:
//#line 454 "parser.y"
{codegen();}
break;
case 115:
//#line 457 "parser.y"
{yyval.sval=val_peek(1).sval;}
break;
case 116:
//#line 458 "parser.y"
{st1[++top] = "-";}
break;
case 117:
//#line 458 "parser.y"
{codegen_umin();}
break;
case 118:
//#line 459 "parser.y"
{push(val_peek(0).sval);fl=1;}
break;
case 119:
//#line 460 "parser.y"
{push(val_peek(0).sval);}
break;
//#line 1718 "Parser.java"
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
