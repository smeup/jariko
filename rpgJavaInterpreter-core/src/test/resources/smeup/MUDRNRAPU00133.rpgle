     V* ==============================================================
     V* 17/10/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Using API which declares a standalone variable that is already
    O *  declared inline from MUDRNRAPU00133_API, imported
    O *  by API directive.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was
    O *  `Cannot find searched value for InStatementDataDefinition
    O *  name=FOO`.
     V* ==============================================================
     D FOO             S             10

     C                   EXSR      SUB_R
      /API QILEGEN,MUDRNRAPU00133_API                                           #Error in API: Cannot find searched value for InStatementDataDefinition name=FOO...