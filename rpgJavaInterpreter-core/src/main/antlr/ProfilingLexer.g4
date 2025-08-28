lexer grammar ProfilingLexer;

OPEN_PAREN : '(';
CLOSE_PAREN : ')';
DOUBLE_QUOTE: '"';
TELEMETRY_SPAN_ID_SCOPE: [mM];

TELEMETRY_SPAN_START: '@'[sS][tT][aA][rR][tT][tT][rR][aA][cC][eE];
TELEMETRY_SPAN_END: '@'[sS][tT][oO][pP][tT][rR][aA][cC][eE];
TELEMETRY_SPAN_ID: [§£#@$a-zA-Z0-9_]+;
TELEMETRY_ENABLE_SPLAT: '*'[tT][rR][aA][cC][eE];

TELEMETRY_SPAN_COMMENT: DOUBLE_QUOTE ~[\r\n]+ DOUBLE_QUOTE;

WS : [ \r\t\n]+ -> skip ;
