
public class SymbolTable {

    public static int sno = 0;
    public int lineno;
    public String type;
    public String token;

    SymbolTable(int lineno, String type, String token) {
        this.lineno = lineno;
        this.type = type;
        this.token = token;    
    }
    
    public static void insertToken(SymbolTable table, int lineno, String type, String tokn) {
        table = new SymbolTable(lineno, type, tokn);
        sno++;
    }
}