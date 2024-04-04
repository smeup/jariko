     D £DBG_Str        S            150          VARYING
     D A30_SEC         S             15  5
     D A30_N70         S             30  0
     D A30_A08         S              8    INZ('12345.67')

     C                   EXSR      SUB_A30_B
     C                   EVAL      £DBG_Str=%CHAR(A30_N70)

     C     £DBG_Str      DSPLY

      *---------------------------------------------------------------------
    RD* Subsezione di SEZ_A30 P03 e P05
      *---------------------------------------------------------------------
     C     SUB_A30_B     BEGSR
      *
     C                   EVAL      A30_N70=%INTH(A30_A08)
      *
     C                   ENDSR