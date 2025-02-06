     V* ==============================================================
     V* 11/12/2024 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * LOOKUP followed by %FOUND
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, jariko did not consider LOOKUP accountable for %FOUND
     V* ==============================================================
     D RESULT          S              1 0
     D FND             S               N
     D ARY             S              3    DIM(10)
     C                   EVAL      ARY(1)='ABC'
     C* Lookup opcode found
     C     'ABC'         LOOKUP    ARY                                    26
     C                   EVAL      FND=%FOUND
     C     FND           DSPLY
     C* Lookup opcode not found
     C     'FOO'         LOOKUP    ARY                                    26
     C                   EVAL      FND=%FOUND
     C     FND           DSPLY

     C                   SETON                                        RT