      **************************************************************************
     D £DBG_Str        S           2560                                         Stringa
      *
     D A10_DS2         DS                  OCCURS(3)
     D  DS2_FL1                       3  0
     D  DS2_FL2                      10
     D  DS2_FL3                       1
      *
     D A10_A1          S             15
      *
     C     3             OCCUR     A10_DS2
     C                   EVAL      DS2_FL1 = 114
     C                   EVAL      DS2_FL2 = 'PROVA 3'
     C                   EVAL      DS2_FL3 = 'C'
      * The problem was on next statement: on AS400 the CLEAR statement clean
      * the DS content but don't reset the OCCOUR index to 1.
     C                   CLEAR                   A10_DS2
     C                   EVAL      DS2_FL1=1
     C                   EVAL      DS2_FL2='PROVA 2'
     C                   EVAL      DS2_FL3='B'
     C     3             OCCUR     A10_DS2
     C                   EVAL      A10_A1=%CHAR(DS2_FL1)
     C                   OCCUR     A10_DS2
     C                   EVAL      A10_A1=%TRIMR(A10_A1)+DS2_FL3
     C                   EVAL      £DBG_Str=A10_A1
     C     £DBG_Str      DSPLY
      *
     C                   SETON                                        LR