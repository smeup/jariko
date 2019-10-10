q      * Examples for Whenxx
     D  AAA015         S             15
      *
      *
  019C                   EVAL      AAA015='PROVA'                               COSTANTE
      *
     C                   SELECT
  019C     1             WHENEQ    1
     C     1             OREQ      2
  019C                   EVAL      AAA015='PROVA'                               COSTANTE
     C                   OTHER
  019C                   EVAL      AAA015='PROVA ERRATA'                        COSTANTE
     C                   ENDSL
    MU* VAL1(AAA015) VAL2('PROVA') COMP(EQ)
     C     AAA015        DSPLY
     **************************************************************************************
     C                   SELECT
  019C     1             WHENGE    1
     C     2             ANDLE     2
  019C                   EVAL      AAA015='PROVA'                               COSTANTE
     C                   OTHER
  019C                   EVAL      AAA015='PROVA ERRATA'                        COSTANTE
     C                   ENDSL
    MU* VAL1(AAA015) VAL2('PROVA') COMP(EQ)
     C     AAA015        DSPLY
     **************************************************************************************
     C                   SELECT
  019C     2             WHENEQ    1
     C     1             OREQ      2
  019C                   EVAL      AAA015='PROVA'                               COSTANTE
     C                   OTHER
  019C                   EVAL      AAA015='PROVA ERRATA'                        COSTANTE
     C                   ENDSL
    MU* VAL1(AAA015) VAL2('PROVA ERRATA') COMP(EQ)
     C     AAA015        DSPLY
     **************************************************************************************
     C                   SELECT
  019C     1             WHENEQ    1
     C     1             ANDEQ     2
  019C                   EVAL      AAA015='PROVA'                               COSTANTE
     C                   OTHER
  019C                   EVAL      AAA015='PROVA ERRATA'                        COSTANTE
     C                   ENDSL
    MU* VAL1(AAA015) VAL2('PROVA ERRATA') COMP(EQ)
     C     AAA015        DSPLY
     **************************************************************************************
     C                   SELECT
  019C     1             WHENNE    2
     C     1             ANDNE     3
  019C                   EVAL      AAA015='PROVA'                               COSTANTE
     C                   OTHER
  019C                   EVAL      AAA015='PROVA ERRATA'                        COSTANTE
     C                   ENDSL
    MU* VAL1(AAA015) VAL2('PROVA') COMP(EQ)
     C     AAA015        DSPLY
     **************************************************************************************
     C                   SELECT
  019C     1             WHENNE    2
     C     1             ANDEQ     1
  019C                   EVAL      AAA015='PROVA'                               COSTANTE
     C                   OTHER
  019C                   EVAL      AAA015='PROVA ERRATA'                        COSTANTE
     C                   ENDSL
    MU* VAL1(AAA015) VAL2('PROVA') COMP(EQ)
     C     AAA015        DSPLY
     **************************************************************************************
     C                   SELECT
  019C     1             WHENEQ    1
     C     1             ANDNE     2
  019C                   EVAL      AAA015='PROVA'                               COSTANTE
     C                   OTHER
  019C                   EVAL      AAA015='PROVA ERRATA'                        COSTANTE
     C                   ENDSL
    MU* VAL1(AAA015) VAL2('PROVA') COMP(EQ)
     C     AAA015        DSPLY
     **************************************************************************************
     C                   SELECT
  019C     1             WHENGT    1
     C     2             ANDLT     2
  019C                   EVAL      AAA015='PROVA'                               COSTANTE
     C                   OTHER
  019C                   EVAL      AAA015='PROVA ERRATA'                        COSTANTE
     C                   ENDSL
    MU* VAL1(AAA015) VAL2('PROVA ERRATA') COMP(EQ)
     C     AAA015        DSPLY
     **************************************************************************************
     C                   SELECT
  019C     1             WHENEQ    2
     C     2             ORNE      2
  019C                   EVAL      AAA015='PROVA'                               COSTANTE
     C                   OTHER
  019C                   EVAL      AAA015='PROVA ERRATA'                        COSTANTE
     C                   ENDSL
    MU* VAL1(AAA015) VAL2('PROVA ERRATA') COMP(EQ)
     C     AAA015        DSPLY
     **************************************************************************************                       
     C                   SETON                                        LR