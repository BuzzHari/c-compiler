import sys
import re

def printLines(arr):
    for line in arr:
        print(line)
    print("\n")

def eval(line):
    # print("x: ", line)

    tokens = line.split()
    if len(tokens) == 3 and re.match("[0-9]+", tokens[2]):
        table[tokens[0]] = int(tokens[2])
    elif tokens[3] == '+':
        table[tokens[0]] = int(tokens[2]) + int(tokens[4])
    elif tokens[3] == '*':
        table[tokens[0]] = int(tokens[2]) * int(tokens[4])
    elif tokens[3] == '/':
        table[tokens[0]] = int(tokens[2]) / int(tokens[4])
    elif tokens[3] == '-':
        table[tokens[0]] = int(tokens[2]) - int(tokens[4])

def prop(lines):
    #nothing yet
    z = 1


table = dict()

if __name__ == "__main__":
    if len(sys.argv) == 2:
        icg = str(sys.argv[1])

    lines = []
    f = open(icg, 'r')
    for line in f:
        if line != '\n':
            lines.append(line)
    f.close()
    pure = []
    for line in lines:
        pure.append(line.split('\n')[0])

    print("Input ICG")
    printLines(pure)

    opt1 = []
    for line in pure:
        if re.search("= [0-9]+ [\+\-\*\/] [0-9]+", line):
            eval(line)
        elif re.search("= [0-9]+", line):
            eval(line)
        else:
            opt1.append(line)


    # print(table)
    # print('opt1: ', opt1)

    opt2 = []
    keys = list(table.keys())

    reg = ".*= ["
    for key in keys:
        reg += key + ","
    reg = reg[:-1] + "]\0"
    # print("reg:", reg)

    # exit(0)
    for line in opt1:
        x = re.match(reg, line)
        tokens = line.split()
        if x and tokens[2] in keys:
            new_line = tokens[0] + " " + tokens[1] + " " + str(table[tokens[2]])
            opt2.append(new_line)
        else:
            opt2.append(line)

    usefullness = dict()

    for line in opt2:
        tokens = line.split()
        for tok in tokens:
            if re.match("[a-z]+[0-9]*", tok):
                if tok in usefullness.keys():
                    usefullness[tok] += 1
                else:
                    usefullness[tok] = 0

    # print(usefullness)

    opt3 = list()
    for line in opt2:
        tokens = line.split()
        # for tok in tokens:
        if re.match("^[a-z]+[0-9]*", tokens[0]):
            if tokens[0] in usefullness.keys() and usefullness[tokens[0]] > 0:
                if line not in opt3:
                    opt3.append(line)


    print("After Optimization")
    printLines(opt3)


