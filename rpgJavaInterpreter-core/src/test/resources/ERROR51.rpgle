     V* ==============================================================
     D* 05/02/25
     D* Purpose: Must fire the following errors during execution of
     D*  `C                   MOVEA     SCAATTDS      SCAATT`:
     D*     line 19 - "Factor 2 and Result aren't same type"
     D* because isn't possible to assign DS to a Standalone defined
     D* as number
     V* ==============================================================
     D SCAATTDS        DS            10
     D SCAATT          S              1  0 DIM(10)

     C                   EVAL      SCAATTDS='0123456789'
     C                   MOVEA     SCAATTDS      SCAATT

     C                   SETON                                          LR