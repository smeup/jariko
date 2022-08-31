      * Factor 1 and 2 constant resolution
     D FLAG            S              1N
     D STR             S              1

     C                   EVAL      FLAG=*ON
     C     FLAG          CABEQ     *OFF          G1MAIN
     C     'FLAG=ON'     DSPLY
     C     G1MAIN        TAG

     C                   EVAL      FLAG=*OFF
     C     *ON           CABEQ     FLAG          G2MAIN
     C     'FLAG=OFF'    DSPLY
     C     G2MAIN        TAG

     C     *IN34         CABEQ     *ON           G3MAIN
     C     'IN34=OFF'    DSPLY
     C     G3MAIN        TAG

     C                   SETON                                          34
     C     *IN34         CABEQ     *ON           G4MAIN
     C     'IN34=OFF'    DSPLY
     C     G4MAIN        TAG
     C     'IN34=ON'     DSPLY

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

     C     *IN35         IFEQ      *OFF
     C     'IN35=OFF'    DSPLY
     C                   ENDIF

     C                   SETON                                          LR