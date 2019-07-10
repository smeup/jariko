parser grammar MuteParser;


options {   tokenVocab = MuteLexer; }

muteLine: muteAnnotation EOF ;

muteAnnotation : VAL1 val1=EXP
                 VAL2 val2=EXP
                 COMP cp=muteComparisonOperator #muteComparisonAnnotation
               | #muteTypeAnnotation
               ;

muteComparisonOperator: EQ | NE | LT | LE | GT | GE ;
