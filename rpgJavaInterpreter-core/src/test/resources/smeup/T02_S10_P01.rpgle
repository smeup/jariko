     D A10_A1          C                   'abcdefghijklmnopqrs-
     D                                     tuvwxyzèéàòù'
     D A10_A2          C                   'ABCDEFGHIJKLMNOPQRS-
     D                                             TUVWXYZEEAOU'
     D A10_A3          C                   'ABCDEFGHIJKLMNOPQRS+
     D                                     TUVWXYZEEAOU'
     D A10_A4          C                   'ABCDEFGHIJKLMNOPQRS+
     D                                             TUVWXYZEEAOU'
     D A10_A5          C                   'ABCDEFGHIJKLMNOPQRSTUVWXYZEEAOU'
     D A10_A6          C                   CONST('ABCDEFGH')
     D A10_A7          C                   ''''
     D A10_A8          C                   ''''''
     D A10_A9          C                   '*%'
      *
     D £DBG_Str        S            180
      *
     C                   EVAL      £DBG_Str=A10_A1+A10_A2+A10_A3
     C                                     +A10_A4+A10_A5+A10_A6
     C                                     +A10_A7+A10_A8+A10_A9
     C     £DBG_Str      DSPLY
      *
      * Expected:
      * abcdefghijklmnopqrstuvwxyzèéàòùABCDEFGHIJKLMNOPQRS        TU
      * VWXYZEEAOUABCDEFGHIJKLMNOPQRSTUVWXYZEEAOUABCDEFGHIJKLMNOPQRS
      * TUVWXYZEEAOUABCDEFGHIJKLMNOPQRSTUVWXYZEEAOUABCDEFGH'''*%
      *
      * Note:
      * A10_A7: should contain 1 quotation mark:   '
      * A10_A8: should contain 2 quotation marks:  ''
      *
      *
     C                   SETON                                          LR
      *