     V* ==============================================================
     V* 17/10/2024 APU001 Creation
     V* ==============================================================
    O * This code is used for purposes of MUDRNRAPU00133 program.
     V* ==============================================================
     D MSG             S             10          INZ('OK')

     C     SUB_R         BEGSR
     C                   IF        FOO=''                                       #Cannot find searched value for InStatementDataDefinition name=FOO...
     C                   ENDIF
     C                   MOVEL     'FOO'           FOO
     C     'A'           IFEQ      *BLANKS
     C                   CALL      'MYPGM'
     C                   PARM                    FOO             10
     C                   ENDIF
     C     MSG           DSPLY
     C                   ENDSR