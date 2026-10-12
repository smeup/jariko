     Da1               S              8    inz('31231   ')
     Dn1               S              8  0
     Da2               S              4    inz('12 4')
     Dn2               S              4  0
     Da3               S              6    inz('   123')
     Dn3               S              6  0
     Da4               S              4    inz('123 ')
     Dn4               S              6  2
     Dmsg              S             50
     C                   MOVEL(P)  a1            n1
     C                   Eval      msg = %Char(n1)
     C     msg           dsply
     C                   MOVEL     a2            n2
     C                   Eval      msg = %Char(n2)
     C     msg           dsply
     C                   MOVE      a3            n3
     C                   Eval      msg = %Char(n3)
     C     msg           dsply
     C                   MOVEL     a4            n4
     C                   Eval      msg = %Char(n4)
     C     msg           dsply
      ***********************************************
     C                   SETON                                          LR
