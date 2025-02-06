     V* ==============================================================
     V* 06/02/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * MOVEA from S, defined as array, to DS.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Error occurred: 'Result must be an Array or a String'.
     V* ==============================================================
     D SCAATTDS        DS            10
     D SCAATTSTR       S             10
     D SCAATT          S              1    DIM(10) INZ('A')
     D I               S              2  0

     C                   EVAL      SCAATTDS='0123456789'
     C                   EXSR      SHOW

     C                   MOVEA     SCAATT        SCAATTDS                         #Result must be an Array or a String
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