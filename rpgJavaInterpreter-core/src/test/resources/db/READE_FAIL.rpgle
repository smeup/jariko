     FVERAPG9L  IF   E           K DISK
      *--------------------------------------------------------------------------------------------*
     D $$DATA          S              8  0                                        Data
     D $$CDC           S             15                                           Commessa
      *--------------------------------------------------------------------------------------------*
     C                   EVAL      $$DATA=*LOVAL
     C                   EVAL      $$CDC='XXXXXXXXXXXXXXX'
     C     K9L           KLIST
     C                   KFLD                    $$DATA
     C                   KFLD                    $$CDC
      *...............................................................HiLoEq
     C     K9L           SETLL     VERAPG9L                           505152
     C     K9L           READE     VERAPG9L                             6162
      *
      * 50 must be *OFF
      * 51 must be *OFF
      * 52 must be *OFF
      * 61 must be *OFF
      * 62 must be *ON
      *
     C                   SETON                                        LR
      *--------------------------------------------------------------------------------------------*