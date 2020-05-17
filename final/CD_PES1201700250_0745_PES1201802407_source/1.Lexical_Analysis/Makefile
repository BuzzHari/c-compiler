Scanner.class: Scanner.java
	@javac Scanner.java > /dev/null 2>&1

Scanner.java: Scanner.l
	@jflex Scanner.l > /dev/null 2>&1

clean:
	@rm *.java*
	@rm *.class
