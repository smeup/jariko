     FAUTOOG1L  IF   E           K DISK
     D PNCLAS          S                   DIM(1000) LIKE(AO£CLA)
     D DSCLAS          DS                  OCCURS(%ELEM(PNCLAS))
     D  XC£GRA                             LIKE(AO£GRA)
      *
     C                   EVAL      AO£GRA = 'HELLO THERE'
     C                   EXSR      MEMREC
      *
     C     MEMREC        BEGSR
     C                   EVAL      XC£GRA=AO£GRA
     C     XC£GRA        DSPLY
     C                   ENDSR