     D £DBG_Str        S            150          VARYING
     d A30_DS_P01A   e ds                  extname(mulangtf) inz
     d                                     prefix(S1:2)
     D A30_DS_P01C   E DS                  EXTNAME(mulangtf) INZ
     D                                     PREFIX(S3:2)

     C                   EVAL      £DBG_Str='Lunghezza: '
     C                             +%CHAR(%LEN(A30_DS_P01C))
     C                             +' Contenuto: '
     C                             +S3SYST
     C                             +'-'+S3LIBR
     C                             +'-'+S3FILE
     C                             +'-'+S3TIPO
     C                             +'-'+S3PROG
     C                             +'-'+S3PSEZ
     C                             +'-'+S3PPAS
     C                             +'-'+S3PDES
     C                             +'-'+S3SQIN
     C                             +'-'+S3SQFI
     C                             +'-'+S3AAAT
     C                             +'-'+S3AAVA
     C                             +'-'+%CHAR(S3NNAT)
     C                             +'-'+%CHAR(S3NNVA)
     C                             +'-'+S3INDI
     C                             +'-'+%CHAR(S3TEES)
     C                             +'-'+S3USES
     C                             +'-'+%CHAR(S3DTES)
     C                             +'-'+%CHAR(S3ORES)
     C                             +'-'+S3ASLA
     C                             +'-'+%CHAR(S3ASNR)
     C                   EXSR      £DBG

     C     £DBG_Str      DSPLY