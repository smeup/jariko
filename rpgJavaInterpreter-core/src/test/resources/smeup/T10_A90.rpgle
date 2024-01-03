     D A90_A2          S             30    INZ
     D A90_A3          S              8
     D A90_A4          S              8
     D A90_A5          S              8
     D £DBG_Pas        S              3
     D £DBG_Str        S            100
     D NNN             S              6  0 INZ(100000)
     D A90_UP          C                   'ABCDEFGHIJKLMNOPQRS-
     D                                     TUVWXYZ'
     D A90_LO          C                   'abcdefghijklmnopqrs-
     D                                     tuvwxyz'
      *
     C                   EVAL      £DBG_Pas='P01'
     C                   MOVE      '999 9999'    A90_N1            8
     C      ' ':'-'      XLATE     A90_N1        A90_A1            8
     C                   EVAL      £DBG_Str=A90_A1
     C     £DBG_Str      DSPLY
      *
     C                   EVAL      £DBG_Pas='P02'
     C                   MOVE      'RPG DEPT'    A90_A2
     C                   MOVE      'RPG dept'    A90_A3
     C                   EXSR      SEZ_A90_A
     C                   EVAL      £DBG_Str='A90_A4('+A90_A4+') '
     C                                     +'A90_A5('+A90_A5+')'
      *
      * Expeted: £DBG_Str='A90_A4(        ) A90_A5(RPG DEPT)'
      * Note: A90_A2 is 30 characters long but A90_A4 is 8 characters long.
      *       for this reason, A90_A4 will contain only the first 8 characters of A90_A2
      *       which are just spaces
     C     £DBG_Str      DSPLY
      *
      *
     C                   EVAL      £DBG_Pas='P03'
     C                   MOVE      'RPG DEPT'    A90_A2
     C                   MOVE      'RPG dept'    A90_A3
     C                   DO        NNN
     C                   EXSR      SEZ_A90_A
     C                   ENDDO
     C                   EVAL      £DBG_Str='A90_A4('+A90_A4+') '
     C                                     +'A90_A5('+A90_A5+')'
      *
      * Expeted: £DBG_Str='A90_A4(        ) A90_A5(RPG DEPT)'
      * Note: A90_A2 is 30 characters long but A90_A4 is 8 characters long.
      *       for this reason, A90_A4 will contain only the first 8 characters of A90_A2
      *       which are just spaces
     C     £DBG_Str      DSPLY
      *
     C                   SETON                                          LR
      *---------------------------------------------------------------------
    RD* Subsezione di SEZ_A90 P02 e P03
      *---------------------------------------------------------------------
     C     SEZ_A90_A     BEGSR
      *
     C     A90_UP:A90_LO XLATE     A90_A2        A90_A4
     C     A90_LO:A90_UP XLATE     A90_A3:5      A90_A5
      *
     C                   ENDSR