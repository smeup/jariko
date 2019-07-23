     D Msg             S             12
     D A               S              2  0 INZ(1)
     D B               S              2  0 INZ(2)
     D $$FLT           S           1000    INZ('-;')
     D $S              S              4  0 INZ(1)
     D $C              S              4  0 INZ(1)
     C                   IF        A<=B
     C                   Eval      Msg  = 'A<=B'
     C                   ELSE
     C                   Eval      Msg  = 'A>B'
     C                   ENDIF
     C     Msg           DSPLY
     C                   IF        $S>0 AND $S+$C <= %LEN($$FLT) AND
     C                             (%SUBST($$FLT:$S+$C:1)='' OR
     C                             %SUBST($$FLT:$S+$C:1)=';')
     C                   Eval      Msg  = 'OK'
     C                   ELSE
     C                   Eval      Msg  = 'NO'
     C                   ENDIF
     C     Msg           DSPLY
     C                   SETON                                          LR