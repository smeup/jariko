     D A10_DS_P01    E DS                  EXTNAME(MULANGTF) INZ
      *
     D A10_DS_P04    E DS                  EXTNAME(MULANGTF) INZ
     D                                     PREFIX(P4:2)
      *
     D £DBG_Str        S           2580
      *
     C                   EVAL      £DBG_Str='Lunghezza: '
     C                             +%CHAR(%LEN(A10_DS_P01))
     C                             +' Contenuto: '
     C                             +MLSYST
     C                             +'-'+MLLIBR
     C                             +'-'+MLFILE
     C                             +'-'+MLTIPO
     C                             +'-'+MLPROG
     C                             +'-'+MLPSEZ
     C                             +'-'+MLPPAS
     C                             +'-'+MLPDES
     C                             +'-'+MLSQIN
     C                             +'-'+MLSQFI
     C                             +'-'+MLAAAT
     C                             +'-'+MLAAVA
     C                             +'-'+%CHAR(MLNNAT)
     C                             +'-'+%CHAR(MLNNVA)
     C                             +'-'+MLINDI
     C                             +'-'+%CHAR(MLTEES)
     C                             +'-'+MLUSES
     C                             +'-'+%CHAR(MLDTES)
     C                             +'-'+%CHAR(MLORES)
     C                             +'-'+MLASLA
     C                             +'-'+%CHAR(MLASNR)
      *
     C     £DBG_Str      DSPLY
     C                   SETON                                        LR