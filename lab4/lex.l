%{
    #include"syntax.tab.h"
%}
%option noyywrap

unic u[0-9a-fA-F]{4}
esc \\([\"\\/bfnrt]|{unic})
scp [^"\\\x00-\x1f]
string \"({esc}|{scp})*\"

int 0|[1-9][0-9]*
frac \.[0-9]+
exp [Ee][+-]?[0-9]+
number -?{int}{frac}?{exp}?
errorNumber -?0+[0-9]*

empty [ \n\r\t]

%%

"{" { return LC; }
"}" { return RC; }
"[" { return LB; }
"]" { return RB; }
":" { return COLON; }
"," { return COMMA; }

"true" { return TRUE; }
"false" { return FALSE; }
"null" { return VNULL; }

{string} { return STRING; }
{errorNumber} { return ERROR_NUMBER; }
{number} { return NUMBER; }

{empty} {}

. { printf("lexical error: %s\n", yytext); }
