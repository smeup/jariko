     DMsg              S              3
     DP1               DS
     D Name                           5A
     D Surname                        5A
     D Nbr                            3  0
     C     *ENTRY        PLIST
     C                   PARM                    P1
     C     Name          DSPLY
     C     Surname       DSPLY
     C                   Eval      Msg = %editc(Nbr:'X')
     C     Msg           DSPLY
     C                   SETON                                        LR
