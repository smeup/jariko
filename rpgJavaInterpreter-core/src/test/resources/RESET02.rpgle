     V* ==============================================================
     V* Purpose: RESET of data structure subfields.
     V* Checks that RESET restores a subfield (also an array one, with
     V* OVERLAY subfields) to its INZ value, or to blank/zero when it
     V* has none, leaving the rest of the data structure untouched.
     V* Expected output: ARR_OK, F1_OK, F2_OK, F3_OK
     V* ==============================================================

     D MSGOK           S             50          VARYING
     D MSGKO           S             50          VARYING

     D                 DS
     D ARR                            6    DIM(3)
     D  SUB1                          3    OVERLAY(ARR:01) INZ('XXX')
     D  SUB2                          3    OVERLAY(ARR:*NEXT)

     D DS              DS
     D  F1                            5    INZ('INIT')
     D  F2                            5  0 INZ(7)
     D  F3                            5

     C                   EVAL      ARR(1) = 'AAABBB'
     C                   EVAL      ARR(2) = 'CCCDDD'
     C                   EVAL      F1 = 'CHG'
     C                   EVAL      F2 = 99
     C                   EVAL      F3 = 'ZZZ'

     C                   RESET                   ARR
     C                   RESET                   F1
     C                   RESET                   F2
     C                   RESET                   F3

     C                   EVAL      MSGOK='ARR_OK'
     C                   EVAL      MSGKO='ARR_KO'
     C     ARR(1)        IFEQ      *BLANKS
     C     ARR(2)        ANDEQ     *BLANKS
     C     MSGOK         DSPLY
     C                   ELSE
     C     MSGKO         DSPLY
     C                   ENDIF

     C                   EVAL      MSGOK='F1_OK'
     C                   EVAL      MSGKO='F1_KO'
     C     F1            IFEQ      'INIT'
     C     MSGOK         DSPLY
     C                   ELSE
     C     MSGKO         DSPLY
     C                   ENDIF

     C                   EVAL      MSGOK='F2_OK'
     C                   EVAL      MSGKO='F2_KO'
     C     F2            IFEQ      7
     C     MSGOK         DSPLY
     C                   ELSE
     C     MSGKO         DSPLY
     C                   ENDIF

     C                   EVAL      MSGOK='F3_OK'
     C                   EVAL      MSGKO='F3_KO'
     C     F3            IFEQ      *BLANKS
     C     MSGOK         DSPLY
     C                   ELSE
     C     MSGKO         DSPLY
     C                   ENDIF

     C                   SETON                                        LR
