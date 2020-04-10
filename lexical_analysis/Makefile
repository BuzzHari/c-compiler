target:
	@jflex Scanner.l > /dev/null 2>&1
	@javac Scanner.java > /dev/null 2>&1

run:
	@java Scanner < test.c

clean:
	@rm *.java
	@rm *.class
