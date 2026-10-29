     V* ==============================================================
     V* Purpose: indicators spread over several lines (conditioning
     V* line followed by an AND line) must be evaluated as an AND, no
     V* matter the numeric order of the indicators.
     V* Each pair of statements is mutually exclusive, so exactly one
     V* of them must be executed.
     V* Expected output: 1:A_ 2:_B 3:A_ 4:_B
     V* ==============================================================

     D R1              S              1
     D R2              S              1
     D OUT             S             50    VARYING

      * Higher indicator first (56 then 53), 56 off
     C                   SETOFF                                       5356
     C                   EVAL      R1='_'
     C                   EVAL      R2='_'
     C  N56
     CANN53              EVAL      R1='A'
     C   56
     CANN53              EVAL      R2='B'
     C                   EVAL      OUT='1:'+R1+R2
     C     OUT           DSPLY

      * Higher indicator first (56 then 53), 56 on
     C                   SETON                                        56
     C                   EVAL      R1='_'
     C                   EVAL      R2='_'
     C  N56
     CANN53              EVAL      R1='A'
     C   56
     CANN53              EVAL      R2='B'
     C                   EVAL      OUT='2:'+R1+R2
     C     OUT           DSPLY

      * Lower indicator first (53 then 56), 53 off
     C                   SETOFF                                       5356
     C                   EVAL      R1='_'
     C                   EVAL      R2='_'
     C  N53
     CANN56              EVAL      R1='A'
     C   53
     CANN56              EVAL      R2='B'
     C                   EVAL      OUT='3:'+R1+R2
     C     OUT           DSPLY

      * Lower indicator first (53 then 56), 53 on
     C                   SETON                                        53
     C                   EVAL      R1='_'
     C                   EVAL      R2='_'
     C  N53
     CANN56              EVAL      R1='A'
     C   53
     CANN56              EVAL      R2='B'
     C                   EVAL      OUT='4:'+R1+R2
     C     OUT           DSPLY

     C                   SETON                                        LR
