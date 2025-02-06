     V* ==============================================================
     V* 06/02/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * MOVEA from S, defined as array, to DS.
    O * The array size is greater than DS.
     V* ==============================================================
     D SCAATTDS        DS            10
     D SCAATTSTR       S             12
     D SCAATT          S              1    DIM(12) INZ('A')
     D I               S              2  0

     C                   EVAL      SCAATTDS='0123456789'
     C                   EXSR      SHOW

     C                   MOVEA     SCAATT        SCAATTDS
     C                   EXSR      SHOW

     C                   SETON                                          LR



     C     SHOW          BEGSR

     C     SCAATTDS      DSPLY
     C                   CLEAR                   SCAATTSTR
     C                   FOR       I = 1 TO 12
     C                   EVAL      SCAATTSTR=%TRIM(SCAATTSTR)+
     C                                       %CHAR(SCAATT(I))
     C                   ENDFOR
     C     SCAATTSTR     DSPLY
     C                   ENDSR