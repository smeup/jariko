parser grammar MuteParser;

import RpgParser;

options {   tokenVocab = MuteLexer; }

muteLine: muteAnnotation EOF ;

muteAnnotation : ID OPEN_PAREN val1=expression CLOSE_PAREN
                 ID OPEN_PAREN val2=expression CLOSE_PAREN
                 ID OPEN_PAREN cp=expression   CLOSE_PAREN #muteComparisonAnnotation
               | #muteTypeAnnotation
               ;

comparisonOperator: EQ | NE | LT | LE | GT | GE ;