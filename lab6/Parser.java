/*
Non-terminals: S, T, E, F, X
Terminal: a, +, *, (, )
S -> TE
E -> +TE
E -> ε
T -> FX
X -> *FX
X -> ε
F -> (S)
F -> a
*/

import java.util.ArrayList;
import java.util.Arrays;

public class Parser {
    static ArrayList<Character> nonterminals = new ArrayList<>();
    static ArrayList<Character> terminals = new ArrayList<>();
    static ArrayList<Production> productions = new ArrayList<>();
    static Production[][] parsingTable;
    static { populateParsingTable(); }
    static ArrayList<Character> inputBuffer;
    static ArrayList<Production> productionsToApply = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        if( args == null || args.length == 0 ) return;
        inputBuffer = new ArrayList<>();
        for(char c : args[0].toCharArray()) {
            inputBuffer.add(c);
        }
        inputBuffer.add('$');
        procedure('S');
        if(inputBuffer.size() == 1 && inputBuffer.get(0).charValue() == '$') {
            for(Production p : productionsToApply) p.print();
            System.out.println("success");
        }
    }

    public static void procedure(Character nonterminal) throws Exception {
        // write your code here
    }

    public static void populateParsingTable() {
        // create non-terminals
        nonterminals.add('S');
        nonterminals.add('T');
        nonterminals.add('E');
        nonterminals.add('F');
        nonterminals.add('X');

        // create terminals
        terminals.add('a');
        terminals.add('+');
        terminals.add('*');
        terminals.add('(');
        terminals.add(')');
        terminals.add('$');

        // add entries to parsing table
        parsingTable = new Production[nonterminals.size()][terminals.size()];
        Production p1 = new Production('S', 'T', 'E');
        parsingTable[nonterminals.indexOf('S')][terminals.indexOf('a')] = p1;
        parsingTable[nonterminals.indexOf('S')][terminals.indexOf('(')] = p1;
        parsingTable[nonterminals.indexOf('E')][terminals.indexOf('+')] = new Production('E', '+', 'T', 'E');
        Production p2 = new Production('E', new Character[]{});
        parsingTable[nonterminals.indexOf('E')][terminals.indexOf(')')] = p2;
        parsingTable[nonterminals.indexOf('E')][terminals.indexOf('$')] = p2;
        Production p3 = new Production('T', 'F', 'X');
        parsingTable[nonterminals.indexOf('T')][terminals.indexOf('a')] = p3;
        parsingTable[nonterminals.indexOf('T')][terminals.indexOf('(')] = p3;
        Production p4 = new Production('X', new Character[]{});
        parsingTable[nonterminals.indexOf('X')][terminals.indexOf('+')] = p4;
        parsingTable[nonterminals.indexOf('X')][terminals.indexOf('*')] = new Production('X', '*', 'F', 'X');
        parsingTable[nonterminals.indexOf('X')][terminals.indexOf(')')] = p4;
        parsingTable[nonterminals.indexOf('X')][terminals.indexOf('$')] = p4;
        parsingTable[nonterminals.indexOf('F')][terminals.indexOf('a')] = new Production('F', 'a');
        parsingTable[nonterminals.indexOf('F')][terminals.indexOf('(')] = new Production('F', '(', 'S', ')');
    }
}

class Production {
    Character head;
    ArrayList<Character> body;
    public Production(Character head, Character... body) {
        this.head = head;
        this.body = new ArrayList<Character>(Arrays.asList(body));
    }
    public void print() {
        System.out.print(head);
        System.out.print("->");
        if(body.size() == 0) {
            System.out.println("\u03B5");
            return;
        }
        for(Character c : body) {
            System.out.print(c);
        }
        System.out.println();
    }
}

