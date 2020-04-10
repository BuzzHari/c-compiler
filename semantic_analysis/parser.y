%{
    import java.util.*;
    import java.io.*;
%}

%token<ival> INT FLOAT VOID
%token<str> ID NUM REAL
%token WHILE IF RETURN PREPROC LE STRING PRINT FUNCTION DO ARRAY ELSE STRUCT STRUCT_VAR FOR GE EQ NE INC DEC
%right '='

/* %type<str> assignment assignment1 consttype assignment2 */
%token<ival> Type
%start start

%%

start : Function start
	| PREPROC start
	| Declaration start
	|
	;


Function : Type ID '('')' CompoundStmt {
	if ($1!=returntype_func(ct))
	{
		System.out.print("\nError : Type mismatch : Line "+printline()+"\n"); errc++;
	}

	if (!($2 != "printf" && $2 != "scanf" && $2 != "getc" && $2 != "gets" && $2 != "getchar" && $2 != "puts" && $2 != "putchar" && $2 != "clearerr" && $2 != "getw" && $2 != "putw" && $2 != "putc" && $2 != "rewind" && $2 != "sprint" && $2 != "sscanf" && $2 != "remove" && $2 != "fflush"))
		{System.out.print("Error : Redeclaration of "+$2+" : Line "+printline()+"\n"); errc++;}
	else
	{
		insert($2,FUNCTION);
		insert($2,$1);
	}
	}
        | Type ID '(' parameter_list ')' CompoundStmt  {
	if ($1!=returntype_func(ct))
	{
		System.out.print("\nError : Type mismatch : Line "+printline()+"\n"); errc++;
	}

	if (!($2 != "printf" && $2 != "scanf" && $2 != "getc" && $2 != "gets" && $2 != "getchar" && $2 != "puts" && $2 != "putchar" && $2 != "clearerr" && $2 != "getw" && $2 != "putw" && $2 != "putc" && $2 != "rewind" && $2 != "sprint" && $2 != "sscanf" && $2 != "remove" && $2 != "fflush"))
		{System.out.print("Error : Redeclaration of "+$2+" : Line "+printline()+"\n");errc++;}
	else
	{
		insert($2,FUNCTION);
		insert($2,$1);
                for(j=0;j<=k;j++)
                {insertp($2,plist[j]);}
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
	| CompoundStmt
	|
	;

stmt : Declaration
	| if
        | for
	| while
	| dowhile
	| RETURN consttype ';' {

					if(!(strspn($2,"0123456789")==$2.length())) //TODO
						storereturn(ct,FLOAT);
					else
						storereturn(ct,INT);
					ct++;
				}
	| RETURN ';' {storereturn(ct,VOID); ct++;}
        | RETURN ID ';' {
                          int sct=returnscope($2,stack[top-1]);	//stack[top-1] - current scope
		          int type=returntype($2,sct);
                          if (type==259) storereturn(ct,FLOAT);
                          else storereturn(ct,INT);
                          ct++;
                         }
	| ';'
	| PRINT '(' STRING ')' ';'
	| CompoundStmt
	;

dowhile : DO CompoundStmt WHILE '(' expr1 ')' ';'
	;

if : IF '(' expr1 ')' CompoundStmt
	| IF '(' expr1 ')' CompoundStmt ELSE CompoundStmt
	;

for : FOR '(' expr1 ';' expr1 ';' expr1 ')' '{' {loop=1;} StmtList {loop=0;} '}'
     ;

while : WHILE '(' expr1 ')' '{' {loop=1;} StmtList {loop=0;} '}'
	;

expr1 : expr1 LE expr1
        | expr1 GE expr1
        | expr1 NE expr1
        | expr1 EQ expr1
        | expr1 INC
        | expr1 DEC
        | expr1 '>' expr1
        | expr1 '<' expr1
	| assignment1
	;

assignment : ID '=' consttype
	| ID '+' assignment
	| ID ',' assignment
	| consttype ',' assignment
	| ID
	| consttype
	;


assignment1 : ID '=' assignment1
	{
		int sct=returnscope($1,stack[top-1]);
		int type=returntype($1,sct);
		if((!(strspn($3,"0123456789")==$3.length())) && type==258) //TODO
			{System.out.print("\nError : Type Mismatch : Line "+printline()+"\n"); errc++;}
                else if (type==273)  {System.out.print("\nError : Type Mismatch : Line "+printline()+"\n");errc++;}
		if(!lookup($1))
		{
			int currscope=stack[top-1];
			int scope=returnscope($1,currscope);
			if((scope<=currscope && end[scope]==0) && !(scope==0))
				check_scope_update($1,$3,currscope);
		}
	}

	| ID ',' assignment1
	{
		if(lookup($1))
			System.out.print("\nUndeclared Variable "+$1+" : Line "+printline()+"\n"); errc++;
	}
	| assignment2
	| consttype ',' assignment1
	| ID
	{
		if(lookup($1))
			{ System.out.print("\nUndeclared Variable "+$1+" : Line "+printline()+"\n"); errc++; }
	}
	| ID '=' ID '(' paralist ')'			//function call
        {
                int sct=returnscope($1,stack[top-1]);
		int type=returntype($1,sct);
                //System.out.print($3);
                int rtype;
                rtype=returntypef($3); int ch=0;
                //System.out.print(rtype);
		if(rtype!=type)
			{ System.out.print("\nError : Type Mismatch : Line "+printline()+"\n"); errc++;}
		if(!lookup($1))
		{
		  for(j=0;j<=l;j++)
                  {ch = ch+checkp($3,flist[j],j);}
                  if(ch>0) { System.out.print("\nError : Parameter Type Mistake or Function undeclared : Line "+printline()+"\n"); errc++;}
                  l=-1;
		}
	}
	| ID '(' paralist ')'			//function call without assignment
	{
                int sct=returnscope($1,stack[top-1]);
		int type=returntype($1,sct); int ch=0;
		if(!lookup($1))
		{
		  for(j=0;j<=l;j++)
                  {ch = ch+checkp($1,flist[j],j);}
                  if(ch>0) { System.out.print("\nError : Parameter Type Mistake or Required Function undeclared : Line "+printline()+"\n"); errc++;}
                  l=-1;
		}
                else {System.out.print("\nUndeclared Function "+$1+" : Line "+printline()+"\n");errc++;}
	}
/*	| ID '[' ID ']' '=' ID
	{
        	int sct=returnscope($1,stack[top-1]);
		int itype=returntype($3,sct);
                int type=returntype2($1,sct); int ch=0;
                int rtype=returntype($6,sct);
                if(itype!=258)
                        { System.out.print("\nError : Array index must be of type int : Line "+printline()+"\n");errc++;}
		if(rtype!=type)
			{ System.out.print("\nError : Type Mismatch : Line "+printline()+"\n"); errc++;}
	}*/
	| consttype
	;

paralist : paralist ',' param
         | param
         ;

param : ID
	{
                if(lookup($1))
	        	{System.out.print("\nUndeclared Variable "+$1+" : Line "+printline()+"\n");errc++;}
                else
                {
                	int sct=returnscope($1,stack[top-1]);
                	flist[++l]=returntype($1,sct);
                }
	}
	;

assignment2 : ID '=' exp {c=0;}
		| ID '=' '(' exp ')'
		;

exp : ID
	{
		if(c==0)							//check compatibility of mathematical operations
		{
			c=1;
			int sct=returnscope($1,stack[top-1]);
			b=returntype($1,sct);
		}
		else
		{
			int sct1=returnscope($1,stack[top-1]);
			if(b!=returntype($1,sct1)){}
				{System.out.print("\nError : Type Mismatch : Line "+printline()+"\n");errc++;}
		}
	}
	| exp '+' exp
	| exp '-' exp
	| exp '*' exp
	| exp '/' exp
	| '(' exp '+' exp ')'
	| '(' exp '-' exp ')'
	| '(' exp '*' exp ')'
	| '(' exp '/' exp ')'
	| consttype
	;

consttype : NUM
	| REAL
	;

Declaration : Type ID '=' consttype ';'
	{
		if( (!(strspn($4,"0123456789")==$4.length())) && $1==258) //TODO
			{System.out.print("\nError : Type Mismatch : Line "+printline()+"\n");errc++;}
                else if ($1==273)  {System.out.print("\nError : Type Mismatch : Line "+printline()+"\n");errc++;}
		if(!lookup($2))
		{
			int currscope=stack[top-1];
			int previous_scope=returnscope($2,currscope);
			if(currscope==previous_scope)
				{System.out.print("\nError : Redeclaration of "+$2+" : Line "+printline()+"\n",$2,printline());errc++;}
			else
			{
				insert_dup($2,$1,currscope);
				check_scope_update($2,$4,stack[top-1]);
			}
		}
		else
		{
			int scope=stack[top-1];
			insert($2,$1);
			insertscope($2,scope);
			check_scope_update($2,$4,stack[top-1]);
		}
	}
	| assignment1 ';'
	{
		if(!lookup($1))
		{
			int currscope=stack[top-1];
			int scope=returnscope($1,currscope);
			int type=returntype($1,scope);
			if(!(scope<=currscope && end[scope]==0) || scope==0 && type!=271)
				{System.out.print("\nError : Variable "+$1+" out of scope : Line "+printline()+"\n");errc++;}
		}
		else
			{System.out.print("\nError : Undeclared Variable "+$1+" : Line "+printline()+"\n");errc++;}
	}
        | Type ID ';'
        {
        	if(!lookup($2))
		{
			int currscope=stack[top-1];
			int previous_scope=returnscope($2,currscope);
			if(currscope==previous_scope)
				{System.out.print("\nError : Redeclaration of "+$2+" : Line "+printline()+"\n");errc++;}
			else
			{
				insert_dup($2,$1,currscope);
				//check_scope_update($2,$4,stack[top-1]);
			}
		}
		else
		{
			int scope=stack[top-1];
			//System.out.print(type);
			insert($2,$1);
			insertscope($2,scope);
			//check_scope_update($2,$4,stack[top-1]);
		}
	}
	| Type ID '[' assignment ']' ';' {
                       int itype;
                       if(!(strspn($4,"0123456789")==$4.length)) { itype=259; } else itype = 258; //TODO
                       if(itype!=258)
                       { System.out.print("\nError : Array index must be of type int : Line "+printline()+"\n");errc++;}
                       if(atoi($4)<=0) //TODO
                       { System.out.print("\nError : Array index must be of type int > 0 : Line "+printline()+"\n");errc++;}
                       if(!lookup($2))
		       {
			int currscope=stack[top-1];
			int previous_scope=returnscope($2,currscope);
			if(currscope==previous_scope)
				{System.out.print("\nError : Redeclaration of "+$2+" : Line "+printline()+"\n");errc++;}
			else
			{

				insert_dup($2,ARRAY,currscope);
                                insert_by_scope($2,$1,currscope);	//to insert type to the correct identifier in case of multiple entries of the identifier by using scope
                                if (itype==258) {insert_index($2,$4);}
			}
		      }
		      else
		      {
			int scope=stack[top-1];
                        insert($2,ARRAY);
			insert($2,$1);
			insertscope($2,scope);
                        if (itype==258) {insert_index($2,$4);}
		      }

		    }
	| STRUCT ID '{' Declaration '}' ';' {
						insert($2,STRUCT);
						}
	| STRUCT ID ID ';' {
				insert($3,STRUCT_VAR);
				}
	| error
	;



%%


private Yylex lexer;
int i=1,k=-1,l=-1;
int j=0;
String curfunc;
int stack[100];
int top=0;
int plist[100],flist[100];
int end[100];
int arr[10];
int ct=0,c=0,b;
int loop = 0;
int errc=0;
int type=0;

class sym{
    public int sno;
    public int type[100];
    public int paratype[100];
    public int tn;
    public int pn;
    public int index;
    public int scope;
    public String token;
    public float fvalue;
}

sym[] st = new sym[100];
int n=0;
int arr[10];
int tnp;


public int returntype_func(int ct)
{
	return arr[ct-1];
}
public void storereturn( int ct, int returntype )
{
	arr[ct] = returntype;
	return;
}
public void insertscope(char *a,int s)
{
	int i;
	for(i=0;i<n;i++)
	{
		if(a == st[i].token)
		{
			st[i].scope=s;
			break;
		}
	}
}
public int returnscope(char *a,int cs)
{
	int i;
	int max = 0;
	for(i=0;i<=n;i++)
	{
		if(a == st[i].token && cs>=st[i].scope)
		{
			if(st[i].scope>=max)
				max = st[i].scope;
		}
	}
	return max;
}
public int lookup(char *a)
{
	int i;
	for(i=0;i<n;i++)
	{
		if( a == st[i].token)
			return 0;
	}
	return 1;
}
int returntype(char *a,int sct)
{
	int i;
	for(i=0;i<n;i++)
	{
		if((a == st[i].token) && st[i].scope==sct)
			return st[i].type[0];
	}
}

int returntype2(char *a,int sct)
{
	int i;
	for(i=0;i<n;i++)
	{
		if((a == st[i].token) && st[i].scope==sct)
			{ return st[i].type[1];}
	}
}

int returntypef(char *a)
{
	int i;
	for(i=0;i<n;i++)
	{
		if(a == st[i].token)
			{ return st[i].type[1];}
	}
}


void check_scope_update(char *a,char *b,int sc)
{
	int i,j,k;
	int max=0;
	for(i=0;i<=n;i++)
	{
		if((a == st[i].token)   && sc>=st[i].scope)
		{
			if(st[i].scope>=max)
				max=st[i].scope;
		}
	}
	for(i=0;i<=n;i++)
	{
		if((a == st[i].token)   && max==st[i].scope)
		{
			float temp=atof(b);
			for(k=0;k<st[i].tn;k++)
			{
				if(st[i].type[k]==258)
					st[i].fvalue=(int)temp;
				else
					st[i].fvalue=temp;
			}
		}
	}
}
void storevalue(char *a,char *b,int s_c)
{
	int i;
	for(i=0;i<=n;i++)
	{
		if((a == st[i].token) && s_c==st[i].scope)
		{
			st[i].fvalue=atof(b);
		}
	}
}

void insert(char *name, int type)
{
	int i;
	if(lookup(name))
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
			if(name == st[i].token)
			{
				st[i].tn++;
				st[i].type[st[i].tn-1]=type;
				break;
			}
		}
	}

	return;
}

