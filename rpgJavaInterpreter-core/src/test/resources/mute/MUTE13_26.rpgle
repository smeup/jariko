     D R               S             10  3                                       Result
     D V01             S             10  3                                       Value01
     D V02             S             10  3                                       Value02
     D V03             S             10  3                                       Value03
     D V04             S             10  3                                       Value04
     D V05             S             10  3                                       Value05
     D V06             S             10  3                                       Value06
     D V07             S             10  3                                       Value07
     D V08             S             10  3                                       Value08
     D V09             S             10  3                                       Value09
     D V10             S             10  3                                       Value10
     D V11             S             10  3                                       Value11
     D V12             S             10  3                                       Value12
     D V13             S             10  3                                       Value13
     D V14             S             10  3                                       Value14
     D V15             S             10  3                                       Value15
      *
     C                   EVAL      V01 = 10
     C                   EVAL      V02 = 2
    MU* VAL1(R) VAL2(-12.000) COMP(EQ)
     C                   EVAL      R = - V01 - V02
      *
     C                   EVAL      V01 = 10
     C                   EVAL      V02 = 2
     C                   EVAL      V03 = 4
     C                   EVAL      V04 = 3
     C                   EVAL      V05 = 4
    MU* VAL1(R) VAL2(3.000) COMP(EQ)
     C                   EVAL      R = V01 - V02 - V03 + V04 - V05
      *
     C                   EVAL      V01 = 10
     C                   EVAL      V02 = 2
     C                   EVAL      V03 = 4
     C                   EVAL      V04 = 3
     C                   EVAL      V05 = 4
    MU* VAL1(R) VAL2(-17.000) COMP(EQ)
     C                   EVAL      R = - V01 - V02 - V03 + V04 - V05
      *
     C                   EVAL      V01 = 10
     C                   EVAL      V02 = 2
     C                   EVAL      V03 = 4
     C                   EVAL      V04 = 3
     C                   EVAL      V05 = 4
    MU* VAL1(R) VAL2(3.750) COMP(EQ)
     C                   EVAL      R = V01 * V02 / V03 * V04 / V05
      *
     C                   EVAL      V01 = 10
     C                   EVAL      V02 = 2
    MU* VAL1(R) VAL2(5.000) COMP(EQ)
     C                   EVAL      R = V01 / V02
      *
     C                   EVAL      V01 = 8
     C                   EVAL      V02 = 4
    MU* VAL1(R) VAL2(-2.000) COMP(EQ)
     C                   EVAL      R = V01 / -V02
      *
     C                   EVAL      V01 = 10
     C                   EVAL      V02 = 2
     C                   EVAL      V03 = 4
     C                   EVAL      V04 = 3
     C                   EVAL      V05 = 4
    MU* VAL1(R) VAL2(21.500) COMP(EQ)
     C                   EVAL      R = V01 -V02 / V03 + V04 * V05
      *
     C                   EVAL      V01 = 10
     C                   EVAL      V02 = 32
     C                   EVAL      V03 = 4
     C                   EVAL      V04 = 3
     C                   EVAL      V05 = 4
    MU* VAL1(R) VAL2(14.000) COMP(EQ)
     C                   EVAL      R = V01 -V02 / V03 + V04 * V05
      *
     C                   EVAL      V01 = 10
     C                   EVAL      V02 = 2
     C                   EVAL      V03 = 1
     C                   EVAL      V04 = 3
     C                   EVAL      V05 = 4
    MU* VAL1(R) VAL2(9.750) COMP(EQ)
     C                   EVAL      R = V01 - V02 + V03 + V04 / V05
      *
     C                   EVAL      V01 = 10
     C                   EVAL      V02 = 2
     C                   EVAL      V03 = 1
     C                   EVAL      V04 = 3
     C                   EVAL      V05 = 4
    MU* VAL1(R) VAL2(8.750) COMP(EQ)
     C                   EVAL      R = V01 -V02 * V03 + V04 / V05
      *
     C                   EVAL      V01 = 10
     C                   EVAL      V02 = 2
     C                   EVAL      V03 = 1
     C                   EVAL      V04 = 3
     C                   EVAL      V05 = 4
    MU* VAL1(R) VAL2(7.000) COMP(EQ)
     C                   EVAL      R = V01 - V02 + V03 * V04 - V05
      *
     C                   EVAL      V01 = 10
     C                   EVAL      V02 = 2
     C                   EVAL      V03 = 1
     C                   EVAL      V04 = 3
     C                   EVAL      V05 = 4
    MU* VAL1(R) VAL2(-3.000) COMP(EQ)
     C                   EVAL      R = V01 -V02 + V03 - V04 * V05
      *
     C                   EVAL      V01 = 10
     C                   EVAL      V02 = 2
     C                   EVAL      V03 = 1
     C                   EVAL      V04 = 3
     C                   EVAL      V05 = 4
    MU* VAL1(R) VAL2(19.250) COMP(EQ)
     C                   EVAL      R = V01 * V02 / V03 - V04 / V05
      *
     C                   EVAL      V01 = 10
     C                   EVAL      V02 = 2
     C                   EVAL      V03 = 1
     C                   EVAL      V04 = 3
     C                   EVAL      V05 = 4
     C                   EVAL      V06 = 2
    MU* VAL1(R) VAL2(7.500) COMP(EQ)
     C                   EVAL      R = V01 * -V02 / -V03 *
     C                                  - V04 / V05 / -V06
      *
     C                   EVAL      V01 = 10
     C                   EVAL      V02 = 2.3
     C                   EVAL      V03 = 1
     C                   EVAL      V04 = 3.4
     C                   EVAL      V05 = 0.4
     C                   EVAL      V06 = 1
     C                   EVAL      V07 = 3
     C                   EVAL      V08 = 2
     C                   EVAL      V09 = 5.2
     C                   EVAL      V10 = 9
     C                   EVAL      V11 = 0.1
     C                   EVAL      V12 = 9.3
     C                   EVAL      V13 = 5.1
     C                   EVAL      V14 = 0.89
     C                   EVAL      V15 = 2
    MU* VAL1(R) VAL2(-200.378) COMP(EQ)
     C                   EVAL      R = V01 * -V02 / -V03 * -V04 / +
     C                                 V05 -V06 * -V07 / -V08 * V09 -
     C                                 V10 * -V11 * V12 / -V13 *
     C                                 -V14 * V15
      *
     C                   SETON                                        LR