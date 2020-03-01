%%

%byaccj

%{
    private Parser yyparser;
    public Yylex(java.io.Reader r, Parser yyparser){
        this(r);
        this.yyparser = yyparser;
    }

    String datatype = "";
%}

NL              = \n
SINGLECOMMENT   = \/\/.+\n
MULTISTART      = \/\*
MULTISTOP       = \*\/
WHITESPACE      = [ \t]
IDENTIFIERS     = [a-zA-z_]

%xstates STATE1, MCMT
%%

"int"|"float"|"double"|"long"                   { yybegin(STATE1);  datatype=yytext(); }
^{WHITESPACE}+                                  {  }
{MULTISTART}                                    { yybegin(MCMT); }
{SINGLECOMMENT}                                 {  }
[^]                                             { System.out.print(yytext()); }

<STATE1>{
    "main"                                      { System.out.print(yytext()); }
    {IDENTIFIERS}                               { System.out.print(datatype); }
    {NL}                                        { System.out.print("\n"); yybegin(YYINITIAL); }
    [^]                                         { System.out.print(yytext()); }
}

<MCMT>{
    [^]                                         {  }
    {MULTISTOP}                                 { yybegin(YYINITIAL); }
}
