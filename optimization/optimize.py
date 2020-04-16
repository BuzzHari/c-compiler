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

def match(line):
    # print("inputline:", line)
    if re.match("t\d+ = "+num+" [\+\-\/\*] "+num+"", line):
        return 1
    elif re.match("t\d+ = [a-z]+ [\+\-\/\*] "+num+"", line):
        return 5
    elif re.match("t\d+ = "+num+" [\+\-\/\*] [a-z]+", line):
        return 6
    elif re.match("t\d* = "+num+"", line):
        return 2
    elif re.match("[a-z]+ = "+num+"", line):
        return 3
    elif re.match("[a-z]+ = t\d*", line):
        return 4
    # elif re.match("t[0-9]+ = [a-z]+ [\+\-\/\*] [0-9]+", line):
        # return 5
    # elif re.match("t[0-9]+ = [0-9]+ [\+\-\/\*] [a-z]+", line):
        # return 6
    elif re.match("t\d+ = [a-z]+ [\+\-\/\*] [a-z]+", line):
        return 7

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
    # print(line)
    tokens = line.split()
    if tokens[2] in table.keys():
        new_line = tokens[0] + " = " + str(table[tokens[2]])
    if not new_line:
        return line
    return new_line

def func5(line):
    tokens = line.split()
    if tokens[2] in table.keys():
        new_line = tokens[0] + " = " + str(table[tokens[2]]) + " " + tokens[3] + " " + tokens[4]
    else:
        return line
    return new_line

def func6(line):
    tokens = line.split()
    if tokens[4] in table.keys():
        new_line = tokens[0] + " = " + tokens[2] + " " + tokens[3] + " " + str(table[tokens[4]])
    else:
        return line
    # print("??",new_line)
    return new_line


def func7(line):
    tokens = line.split()
    new_line = tokens[0] + " = "
    if tokens[2] in table.keys():
        new_line += str(getVal(table[tokens[2]])) + " + "
    else:
        new_line += tokens[2] + " + "

    if tokens[4] in table.keys():
        new_line += str(getVal(table[tokens[4]]))
    else:
        new_line += tokens[4]

    # print("#7:", new_line)
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

    print("Input ICG")
    printLines(lines)

    opt1 = []

    for line in lines:
        f = match(line)
        # print(line, ":", f)
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
                # break
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
            # print(line, ":", f)
        if line:
            opt1.append(line)

    # print(table)

    print("After Optimizing")
    printLines(opt1)

if __name__ == "__main__":
    try:
        main()
        # print(table)
    except KeyboardInterrupt:
        print(table)
