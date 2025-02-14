     V* ==============================================================
     V* 13/02/2025 APU001 Creation
     V* 14/02/2025 APU001 Simplification
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program defines a `DS` with its name, and its field name,
    O *  like those already declared from file.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O *     `S$INTE has been defined twice`
     V* ==============================================================
     FB£G11G0V  IF   E           K DISK

     D S$INTE          DS            22                                         # S$INTE has been defined twice
     D  S4INTE                        4    OVERLAY(S$INTE:1)

     C     'FOO'         DSPLY
     C                   SETON                                        RT