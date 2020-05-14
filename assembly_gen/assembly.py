import re
from sys import argv

class quad:
    def __init__(self, pos, op, arg1, arg2, dest):
        self.pos = pos
        self.op = op
        self.arg1 = arg1
        self.arg2 = arg2
        self.dest = dest

    def disp(self):
        print(self.pos, self.op, self.arg1, self.arg2, self.dest, sep="\t")

def parseVal(x):
    try:
        int(x)
        return 1
    except ValueError:
        try:
            float(x)
            return 1
        except ValueError:
            return 0

def usage():
    print("Usage: python", argv[0], "\"input_file\"")

def codegen(op, i):
    if (parseVal(i.arg1)):
        print("MOV R1, ", i.arg1, sep="")
    else:
        print("LW R1, ", i.arg1, sep="")

    if (parseVal(i.arg2)):
        print("MOV R2, ", i.arg2, sep="")
    else:
        print("LW R2, ", i.arg2, sep="")

    if (op == "SUB"):
        print(op, "R0, R2, R1")
    else:
        print(op, "R0, R1, R2")

    print("SW R0,", i.dest)
    # print("MOV ", i.arg1, ", R0", sep="")
    # print(op, " ", i.arg2, ", R0", sep="")
    # print("MOV R0, ", i.dest, sep="")

def genif(i):
    if (parseVal(i.arg1)):
        print("MOV R1, ", i.arg1, sep="")
    else:
        print("LW R1, ", i.arg1, sep="")

    if (parseVal(i.arg2)):
        print("MOV R2, ", i.arg2, sep="")
    else:
        print("LW R2, ", i.arg2, sep="")

    print("CMP R1, R2")


def genjmp(cond, i):
    cond = cond.upper()
    if cond == "":
        print("JMP", i.dest)
        return
    print(cond, i.dest)

def assign(i):
    if (parseVal(i.arg1)):
        print("MOV", "R0,", i.arg1)
        print("SW", "R0,", i.dest)
    else:
        print("LW", "R0,", i.arg1)
        print("SW", "R0,", i.dest)


def main(argc, argv):
    # print(argc)
    # print(argv)

    if (argc < 2):
        usage()
        exit()

    try:
        f = open(argv[1], "r")
    except IOError:
        print("Cannot Open \"", argv[1], "\".", sep="")
        usage()
        exit()

    table = list()
    i = -1
    for line in f:
        i = i + 1
        line = line.split("\n")[0]
        tokens = line.split(" ")
        # print(len(tokens))
        if (len(tokens) == 5):
            table.append(quad(str(i), tokens[3], tokens[2], tokens[4], tokens[0]))
        if (len(tokens) == 4):
            if tokens[2] == "goto":
                table.append(quad(str(i), tokens[2], tokens[0], tokens[1], tokens[3]))
            else:
                table.append(quad(str(i), tokens[2], tokens[0], "", tokens[3]))
        if (len(tokens) == 3):
            table.append(quad(str(i), tokens[1], tokens[2], "", tokens[0]))
        if (len(tokens) == 2):
            table.append(quad(str(i), tokens[0], "", "", tokens[1]))
        if (len(tokens) == 1):
            table.append(quad(str(i), "", "", "", tokens[0]))

        # if (len(tokens) == 2):

        #     table.append(quad(str(i), tokens[0], tokens[1], "", ""))
    f.close()
    print("#", "OP", "ARG1", "ARG2", "DEST", sep="\t")
    for i in table:
        i.disp()
    cond = ""
    for i in table:
        print()
        if (i.op == "+"):
            codegen("ADD", i)
        elif (i.op == "-"):
            codegen("SUB", i)
        elif (i.op == "*"):
            codegen("MUL", i)
        elif (i.op == "/"):
            codegen("DIV", i)
        elif (i.op == "="):
            assign(i)
        elif (i.op == "goto"):
            genjmp(cond, i)
            cond = ""
        elif (i.op == "=="):
            genif(i)
            cond="jne"
        elif (i.op == "!="):
            genif(i)
            cond="je"
        elif (i.op == ">"):
            genif(i)
            cond="jle"
        elif (i.op == "<"):
            genif(i)
            cond="jge"
        elif (i.op == ">="):
            genif(i)
            cond="jl"
        elif (i.op == "<="):
            genif(i)
            cond="jg"
        elif (i.op == ""):
            print(".", i.dest, ":", sep="")

if __name__ == "__main__":
    # print("Begin")
    main(len(argv), argv)
