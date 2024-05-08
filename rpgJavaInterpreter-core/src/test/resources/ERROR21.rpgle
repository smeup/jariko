     V* ==============================================================
     D* Subroutine £MYSUB not resolved test when EXSR is inside
     D* another subroutine
     D* Jariko must fire two error events:
     D*  - data reference not resolved £MYVAR at line 9
     D*  - subroutine call not resolved £MYSUB at line 10
     V* ==============================================================
     C     SUB           BEGSR
     C                   MOVEL(P)  'D1'          £MYVAR1
     C                   EXSR      £MYSUB
     C                   ENDSR