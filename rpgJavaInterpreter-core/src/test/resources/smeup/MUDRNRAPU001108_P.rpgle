     V* ==============================================================
     V* 01/04/2025 APU001 Creation
     V* 02/04/2025 APU001 Edited size for `DS1_F1`
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program is used by 'MUDRNRAPU001108' for its purpose.
     V* ==============================================================
     D DS1             DS
     D  DS1_F1                        5  2 DIM(3) INZ
     D VAR_PARM        S                   LIKE(DS1)
     D INX             S              1  0
     D RES             S              6

     C     VAR_PARM      DSPLY
     C                   MOVE      VAR_PARM      DS1
     C                   FOR       INX=1 TO 3
     C                   EVAL      RES=%CHAR(DS1_F1(INX))
     C     RES           DSPLY
     C                   ENDFOR
     C                   SETON                                          LR

     C     *INZSR        BEGSR
     C     *ENTRY        PLIST
     C                   PARM                    VAR_PARM
     C                   ENDSR
