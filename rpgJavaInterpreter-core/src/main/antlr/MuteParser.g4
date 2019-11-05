parser grammar MuteParser;

options {   tokenVocab = MuteLexer; }

muteLine: muteAnnotation EOF ;

intNumber: OPEN_PAREN NUMBER CLOSE_PAREN;

muteAnnotation : VAL1 val1=EXP
                 VAL2 val2=EXP
                 COMP cp=muteComparisonOperator #muteComparisonAnnotation
               | TYPE EQUAL NOXMI #muteTypeAnnotation
               | TIMEOUT intNumber #muteTimeout
               | FAIL msg=EXP2 #muteFailAnnotation
               ;

muteComparisonOperator: EQ | NE | LT | LE | GT | GE ;
