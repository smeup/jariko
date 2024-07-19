     D £DBG_Str        S             2
     D $X              S              5  0

     D OGG$N           C                   106
     D OGG_IP          S             12  0 DIM(OGG$N)
     D OGG_E           S              2  0 DIM(OGG$N)
     D OGG_L           S              2  0 DIM(OGG$N)

     D£K04DI           DS          2000    INZ
     D £K04I_OG                      52                                         Classe

     D£K04DO           DS          3000    INZ
     D £K04O_IP                       1                                         Modalità Input Panel

     D OK04O_IP        S                   LIKE(£K04O_IP)
      *
     C                   EVAL      $X=2
     C                   IF        %SUBST(£K04I_OG:1:OGG_L($X))=
     C                             %SUBST(OGG_IP($X):1:OGG_L($X))
     C                   EVAL      OK04O_IP=OGG_E($X)
     C                   ENDIF

     C                   EVAL      £DBG_Str='ok'
     C     £DBG_Str      DSPLY