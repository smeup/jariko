lexer grammar MuteLexer;

OPEN_PAREN : '(';
CLOSE_PAREN : ')';
EQUAL : '=';


VAL1 : 'VAL1';
VAL2 : 'VAL2';
NOXMI: '"NOXMI"';

EQ : '(EQ)' ;
NE : '(NE)' ;
GT : '(GT)' ;
GE : '(GE)' ;
LT : '(LT)' ;
LE : '(LE)' ;

COMP : 'COMP' ;
TYPE : 'Type' ;

WS : [ \r\t\n]+ -> skip ;
EXP: '['.*?']';

