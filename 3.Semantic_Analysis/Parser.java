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
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    6,    6,    9,    9,   10,    5,
    5,    5,    8,   11,   11,   11,   12,   12,   12,   12,
   12,   12,   12,   12,   12,   12,   12,   16,   13,   13,
   18,   19,   14,   20,   21,   15,   17,   17,   17,   17,
   17,   17,   17,   17,   17,    1,    1,    1,    1,    1,
    1,    2,    2,    2,    2,    2,    2,    2,    2,   22,
   22,   23,    4,    4,   24,   24,   24,   24,   24,   24,
   24,   24,   24,   24,    3,    3,    7,    7,    7,    7,
    7,    7,    7,
};
final static short yylen[] = {                            2,
    2,    2,    2,    0,    5,    6,    3,    1,    2,    1,
    1,    1,    3,    2,    1,    0,    1,    1,    1,    1,
    1,    3,    2,    3,    1,    5,    1,    7,    5,    7,
    0,    0,   13,    0,    0,    9,    3,    3,    3,    3,
    2,    2,    3,    3,    1,    3,    3,    3,    3,    1,
    1,    3,    3,    1,    3,    1,    6,    4,    1,    3,
    1,    1,    3,    5,    1,    3,    3,    3,    3,    5,
    5,    5,    5,    1,    1,    1,    5,    2,    3,    6,
    6,    4,    1,
};
final static short yydefred[] = {                         0,
   83,   10,   11,   12,    0,   75,   76,    0,    0,    0,
    0,    0,   54,    0,    0,    0,    0,    0,    0,    2,
    0,   78,    0,    0,    1,    3,    0,    0,   52,    0,
    0,   62,    0,   61,   53,    0,    0,   55,    0,    0,
   79,    0,    0,   65,    0,   74,    0,    0,    0,    0,
    0,   58,    0,   82,    0,    0,    0,    0,    0,    0,
    8,    0,    0,    0,    0,    0,   64,    0,    0,    0,
    0,    0,    0,    0,    0,   60,    0,    0,   77,    0,
    5,    9,    0,    0,    0,    0,    0,    0,    0,   57,
    0,    0,    0,    0,   81,   15,    0,    6,    7,   46,
   48,   47,   80,   49,   70,   71,   72,   73,    0,    0,
    0,    0,    0,    0,   13,   25,   17,   27,   14,   18,
   19,   20,   21,    0,    0,    0,   23,    0,    0,    0,
    0,   45,    0,    0,   24,   22,    0,    0,    0,    0,
    0,    0,    0,   41,   42,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   34,    0,    0,    0,
   26,    0,    0,    0,    0,    0,    0,    0,   30,   28,
    0,    0,    0,   36,   31,    0,    0,    0,   33,
};
final static short yydgoto[] = {                         10,
   63,  132,   12,   13,   14,   15,   16,   96,   60,   61,
   97,  119,  120,  121,  122,  123,  133,  176,  178,  164,
  172,   33,   34,   31,
};
final static short yysindex[] = {                       174,
    0,    0,    0,    0,   43,    0,    0,  174, -229,    0,
   18,   81,    0, -153,  174,  174,  -37, -139, -120,    0,
 -107,    0, -120,   -2,    0,    0,   51,  -30,    0,   81,
   55,    0,  -21,    0,    0,   68,  193,    0, -148,   96,
    0, -111, -139,    0,  -30,    0,  124,  -30,  -30,  -30,
  -30,    0, -139,    0, -130,    9,   88,   39, -116,   78,
    0,   85,   75,  113,   91,   63,    0,  -30,  -30,  -30,
  -30,   55,   55,   55,   55,    0,  -15,  105,    0,   39,
    0,    0,   39,  -68, -148, -111, -111,  115, -111,    0,
  423,  430,  437,  445,    0,    0,  104,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  137,  142,
   97,  143,   39,  154,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -120, -120,  136,    0,  139,  -69,  -63,
 -120,    0,   26,   34,    0,    0,  160,  173,   56, -120,
 -120, -120, -120,    0,    0,   93, -120, -120,   39,  155,
 -120, -120,   71,   71,   71,   71,    0,   71,   71,  -55,
    0,   41,   64,   39,   39,  162, -120,  -54,    0,    0,
   49,   94,  111,    0,    0,   39,  -54,  103,    0,
};
final static short yyrindex[] = {                       235,
    0,    0,    0,    0,  -26,    0,    0,  235,    0,    0,
    0,  -19,    0,    0,  235,  235,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -41,    0,    0,  -34,
  -11,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  149,    0,  156,    0,    0,    0,    0,    0,    0,
    0,   -4,    4,   11,   19,    0,    0,    0,    0,  125,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -17,   -9,   -6,    6,    0,   21,   58,  251,
    0,    0,    0,  125,    0,    0,    0,  132,    0,    0,
    0,    0,    0,    0,    0,  125,  140,    0,    0,
};
final static short yygindex[] = {                        77,
   86,  392,  374,    0,  -23,    0,  -25,  -29,    0,  180,
 -137,    0,    0,    0,    0,    0,   45,    0,    0,    0,
    0,  229,  226,  110,
};
final static int YYTABLESIZE=569;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         56,
   65,   65,   28,   65,  116,   65,   59,   74,   74,   45,
   74,   56,   74,   55,   56,   37,   59,   56,   56,   52,
   56,   59,   53,   37,   59,   59,  168,   59,   81,   63,
   21,   38,   56,   56,   40,   56,   66,   40,  177,   59,
   59,   37,   59,   41,   67,   39,   39,   63,   63,   38,
   63,   68,   40,   98,   66,   66,   41,   66,   39,   69,
   59,   43,   67,   67,   39,   67,  146,  118,   80,   68,
   68,  117,   68,   55,  149,   42,   22,   69,   69,   43,
   69,  166,   18,  130,   20,  148,   19,  147,   42,  173,
   43,   25,   26,  148,   19,  147,   50,   48,   44,   49,
  148,   51,  147,   17,   70,   68,   24,   69,  148,   71,
  147,   17,    6,    7,  152,  148,   44,  147,   83,  160,
   32,   84,  167,  148,   23,  147,   54,   87,   86,   77,
  148,   90,  147,   78,   53,  169,   58,   47,  118,    5,
    6,    7,  117,   82,   55,   85,   79,  118,   62,    6,
    7,  117,   36,   55,   66,  127,   89,   72,   73,   74,
   75,   80,  116,   95,   67,   70,   68,   88,   69,  134,
   71,  101,  102,  103,  104,  139,  124,   91,   92,   93,
   94,  125,  129,   16,  153,  154,  155,  156,    2,    3,
    4,  158,  159,  131,  135,  162,  163,  136,  137,  138,
  150,    1,    2,    3,    4,    5,    6,    7,  109,  110,
  111,  171,  151,  161,  112,  157,  113,  165,  174,    9,
  170,  114,   27,    6,    7,   56,   80,  179,  115,   44,
    6,    7,   59,  175,    4,   56,   56,   56,   56,   56,
   56,   50,   59,   59,   59,   59,   59,   59,   51,   16,
   56,   56,   56,   56,   56,   63,   35,   59,   59,   59,
   59,   59,   66,   99,   32,   63,   63,   63,   63,   63,
   67,   65,   66,   66,   66,   66,   66,   68,   76,    0,
   67,   67,   67,   67,   67,   69,    0,   68,   68,   68,
   68,   68,  140,    0,    0,   69,   69,   69,   69,   69,
  140,    0,  141,  142,  143,  144,  145,  140,    0,   29,
  141,  142,  143,  144,  145,  140,    0,  141,  142,  143,
  144,  145,  140,    0,    0,  141,  142,  143,  144,  145,
  140,    0,  141,  142,  143,  144,  145,  140,    0,    0,
  141,  142,  143,  144,  145,    0,    0,  141,  142,  143,
  144,  145,    2,    3,    4,    0,  126,    6,    7,    1,
    2,    3,    4,    5,    6,    7,  109,  110,  111,    0,
    0,    0,  112,   29,  113,   29,    0,    9,    0,  114,
   16,   16,   16,   16,   16,   16,   16,   16,   16,   16,
   30,   11,    0,   16,    0,   16,    0,    0,   16,   11,
   16,   46,    0,    0,    0,    0,   11,   11,   29,    0,
   35,    0,   57,    0,   38,   64,    0,    0,   46,    0,
    0,   46,   46,   46,   46,    0,    0,    0,   11,    1,
    2,    3,    4,    5,    6,    7,    0,    0,    0,    8,
    0,   46,   46,   46,   46,    0,    0,    9,    1,    2,
    3,    4,    5,    6,    7,    0,    0,    0,  100,   64,
   64,    0,   64,  105,   50,   48,    9,   49,    0,   51,
  106,   50,   48,    0,   49,    0,   51,  107,   50,   48,
    0,   49,    0,   51,  128,  108,   50,   48,   11,   49,
    0,   51,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   29,   29,   29,   29,
   29,   29,   29,   29,   29,   29,    0,    0,    0,   29,
    0,   29,    0,    0,   29,    0,   29,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   11,
    0,    0,    0,    0,    0,    0,    0,    0,   11,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   40,   45,   59,   47,   41,   42,   43,   40,
   45,   37,   47,   37,   41,  123,   40,   59,   60,   41,
   62,   41,   44,   41,   59,   60,  164,   62,   58,   41,
  260,   41,   59,   60,   41,   62,   41,   40,  176,   59,
   60,   59,   62,   59,   41,   61,   41,   59,   60,   59,
   62,   41,   59,   83,   59,   60,   59,   62,   61,   41,
   84,   41,   59,   60,   59,   62,   41,   97,  123,   59,
   60,   97,   62,   97,   41,   91,   59,   59,   60,   59,
   62,   41,   40,  113,    8,   60,   44,   62,   91,   41,
   40,   15,   16,   60,   44,   62,   42,   43,   41,   45,
   60,   47,   62,   61,   42,   43,  260,   45,   60,   47,
   62,   61,  261,  262,   59,   60,   59,   62,   41,  149,
  260,   44,   59,   60,   44,   62,   59,   43,   44,  260,
   60,   41,   62,  125,   44,  165,   41,   28,  168,  260,
  261,  262,  168,  260,  168,   61,   59,  177,  260,  261,
  262,  177,  260,  177,   45,   59,   44,   48,   49,   50,
   51,  123,   59,   59,   41,   42,   43,   93,   45,  125,
   47,   86,   87,   59,   89,  131,   40,   68,   69,   70,
   71,   40,   40,   59,  140,  141,  142,  143,  257,  258,
  259,  147,  148,   40,   59,  151,  152,   59,  268,  263,
   41,  256,  257,  258,  259,  260,  261,  262,  263,  264,
  265,  167,   40,   59,  269,  123,  271,  273,  125,  274,
   59,  276,  260,  261,  262,  267,  123,  125,  125,  260,
  261,  262,  267,  123,    0,  277,  278,  279,  280,  281,
  267,   93,  277,  278,  279,  280,  281,  267,   93,  125,
  277,  278,  279,  280,  281,  267,  125,  277,  278,  279,
  280,  281,  267,   84,  125,  277,  278,  279,  280,  281,
  267,   43,  277,  278,  279,  280,  281,  267,   53,   -1,
  277,  278,  279,  280,  281,  267,   -1,  277,  278,  279,
  280,  281,  267,   -1,   -1,  277,  278,  279,  280,  281,
  267,   -1,  277,  278,  279,  280,  281,  267,   -1,   59,
  277,  278,  279,  280,  281,  267,   -1,  277,  278,  279,
  280,  281,  267,   -1,   -1,  277,  278,  279,  280,  281,
  267,   -1,  277,  278,  279,  280,  281,  267,   -1,   -1,
  277,  278,  279,  280,  281,   -1,   -1,  277,  278,  279,
  280,  281,  257,  258,  259,   -1,  260,  261,  262,  256,
  257,  258,  259,  260,  261,  262,  263,  264,  265,   -1,
   -1,   -1,  269,  123,  271,  125,   -1,  274,   -1,  276,
  256,  257,  258,  259,  260,  261,  262,  263,  264,  265,
   17,    0,   -1,  269,   -1,  271,   -1,   -1,  274,    8,
  276,   28,   -1,   -1,   -1,   -1,   15,   16,   17,   -1,
   19,   -1,   39,   -1,   23,   42,   -1,   -1,   45,   -1,
   -1,   48,   49,   50,   51,   -1,   -1,   -1,   37,  256,
  257,  258,  259,  260,  261,  262,   -1,   -1,   -1,  266,
   -1,   68,   69,   70,   71,   -1,   -1,  274,  256,  257,
  258,  259,  260,  261,  262,   -1,   -1,   -1,   85,   86,
   87,   -1,   89,   41,   42,   43,  274,   45,   -1,   47,
   41,   42,   43,   -1,   45,   -1,   47,   41,   42,   43,
   -1,   45,   -1,   47,  111,   41,   42,   43,   97,   45,
   -1,   47,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,  258,  259,
  260,  261,  262,  263,  264,  265,   -1,   -1,   -1,  269,
   -1,  271,   -1,   -1,  274,   -1,  276,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  168,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  177,
};
}
final static short YYFINAL=10;
final static short YYMAXTOKEN=281;
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
"ELSE","STRUCT","STRUCT_VAR","FOR","GE","EQ","NE","INC","DEC",
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
"stmt : for",
"stmt : while",
"stmt : dowhile",
"stmt : RETURN consttype ';'",
"stmt : RETURN ';'",
"stmt : RETURN ID ';'",
"stmt : ';'",
"stmt : PRINT '(' STRING ')' ';'",
"stmt : CompoundStmt",
"dowhile : DO CompoundStmt WHILE '(' expr1 ')' ';'",
"if : IF '(' expr1 ')' CompoundStmt",
"if : IF '(' expr1 ')' CompoundStmt ELSE CompoundStmt",
"$$1 :",
"$$2 :",
"for : FOR '(' expr1 ';' expr1 ';' expr1 ')' '{' $$1 StmtList $$2 '}'",
"$$3 :",
"$$4 :",
"while : WHILE '(' expr1 ')' '{' $$3 StmtList $$4 '}'",
"expr1 : expr1 LE expr1",
"expr1 : expr1 GE expr1",
"expr1 : expr1 NE expr1",
"expr1 : expr1 EQ expr1",
"expr1 : expr1 INC",
"expr1 : expr1 DEC",
"expr1 : expr1 '>' expr1",
"expr1 : expr1 '<' expr1",
"expr1 : assignment1",
"assignment : ID '=' consttype",
"assignment : ID '+' assignment",
"assignment : ID ',' assignment",
"assignment : consttype ',' assignment",
"assignment : ID",
"assignment : consttype",
"assignment1 : ID '=' assignment1",
"assignment1 : ID ',' assignment1",
"assignment1 : assignment2",
"assignment1 : consttype ',' assignment1",
"assignment1 : ID",
"assignment1 : ID '=' ID '(' paralist ')'",
"assignment1 : ID '(' paralist ')'",
"assignment1 : consttype",
"paralist : paralist ',' param",
"paralist : param",
"param : ID",
"assignment2 : ID '=' exp",
"assignment2 : ID '=' '(' exp ')'",
"exp : ID",
"exp : exp '+' exp",
"exp : exp '-' exp",
"exp : exp '*' exp",
"exp : exp '/' exp",
"exp : '(' exp '+' exp ')'",
"exp : '(' exp '-' exp ')'",
"exp : '(' exp '*' exp ')'",
"exp : '(' exp '/' exp ')'",
"exp : consttype",
"consttype : NUM",
"consttype : REAL",
"Declaration : Type ID '=' consttype ';'",
"Declaration : assignment1 ';'",
"Declaration : Type ID ';'",
"Declaration : Type ID '[' assignment ']' ';'",
"Declaration : STRUCT ID '{' Declaration '}' ';'",
"Declaration : STRUCT ID ID ';'",
"Declaration : error",
};

