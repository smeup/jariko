      * Examples for Whenxx
     D  AAA015         S             15
      *
      *
     C                   EVAL      AAA015='PROVA'                                  
      *
     C                   SELECT
     C     1             WHENEQ    1
     C     1             OREQ      2
     C                   EVAL      AAA015='PROVA'                                  
     C                   OTHER
     C                   EVAL      AAA015='PROVA ERRATA'                           
     C                   ENDSL
    MU* VAL1(AAA015) VAL2('PROVA') COMP(EQ)
     C     AAA015        DSPLY
     **************************************************************************************
     C                   SELECT
     C     1             WHENGE    1
     C     2             ANDLE     2
     C                   EVAL      AAA015='PROVA'                                  
     C                   OTHER
     C                   EVAL      AAA015='PROVA ERRATA'                           
     C                   ENDSL
    MU* VAL1(AAA015) VAL2('PROVA') COMP(EQ)
     C     AAA015        DSPLY
     **************************************************************************************
     C                   SELECT
     C     2             WHENEQ    1
     C     1             OREQ      2
     C                   EVAL      AAA015='PROVA'                                  
     C                   OTHER
     C                   EVAL      AAA015='PROVA ERRATA'                           
     C                   ENDSL
    MU* VAL1(AAA015) VAL2('PROVA ERRATA') COMP(EQ)
     C     AAA015        DSPLY
     **************************************************************************************
     C                   SELECT
     C     1             WHENEQ    1
     C     1             ANDEQ     2
     C                   EVAL      AAA015='PROVA'                                  
     C                   OTHER
     C                   EVAL      AAA015='PROVA ERRATA'                           
     C                   ENDSL
    MU* VAL1(AAA015) VAL2('PROVA ERRATA') COMP(EQ)
     C     AAA015        DSPLY
     **************************************************************************************
     C                   SELECT
     C     1             WHENNE    2
     C     1             ANDNE     3
     C                   EVAL      AAA015='PROVA'                                  
     C                   OTHER
     C                   EVAL      AAA015='PROVA ERRATA'                           
     C                   ENDSL
    MU* VAL1(AAA015) VAL2('PROVA') COMP(EQ)
     C     AAA015        DSPLY
     **************************************************************************************
     C                   SELECT
     C     1             WHENNE    2
     C     1             ANDEQ     1
     C                   EVAL      AAA015='PROVA'                                  
     C                   OTHER
     C                   EVAL      AAA015='PROVA ERRATA'                           
     C                   ENDSL
    MU* VAL1(AAA015) VAL2('PROVA') COMP(EQ)
     C     AAA015        DSPLY
     **************************************************************************************
     C                   SELECT
     C     1             WHENEQ    1
     C     1             ANDNE     2
     C                   EVAL      AAA015='PROVA'                                  
     C                   OTHER
     C                   EVAL      AAA015='PROVA ERRATA'                           
     C                   ENDSL
    MU* VAL1(AAA015) VAL2('PROVA') COMP(EQ)
     C     AAA015        DSPLY
     **************************************************************************************
     C                   SELECT
     C     1             WHENGT    1
     C     2             ANDLT     2
     C                   EVAL      AAA015='PROVA'                                  
     C                   OTHER
     C                   EVAL      AAA015='PROVA ERRATA'                           
     C                   ENDSL
    MU* VAL1(AAA015) VAL2('PROVA ERRATA') COMP(EQ)
     C     AAA015        DSPLY
     **************************************************************************************
     C                   SELECT
     C     1             WHENEQ    2
     C     2             ORNE      2
     C                   EVAL      AAA015='PROVA'                                  
     C                   OTHER
     C                   EVAL      AAA015='PROVA ERRATA'                           
     C                   ENDSL
    MU* VAL1(AAA015) VAL2('PROVA ERRATA') COMP(EQ)
     C     AAA015        DSPLY
     **************************************************************************************                       
     C                   SETON                                        LR