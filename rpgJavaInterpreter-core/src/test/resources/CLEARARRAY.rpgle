      *-----------------------------------------------------------
      * Simple execution of CLEAR statement on ARRAY
      *-----------------------------------------------------------
     DMSG              S             52
      *-----------------------------------------------------------
     D                 DS
     D $DS                          100    DIM(200) ASCEND
     D  $A                            5S 0 OVERLAY($DS:1)
     D  $B                            2S 1 OVERLAY($DS:*NEXT)
     D  $C                            4S 2 OVERLAY($DS:*NEXT)
     D  $D                            1    OVERLAY($DS:*NEXT)
     D  $E                           10    OVERLAY($DS:*NEXT)
      *-----------------------------------------------------------
      * Main
      *-----------------------------------------------------------
     C                   EVAL      $A(1) = 11111
     C                   EVAL      $B(1) = 1.1
     C                   EVAL      $C(1) = 11.11
     C                   EVAL      $D(1) = 'A'
     C                   EVAL      $E(1) = 'AAAAAAAAAA'
     C                   EXSR      $DSP
      *
     C                   EVAL      $A(2) = 22222
     C                   EVAL      $B(2) = 2.2
     C                   EVAL      $C(2) = 22.22
     C                   EVAL      $D(2) = 'B'
     C                   EVAL      $E(2) = 'BBBBBBBBBB'
     C                   EXSR      $DSP
     C                   CLEAR                   $DS(1)
     C                   EXSR      $DSP
      *
     C                   EVAL      $A(3) = 0
     C                   EVAL      $B(3) = 0
     C                   EVAL      $C(3) = 0
     C                   EVAL      $D(3) = ''
     C                   EVAL      $E(3) = ''
     C                   EXSR      $DSP
      *
     C                   EVAL      $A(4) = 44444
     C                   EVAL      $B(4) = 4.4
     C                   EVAL      $C(4) = 44.44
     C                   EVAL      $D(4) = 'D'
     C                   EVAL      $E(4) = 'DDDDDDDDDD'
     C                   EXSR      $DSP
     C                   CLEAR                   $DS
     C                   EXSR      $DSP
      *
     C                   SETON                                          LR
      *-----------------------------------------------------------
      * Print results
      *-----------------------------------------------------------
     C     $DSP          BEGSR
      *
     C                   EVAL      MSG=$DS(1)
     C     MSG           DSPLY
     C                   EVAL      MSG=$DS(2)
     C     MSG           DSPLY
     C                   EVAL      MSG=$DS(3)
     C     MSG           DSPLY
     C                   EVAL      MSG=$DS(4)
     C     MSG           DSPLY
      *
     C                   ENDSR
      *-----------------------------------------------------------