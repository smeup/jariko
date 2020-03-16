     D Msg             S             50
     D  ARRAY          S              1    DIM(10) ASCEND
     D N               S              2  0
     C                   EVAL      ARRAY(1)='B'
     C                   EVAL      ARRAY(2)='C'
     C                   EVAL      ARRAY(3)='D'
     C                   EVAL      ARRAY(4)='G'
     C                   EVAL      ARRAY(5)='H'
     C                   EVAL      ARRAY(6)='I'
     C                   EVAL      ARRAY(7)='A'
     C                   EVAL      ARRAY(8)='B'
     C                   EVAL      ARRAY(9)='D'
     C                   EVAL      ARRAY(10)='Z'
      *---------------------------------------------------------------------
     C     'Test 1'      DSPLY
     C                   EVAL      N = 1
     C                   SETOFF                                           69
     C                   SETOFF                                           68
     C     'Z'           LOOKUP    ARRAY(N)                             6869
     C   69'69 On'       DSPLY
     C   68'68 On'       DSPLY
     C  N69'69 Off'      DSPLY
     C  N68'68 Off'      DSPLY
     C     N             DSPLY
      *---------------------------------------------------------------------
     C     'Test 2'      DSPLY
     C                   EVAL      N = 1
     C                   SETOFF                                           69
     C     'D'           LOOKUP    ARRAY(N)                               69
     C   69'69 On'       DSPLY
     C     N             DSPLY
      *---------------------------------------------------------------------
     C     'Test 3'      DSPLY
     C                   EVAL      N = 4
     C                   SETOFF                                           69
     C     'D'           LOOKUP    ARRAY(N)                               69
     C   69'69 On'       DSPLY
     C     N             DSPLY
      *---------------------------------------------------------------------
     C     'Test 4'      DSPLY
     C                   EVAL      N = 2
     C                   SETOFF                                           69
     C     'Q'           LOOKUP    ARRAY(N)                               69
     C   69'69 On'       DSPLY
     C     N             DSPLY
      *---------------------------------------------------------------------
     C     'Test 5'      DSPLY
     C                   EVAL      N = 1
     C                   SETOFF                                           69
     C                   SETOFF                                           68
     C     'J'           LOOKUP    ARRAY(N)                             6869
     C   69'69 On'       DSPLY
     C   68'68 On'       DSPLY
     C  N69'69 Off'      DSPLY
     C  N68'68 Off'      DSPLY
     C     N             DSPLY
      *---------------------------------------------------------------------
     C     'Test 6'      DSPLY
     C                   EVAL      N = 6
     C                   SETOFF                                           69
     C                   SETOFF                                           68
     C     'A'           LOOKUP    ARRAY(N)                             6869
     C   69'69 On'       DSPLY
     C   68'68 On'       DSPLY
     C  N69'69 Off'      DSPLY
     C  N68'68 Off'      DSPLY
     C     N             DSPLY
      *---------------------------------------------------------------------
     C                   SETON                                        LR
