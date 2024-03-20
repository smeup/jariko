     D                 DS
     D A40_DS10A                      5    DIM(3)
     D  DS10A_FL1                     2    OVERLAY(A40_DS10A:01)
     D  DS10A_FL2                     3    OVERLAY(A40_DS10A:*NEXT)
     D A40_DS10B                      9    DIM(3)
     D  DS10B_FL1                     4    OVERLAY(A40_DS10B:01)
     D  DS10B_FL2                     5    OVERLAY(A40_DS10B:*NEXT)
     D £DBG_Str        S            150          VARYING

     C                   EVAL      DS10A_FL1(1)='CN'
     C                   EVAL      DS10A_FL2(1)='CLI'
     C                   EVAL      DS10B_FL1(1)='AAAA'
     C                   EVAL      DS10B_FL2(1)='BBBBB'
     C                   EVAL      DS10A_FL1(2)='CN'
     C                   EVAL      DS10A_FL2(2)='FOR'
     C                   EVAL      DS10B_FL1(2)='CCCC'
     C                   EVAL      DS10B_FL2(2)='DDDDD'
     C                   EVAL      DS10A_FL1(3)='CN'
     C                   EVAL      DS10A_FL2(3)='COL'
     C                   EVAL      DS10B_FL1(3)='EEEE'
     C                   EVAL      DS10B_FL2(3)='FFFFF'
     C                   EVAL      £DBG_Str=%TRIM(A40_DS10A(1))+
     C                                      %TRIM(DS10A_FL1(1))+
     C                                      %TRIM(DS10A_FL2(1))+
     C                                      %TRIM(A40_DS10B(1))+
     C                                      %TRIM(DS10B_FL1(1))+
     C                                      %TRIM(DS10B_FL2(1))+
     C                                      %TRIM(A40_DS10A(2))+
     C                                      %TRIM(DS10A_FL1(2))+
     C                                      %TRIM(DS10A_FL2(2))+
     C                                      %TRIM(A40_DS10B(2))+
     C                                      %TRIM(DS10B_FL1(2))+
     C                                      %TRIM(DS10B_FL2(2))+
     C                                      %TRIM(A40_DS10A(3))+
     C                                      %TRIM(DS10A_FL1(3))+
     C                                      %TRIM(DS10A_FL2(3))+
     C                                      %TRIM(A40_DS10B(3))+
     C                                      %TRIM(DS10B_FL1(3))+
     C                                      %TRIM(DS10B_FL2(3))
     C     £DBG_Str      DSPLY