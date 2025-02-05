     V* ==============================================================
     V* 05/02/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * MOVEA from DS to S defined as array.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * An operation is not implemented: takeFirst not yet implemented
    O *  for DataStructValue.
     V* ==============================================================
     D SCAATTDS        DS            10
     D SCAATTSTR       S             10
     D SCAATT          S              1    DIM(10)
     D I               S              2  0

     C                   EVAL      SCAATTDS='0123456789'
     C                   EXSR      SHOW

     C                   MOVEA     SCAATTDS      SCAATT                         #Issue executing MoveAStmt at line 4. An operation is not implemented: takeFirst not yet implemented for DataStructValue
     C                   EXSR      SHOW

     C                   SETON                                          LR



     C     SHOW          BEGSR

     C     SCAATTDS      DSPLY
     C                   CLEAR                   SCAATTSTR
     C                   FOR       I = 1 TO 10
     C                   EVAL      SCAATTSTR=%TRIM(SCAATTSTR)+
     C                                       %CHAR(SCAATT(I))
     C                   ENDFOR
     C     SCAATTSTR     DSPLY
     C                   ENDSR