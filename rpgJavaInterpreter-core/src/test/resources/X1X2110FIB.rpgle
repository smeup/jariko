     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 11/07/19  000983  AS Creazione
     V* 11/07/19  V5R1   BMA Check-out 000983 in SMEUP_TST
     V* ==============================================================
      *
      * Calculates number of Fibonacci and return the result
      *
      ****************************************************************
      * Input Number
     D U$INNR          S             21  0
      * Fibonacci Number (calucated on U$INNR)
     D U$FIBO          S             21  0
      *
     D NBR             S             21  0
     D RESULT          S             21  0 INZ(0)
     D COUNT           S             21  0
     D A               S             21  0 INZ(0)
     D B               S             21  0 INZ(1)
      *
     C     *entry        plist
     C                   PARM                    U$INNR
     C                   PARM                    U$FIBO
      *
     C                   Eval      NBR = U$INNR
     C                   EXSR      FIB
     C                   Eval      U$FIBO = RESULT
      *
     C                   seton                                        lr
      *--------------------------------------------------------------*
     C     FIB           BEGSR
     C                   SELECT
     C                   WHEN      NBR = 0
     C                   EVAL      RESULT = 0
     C                   WHEN      NBR = 1
     C                   EVAL      RESULT = 1
     C                   OTHER
     C                   FOR       COUNT = 2 TO NBR
     C                   EVAL      RESULT = A + B
     C                   EVAL      A = B
     C                   EVAL      B = RESULT
     C                   ENDFOR
     C                   ENDSL
     C                   ENDSR
      *--------------------------------------------------------------*