     D ARRA1           S             20    DIM(3)
     D $N              S              2  0
     D ZZSTR           S             20
     D XXSTR           S             10
     C                   EVAL      XXSTR='ABCDEFGHIL'
     C                   EVAL      ZZSTR=*ALL'X'
     C                   MOVEL(P)  XXSTR         ARRA1
     C                   FOR       $N = 1 TO 3
     C                   DSPLY                   ARRA1($N)
     C                   ENDFOR
     C                   EVAL      $N=2
     C                   EVAL      ARRA1($N)='  '+ZZSTR
     C                   DSPLY                   ARRA1($N)
     C                   SETON                                        LR
