

Parser.class: Parser.java Yylex.java
	@javac Parser.java Yylex.java > /dev/null 2>&1

Yylex.java: scanner.l
	@jflex scanner.l > /dev/null 2>&1

ParserVal.class: ParserVal.java
	@javac ParserVal.java > /dev/null 2>&1

ParserVal.java: parser.y
	@byaccj -J parser.y > /dev/null 2>&1

Parser.java: parser.y
	@byaccj -J parser.y > /dev/null 2>&1


clean:
	@rm *java* *.class
