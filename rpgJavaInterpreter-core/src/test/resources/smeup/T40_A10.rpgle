     D A10_DS_P04    E DS                  EXTNAME(MULANGTF) INZ
     D                                     PREFIX(P4:2)
      *
     C                   EVAL      P4SYST='AAA'
     C                   EVAL      P4LIBR='BBB'
      *
     C                             +') Contenuto A10_DS_P04('
     C                             +P4SYST
     C                             +'-'+P4LIBR
     C                             +'-'+P4FILE
     C*                             +'-'+P4TIPO
     C*                             +'-'+P4PROG
     C*                             +'-'+P4PSEZ
     C*                             +'-'+P4PPAS
     C*                             +'-'+P4PDES
     C*                             +'-'+P4SQIN
     C*                             +'-'+P4SQFI
     C*                             +'-'+P4AAAT
     C*                             +'-'+P4AAVA
     C*                             +'-'+%CHAR(P4NNAT)
     C*                             +'-'+%CHAR(P4NNVA)
     C*                             +'-'+P4INDI
     C*                             +'-'+%CHAR(P4TEES)
     C*                             +'-'+P4USES
     C*                             +'-'+%CHAR(P4DTES)
     C*                             +'-'+%CHAR(P4ORES)
     C*                             +'-'+P4ASLA
     C*                             +'-'+%CHAR(P4ASNR)
     C                             +')'