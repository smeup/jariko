     D £DBG_Str        S            150          VARYING

     D A90_A1          S             30    INZ
     D A90_A2          S             30    INZ
     D A90_Z1          S               Z   INZ
     D A90_Z2          S               Z   INZ
     D A90_N1          S             20  0
     D A90_N2          S             20  0
     D A90_N3          S             20  0
     D A90_N4          S             20  0
     D A90_N5          S             20  0
     D NNN             S              6  0 INZ(100000)

     C                   EVAL      A90_A1='2023-11-11-14.11.17.725000'
     C                   EVAL      A90_Z1=%TIMESTAMP(A90_A1)
     C                   EVAL      A90_A2='2023-11-11-20.14.01.538000'
     C                   EVAL      A90_Z2=%TIMESTAMP(A90_A2)
     C                   DO        NNN
     C                   EXSR      SUB_A90_A
     C                   ENDDO
     C                   EVAL      A90_N5=A90_N1/1000
     C                   EVAL      £DBG_Str=
     C                              'Microsecondi('+%CHAR(A90_N1)+') '
     C                             +'Millisecondi('+%CHAR(A90_N5)+') '
     C                             +'Secondi('+%CHAR(A90_N2)+') '
     C                             +'Minuti('+%CHAR(A90_N3)+') '
     C                             +'Ore('+%CHAR(A90_N4)+') '

     C     £DBG_Str      DSPLY

      *---------------------------------------------------------------------
    RD* Subsezione di SEZ_A90 P01 e P03
      *---------------------------------------------------------------------
     C     SUB_A90_A     BEGSR
      *
     C     A90_Z2        SUBDUR    A90_Z1        A90_N1:*MS
     C     A90_Z2        SUBDUR    A90_Z1        A90_N2:*S
     C     A90_Z2        SUBDUR    A90_Z1        A90_N3:*MN
     C     A90_Z2        SUBDUR    A90_Z1        A90_N4:*H
      *
     C                   ENDSR