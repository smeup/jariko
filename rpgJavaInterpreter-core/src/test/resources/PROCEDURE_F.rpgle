      *---------------------------------------------------------------
      * Tested features:
      * ARRAY OF 3 ELEMS PASSED AS PARAMETER
      * 1. For parameters are passed to procedure, 1st is an array;
      * 2. Procedure execute sum of some array values and return the result;
      * 3. Check result of returned sum
      *---------------------------------------------------------------
      *---------------------------------------------------------------
     DCALL1            PR
     Dp                               2  0 DIM(3)
     Dq                               2  0
     Dr                               2  0
     Ds                               2  0
      *
     Daa               S              2  0 DIM(3)
      *---------------------------------------------------------------
      * Main
      *---------------------------------------------------------------
     C                   EVAL      aa(1)=11
     C                   EVAL      aa(2)=22
     C                   EVAL      aa(3)=33
     C                   Z-ADD     22            b                 2 0
     C                   Z-ADD     33            c                 2 0
     C                   Z-ADD     0             d                 2 0
     C                   CALLP     CALL1(aa:b:c:d)
      * Must be 99
     C     d             DSPLY
     C                   SETON                                        LR
      *---------------------------------------------------------------
     PCALL1            B
     DCALL1            PI
     Dp                               2  0 DIM(3)
     Dq                               2  0
     Dr                               2  0
     Ds                               2  0
      *
     C                   EVAL      s=p(1)+p(3)+q+r
     PCALL1            E