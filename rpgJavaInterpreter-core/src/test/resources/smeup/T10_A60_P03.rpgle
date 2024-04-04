     D £DBG_Str        S            150          VARYING
     D NNN             S              6  0 INZ(100000)

     D A60_P1          S             10    INZ('MULANGT10')
     D A60_P2          S              2  0
     D A60_P3          S             50

     C                   CLEAR                   A60_P3
     C                   DO        NNN
     C                   EXSR      SUB_A60_A
     C                   ENDDO
     C                   EVAL      £DBG_Str='CALL('+A60_P1+', '
     C                                     +%CHAR(A60_P2)
     C                                     +', '+A60_P3+') '
      *
     C     £DBG_Str      DSPLY

      *---------------------------------------------------------------------
    RD* Subsezione di SEZ_A60 P02 e P03
      *---------------------------------------------------------------------
     C     SUB_A60_A     BEGSR
     C                   CALL      'MULANGTB10'
     C                   PARM                    A60_P1
     C                   PARM      1             A60_P2
     C                   PARM                    A60_P3
     C                   ENDSR