void insert_dup(char *name, int type, int s_c)
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

void insert_by_scope(char *name, int type, int s_c)
{
 	int i;
	for(i=0;i<n;i++)
 	{
  		if((name == st[i].token) && st[i].scope==s_c)
  		{
   			st[i].tn++;
   			st[i].type[st[i].tn-1]=type;
  		}
 	}
}

void insertp(char *name,int type)
{
 	int i;
 	for(i=0;i<n;i++)
 	{
  		if(name == st[i].token)
  		{
   			st[i].pn++;
   			st[i].paratype[st[i].pn-1]=type;
   			break;
  		}
 	}
}

void insert_index(char *name,int ind)
{
 	int i;
 	for(i=0;i<n;i++)
 	{
  		if((name == st[i].token) && st[i].type[0]==273)
  		{
   			st[i].index = atoi(ind); // TODO
  		}
	}
}

int checkp(char *name,int flist,int c)
{
 	int i,j;
 	for(i=0;i<n;i++)
 	{
  		if(name == st[i].token)
  		{
    			if(st[i].paratype[c]!=flist)
    			return 1;
  		}
 	}
 	return 0;
}

void display()
{
	int i,j;
	System.out.print("\n");
	System.out.print("-------------------------------------------------------Symbol Table-----------------------------------------------------------\n");
	System.out.print("------------------------------------------------------------------------------------------------------------------------------\n");
	System.out.print("\nSNo\tIdentifier\tScope\t\tValue\t\tType\t\t\tParameter type(for functions)\n");
	System.out.print("------------------------------------------------------------------------------------------------------------------------------\n\n");
	for(i=0;i<n;i++)
	{
		if(st[i].type[0]==258 || st[i].type[1]==258 || st[i].type[1]==260)
			System.out.print(st[i].sno+"\t"+st[i].token+"\t\t"+st[i].scope+"\t\t"+(int)st[i].fvalue+"\t");
		else
			System.out.print(st[i].sno+"\t"+st[i].token+"\t\t"+st[i].scope+"\t\t"+st[i].fvalue+"\t");
                System.out.print("\t");
		for(j=0;j<st[i].tn;j++)
		{
			if(st[i].type[j]==258)
				System.out.print("INT");
			else if(st[i].type[j]==259)
				System.out.print("FLOAT");
			else if(st[i].type[j]==271)
				System.out.print("FUNCTION");
			else if(st[i].type[j]==273)
				System.out.print("ARRAY");
			else if(st[i].type[j]==260)
				System.out.print("VOID");
                        if(st[i].tn>1 && j<(st[i].tn-1))System.out.print(" - ");
		}
                System.out.print("\t\t");
		for(j=0;j<st[i].pn;j++)
		{
			if(st[i].paratype[j]==258)
				System.out.print("INT");
			else if(st[i].paratype[j]==259)
				System.out.print("FLOAT");
			if(st[i].pn>1 && j<(st[i].pn-1))System.out.print(", ");
		}
		System.out.print("\n");
	}
	System.out.print("------------------------------------------------------------------------------------------------------------------------------\n\n");
	return;
}


public int printline()
{
	return yylineno;
}
public void push()
{
	stack[top]=i;
	i++;
	top++;
	return;
}
public void pop()
{
	top--;
	end[stack[top]]=1;
	stack[top]=0;
	return;
}

private int yylex(){
    int yyl_return = -1;
    try{
        yylval = new ParserVal("");
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
    lexer.printToken();
}

public Parser(Reader r){
    lexer = new Yylex(r, this);
}

public static void main(String args[]) throws IOException{

    Parser yyparser;
    if(args.length == 0)
        System.exit(1);

    yyparser = new Parser(new FileReader(args[0]));
    int par = yyparser.yyparse();
	if(par == 0)
        System.out.println("\nParsing Complete and OK!");
    else
        System.out.println("\nParsing Failed!");

    Yylex.display();
}

