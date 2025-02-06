     V* ==============================================================
     D* 06/02/25
     D* Purpose: Must fire the following errors during execution of
     D*  `C                   MOVEA     SCAATT        SCAATTDS`:
     D*     line 19 - "Factor 2 and Result aren't same type"
     D* because isn't possible to assign Standalone defined
     D* as number to a DS.
     V* ==============================================================
     D SCAATTDS        DS            10
     D SCAATT          S              1  0 DIM(10) INZ('A')

     C                   EVAL      SCAATTDS='0123456789'
     C                   MOVEA     SCAATT        SCAATTDS

     C                   SETON                                          LR