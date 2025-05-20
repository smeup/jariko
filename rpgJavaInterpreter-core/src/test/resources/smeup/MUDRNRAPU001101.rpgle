     V* ==============================================================
     V* 22/08/2024 APU001 Creation
     V* 06/02/2025 API001 Changed name
     V* 06/02/2025 API001 Improved description
     V* ==============================================================
    O * PROGRAM GOAL
    O * Strict inclusions, by `API` directive, of a DS already
    O *  from caller program.
    O * This one declares `£UDLDA`, by using `/COPY QILEGEN,£PDS`,
    O *  and appends to it other fields by using
    O *  `/COPY QILEGEN,£C5PDS`.
    O * `MUDRNRAPU001101_API`, imported by `API` directive,
    O *  imports only `£UDLDA`, without any addition.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was
    O *  `Incongruous definitions of £UDLDA`.
     V* ==============================================================
      /COPY QILEGEN,£PDS
      /COPY QILEGEN,£C5PDS

      /API QILEGEN,MUDRNRAPU001101_API                                                Jariko Runtime Error: `Incongruous definitions of £UDLDA`

     C                   EVAL      £UDO2F = 'FOO'
     C                   EVAL      U$FUW2 = 'BAR'
     C     £UDO2F        DSPLY
     C     U$FUW2        DSPLY

     C                   SETON                                          LR