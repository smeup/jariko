     D A4              S              1    DIM(4)
     D A4_ASC          S              1    DIM(4) ASCEND
     D A4_DESC         S              1    DIM(4) DESCEND
     D A4_NUM          S              1  0 DIM(4) DESCEND
      *
      * Sort Array (default ASCEND) ('C', 'A', 'D', 'B') => ('A', 'B', 'C', 'D')
     C                   EVAL      A4(1)='C'
     C                   EVAL      A4(2)='A'
     C                   EVAL      A4(3)='D'
     C                   EVAL      A4(4)='B'
     C                   SORTA     A4
     C     A4(1)         DSPLY
     C     A4(2)         DSPLY
     C     A4(3)         DSPLY
     C     A4(4)         DSPLY
      *
      * Sort Array Ascend ('C', 'A', 'D', 'B') => ('A', 'B', 'C', 'D')
     C                   EVAL      A4_ASC(1)='C'
     C                   EVAL      A4_ASC(2)='A'
     C                   EVAL      A4_ASC(3)='D'
     C                   EVAL      A4_ASC(4)='B'
     C                   SORTA     A4_ASC
     C     A4_ASC(1)     DSPLY
     C     A4_ASC(2)     DSPLY
     C     A4_ASC(3)     DSPLY
     C     A4_ASC(4)     DSPLY
      *
      * Sort Array Descend ('C', 'A', 'D', 'B') => ('D', 'C', 'B', 'A')
     C                   EVAL      A4_DESC(1)='C'
     C                   EVAL      A4_DESC(2)='A'
     C                   EVAL      A4_DESC(3)='D'
     C                   EVAL      A4_DESC(4)='B'
     C                   SORTA     A4_DESC
     C     A4_DESC(1)    DSPLY
     C     A4_DESC(2)    DSPLY
     C     A4_DESC(3)    DSPLY
     C     A4_DESC(4)    DSPLY
      *
      * Sort Number Array Descend (3, 1, 4, 2) => (4, 3, 2, 1)
     C                   EVAL      A4_NUM(1)=3
     C                   EVAL      A4_NUM(2)=1
     C                   EVAL      A4_NUM(3)=4
     C                   EVAL      A4_NUM(4)=2
     C                   SORTA     A4_NUM
     C     A4_NUM(1)     DSPLY
     C     A4_NUM(2)     DSPLY
     C     A4_NUM(3)     DSPLY
     C     A4_NUM(4)     DSPLY
      *
     C                   SETON                                        LR