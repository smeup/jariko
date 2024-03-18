     D £DBG_Str        S            150          VARYING
     d A30_DS_P01A   e ds                  extname(mulangtf) inz
     d                                     prefix(S1:2)
     D A30_DS_P01C   E DS                  EXTNAME(mulangtf) INZ
     D                                     PREFIX(S3:2)

     C                   EVAL      £DBG_Str='Lunghezza: '
     C                             +%CHAR(%LEN(A30_DS_P01A))
     C                             +' Contenuto: '
     C                             +S1SYST
     C                             +'-'+S1LIBR
     C                             +'-'+S1FILE
     C                             +'-'+S1TIPO
     C                             +'-'+S1PROG
     C                             +'-'+S1PSEZ
     C                             +'-'+S1PPAS
     C                             +'-'+S1PDES
     C                             +'-'+S1SQIN
     C                             +'-'+S1SQFI
     C                             +'-'+S1AAAT
     C                             +'-'+S1AAVA
     C                             +'-'+%CHAR(S1NNAT)
     C                             +'-'+%CHAR(S1NNVA)
     C                             +'-'+S1INDI
     C                             +'-'+%CHAR(S1TEES)
     C                             +'-'+S1USES
     C                             +'-'+%CHAR(S1DTES)
     C                             +'-'+%CHAR(S1ORES)
     C                             +'-'+S1ASLA
     C                             +'-'+%CHAR(S1ASNR)

     C     £DBG_Str      DSPLY