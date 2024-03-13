     D £DBG_Str        S            150          VARYING
     D A90_A1          S             30    INZ
     D A90_A2          S             30    INZ
     D A90_Z1          S               Z   INZ
     D A90_Z2          S               Z   INZ
     D A90_N1          S             10I 0
     D A90_N2          S             10I 0
     D A90_N3          S             10I 0
     D A90_N4          S             10I 0

     C                   EVAL      A90_A1='2020-11-01-14.11.17.725000'
     C                   EVAL      A90_Z1=%TIMESTAMP(A90_A1)
     C                   EVAL      A90_A2='2023-12-11-20.14.01.538000'
     C                   EVAL      A90_Z2=%TIMESTAMP(A90_A2)
     C     A90_Z2        SUBDUR    A90_Z1        A90_D1:*MS       20 0
     C     A90_Z2        SUBDUR    A90_Z1        A90_D2:*S        20 0
     C     A90_Z2        SUBDUR    A90_Z1        A90_D3:*MN       20 0
     C     A90_Z2        SUBDUR    A90_Z1        A90_D4:*H        20 0
     C     A90_Z2        SUBDUR    A90_Z1        A90_D5:*D        10 0
     C     A90_Z2        SUBDUR    A90_Z1        A90_D6:*M        10 0
     C     A90_Z2        SUBDUR    A90_Z1        A90_D7:*Y         5 0
     C                   EVAL      £DBG_Str=
     C                              'Microsecondi('+%CHAR(A90_D1)+') '
     C                             +'Secondi('+%CHAR(A90_D2)+') '
     C                             +'Minuti('+%CHAR(A90_D3)+') '
     C                             +'Ore('+%CHAR(A90_D4)+') '
     C                             +'Giorni('+%CHAR(A90_D5)+') '
     C                             +'Mesi('+%CHAR(A90_D6)+') '
     C                             +'Anni('+%CHAR(A90_D7)+') '

     C     £DBG_Str      DSPLY