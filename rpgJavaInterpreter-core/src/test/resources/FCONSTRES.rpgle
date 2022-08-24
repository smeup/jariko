      * Factor 1 and 2 constant resolution
     D FLAG            S              1N
     D STR             S              1

     C                   EVAL      FLAG=*ON
     C     FLAG          CABEQ     *OFF          G1MAIN
     C     'FLAG=ON'     DSPLY
     C     G1MAIN        TAG
     C                   EVAL      FLAG=*OFF
      *Warning: *ON is not resolved but Jariko does not throw any error!!!
     C     *ON           CABEQ     FLAG          G2MAIN
     C     'FLAG=OFF'    DSPLY
     C     G2MAIN        TAG

     C                   EVAL      STR=*BLANK
     C     STR           IFEQ        *BLANK
     C     STR           ANDEQ       *BLANK
     C     'STR=BLANK'   DSPLY
     C                   ENDIF

     C                   EVAL      STR='A'
     C     *BLANK        IFNE       STR
     C     *BLANK        ANDNE      STR
     C     'STR=NOBLANK' DSPLY
     C                   ENDIF