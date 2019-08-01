      *
     C                   EXSR      sr01
     C                   clear                   dsp              50
     C                   eval      dsp= 'Result = ' + %CHAR(RESULT)
     C                   dsply                   dsp
     C                   seton                                        lr
      *--------------------------------------------------------------*
     C     sr01          BEGSR
     C                   CLEAR                   result            5 0
     C                   EVAL      RESULT = 3 + 2
     C                   ENDSR
      *--------------------------------------------------------------*
