target:
	jflex scanner.l 
	byaccj -J parser.y 
	javac *java

run:
	java Parser < test.c

clean:
	rm *java* *.class
