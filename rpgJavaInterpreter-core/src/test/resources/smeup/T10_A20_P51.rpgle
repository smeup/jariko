     D £DBG_Str        S            150          VARYING
     D A20_N60         S              6  0
     D A20_AR2         S              2  0 DIM(6)
     D A20I            S              2  0

     C                   EVAL      A20_N60=20
     C                   EVAL      A20_AR2(1)=1
     C                   EVAL      A20_AR2(2)=1
     C                   EVAL      A20_AR2(3)=1
     C                   EVAL      A20_AR2(4)=40
     C                   EVAL      A20_AR2(5)=20
     C                   EVAL      A20_AR2(6)=20
     C                   EVAL      A20I=1
     C                   ADD       A20_N60       A20_AR2 (A20I)
     C                   EVAL      A20I=2
     C                   SUB       A20_N60       A20_AR2 (A20I)
     C                   EVAL      A20I=3
     C                   MULT      A20_N60       A20_AR2 (A20I)
     C                   EVAL      A20I=4
     C                   DIV       A20_N60       A20_AR2 (A20I)
     C                   EVAL      A20I=5
     C                   Z-ADD     A20_N60       A20_AR2 (A20I)
     C                   EVAL      A20I=6
     C                   Z-SUB     A20_N60       A20_AR2 (A20I)
     C                   EVAL      £DBG_Str='Res('
     C                                    +%CHAR(A20_AR2(1))+', '
     C                                    +%CHAR(A20_AR2(2))+', '
     C                                    +%CHAR(A20_AR2(3))+', '
     C                                    +%CHAR(A20_AR2(4))+', '
     C                                    +%CHAR(A20_AR2(5))+', '
     C                                    +%CHAR(A20_AR2(6))+')'
     C     £DBG_Str      DSPLY