     V* ==============================================================
     V* 05/09/2024 APU011 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Verifies the copying of values between variables of type
    O * UnlimitedStringType using the MOVEL command and displays the results.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error encountered was:
    O * MOVE/MOVEL not supported for the type: UnlimitedStringType
     V* ==============================================================

     D £UIBDS          DS            10
     D £UIBME                          0   INZ('test')
     D XUIBME                          0   INZ('')

     DXUIBME           S                   LIKE(£UIBME)
     C                   MOVEL(P)  £UIBME        XUIBME
     C     XUIBME        DSPLY
     C     £UIBME        DSPLY