     D VALUE1          S             15A
     D VAR1            S             15A   INZ('AAAA')
    MU* VAL1(VAR1) VAL2(%TRIM(' AAAA ')) COMP(EQ)
     D AR              S              3  0 DIM(10)
     DB                S              8  0 INZ(1)
      *
     C                   CLEAR                   VALUE1
     C                   EVAL      VAR1   = '  AAA              ' + ':'
     C                   EVAL      VALUE1 = '  AAA              ' + ':'
    MU* VAL1(VALUE1) VAL2('AAA:') COMP(EQ)
     C                   EVAL      VALUE1 = %TRIM(VAR1) +':'
    MU* VAL1(VALUE1) VAL2('  AAA:') COMP(EQ)
     C                   EVAL      VALUE1 = %TRIM(VAR1) +':'
    MU* VAL1(%TRIMR(VAR1) +':') VAL2('  AAA:') COMP(EQ)
    MU* VAL1(VALUE1) VAL2('AAA                         :') COMP(NE)
     C                   EVAL      AR(1) = 5
     C                   SETON                                        LR
