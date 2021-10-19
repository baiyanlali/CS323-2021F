%{
    #include"lex.yy.c"
    void yyerror(const char*);
%}

%token LC RC LB RB COLON COMMA
%token STRING NUMBER ERROR_NUMBER
%token TRUE FALSE VNULL
%%

Json:
      Value
    ;
Value:
    Object STRING error{puts("misplaced quoted value, recovered");}
    | Object
    | Array RB error{puts("Extra close, recovered");}
    | Array
    | STRING
    | ERROR_NUMBER error{puts("Numbers cannot have leading zeroes, recovered");}
    | NUMBER
    | TRUE
    | FALSE
    | VNULL
    ;
Object:
      LC RC
    | LC Members RC
    | LC Members COMMA error{puts("Comma instead if closing brace, recovered");}
    ;
Members:
      Member
    | Member COMMA Members
    | Member COMMA error {puts("extra comma, recovered");}
    ;
Member:
      STRING COMMA Value error{puts("Comma instead of colon, recovered");}
    | STRING Value error{puts("Missing colon, recovered");}
    |  STRING COLON Value
    |  STRING COLON COLON Value error{puts("Double colon, recovered");}
    ;
Array:
      LB RB
    | LB COMMA Values RB error{puts("missing value, recovered"); }
    | LB Values RC error { puts("unmatched right bracket, recovered"); }
    | LB Values RB COMMA error{puts("Comma after the close, recovered");}
    | LB Values RB
    | LB Values error{puts("Unclosed array, recovered");}
    ;
Values:
      Value
    | Value COLON Values error {puts("Colon instread of comma, recovered");}
    | Value COMMA COMMA Values error{puts("double extra comma, recovered");}
    | Value COMMA COMMA error{puts("double extra comma, recovered");}
    | Value COMMA error {puts("extra comma, recovered");}
    | Value COMMA Values

    ;
%%

void yyerror(const char *s){
    printf("syntax error: ");
}

int main(int argc, char **argv){
    if(argc != 2) {
        fprintf(stderr, "Usage: %s <file_path>\n", argv[0]);
        exit(-1);
    }
    else if(!(yyin = fopen(argv[1], "r"))) {
        perror(argv[1]);
        exit(-1);
    }
    yyparse();
    return 0;
}
