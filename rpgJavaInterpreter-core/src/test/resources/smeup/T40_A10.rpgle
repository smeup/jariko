     D A10_DS_P01    E DS                  EXTNAME(MULANGTF) INZ
      *
     D A10_DS_P04    E DS                  EXTNAME(MULANGTF) INZ
     D                                     PREFIX(P4:2)
      *
     D £DBG_Str        S           5000
      *
     C                   EVAL      P4SYST='AAA'
     C                   EVAL      MLSYST='BBB'
      *
     C                   EVAL      £DBG_Str='A10_DS_P01('
     C                             +MLSYST
     C                             +'-'+%CHAR(MLNNAT)
     C                             +'-'+%CHAR(MLNNVA)
     C                             +'-'+%CHAR(MLTEES)
     C                             +'-'+%CHAR(MLDTES)
     C                             +'-'+%CHAR(MLORES)
     C                             +'-'+%CHAR(MLASNR)
     C                             +') A10_DS_P04('
     C                             +P4SYST
     C                             +'-'+%CHAR(P4NNAT)
     C                             +'-'+%CHAR(P4NNVA)
     C                             +'-'+%CHAR(P4TEES)
     C                             +'-'+%CHAR(P4DTES)
     C                             +'-'+%CHAR(P4ORES)
     C                             +'-'+%CHAR(P4ASNR)
     C                             +')'
     C     £DBG_Str      DSPLY
     C                   SETON                                        LR
