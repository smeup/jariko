     V* ==============================================================
     V* 06/02/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * MOVEA from DS to S defined as array, with size lower than DS.
     V* ==============================================================
     D SCAATTDS        DS            10
     D SCAATTSTR       S              8
     D SCAATT          S              1    DIM(8)
     D I               S              1  0

     C                   EVAL      SCAATTDS='0123456789'
     C                   EXSR      SHOW

     C                   MOVEA     SCAATTDS      SCAATT
     C                   EXSR      SHOW

     C                   SETON                                          LR



     C     SHOW          BEGSR

     C     SCAATTDS      DSPLY
     C                   CLEAR                   SCAATTSTR
     C                   FOR       I = 1 TO 8
     C                   EVAL      SCAATTSTR=%TRIM(SCAATTSTR)+
     C                                       %CHAR(SCAATT(I))
     C                   ENDFOR
     C     SCAATTSTR     DSPLY
     C                   ENDSR