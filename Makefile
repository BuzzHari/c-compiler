target:
	jflex lexer.l
	byaccj -J parser.y
	javac *java

run:
	java Parser inp.c

clean:
	rm *java*