//#line 389 "parser.y"


private Yylex lexer;
public static int i=1,k=-1,l=-1;
public static int j=0;
public static String curfunc;
public static int[] stack = new int[100];
public static int top=0;
public static int[] plist = new int[100];
public static int[] flist = new int[100];
public static int[] end = new int[100];
public static int[] arr = new int[10];
public static int ct=0,c=0,b;
public static int loop = 0;
public static int errc=0;
public static int type=0;

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
    System.out.print("Error: " + error + " at: " + lexer.getLine());
    errc+=1;
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

//#line 810 "Parser.java"
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
//#line 24 "parser.y"
{
        if (val_peek(4).ival!=returntype_func(ct))
        {
            System.out.print("\nError : Type mismatch : Line "+lexer.getLine()+"\n"); 
            errc++;
        }

        if (!(val_peek(3).sval != "printf" && val_peek(3).sval != "scanf" && val_peek(3).sval != "getc" && val_peek(3).sval != "gets" && val_peek(3).sval != "getchar" && val_peek(3).sval != "puts" && val_peek(3).sval != "putchar" && val_peek(3).sval != "clearerr" && val_peek(3).sval != "getw" && val_peek(3).sval != "putw" && val_peek(3).sval != "putc" && val_peek(3).sval != "rewind" && val_peek(3).sval != "sprint" && val_peek(3).sval != "sscanf" && val_peek(3).sval != "remove" && val_peek(3).sval != "fflush"))
            {System.out.print("Error : Redeclaration of "+val_peek(3).sval+" : Line "+lexer.getLine()+"\n"); errc++;}
        else
        {
            insert(val_peek(3).sval,FUNCTION);
            insert(val_peek(3).sval,val_peek(4).ival);
        }
	}
