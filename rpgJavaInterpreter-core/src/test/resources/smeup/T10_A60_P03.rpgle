     D NNN             S              6  0 INZ(100000)
     D £DBG_Str        S           2560                                         Stringa
     D £DBG_Pas        S             10
      * Variabili sezione A60
     D A60_A10         S             10
     D A60_P1          S             10    INZ('MULANGT10')
     D A60_P2          S              2  0
     D A60_P3          S             50

     C                   EVAL      £DBG_Pas='P03'
      *
     C                   CLEAR                   A60_P3
     C                   DO        NNN
     C                   EXSR      SUB_A60_A
     C                   ENDDO
     C                   EVAL      £DBG_Str='CALL('+A60_P1+', '
     C                                     +%CHAR(A60_P2)
     C                                     +', '+A60_P3+') '
      *

     C     SUB_A60_A     BEGSR
     C                   CALL      'MULANGTB10'
     C                   PARM                    A60_P1
     C                   PARM      1             A60_P2
     C                   PARM                    A60_P3
     C                   ENDSR