     V* ==============================================================
     V* 13/02/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program defines a `DS` with its name, and its field name,
    O *  like those already declared from file.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O *     `S$INTE has been defined twice`
     V* ==============================================================
     FB£G11G0V  CF   E             WORKSTN USROPN
     F                                     SFILE(SFL1:RRN01)
     F                                     SFILE(SFL3:RRN03)
     F                                     SFILE(SFL4:RRN04)
     F                                     INFDS(NUMSFL)

     D NUMSFL          DS
     D  £KEY                 369    369
     D  SAVRIG               378    379B 0
     D  POSFMT               370    371B 0
     D  POSWIN               382    383B 0

     D S$INTE          DS            22                                         # S$INTE has been defined twice
     D  S4INTE                        4    OVERLAY(S$INTE:1)

     C     'FOO'         DSPLY
     C                   SETON                                        RT