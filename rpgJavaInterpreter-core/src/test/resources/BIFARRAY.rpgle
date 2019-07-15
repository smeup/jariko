     D SI              S             10    DIM(17) PERRCD(1) CTDATA             _NOTXT
     D V1              S             52
     D I1              S              5  0
      *---------------------------------------------------------------------
     C                   FOR       I1 = 1 TO 17
     C                   EVAL      V1=%TRIM(V1)+%TRIM(SI(I1))+%TRIMR(SI(I1))
     C                   ENDFOR
     C     V1            DSPLY
     C                   EVAL      V1=%CHAR(%LEN(SI(1)))
     C     V1            DSPLY
     C                   SETON                                        LR
      *---------------------------------------------------------------------
** SI
001
d01
A01
c01
B01
b01
C01
901
101
D01
H01
E01
201
e01
a01
x01
X01