break;
case 6:
//#line 39 "parser.y"
{
        if (val_peek(5).ival!=returntype_func(ct))
        {
            System.out.print("\nError : Type mismatch : Line "+lexer.getLine()+"\n"); errc++;
        }

        if (!(val_peek(4).sval != "printf" && val_peek(4).sval != "scanf" && val_peek(4).sval != "getc" && val_peek(4).sval != "gets" && val_peek(4).sval != "getchar" && val_peek(4).sval != "puts" && val_peek(4).sval != "putchar" && val_peek(4).sval != "clearerr" && val_peek(4).sval != "getw" && val_peek(4).sval != "putw" && val_peek(4).sval != "putc" && val_peek(4).sval != "rewind" && val_peek(4).sval != "sprint" && val_peek(4).sval != "sscanf" && val_peek(4).sval != "remove" && val_peek(4).sval != "fflush"))
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
//#line 62 "parser.y"
{plist[++k]=val_peek(1).ival;insert(val_peek(0).sval,val_peek(1).ival);insertscope(val_peek(0).sval,i);}
break;
case 22:
//#line 83 "parser.y"
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
//#line 93 "parser.y"
{storereturn(ct,VOID); ct++;}
break;
case 24:
//#line 94 "parser.y"
{
          int sct=returnscope(val_peek(1).sval,stack[top-1]);	/*stack[top-1] - current scope*/
          int type=returntype(val_peek(1).sval,sct);
          if (type == FLOAT) 
            storereturn(ct,FLOAT);
          else storereturn(ct,INT);
          ct++;
     }
break;
case 31:
//#line 114 "parser.y"
{loop=1;}
break;
case 32:
//#line 114 "parser.y"
{loop=0;}
break;
case 34:
//#line 117 "parser.y"
{loop=1;}
break;
case 35:
//#line 117 "parser.y"
{loop=0;}
break;
case 52:
//#line 141 "parser.y"
{
		int sct=returnscope(val_peek(2).sval,stack[top-1]);
		int type=returntype(val_peek(2).sval,sct);
        try {
            int i =  Integer.parseInt(val_peek(0).sval);
            if(type != INT) {
                System.out.print("\nError : Type lMismatch : Line "+lexer.getLine()+"\n"); 
                errc++;
            }
        } catch (NumberFormatException nfe) {
            float i = Float.parseFloat(val_peek(0).sval);
            if(type != FLOAT) {
                System.out.print("\nError : Type pMismatch : Line "+lexer.getLine()+"\n"); 
                errc++;
            }
        }
        if (type == ARRAY) {
            System.out.print("\nError : Type xMismatch : Line "+lexer.getLine()+"\n");
            errc++;
        }
        
        if(lookup(val_peek(2).sval) == 0)
		{
			int currscope=stack[top-1];
			int scope=returnscope(val_peek(2).sval,currscope);
			if((scope<=currscope && end[scope]==0) && !(scope==0))
				check_scope_update(val_peek(2).sval,val_peek(0).sval,currscope);
		}
	}
break;
case 53:
//#line 171 "parser.y"
{
		if(lookup(val_peek(2).sval) != 0)
			System.out.print("\nUndeclared Variable "+val_peek(2).sval+" : Line "+lexer.getLine()+"\n"); errc++;
	}
break;
case 56:
//#line 177 "parser.y"
{
		if(lookup(val_peek(0).sval) != 0)
			{ System.out.print("\nUndeclared Variable "+val_peek(0).sval+" : Line "+lexer.getLine()+"\n"); errc++; }
	}
break;
case 57:
//#line 181 "parser.y"
{
        int sct=returnscope(val_peek(5).sval,stack[top-1]);
		int type=returntype(val_peek(5).sval,sct);
        /*System.out.print($3);*/
        int rtype;
        rtype=returntypef(val_peek(3).sval); 
        int ch=0;
        /*System.out.println("Rtype: " + rtype + " type: " + type);*/
		if(rtype != type) { 
            System.out.print("\nError : Type aMismatch : Line "+lexer.getLine()+"\n"); 
            errc++;
        }
		if(lookup(val_peek(5).sval) == 0) {
		  for(j=0;j<=l;j++) {
             ch = ch+checkp(val_peek(3).sval,flist[j],j);}
             if(ch>0) { 
             System.out.print("\nError : Parameter Type Mistake or Function undeclared : Line "+lexer.getLine()+"\n"); errc++;
             }
          l=-1;
		}
	}
break;
case 58:
//#line 202 "parser.y"
{/*function call without assignment */
        int sct=returnscope(val_peek(3).sval,stack[top-1]);
		int type=returntype(val_peek(3).sval,sct); int ch=0;
		if(lookup(val_peek(3).sval) == 0) {
		  for(j=0;j<=l;j++)
                  {ch = ch+checkp(val_peek(3).sval,flist[j],j);}
                  if(ch>0) { System.out.print("\nError : Parameter Type Mistake or Required Function undeclared : Line "+lexer.getLine()+"\n"); errc++;}
                  l=-1;
		}
                else {System.out.print("\nUndeclared Function "+val_peek(3).sval+" : Line "+lexer.getLine()+"\n");errc++;}
	}
break;
case 62:
//#line 220 "parser.y"
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
case 63:
//#line 231 "parser.y"
{c=0;}
break;
case 65:
//#line 235 "parser.y"
{
		if(c==0) {/*check compatibility of mathematical operations*/
			c=1;
			int sct=returnscope(val_peek(0).sval,stack[top-1]);
			b=returntype(val_peek(0).sval,sct);
		}
		else {
			int sct1=returnscope(val_peek(0).sval,stack[top-1]);
			if(b!=returntype(val_peek(0).sval,sct1))
				{System.out.print("\nError : Type tMismatch : Line "+lexer.getLine()+"\n");errc++;}
		}
	}
break;
case 77:
//#line 262 "parser.y"
{
        try {
            int i =  Integer.parseInt(val_peek(1).sval);
            if(val_peek(4).ival != INT) {
                System.out.print("\nError : Type Mmismatch : Line "+lexer.getLine()+"\n"); errc++;
            }
        } catch (NumberFormatException nfe) {
            float i = Float.parseFloat(val_peek(1).sval);
            if(val_peek(4).ival != FLOAT) {
                System.out.print("\nError : Type mMismatch : Line "+lexer.getLine()+"\n"); errc++;
            }
        }
        if (val_peek(4).ival == ARRAY) {
            System.out.print("\nError : Type Mmmismatch : Line "+lexer.getLine()+"\n");errc++;
        }
		if(lookup(val_peek(3).sval) == 0) {
			int currscope=stack[top-1];
			int previous_scope=returnscope(val_peek(3).sval,currscope);
			if(currscope==previous_scope) {
                System.out.println("\nError : Redeclaration of "+val_peek(3).sval+" : Line "+lexer.getLine());
                errc++;
            }
			else {
				insert_dup(val_peek(3).sval,val_peek(4).ival,currscope);
				check_scope_update(val_peek(3).sval,val_peek(1).sval,stack[top-1]);
			}
		}
		else {
			int scope=stack[top-1];
			insert(val_peek(3).sval,val_peek(4).ival);
			insertscope(val_peek(3).sval,scope);
			check_scope_update(val_peek(3).sval,val_peek(1).sval,stack[top-1]);
		}
	}
break;
case 78:
//#line 296 "parser.y"
{
		if(lookup(val_peek(1).sval) == 0)
		{
			int currscope=stack[top-1];
			int scope=returnscope(val_peek(1).sval,currscope);
			int type=returntype(val_peek(1).sval,scope);
			if(!(scope<=currscope && end[scope]==0) || scope==0 && type!=FUNCTION)
				{System.out.print("\nError : Variable "+val_peek(1).sval+" out of scope : Line "+lexer.getLine()+"\n");errc++;}
		}
		else
			{System.out.print("\nError : Undeclared Variable "+val_peek(1).sval+" : Line "+lexer.getLine()+"\n");errc++;}
	}
break;
case 79:
//#line 308 "parser.y"
{
        if(lookup(val_peek(1).sval) == 0) {
            int currscope=stack[top-1];
            int previous_scope=returnscope(val_peek(1).sval,currscope);
            if(currscope==previous_scope)
                {System.out.print("\nError : Redeclaration of "+val_peek(1).sval+" : Line "+lexer.getLine()+"\n");errc++;}
            else
            {
                insert_dup(val_peek(1).sval,val_peek(2).ival,currscope);
                /*check_scope_update($2,$4,stack[top-1]);*/
            }
		}
		else {
			int scope=stack[top-1];
			/*System.out.print(type);*/
			insert(val_peek(1).sval,val_peek(2).ival);
			insertscope(val_peek(1).sval,scope);
			/*check_scope_update($2,$4,stack[top-1]);*/
		}
	}
break;
case 80:
//#line 328 "parser.y"
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
case 81:
//#line 377 "parser.y"
{
	    insert(val_peek(4).sval,STRUCT);
    }
break;
case 82:
//#line 380 "parser.y"
{
        insert(val_peek(1).sval,STRUCT_VAR);
    }
break;
//#line 1299 "Parser.java"
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
