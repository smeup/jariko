     D £DBG_Str        S             50

     D A40_A01         S              2
     D A40_A02         S              3

     D                 DS
     D A40_DS11A                      5    DIM(3)
     D  DS11A_FL1                          LIKE(A40_A01)
     D                                     OVERLAY(A40_DS11A:01)
     D  DS11A_FL2                          LIKE(A40_A02)
     D                                     OVERLAY(A40_DS11A:*NEXT)

     C                   EVAL      DS11A_FL1(1)='CN'
     C                   EVAL      DS11A_FL2(1)='CLI'
     C                   EVAL      DS11A_FL1(2)='CN'
     C                   EVAL      DS11A_FL2(2)='FOR'
     C                   EVAL      DS11A_FL1(3)='CN'
     C                   EVAL      DS11A_FL2(3)='COL'
     C                   EVAL      £DBG_Str=%TRIM(A40_DS11A(1))+
     C                                      %TRIM(DS11A_FL1(1))+
     C                                      %TRIM(DS11A_FL2(1))+
     C                                      %TRIM(A40_DS11A(2))+
     C                                      %TRIM(DS11A_FL1(2))+
     C                                      %TRIM(DS11A_FL2(2))+
     C                                      %TRIM(A40_DS11A(3))+
     C                                      %TRIM(DS11A_FL1(3))+
     C                                      %TRIM(DS11A_FL2(3))
     C     £DBG_Str      DSPLY
