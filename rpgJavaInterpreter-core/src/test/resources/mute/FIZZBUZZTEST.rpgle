     D x               S              5           
     D result          S              8  
     *************************************************************************
     C                   EVAL      result=''
     C                   EVAL      x='1'
    MU* VAL1('1') VAL2(result) COMP(EQ) 
     C                   call      'FIZZBUZZ'
     C                   parm                    x
     C                   parm                    result
     *************************************************************************
     C                   EVAL      result=''
     C                   EVAL      x='3'
    MU* VAL1('FIZZ') VAL2(result) COMP(EQ) 
     C                   call      'FIZZBUZZ'
     C                   parm                    x
     C                   parm                    result
     *************************************************************************
     C                   EVAL      result=''
     C                   EVAL      x='5'
    MU* VAL1('BUZZ') VAL2(result) COMP(EQ) 
     C                   call      'FIZZBUZZ'
     C                   parm                    x
     C                   parm                    result
     *************************************************************************
     C                   EVAL      result=''
     C                   EVAL      x='15'
    MU* VAL1('FIZZBUZZ') VAL2(result) COMP(EQ) 
     C                   call      'FIZZBUZZ'
     C                   parm                    x
     C                   parm                    result
     *************************************************************************
     C                   seton                                        lr