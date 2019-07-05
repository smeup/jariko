      * Calculates number of Fibonacci in an iterative way
     D ppdat           S              8
     D NBR             S              8  0
     D RESULT          S              8  0 INZ(0)
     D COUNT           S              8  0
     D A               S              8  0 INZ(0)
     D B               S              8  0 INZ(1)
     C     *entry        plist
     C                   parm                    ppdat                          I
      *
     C                   Eval      NBR    = %Dec(ppdat : 8 : 0)
     C                   EXSR      FIB
     C                   clear                   dsp              50
     C                   eval      dsp= 'FIBONACCI OF: ' +  ppdat +
     C                                 ' IS: ' + %CHAR(RESULT)
     C                   dsply                   dsp
     C                   eval      ppdat = %CHAR(RESULT)
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
