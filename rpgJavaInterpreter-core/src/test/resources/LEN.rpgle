      *-----------------------------------------------------------
      * Test of %LEN to check variables string length.
      * Warning about "VARYING" types, cause they assume
      * length of their content, even if blanks content (blanks chars
      * are chars too)
      *-----------------------------------------------------------
     D Msg             S             52
     D X               S             23    INZ('abc')
     D B_01            S              1
     D B_02            S             20
     D B_03            S             30    VARYING
     D B_04            S             40    VARYING
     D B_05            S              7  0
      *
      * Must be 'Hello World! 23'
     C                   Eval      Msg  = 'Hello World! ' + %CHAR(%LEN(X))
     C                   dsply                   Msg
      *
      * Must be length 1
     C                   EVAL      B_01 = ' '
     C                   Eval      Msg = '%LEN(B_01) is ' + %CHAR(%LEN(B_01))
     C                   dsply                   Msg
      *
      * Must be length 20
     C                   EVAL      B_02 = '     '
     C                   Eval      Msg = '%LEN(B_02) is ' + %CHAR(%LEN(B_02))
     C                   dsply                   Msg
      *
      * Must be length 0
     C                   EVAL      B_03 = ''
     C                   Eval      Msg = '%LEN(B_03) is ' + %CHAR(%LEN(B_03))
     C                   dsply                   Msg
      *
      * Must be length 1
     C                   EVAL      B_03 = ' '
     C                   Eval      Msg = '%LEN(B_03) is ' + %CHAR(%LEN(B_03))
     C                   dsply                   Msg
      *
      * Must be length 5
     C                   EVAL      B_03 = '     '
     C                   Eval      Msg = '%LEN(B_03) is ' + %CHAR(%LEN(B_03))
     C                   dsply                   Msg
      *
      * Must be length 0
     C                   Eval      Msg = '%LEN(B_04) is ' + %CHAR(%LEN(B_04))
     C                   dsply                   Msg
      *
      * Must be length 7
     C                   Eval      Msg = '%LEN(B_05) is ' + %CHAR(%LEN(B_05))
     C                   dsply                   Msg
      *
     C                   SETON                                          LR
