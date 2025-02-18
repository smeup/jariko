      * TODO
      * ------------------------------------------------------------------
     FST02      IF   E           K DISK    RENAME(ST02:ST02R)
     DC5RREG         E DS                  EXTNAME(ST02D) INZ

     C     KRREH5        KLIST
     C                   KFLD                    ST02F1
     C                   KFLD                    ST02F2
     C                   KFLD                    ST02F3
     C                   KFLD                    ST02F4
     C                   KFLD                    ST02F5

     C                   EVAL      ST02F1='01'
     C                   EVAL      ST02F2=''
     C                   EVAL      ST02F3=''

     C     ST02F1        DSPLY
     C     ST02F2        DSPLY
     C     ST02F3        DSPLY
     C     ST02F4        DSPLY
     C     ST02F5        DSPLY

     C     KRREH5        SETGT     ST02
     C                   READ      ST02

     C     ST02F1        DSPLY
     C     ST02F2        DSPLY
     C     ST02F3        DSPLY
     C     ST02F4        DSPLY
     C     ST02F5        DSPLY

     C                   SETON                                          LR