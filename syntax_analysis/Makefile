target:
	@jflex scanner.l > /dev/null 2>&1
	@byaccj -J parser.y > /dev/null 2>&1
	@javac *java

run:
	@java Parser < test.c

clean:
	@rm *java* *.class
