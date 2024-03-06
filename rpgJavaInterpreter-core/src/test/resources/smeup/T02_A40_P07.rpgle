     D                 DS
     D DS7_AR1                       15    DIM(3)
     D DS7_FL1                        5    OVERLAY(DS7_AR1:01)
     D DS7_FL2                       10    OVERLAY(DS7_AR1:*NEXT)
     D DS7_AL1                        5
     D DS7_NR1                        5  0
     D £DBG_Str        S            100          VARYING

     D* DS con overlay e campi definiti singolarmente
     C                   EVAL      DS7_FL1(1) = 'AAAAA'
     C                   EVAL      DS7_FL2(1) = 'BBBBBCCCCC'
     C                   EVAL      DS7_FL1(2) = 'DDDDD'
     C                   EVAL      DS7_FL2(2) = 'EEEEEFFFFF'
     C                   EVAL      DS7_FL1(3) = 'GGGGG'
     C                   EVAL      DS7_FL2(3) = 'HHHHHIIIII'
     C                   EVAL      DS7_AL1 = 'ZZZZZ'
     C                   EVAL      DS7_NR1 =  12345
     C                   EVAL      £DBG_Str=DS7_AR1(1)+DS7_AR1(2)+DS7_AR1(3)
     C                                     +DS7_FL1(1)+DS7_FL1(2)+DS7_FL1(3)
     C                                     +DS7_FL2(1)+DS7_FL2(2)+DS7_FL2(3)
     C                                     +DS7_AL1+%CHAR(DS7_NR1)
     C     £DBG_Str      DSPLY
