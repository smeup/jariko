     V* ==============================================================
     D* 20/11/24  nn.mm author
     V* ==============================================================

     D END             S             10  0 INZ(10000000)
     D $1              S             10  0 INZ(1)



     C      1            DO        END           COUNTER          10 0
     C     100000        IFEQ      $1
     C                   EVAL      $1=0
     C     COUNTER       DSPLY
     C                   ENDIF
     C                   EVAL      $1=$1+1
     C                   ENDDO
