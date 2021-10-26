
import java.util.ArrayList;
import java.util.Arrays;
import java.util.*;

public class Parser2 {
    static ArrayList<Character> nonterminals = new ArrayList<>();
    static ArrayList<Character> terminals = new ArrayList<>();
    static ArrayList<Production2> productions = new ArrayList<>();
    static Production2[][] parsingTable;
    static {
        populateParsingTable();
    }
    static ArrayList<Character> inputBuffer;
    static ArrayList<Production2> productionsToApply = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        if (args == null || args.length == 0)
            return;
        inputBuffer = new ArrayList<>();
        for (char c : args[0].toCharArray()) {
            inputBuffer.add(c);
        }
        inputBuffer.add('$');
        procedure('S');
        if (inputBuffer.size() == 1 && inputBuffer.get(0).charValue() == '$') {
            for (Production2 p : productionsToApply)
                p.print();
            System.out.println("success");
        }
    }

    public static void procedure(Character nonterminal) throws Exception {


        Stack<Character> stack=new Stack<Character>();

        stack.push('$');
        stack.push('S');

        while(stack.peek()!='$'){
            Character c=(Character)stack.peek();
            if(c==inputBuffer.get(0)){
                stack.pop();
                inputBuffer.remove(0);
            }else if(!terminals.contains(inputBuffer.get(0))){
                throw new Exception("error");
            }else{
                stack.pop();
                var table = parsingTable[nonterminals.indexOf(c)][terminals.indexOf(inputBuffer.get(0))];
                productionsToApply.add(table);
                var body = table.body;
                for (int i = 0; i < body.size(); i++) {
                    stack.push(body.get(body.size()-i-1));
                }
            }
        }

        
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
        parsingTable = new Production2[nonterminals.size()][terminals.size()];
        Production2 p1 = new Production2('S', 'T', 'E');
        parsingTable[nonterminals.indexOf('S')][terminals.indexOf('a')] = p1;
        parsingTable[nonterminals.indexOf('S')][terminals.indexOf('(')] = p1;
        parsingTable[nonterminals.indexOf('E')][terminals.indexOf('+')] = new Production2('E', '+', 'T', 'E');
        Production2 p2 = new Production2('E', new Character[] {});
        parsingTable[nonterminals.indexOf('E')][terminals.indexOf(')')] = p2;
        parsingTable[nonterminals.indexOf('E')][terminals.indexOf('$')] = p2;
        Production2 p3 = new Production2('T', 'F', 'X');
        parsingTable[nonterminals.indexOf('T')][terminals.indexOf('a')] = p3;
        parsingTable[nonterminals.indexOf('T')][terminals.indexOf('(')] = p3;
        Production2 p4 = new Production2('X', new Character[] {});
        parsingTable[nonterminals.indexOf('X')][terminals.indexOf('+')] = p4;
        parsingTable[nonterminals.indexOf('X')][terminals.indexOf('*')] = new Production2('X', '*', 'F', 'X');
        parsingTable[nonterminals.indexOf('X')][terminals.indexOf(')')] = p4;
        parsingTable[nonterminals.indexOf('X')][terminals.indexOf('$')] = p4;
        parsingTable[nonterminals.indexOf('F')][terminals.indexOf('a')] = new Production2('F', 'a');
        parsingTable[nonterminals.indexOf('F')][terminals.indexOf('(')] = new Production2('F', '(', 'S', ')');
    }
}

class Production2 {
    Character head;
    ArrayList<Character> body;

    public Production2(Character head, Character... body) {
        this.head = head;
        this.body = new ArrayList<Character>(Arrays.asList(body));
    }

    public void print() {
        System.out.print(head);
        System.out.print("->");
        if (body.size() == 0) {
            System.out.println("\u03B5");
            return;
        }
        for (Character c : body) {
            System.out.print(c);
        }
        System.out.println();
    }
}
