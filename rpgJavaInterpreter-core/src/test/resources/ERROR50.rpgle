     V* ==============================================================
     D* 16/01/25
     D* Purpose: Must fire the following errors during execution of
     D*  `C                   EVAL      DS1_F2='FOO'`:
     D* line 19 - "Data reference not resolved: DS2_F2 at:
     D*  Position(start=Line 19, Column 35, end=Line 19, Column 41)"
     V* ==============================================================
     D DS1             DS
     D  DS1_F1                        3
     D  DS1_F2                        3
     D DS2             DS                  QUALIFIED
     D  DS2_F1                        3
     D  DS2_F2                        3

     C                   EVAL      DS1.DS1_F1='FOO'
     C                   EVAL      DS1_F2='BAR'

     C                   EVAL      DS2.DS2_F1='FOO'
     C                   EVAL      DS2_F2='BAR'

     C                   SETON                                          LR