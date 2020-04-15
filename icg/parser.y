%{
    import java.util.*;
    import java.io.*;
%}

%token<ival> INT FLOAT VOID
%token<sval> ID NUM REAL
%token WHILE IF RETURN PREPROC LE STRING PRINT FUNCTION DO ARRAY ELSE STRUCT STRUCT_VAR FOR GE EQ NE INC DEC
AND OR
%left LE GE EQ NEQ AND OR '<' '>'
%right '='
%right UMINUS
%left '+' '-'
%left '*' '/'
%type<sval> assignment assignment1 consttype '=' '+' '-' '*' '/' E T F
%type<ival> Type
%start start

%%

start : Function start
	  | PREPROC start
	  | Declaration start
   	  |
   	  ;


Function : Type ID '('')' CompoundStmt {
            if( !$2.equals("main")) {
                System.out.print("goto F"+lnum1+"\n");
            }
            if ($1!=returntype_func(ct)) {
                System.out.print("\nError : Type mismatch : Line "+lexer.getLine()+"\n");
                errc++;
            }
            if ($2.equals("printf") || $2.equals("scanf") || $2.equals("getc") || $2.equals("gets") || $2.equals("getchar") || $2.equals("puts") || $2.equals("putchar") || $2.equals("clearerr") || $2.equals("getw") || $2.equals("putw") || $2.equals("putc") || $2.equals("rewind") || $2.equals("sprint") || $2.equals("sscanf") || $2.equals("remove") || $2.equals("fflush"))
                {System.out.print("Error : RRedeclaration of "+$2+" : Line "+lexer.getLine()+"\n"); errc++;}
            else
            {
                insert($2,FUNCTION);
                insert($2,$1);
                g_addr+=4;
            }
	    }
        | Type ID '(' parameter_list ')' CompoundStmt  {
            if ($1!=returntype_func(ct)) {
                System.out.print("\nError : Type mismatch : Line "+lexer.getLine()+"\n"); 
                errc++;
            }
                if ($2.equals("printf") || $2.equals("scanf") || $2.equals("getc") || $2.equals("gets") || $2.equals("getchar") || $2.equals("puts") || $2.equals("putchar") || $2.equals("clearerr") || $2.equals("getw") || $2.equals("putw") || $2.equals("putc") || $2.equals("rewind") || $2.equals("sprint") || $2.equals("sscanf") || $2.equals("remove") || $2.equals("fflush"))
                {System.out.print("Error : rRedeclaration of "+$2+" : Line "+lexer.getLine()+"\n");errc++;}
                else {
                    insert($2,FUNCTION);
                    insert($2,$1);
                    for(j=0;j<=k;j++) {
                      insertp($2,plist[j]);
                    }
                    k=-1;
                }
	    }
	    ;

parameter_list : parameter_list ',' parameter
               | parameter
               ;

parameter : Type ID {plist[++k]=$1;insert($2,$1);insertscope($2,i);}
          ;

Type : INT
	 | FLOAT
	 | VOID
	 ;

CompoundStmt : '{' StmtList '}'
	         ;

StmtList : StmtList stmt
	     |
	     ;

stmt : Declaration
	 | if
     | ID '(' ')' ';'
     | for
	 | while
	 | dowhile
	 | RETURN consttype ';' {
            try {
                int i =  Integer.parseInt($2);
                storereturn(ct,INT);
            } catch (NumberFormatException nfe) {
                float i = Float.parseFloat($2);
                storereturn(ct,FLOAT);
            }
            ct++;
	 }
	 | RETURN ';' {storereturn(ct,VOID); ct++;}
     | RETURN ID ';' {
          int sct=returnscope($2,stack[top-1]);	//stack[top-1] - current scope
          int type=returntype($2,sct);
          if (type == FLOAT)
            storereturn(ct,FLOAT);
          else 
            storereturn(ct,INT);
          ct++;
     }
	 | ';'
	 | PRINT '(' STRING ')' ';'
	 | CompoundStmt
	 ;


dowhile : DO {dowhile1();} CompoundStmt WHILE '(' E ')' {dowhile2();} ';'
	    ;

for	: FOR '(' E {for1();} ';' E {for2();}';' E {for3();} ')' CompoundStmt {for4();}
	;

if  : IF '(' E ')' {if1();} CompoundStmt {if2();} else
    ;

else : ELSE CompoundStmt {if3();}
	 |
	 ;

while : WHILE {while1();}'(' E ')' {while2();} CompoundStmt {while3();}
	  ;


assignment  : ID '=' consttype
	        | ID '+' assignment
	        | ID ',' assignment
	        | consttype ',' assignment
	        | ID
	        | consttype
	        ;


assignment1 : ID {push($1);} '=' {st1[++top] = "=";} E {codegen_assign();}
	{
		int sct=returnscope($1,stack[index1-1]);
		int type=returntype($1,sct);
        try {
            int i =  Integer.parseInt($5);
        } catch (NumberFormatException nfe) {
            if(type != INT && fl == 0) {
                System.out.print("\nError : Type lMismatch : Line "+lexer.getLine()+"\n");
                errc++;
            }
        }
        if(lookup($1) == 0)
		{
			int currscope=stack[index1-1];
			int scope=returnscope($1,currscope);
			if((scope<=currscope && end[scope]==0) && !(scope==0))
				check_scope_update($1,$5,currscope);
		}
	}

	| ID ',' assignment1 {
		if(lookup($1) != 0)
			System.out.print("\nUUndeclared Variable "+$1+" : Line "+lexer.getLine()+"\n"); errc++;
	}
	| consttype ',' assignment1
	| ID {
		if(lookup($1) != 0)
			{ System.out.print("\nnUndeclared Variable "+$1+" : Line "+lexer.getLine()+"\n"); errc++; }
	}
	| consttype
	;

