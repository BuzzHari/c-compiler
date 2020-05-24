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


x = 0
y = 0
def usage():
    print("Usage: python", argv[0], "\"input_file\"")

def codegen(op, i):
    if (parseVal(i.arg1)):
        print("MOV R1, #", i.arg1, sep="")
    else:
        print("LDR R7, =", i.arg1, sep="")
        print("LDR R1, [R7]")
        x = 1

    if (parseVal(i.arg2)):
        print("MOV R2, #", i.arg2, sep="")
    else:
        print("LDR R7, =", i.arg2, sep="")
        print("LDR R2, [R7]")
        y = 1

    if (op == "SUB"):
        print(op, "R0, R1, R2")
    else:
        print(op, "R0, R1, R2")

    print("LDR R7, =", i.dest, sep="")
    print("STR R0, [R7]")

def genif(i):
    if (parseVal(i.arg1)):
        print("MOV R1, #", i.arg1, sep="")
    else:
        # print("LW R1, ", i.arg1, sep="")
        print("LDR R7, =", i.arg1, sep="")
        print("LDR R1, [R7]")

    if (parseVal(i.arg2)):
        print("MOV R2, #", i.arg2, sep="")
    else:
        # print("LW R2, ", i.arg2, sep="")
        print("LDR R7, =", i.arg2, sep="")
        print("LDR R2, [R7]")

    print("CMP R1, R2")


def genjmp(cond, i):
    cond = cond.upper()
    if cond == "":
        print("B .", i.dest, sep="")
        return
    print(cond, " .", i.dest, sep="")

def assign(i):
    if (parseVal(i.arg1)):
        print("MOV R0, #", i.arg1, sep="")
        print("LDR R7, =", i.dest, sep="")
        print("STR R0, [R7]")
    else:
        print("LDR R7, =", i.arg1)
        print("LDR R0, [R7]")

        print("LDR R7, =", i.dest, sep="")
        print("STR R0, [R7]")


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
    # print("#", "OP", "ARG1", "ARG2", "DEST", sep="\t")
    # for i in table:
    #     i.disp()
    cond = ""
    varrs = list()
    for i in table:
        varrs.append(i.dest)

    uvars = list()
    for i in varrs:
        if not i in uvars and not re.match("L\d+.*", i): #and not re.match("t\d+.*", i)
            uvars.append(i)

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
            cond="bne"
        elif (i.op == "!="):
            genif(i)
            cond="beq"
        elif (i.op == ">"):
            genif(i)
            cond="ble"
        elif (i.op == "<"):
            genif(i)
            cond="bge"
        elif (i.op == ">="):
            genif(i)
            cond="blt"
        elif (i.op == "<="):
            genif(i)
            cond="bgt"
        elif (i.op == ""):
            print(".", i.dest, ":", sep="")

    print("SWI 0x11")

    print()
    for i in uvars:
        print(i, ": .WORD 0", sep="")

if __name__ == "__main__":
    # print("Begin")
    main(len(argv), argv)
