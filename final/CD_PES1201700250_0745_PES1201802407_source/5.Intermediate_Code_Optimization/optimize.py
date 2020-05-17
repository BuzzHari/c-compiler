import sys
import operator
import re

ops = { "+": operator.add,
        "-": operator.sub,
        "*": operator.mul,
        "/": operator.floordiv}

def printLines(arr):
    for line in arr:
        print(line)
    print("\n")

num="(\d+(?:\.\d+)?)"
loop=0
loop_end="unset"
def match(line):

    global loop
    global loop_end
    if not loop:
        if re.match("t\d+ = "+num+" [\+\-\/\*] "+num+"", line) :
            return 1
        elif re.match("t\d+ = [a-z]+ [\+\-\/\*] "+num+"", line) :
            return 5
        elif re.match("t\d+ = "+num+" [\+\-\/\*] [a-z]+", line) :
            return 6
        elif re.match("t\d* = "+num+"", line) :
            return 2
        elif re.match("[a-z]+ = "+num+"", line) :
            return 3
        elif re.match("[a-z]+ = t\d*", line) :
            return 4
        elif re.match("t\d+ = [a-z]+ [\+\-\/\*] [a-z]+", line) :
            return 7
        elif re.match("[a-z]+ = [a-z]+", line):
            return 4
        elif re.match("if .* goto .*", line) :
            loop_end = line.split()[-1]
            loop = 1
    elif re.match("[a-z]+.* =",line):
        var = line.split()[0]
        table[var] = "na";
    elif re.match(loop_end, line):
        loop = 0
        loop_end="unset"



    return 0

def getVal(token):
    try:
        val = int(token)
    except ValueError:
        val = float(token)

    return val


def func1(line):
    tokens = line.split()
    val = ops[tokens[3]](getVal(tokens[2]),getVal(tokens[4]))
    new_line = tokens[0] + " = " + str(val)
    return new_line

def func2(line):
    tokens = line.split()
    table[tokens[0]] = getVal(tokens[2])
    return ""


def func3(line):
    tokens = line.split()
    table[tokens[0]] = getVal(tokens[2])
    return line

def func4(line):
    new_line = ""
    tokens = line.split()
    if tokens[2] in table.keys() and table[tokens[2]] != "na":
        new_line = tokens[0] + " = " + str(table[tokens[2]])
    if not new_line:
        return line
    return new_line

def func5(line):
    tokens = line.split()
    if tokens[2] in table.keys() and table[tokens[2]] != "na":
        new_line = tokens[0] + " = " + str(table[tokens[2]]) + " " + tokens[3] + " " + tokens[4]
    else:
        return line
    return new_line

def func6(line):
    tokens = line.split()
    if tokens[4] in table.keys() and table[tokens[4]] != "na":
        new_line = tokens[0] + " = " + tokens[2] + " " + tokens[3] + " " + str(table[tokens[4]])
    else:
        return line
    # print("??",new_line)
    return new_line


def func7(line):
    tokens = line.split()
    new_line = tokens[0] + " = "
    if tokens[2] in table.keys():
        new_line += str(table[tokens[2]]) + " + "
    else:
        new_line += tokens[2] + " + "

    if tokens[4] in table.keys():
        new_line += str(table[tokens[4]])
    else:
        new_line += tokens[4]

    return new_line


table = dict()

def main():
    if len(sys.argv) == 2:
        icg = str(sys.argv[1])

    lines = []
    f = open(icg, 'r')
    for line in f:
        if line != '\n':
            lines.append(line.split('\n')[0])
    f.close()

    opt1 = []

    for line in lines:
        f = match(line)
        while f:
            if f == 1:
                nline = func1(line)
            elif f == 2:
                nline = func2(line)
            elif f == 3:
                nline = func3(line)
                break
            elif f == 4:
                nline = func4(line)
            elif f == 5:
                nline = func5(line)
            elif f == 6:
                nline = func6(line)
            elif f == 7:
                nline = func7(line)
            else:
                break
            if nline == line:
                break
            else:
                line = nline
            f = match(line)
        if line:
            opt1.append(line)


    table2 = dict()
    temp = []

    for line in opt1:
        if re.match("[a-z]+ = [a-z]+$",line):
            tokens = line.split()
            table2[tokens[0]] = tokens[2]
        else:
            temp.append(line)

    opt2 = []
    for line in opt1:
        new_line = ""
        tokens = line.split()
        i = 2
        if len(tokens) > 2:
            new_line = tokens[0] + " " + tokens [1] + " "
        elif len(tokens) == 2:
            new_line = tokens[0] + " " + tokens [1] + " "
        else:
            new_line = tokens[0] + " "
        while i < len(tokens):
            if tokens[i] in table2.keys():
                new_line += table2[tokens[i]]
            else:
                new_line += tokens[i]
            new_line += " "
            i += 1
        new_line = new_line[:-1]
        opt2.append(new_line)

    for i in opt2:
        if not (len(i) == 1 and i == "\n"):
            print(i)

if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        print("An Error Occurred")
