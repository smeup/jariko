     D Msg             S             10
     D AAA             S              3    INZ('AAA')
     D ZZZ             S              3    INZ('ZZZ')
     **********************************************************************
     C                   EVAL      Msg = 'Test OK'
     C     ZZZ           CABEQ     AAA           SHOW
     C                   EVAL      Msg = 'Test KO'
     **********************************************************************
     C     Msg           DSPLY
     C                   GOTO      fine
     **********************************************************************
     C     SHOW          TAG
     C     Msg           DSPLY
     C     Fine          Tag
     C                   SETON                                          LR
