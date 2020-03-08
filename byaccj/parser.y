%{
    import java.io.*;
%}

%token comment
%token IF

%%

input:  /* empty string */
     | input IF { System.out.println("IF"); }
     ;



%%

private Yylex lexer;

private int yylex(){
    int yyl_return = -1;
    try{
        yylval = new ParserVal(0);
        yyl_return = lexer.yylex();
    }
    catch (IOException e){
        System.err.println("IO error: "+e);
    }
    return yyl_return;
}

public void yyerror(String error){
    System.err.println("Error: "+error);
}

public Parser(Reader r){
    lexer = new Yylex(r, this);
}

public static void main(String args[]) throws IOException{
    System.out.println("C Parser");
    
    Parser yyparser;
    if(args.length == 0)
        System.exit(1);


    yyparser = new Parser(new FileReader(args[0]));
    /* yyparser = new Parser(new InputStreamReader(System.in)); */
    yyparser.yyparse();
}

