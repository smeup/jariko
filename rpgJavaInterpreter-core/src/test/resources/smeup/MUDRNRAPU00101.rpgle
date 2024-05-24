     FMULANGTL  IF   E           K DISK
     D PNCLAS          S                   DIM(1000) LIKE(MLLIBR)
     D DSCLAS          DS                  OCCURS(%ELEM(PNCLAS))
     D  XC£GRA                             LIKE(MLSYST)
      *
     C                   EVAL      MLSYST = 'HELLO THERE'
     C                   EXSR      MEMREC
      *
     C     MEMREC        BEGSR
     C                   EVAL      XC£GRA=MLSYST
     C     XC£GRA        DSPLY
     C                   ENDSR