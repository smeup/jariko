     V* ==============================================================
     V* 31/10/2024 APU001 Creation
     V* 06/11/2024 APU001 Simplification
     V* ==============================================================
    O * PROGRAM GOAL
    O * DS field declared as Array and CTDATA. In this case there
    O *   is only CTDATA.
     V* ==============================================================
     D                 DS
     DIQ5                            37    DIM(3) CTDATA PERRCD(1)

     C     IQ5(1)        DSPLY
     C                   SETON                                          LR

** CTDATA
*SCPAccesso da script             00S
*IQ2Accesso da Pgm £IQ2           18Q
*SETAccesso da SCP_NAV            17T