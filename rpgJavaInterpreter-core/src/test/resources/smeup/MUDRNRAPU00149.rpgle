     V* ==============================================================
     V* 30/10/2024 APU001 Creation
     V* 31/10/2024 APU001 Edited PROGRAM GOAL
     V* 06/11/2024 APU001 Edited PROGRAM GOAL
     V* ==============================================================
    O * PROGRAM GOAL
    O * DS field declared as Array and CTDATA. In this case the error
    O *  is manifested as side effect of `%LOOKUP`.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was
    O *  `Failed requirement'
     V* ==============================================================
     D £IQ5D           DS          5000
     D  £IQ5DCD                      15

     D                 DS
     DIQ5                            42    DIM(3) CTDATA PERRCD(1)
     D IQ5CD                          4    OVERLAY(IQ5:1)
     D IQ5DE                         30    OVERLAY(IQ5:*NEXT)
     D IQ5PG                          2    OVERLAY(IQ5:*NEXT)

     D$X               S              5  0
     DNQ5              S              5  0 INZ(3)
     DA4               S              4

     C                   EVAL      A4='*SCP'
     C                   EVAL      $X=%LOOKUP(A4:IQ5CD:1:NQ5)
     C     $X            DSPLY
     C                   EVAL      £IQ5DCD=IQ5CD($X)                            #Failed requirement. Depends from %LOOKUP result
     C     £IQ5DCD       DSPLY

     C                   SETON                                          LR

**  Descrizione                   PgPref.D
*SCPAccesso da script             00S
*IQ2Accesso da Pgm £IQ2           18Q
*SETAccesso da SCP_NAV            17T