consttype : NUM
       	  | REAL
	      ;

Declaration : Type ID {push($2);} '=' {st1[++top] = "="; } E {codegen_assign();} ';' {
        try {
            int i =  Integer.parseInt($6);
        } catch (NumberFormatException nfe) {
            if($1 == INT && fl == 0) {
                System.out.print("\nError : Type Mmmismatch : Line "+lexer.getLine()+"\n");
                errc++;
                fl=1;
            }
        }
		if(lookup($2) == 0) {
			int currscope=stack[index1-1];
			int previous_scope=returnscope($2,currscope);
			if(currscope==previous_scope) {
                System.out.println("\nError : Redeclaration of "+$2+" : Line "+lexer.getLine());
                errc++;
            }
			else {
				insert_dup($2,$1,currscope);
				check_scope_update($2,$6,stack[index1-1]);
				int sg=returnscope($2,stack[index1-1]);
				g_addr+=4;
			}
		}
		else {
			int scope=stack[index1-1];
			insert($2,$1);
			insertscope($2,scope);
			check_scope_update($2,$6,stack[index1-1]);
			g_addr+=4;
		}
	}
	| assignment1 ';' {
		if(lookup($1) == 0)
		{
			int currscope=stack[index1-1];
			int scope=returnscope($1,currscope);
			if(!(scope<=currscope && end[scope]==0) || scope==0)
				{System.out.print("\nError : Variable "+$1+" out of scope : Line "+lexer.getLine()+"\n");errc++;}
		}
		else
			{System.out.print("\nError : uUndeclared Variable "+$1+" : Line "+lexer.getLine()+"\n");errc++;}
	}
	| Type ID '[' assignment ']' ';' {
            int itype;
            try {
                int i =  Integer.parseInt($4);
                itype = INT;
            } catch (NumberFormatException nfe) {
                float i = Float.parseFloat($4);
                itype = FLOAT;
            }

             if(itype != INT) {
                System.out.print("\nError : Array index must be of type int : Line "+lexer.getLine()+"\n");errc++;
            }

            try {
                if(Integer.parseInt($4)<=0) {
                    System.out.print("\nError : Array index must be of type int > 0 : Line "+lexer.getLine()+"\n");
                    errc++;
                }
            } catch (NumberFormatException nfe) {
                System.out.print("\nError : Array index must be of type int  : Line "+lexer.getLine()+"\n");
                errc++;
            }

            if(lookup($2) == 0) {
                int currscope=stack[top-1];
                int previous_scope=returnscope($2,currscope);
                if(currscope==previous_scope) {
                    System.out.print("\nError : Redeclaration of "+$2+" : Line "+lexer.getLine()+"\n");errc++;
                }
                else {
                    insert_dup($2,ARRAY,currscope);
                    insert_by_scope($2,$1,currscope);	//to insert type to the correct identifier in case of multiple entries of the identifier by using scope
                    if (itype==INT) {
                        insert_index($2,$4);
                    }
                }
            }
            else {
                int scope=stack[top-1];
                insert($2,ARRAY);
                insert($2,$1);
                insertscope($2,scope);
                if (itype==INT) {
                    insert_index($2,$4);
                }
            }
    }
    | ID '[' assignment1 ']' ';'
	| STRUCT ID '{' Declaration '}' ';' {
	    insert($2,STRUCT);
	    g_addr+=4;
    }
	| STRUCT ID ID ';' {
        insert($3,STRUCT_VAR);
	    g_addr+=4;
    }
	| error
	;


array : ID {push($1);}'[' E ']'
	  ;

E : E '+'{st1[++top] = "+";} T{codegen();}
   | E '-'{st1[++top] = "-";} T{codegen();}
   | T
   | ID {push($1);} LE {st1[++top] = "<=";} E {codegen();}
   | ID {push($1);} GE {st1[++top] = ">=";} E {codegen();}
   | ID {push($1);} EQ {st1[++top] = "==";} E {codegen();}
   | ID {push($1);} NEQ {st1[++top] = "!=";} E {codegen();}
   | ID {push($1);} AND {st1[++top] = "&&";} E {codegen();}
   | ID {push($1);} OR {st1[++top] = "||";} E {codegen();}
   | ID {push($1);} '<' {st1[++top] = "<";} E {codegen();}
   | ID {push($1);} '>' {st1[++top] = ">";} E {codegen();}
   | ID {push($1);} '=' {st1[++top] = "=";} E {codegen_assign();}
   | array {array1();}
   ;
T : T '*'{st1[++top] = "*";} F{codegen();}
   | T '/'{st1[++top] = "/";} F{codegen();}
   | F
   ;
F : '(' E ')' {$$=$2;}
   | '-'{st1[++top] = "-";} F{codegen_umin();} %prec UMINUS
   | ID {push($1);fl=1;}
   | consttype {push($1);}
   ;



%%


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

