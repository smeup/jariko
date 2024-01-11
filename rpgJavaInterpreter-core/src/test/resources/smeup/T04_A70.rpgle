      **************************************************************************
     D DBG_Str         S           2560
      *
     D A70_A1          S           1000A   VARYING
     D A70_I7          S             10I 0 INZ(654320)
     D A70_I8          S             10I 0 INZ(40)
     D A70_P7          S              7P 2 INZ(500.90)
     D A70_P8          S              7P 2 INZ( 87.14)
     D A70_S7          S              9S 3 INZ(000440.40)
     D A70_S8          S              9S 3 INZ(32.20)
     D A70_U7          S              5U 0 INZ(320)
     D A70_U8          S              5U 0 INZ(080)
      **************************************************************************
      * %CHAR whit numeric operations as parameter
     C                   CLEAR                   A70_A1
     C                   EVAL      A70_A1 =
     C                              'I_SUM('+%CHAR(A70_I7+A70_I8)+') '
     C                             +'I_DIF('+%CHAR(A70_I7-A70_I8)+') '
     C                             +'I_MUL('+%CHAR(A70_I7*A70_I8)+') '
     C                             +'I_DIV('+%CHAR(A70_I7/A70_I8)+') '
     C                             +'P_SUM('+%CHAR(A70_P7+A70_P8)+') '
     C                             +'P_DIF('+%CHAR(A70_P7-A70_P8)+') '
     C                             +'P_MUL('+%CHAR(A70_P7*A70_P8)+') '
     C                             +'P_DIV('+%CHAR(A70_P7/A70_P8)+') '
     C                             +'S_SUM('+%CHAR(A70_S7+A70_S8)+') '
     C                             +'S_DIF('+%CHAR(A70_S7-A70_S8)+') '
     C                             +'S_MUL('+%CHAR(A70_S7*A70_S8)+') '
     C                             +'S_DIV('+%CHAR(A70_S7/A70_S8)+') '
     C                             +'U_SUM('+%CHAR(A70_U7+A70_U8)+') '
     C                             +'U_DIF('+%CHAR(A70_U7-A70_U8)+') '
     C                             +'U_MUL('+%CHAR(A70_U7*A70_U8)+') '
     C                             +'U_DIV('+%CHAR(A70_U7/A70_U8)+') '
     C                   EVAL      DBG_Str=A70_A1
     C     DBG_Str       DSPLY
     C                   SETON                                        LR