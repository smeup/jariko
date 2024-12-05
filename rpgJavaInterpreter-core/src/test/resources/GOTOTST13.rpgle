     V* ==============================================================
     V* 02/12/2024 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Execute a GOTO into an IF with an ELSE IF body
    O * with multiple nested CompositeStatement
    O * duplications are needed to shift each instruction index
     V* ==============================================================
     C                   GOTO      BGN
     C                   IF        *OFF
     C     'ko'          DSPLY
     C     'ko'          DSPLY
     C     'ko'          DSPLY
     C     'ko'          DSPLY
     C                   ELSEIF    *OFF
     C     BGN           TAG
     C     'ok'          DSPLY
     C                   ELSE
     C     'ko'          DSPLY
     C     'ko'          DSPLY
     C     'ko'          DSPLY
     C     'ko'          DSPLY
     C                   IF        *OFF
     C     'ko'          DSPLY
     C     'ko'          DSPLY
     C     'ko'          DSPLY
     C     'ko'          DSPLY
     C                   IF        *OFF
     C                   IF        *OFF
     C     'ko'          DSPLY
     C     'ko'          DSPLY
     C     'ko'          DSPLY
     C     'ko'          DSPLY
     C                   ENDIF
     C     'ko'          DSPLY
     C     'ko'          DSPLY
     C     'ko'          DSPLY
     C     'ko'          DSPLY
     C                   ENDIF
     C                   ENDIF
     C                   ENDIF
