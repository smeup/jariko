     D A40_A100        S            100    DIM(300)
     D A40_DS          DS
     D  A40_DS_F1                    10
     D  A40_DS_F2                    29
     D  A40_DS_F3                    21
     D  A40_DS_F4                    01
     D  A40_DS_F5                    04
     D  A40_DS_F6                    01
     D  A40_DS_F7                    01
     D  A40_DS_F8                    10
     D  A40_DS_F9                    16
     D  A40_DS_F10                   02
     D  A40_DS_F11                   05
     D  A40_DS_F12                  100
     D  A40_DS_F13                  300
     D A40_A5          S              5  0

     D OUTPUT          S            100

     C                   EVAL      A40_DS_F1='Lorem ipsu'
     C                   EVAL      A40_DS_F2='m dolor sit amet, consectetue'
     C                   EVAL      A40_DS_F3='r adipiscing elit. Ae'
     C                   EVAL      A40_DS_F4='e'
     C                   EVAL      A40_DS_F5='an c'
     C                   EVAL      A40_DS_F6='o'
     C                   EVAL      A40_DS_F7='m'
     C                   EVAL      A40_DS_F8='modo ligul'
     C                   EVAL      A40_DS_F9='a eget dolor. Ae'
     C                   EVAL      A40_DS_F10='ne'
     C                   EVAL      A40_DS_F11='an ma'
     C                   EVAL      A40_DS_F12='ssa. Cum sociis natoque penatib'
     c                                            + 'us et magnis dis '
     C                                            + 'partu rient montes, nasce'
     C                                            + 'tur ridiculus mus.'
     C                                            + ' Donec qua'
     C                   EVAL      A40_DS_F13='m felis, ultricies nec, pellen'
     C                                            + 'tesque eu, pretium'
     C                                            + ' quis, sem. Nulla conse'
     C                                            + 'quat massa quis '
     C                                            + 'enim. Donec pede justo, '
     C                                            + 'fringilla vel, '
     C                                            + 'aliquet nec, vulputate'
     C                                            + ' eget, arcu. '
     C                                            + 'In enim justo, rhoncus '
     C                                            + 'ut, imperdiet a, '
     C                                            + 'venenatis vitae, justo. '
     C                                            + 'Nullam dictum '
     C                                            + 'felis eu pede mollis '
     C                                            + 'pretium. Integer '
     C                                            + 'tincidunt. Cras dapibus'

     C                   EVAL      A40_A5=1
     C                   EVAL      A40_A100(A40_A5)=A40_DS
     C                   EVAL      OUTPUT=A40_A100(A40_A5)
     C     OUTPUT        DSPLY