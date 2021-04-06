      *---------------------------------------------------------------
     DCALL1            PR             3  0 DIM(7)
     Dp                               2  0 DIM(3)
     Dq                               2  0
     Dr                               2  0
     Ds                               2  0
      *
     Daa               S              2  0 DIM(3)
     Drr               S              3  0 DIM(7)
      *---------------------------------------------------------------
      * Main
      *---------------------------------------------------------------
     C                   EVAL      aa(1)=11
     C                   EVAL      aa(2)=22
     C                   EVAL      aa(3)=33
     C                   Z-ADD     22            b                 2 0
     C                   Z-ADD     33            c                 2 0
     C                   Z-ADD     0             d                 2 0
     C                   EVAL      rr=CALL1(aa:b:c:d)
      * Must be 11
     C     rr(1)         DSPLY
      * Must be 22
     C     rr(2)         DSPLY
      * Must be 33
     C     rr(3)         DSPLY
      * Must be 0
     C     rr(4)         DSPLY
      * Must be 33
     C     rr(5)         DSPLY
      * Must be 22
     C     rr(6)         DSPLY
      * Must be 121
     C     rr(7)         DSPLY
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
     PCALL1            B
     DCALL1            PI             3  0 DIM(7)
     Dp                               2  0 DIM(3)
     Dq                               2  0
     Dr                               2  0
     Ds                               2  0
      *
     Dss               S              3  0 DIM(7)
      *
     C                   EVAL      ss(1)=p(1)
     C                   EVAL      ss(2)=p(2)
     C                   EVAL      ss(3)=p(3)
     C                   EVAL      ss(4)=s
     C                   EVAL      ss(5)=r
     C                   EVAL      ss(6)=q
     C                   EVAL      ss(7)=ss(1)+ss(2)+ss(3)+ss(4)+
     C                             ss(5)+ss(6)
      *
     C                   RETURN    ss
     PCALL1            E
