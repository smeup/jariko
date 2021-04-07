      *---------------------------------------------------------------
     DCALL1            PR             2  0
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
     C                   Z-ADD     0             e                 2 0
     C                   EVAL      e=CALL1(aa:b:c:d)
      * Must be 99
     C     e             DSPLY
      *
     C                   EVAL      aa(1)=0
     C                   EVAL      aa(2)=22
     C                   EVAL      aa(3)=0
     C                   EVAL      e=CALL1(aa:b:c:d)
      * Must be 55
     C     e             DSPLY
     C                   SETON                                        LR
      *---------------------------------------------------------------
     PCALL1            B
     DCALL1            PI             2  0
     Dp                               2  0 DIM(3)
     Dq                               2  0
     Dr                               2  0
     Ds                               2  0
      *
     Dt                S              2  0
     Du                S              2  0
      *
     C                   EVAL      s=p(1)+p(3)+q+r
     C                   EVAL      t=66
     C                   EVAL      u=33
      *
     C                   SETON                                        51
      *
     C  N51              RETURN    t
     C   51              RETURN    s
     C                   RETURN    u
      *
     PCALL1            E