     D DSP             S             50
      *
     D  AAA015         S             15
      *
      *
  019C                   EVAL      AAA015='PROVA'                               COSTANTE
      * Examples for Whenxx
      *
    MU* VAL1(DSP) VAL2('PROVA') COMP(EQ)
     C                   SELECT
  019C     1             WHENEQ    1
     C     1             OREQ      2
  019C                   EVAL      AAA015='PROVA'                               COSTANTE
     C                   OTHER
  019C                   EVAL      AAA015='PROVA ERRATA'                        COSTANTE
     C                   ENDSL
     C                   EVAL      DSP=AAA015
     C     DSP           DSPLY     
    MU* VAL1(DSP) VAL2('PROVA') COMP(EQ)
     C                   SELECT
  019C     1             WHENGE    1
     C     2             ANDLE     2
  019C                   EVAL      AAA015='PROVA'                               COSTANTE
     C                   OTHER
  019C                   EVAL      AAA015='PROVA ERRATA'                        COSTANTE
     C                   ENDSL
     C                   EVAL      DSP=AAA015
     C     DSP           DSPLY     
    MU* VAL1(DSP) VAL2('PROVA ERRATA') COMP(EQ)
     C                   SELECT
  019C     2             WHENEQ    1
     C     1             OREQ      2
  019C                   EVAL      AAA015='PROVA'                               COSTANTE
     C                   OTHER
  019C                   EVAL      AAA015='PROVA ERRATA'                        COSTANTE
     C                   ENDSL
     C                   EVAL      DSP=AAA015
     C     DSP           DSPLY     
    MU* VAL1(DSP) VAL2('PROVA ERRATA') COMP(EQ)
     C                   SELECT
  019C     1             WHENEQ    1
     C     1             ANDEQ     2
  019C                   EVAL      AAA015='PROVA'                               COSTANTE
     C                   OTHER
  019C                   EVAL      AAA015='PROVA ERRATA'                        COSTANTE
     C                   ENDSL
     C                   EVAL      DSP=AAA015
     C     DSP           DSPLY     
    MU* VAL1(DSP) VAL2('PROVA') COMP(EQ)
     C                   SELECT
  019C     1             WHENNE    2
     C     1             ANDNE     3
  019C                   EVAL      AAA015='PROVA'                               COSTANTE
     C                   OTHER
  019C                   EVAL      AAA015='PROVA ERRATA'                        COSTANTE
     C                   ENDSL
     C                   EVAL      DSP=AAA015
     C     DSP           DSPLY     
    MU* VAL1(DSP) VAL2('PROVA') COMP(EQ)
     C                   SELECT
  019C     1             WHENNE    2
     C     1             ANDEQ     1
  019C                   EVAL      AAA015='PROVA'                               COSTANTE
     C                   OTHER
  019C                   EVAL      AAA015='PROVA ERRATA'                        COSTANTE
     C                   ENDSL
     C                   EVAL      DSP=AAA015
     C     DSP           DSPLY     
    MU* VAL1(DSP) VAL2('PROVA') COMP(EQ)
     C                   SELECT
  019C     1             WHENEQ    1
     C     1             ANDNE     2
  019C                   EVAL      AAA015='PROVA'                               COSTANTE
     C                   OTHER
  019C                   EVAL      AAA015='PROVA ERRATA'                        COSTANTE
     C                   ENDSL
     C                   EVAL      DSP=AAA015
     C     DSP           DSPLY     
    MU* VAL1(DSP) VAL2('PROVA ERRATA') COMP(EQ)
     C                   SELECT
  019C     1             WHENGT    1
     C     2             ANDLT     2
  019C                   EVAL      AAA015='PROVA'                               COSTANTE
     C                   OTHER
  019C                   EVAL      AAA015='PROVA ERRATA'                        COSTANTE
     C                   ENDSL
     C                   EVAL      DSP=AAA015
     C     DSP           DSPLY     
    MU* VAL1(DSP) VAL2('PROVA ERRATA') COMP(EQ)
     C                   SELECT
  019C     1             WHENEQ    2
     C     2             ORNE      2
  019C                   EVAL      AAA015='PROVA'                               COSTANTE
     C                   OTHER
  019C                   EVAL      AAA015='PROVA ERRATA'                        COSTANTE
     C                   ENDSL
     C                   EVAL      DSP=AAA015
     C     DSP           DSPLY     
     C                   SETON                                        LR