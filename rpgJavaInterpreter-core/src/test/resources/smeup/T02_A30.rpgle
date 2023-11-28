     D A30_AR2         S            100    DIM(09) CTDATA PERRCD(1)             _TXT
     D £DBG_Str        S           2560                                         Stringa
     D £DBG_Pas        S             10                                         Passo     
     C                   EVAL      £DBG_Pas='P02'
     C                   EVAL      £DBG_Str=A30_AR2(01)+A30_AR2(02)+A30_AR2(03)
     C                                     +A30_AR2(04)+A30_AR2(05)+A30_AR2(06)
     C                                     +A30_AR2(07)+A30_AR2(08)+A30_AR2(09)

     C     £DBG_Str      DSPLY
     

** A30_AR2
AAAAA
BBBBB
CCCCC
DDDDD
EEEEE
FFFFF
GGGGG
HHHHH
IIIII