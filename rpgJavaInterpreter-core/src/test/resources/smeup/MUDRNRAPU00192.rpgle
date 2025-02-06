     V* ==============================================================
     V* 03/02/2025 APU001 Creation
     V* 04/02/2025 APU001 Applied improvements by showing results
     V* ==============================================================
    O * PROGRAM GOAL
    O * Writing on a field of DS which use `EXTNAME` of a file.
    O * In this case the file in `EXTNAME` is different from `F` spec
    O *  but shares same fields.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * The error raised is: `BlanksValue cannot be cast to
    O *  class DataStructValue`
     V* ==============================================================
     FC5RREGHL  IF   E           K DISK    RENAME(C5RREGR:C5RREGH)
     DC5RREG         E DS                  EXTNAME(C5RREG0F) INZ

     C     KRREH5        KLIST
     C                   KFLD                    R5AZIE
     C                   KFLD                    R5ESER
     C                   KFLD                    R5TPCN
     C                   KFLD                    R5SOGG
     C                   KFLD                    R5CONT

     C                   EVAL      R5AZIE='01'
     C                   EVAL      R5ESER='2009'
     C                   EVAL      R5TPCN=''

     C     R5AZIE        DSPLY
     C     R5ESER        DSPLY
     C     R5TPCN        DSPLY
     C     R5SOGG        DSPLY
     C     R5CONT        DSPLY

     C     KRREH5        SETGT     C5RREGHL                                     # BlanksValue cannot be cast to class DataStructValue
     C                   READ      C5RREGHL

     C     R5AZIE        DSPLY
     C     R5ESER        DSPLY
     C     R5TPCN        DSPLY
     C     R5SOGG        DSPLY
     C     R5CONT        DSPLY

     C                   SETON                                          LR