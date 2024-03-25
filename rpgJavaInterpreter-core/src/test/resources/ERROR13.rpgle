     V* ==============================================================
     D* Stackoverflowexception evaluation
     D*
     V* ==============================================================     
     FC5RREG0L  IP   E             DISK
     FC5RATE1L  UF   E           K DISK
      *--------------------------------------------------------------*
  L >D £U13MB          S             10    INZ('C5UTX02   ')
  L >D £U13FI          S             10    INZ('C5SRC     ')
1    C                   IF        R5CAUS='206'
     C                   MOVE      R5RIGA        XXRIGA            5
     C                   MOVEL(P)  'E5'          KTPOR
     C                   EVAL      KCDOR=R5PROG+XXRIGA
     C     KEYRAT        SETLL     C5RATER
      *
2    C                   DO        *HIVAL
     C     KEYRAT        READE     C5RATER                                51
     C   51              LEAVE
     C                   MOVEL(P)  'I'           S5FL19
     C                   UPDATE    C5RATER
2e   C                   ENDDO
      *
1e   C                   ENDIF
      *
     C     KEYRAT        KLIST
     C                   KFLD                    KTPOR
     C                   KFLD                    KCDOR
      *
     C     *LIKE         DEFINE    S5TPOR        KTPOR
     C     *LIKE         DEFINE    S5CDOR        KCDOR