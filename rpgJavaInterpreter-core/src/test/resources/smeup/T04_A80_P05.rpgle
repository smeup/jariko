     D £DBG_Str        S            150          VARYING
     D A80_A1          S             28
     D A80_A2          S              1
     D A80_F           C                   '0123456789'
     D A80_T           C                   'xxxxxxxxxx'
     D A80_I1          S              2  0

     C                   TIME                    A80_D1            6 0
     C                   EVAL      A80_A1=%CHAR(A80_D1)
     C                   EXSR      SUB_A80_B
     C                   EVAL      £DBG_Str=
     C                                'A80_D1('+%TRIM(A80_A1)+')'
      *
     C                   TIME                    A80_D2           12 0
     C                   EVAL      A80_A1=%CHAR(A80_D2)
     C                   EXSR      SUB_A80_B
     C                   EVAL      £DBG_Str=%TRIMR(£DBG_Str)
     C                                +' A80_D2('+%TRIM(A80_A1)+')'
      *
     C                   TIME                    A80_D3           14 0
     C                   EVAL      A80_A1=%CHAR(A80_D3)
     C                   EXSR      SUB_A80_B
     C                   EVAL      £DBG_Str=%TRIMR(£DBG_Str)
     C                                +' A80_D3('+%TRIM(A80_A1)+')'

     C     £DBG_Str      DSPLY

      *---------------------------------------------------------------------
    RD* Subsezione di SEZ_A80
      *---------------------------------------------------------------------
     C     SUB_A80_B     BEGSR
      *
     C                   IF        %REM(%LEN(A80_A1):2)<>0
     C                   EVAL      A80_A1='0'+A80_A1
     C                   ENDIF
      *
     C     A80_F:A80_T   XLATE     A80_A1        A80_A1
     C                   DO        2
     C                   EVAL      A80_A2='h'
     C                   EXSR      SUB_A80_D
     C                   ENDDO
     C                   DO        2
     C                   EVAL      A80_A2='m'
     C                   EXSR      SUB_A80_D
     C                   ENDDO
     C                   DO        2
     C                   EVAL      A80_A2='s'
     C                   EXSR      SUB_A80_D
     C                   ENDDO
     C                   DO        2
     C                   EVAL      A80_A2='D'
     C                   EXSR      SUB_A80_D
     C                   ENDDO
     C                   DO        2
     C                   EVAL      A80_A2='M'
     C                   EXSR      SUB_A80_D
     C                   ENDDO
     C                   DO        4
     C                   EVAL      A80_A2='Y'
     C                   EXSR      SUB_A80_D
     C                   ENDDO
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD*
      *---------------------------------------------------------------------
     C     SUB_A80_D     BEGSR
      *
     C                   EVAL      A80_I1=%SCAN('x':A80_A1)
     C                   IF        A80_I1<>0
     C                   EVAL      A80_A1=%REPLACE(A80_A2:A80_A1:A80_I1)
     C                   ENDIF
      *
     C                   ENDSR