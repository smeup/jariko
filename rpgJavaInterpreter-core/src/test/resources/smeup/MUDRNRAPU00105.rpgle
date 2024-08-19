     V* ==============================================================
     V* 07/08/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Runtime evaluation of `£40ANM`, which uses
    O *  `%ELEM(£40A)` for its initialization, during the evaluation
    O *  of `£40A` `PLIST` parameter.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was `Void value for £40A`.
     V* ==============================================================
     D £40A            S             15    DIM(300)
     D £40B            S             12    DIM(300)

     D £40ANM          S              5S 0 INZ(%ELEM(£40A))                       Jariko Runtime Error: 'Void value for £40A' during evaluation of PLIST

     D £G40DS          DS           500
     D  £G40PZ                        1                                         O
     D  £G40EX                        1                                         O
     D  £G40FM                        1                                         O
     D  £G40AE                        1                                         I
     D  £G40FL                        1                                         I
     D  £G40NS                        1                                         I
     D  £G40NE                        9S 0                                      O
     D  £G40SC                       15                                         O
     D  £G40FO                       15                                         O
     D  £G40T2                        2                                         I
     D  £G40P2                       10                                         I
     D  £G40C2                       15                                         I
     D  £G40UT                       10                                         O
     D  £G40AN                        1                                         O

     C     *ENTRY        PLIST
     C                   PARM                    £40A
     C                   PARM                    £G40DS
