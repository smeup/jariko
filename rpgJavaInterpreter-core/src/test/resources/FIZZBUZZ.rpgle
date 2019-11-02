     D NBR             S              8  0
     D RESULT          S              8
     C     *entry        plist
     C                   parm                    NBR
     C                   parm                    RESULT
     C                   IF        0=%REM(NBR:3)
     C                   EVAL      RESULT=%TRIM(RESULT) + 'FIZZ'
     C                   ENDIF
     C                   IF        0=%REM(NBR:5)
     C                   EVAL      RESULT=%TRIM(RESULT) + 'BUZZ'
     C                   ENDIF
     C                   IF        ''= %TRIM(RESULT)
     C                   EVAL      RESULT=%CHAR(NBR)
     C                   ENDIF
     C                   DSPLY                   RESULT
     C                   seton                                        lr
