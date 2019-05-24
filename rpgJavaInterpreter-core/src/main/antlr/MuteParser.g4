parser grammar MuteParser;

import RpgParser;

options {   tokenVocab = MuteLexer; }

muteLine: muteAnnotation EOF ;

muteAnnotation : VAL1 OPEN_PAREN val1=expression CLOSE_PAREN
                 VAL2 OPEN_PAREN val2=expression CLOSE_PAREN
                 COMP OPEN_PAREN comparisonOperator CLOSE_PAREN #muteComparisonAnnotation
               | #muteTypeAnnotation
               ;

comparisonOperator: EQ | NE | LT | LE | GT | GE ;
