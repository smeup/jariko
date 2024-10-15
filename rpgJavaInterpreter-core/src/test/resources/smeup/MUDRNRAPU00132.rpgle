     V* ==============================================================
     V* 14/10/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Assignment of integer value to a DS decimal subfield.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was
    O *  `An operation is not implemented: Converting DecimalValue
    O *   to StringType(length=21, varying=false)`.
     V* ==============================================================
     D INX             S              3  0 INZ(5)
     D RES             S             10

     D                 DS
     DDS_FL1                        300    DIM(10)
     D DS_FL1SUB1                    21  6 OVERLAY(DS_FL1:1)

     C                   EVAL      DS_FL1SUB1(INX)=10                           #An operation is not implemented: Converting DecimalValue to StringType(length=21, varying=false)
     C                   EVAL      RES=%CHAR(DS_FL1SUB1(INX))
     C     RES           DSPLY

     C                   SETON                                          LR
