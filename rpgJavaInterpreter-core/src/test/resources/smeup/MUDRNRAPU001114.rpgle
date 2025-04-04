     V* ==============================================================
     V* 04/04/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * TODO
     V* ==============================================================
     D DS1             DS
     D  DS1_F1                        5  2 DIM(3) INZ
     D INX             S              1  0
     D RES             S             10
     D PGM             S             17    INZ('MUDRNRAPU001114_P')

     C                   FOR       INX=1 TO 3
     C                   EVAL      DS1_F1(INX)=INX + 0.5
     C                   EVAL      RES=%CHAR(DS1_F1(INX))
     C     RES           DSPLY
     C                   ENDFOR

     C                   CALL      PGM
     C                   PARM                    DS1

     C                   SETON                                          LR
