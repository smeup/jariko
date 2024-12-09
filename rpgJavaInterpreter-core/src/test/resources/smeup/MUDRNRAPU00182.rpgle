     V* ==============================================================
     V* 09/12/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Assign a new value to a DS preserving the original DS size.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * DS1 value size changes to a size of a new value during assignment.
    O * This causes:
    O * `Issue arose while setting field DS1_FLD_2. Indexes: 3 to 4.
    O *   Field size: 1. Value: StringValue[1](X)`
    O * on a legal assignment.
     V* ==============================================================
     D DS1             DS
     D  DS1_FLD_1                     3
     D  DS1_FLD_2                     1
     D  DS1_FLD_3                     2

     C     DS1           DSPLY
     C                   EVAL      DS1='FOO'
     C     DS1           DSPLY
     C                   EVAL      DS1_FLD_1='BAR'
     C     DS1           DSPLY
     C                   EVAL      DS1_FLD_2='X'                                #Issue arose while setting field DS1_FLD_2. Indexes: 3 to 4. Field size: 1. Value: StringValue[1](X)
     C     DS1           DSPLY
     C                   SETON                                        RT