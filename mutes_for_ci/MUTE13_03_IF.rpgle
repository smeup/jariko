      * Examples for IFXX
      *
     D  AAA015         S             15
      *
     C                   EVAL      AAA015='PROVA'                                  
      *
      *
     C     1             IFEQ      1
     C     1             OREQ      2
     C                   EVAL      AAA015='PROVA'
     C                   ELSE
     C                   EVAL      AAA015='PROVA ERRATA'
     C                   ENDIF
    MU* VAL1(AAA015) VAL2('PROVA') COMP(EQ)
     C     AAA015        DSPLY
     *********************************************************************
     C     1             IFEQ      1
     C     2             ANDEQ     2
     C                   EVAL      AAA015='PROVA'
     C                   ELSE
     C                   EVAL      AAA015='PROVA ERRATA'
     C                   ENDIF
    MU* VAL1(AAA015) VAL2('PROVA') COMP(EQ)
     C     AAA015        DSPLY
     *********************************************************************
     C     2             IFEQ      1
     C     1             OREQ      2
     C                   EVAL      AAA015='PROVA'
     C                   ELSE
     C                   EVAL      AAA015='PROVA ERRATA'
     C                   ENDIF
    MU* VAL1(AAA015) VAL2('PROVA ERRATA') COMP(EQ)
     C     AAA015        DSPLY
     *********************************************************************
     C     1             IFEQ      1
     C     1             ANDEQ     2
     C                   EVAL      AAA015='PROVA'
     C                   ELSE
     C                   EVAL      AAA015='PROVA ERRATA'
     C                   ENDIF
    MU* VAL1(AAA015) VAL2('PROVA ERRATA') COMP(EQ)
     C     AAA015        DSPLY
     *********************************************************************
     C     1             IFNE      2
     C     1             ANDNE     3
     C                   EVAL      AAA015='PROVA'
     C                   ELSE
     C                   EVAL      AAA015='PROVA ERRATA'
     C                   ENDIF
    MU* VAL1(AAA015) VAL2('PROVA') COMP(EQ)
     C     AAA015        DSPLY
     *********************************************************************
     C     1             IFNE      2
     C     1             ANDEQ     1
     C                   EVAL      AAA015='PROVA'
     C                   ELSE
     C                   EVAL      AAA015='PROVA ERRATA'
     C                   ENDIF
    MU* VAL1(AAA015) VAL2('PROVA') COMP(EQ)
     C     AAA015        DSPLY
     *********************************************************************
     C     1             IFEQ      1
     C     1             ANDNE     2
     C                   EVAL      AAA015='PROVA'
     C                   ELSE
     C                   EVAL      AAA015='PROVA ERRATA'
     C                   ENDIF
    MU* VAL1(AAA015) VAL2('PROVA') COMP(EQ)
     C     AAA015        DSPLY
     *********************************************************************
     C     1             IFEQ      1
     C     2             ANDNE     2
     C                   EVAL      AAA015='PROVA'
     C                   ELSE
     C                   EVAL      AAA015='PROVA ERRATA'
     C                   ENDIF
    MU* VAL1(AAA015) VAL2('PROVA ERRATA') COMP(EQ)
     C     AAA015        DSPLY
     *********************************************************************
     C     1             IFEQ      2
     C     2             ORNE      2
     C                   EVAL      AAA015='PROVA'
     C                   ELSE
     C                   EVAL      AAA015='PROVA ERRATA'
     C                   ENDIF
    MU* VAL1(AAA015) VAL2('PROVA ERRATA') COMP(EQ)
     C     AAA015        DSPLY
     *********************************************************************
     C     1             IFEQ      2
     C                   EVAL      AAA015='PROVA'
     C                   ELSEIF    2=1
     C                   EVAL      AAA015='PROVA'
     C                   ELSE
     C                   EVAL      AAA015='PROVA ERRATA'
     C                   ENDIF
    MU* VAL1(AAA015) VAL2('PROVA ERRATA') COMP(EQ)
     C     AAA015        DSPLY
     *********************************************************************
     C     1             IFGT      2
     C                   EVAL      AAA015='PROVA'
     C                   ELSE
     C                   EVAL      AAA015='PROVA ERRATA'
     C                   ENDIF
    MU* VAL1(AAA015) VAL2('PROVA ERRATA') COMP(EQ)
     C     AAA015        DSPLY
     *********************************************************************
     C     1             IFGE      2
     C                   EVAL      AAA015='PROVA'
     C                   ELSE
     C                   EVAL      AAA015='PROVA ERRATA'
     C                   ENDIF
    MU* VAL1(AAA015) VAL2('PROVA ERRATA') COMP(EQ)
     C     AAA015        DSPLY
     *********************************************************************
     C     1             IFLE      2
     C                   EVAL      AAA015='PROVA'
     C                   ELSE
     C                   EVAL      AAA015='PROVA ERRATA'
     C                   ENDIF
    MU* VAL1(AAA015) VAL2('PROVA') COMP(EQ)
     C     AAA015        DSPLY
     *********************************************************************
     C     1             IFLT      2
     C                   EVAL      AAA015='PROVA'
     C                   ELSE
     C                   EVAL      AAA015='PROVA ERRATA'
     C                   ENDIF
    MU* VAL1(AAA015) VAL2('PROVA') COMP(EQ)
     C     AAA015        DSPLY
     *********************************************************************
     C                   SETON                                        LR