lexer grammar ProfilingLexer;

OPEN_PAREN : '(';
CLOSE_PAREN : ')';
DOUBLE_QUOTE: '"';

TELEMETRY_SPAN_START: [sS][pP][aA][nN][sS][tT][aA][rR][tT];
TELEMETRY_SPAN_END: [sS][pP][aA][nN][eE][nN][dD];
TELEMETRY_SPAN_ID: [§£#@$a-zA-Z0-9_]+;

TELEMETRY_SPAN_COMMENT: DOUBLE_QUOTE ~[\r\n]+ DOUBLE_QUOTE;

WS : [ \r\t\n]+ -> skip ;
