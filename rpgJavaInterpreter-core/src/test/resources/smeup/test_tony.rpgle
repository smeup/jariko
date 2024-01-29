     FMULANGTF  IF   E             DISK
     D A10_DS_P04    E DS                  EXTNAME(MULANGTF) INZ PREFIX(P4:2)
      *
     D MYDS            DS
     D MLPROG
     D MLPSEZ
      *
     D £DBG_Str        S             50
      *
     C                   READ      MULANGTF                               50
      *
     C     *IN50         IFEQ      *OFF
     C                   EVAL      £DBG_Str=
     C                              ' MYDS('+MYDS+')'
     C                             +' MLPROG('+MLPROG+')'
     C                             +' MLPSEZ('+MLPSEZ+')'
     C                             +' P4PROG('+P4PROG+')'
     C     £DBG_Str      DSPLY
     C                   ENDIF
      *
     C                   SETON                                        LR