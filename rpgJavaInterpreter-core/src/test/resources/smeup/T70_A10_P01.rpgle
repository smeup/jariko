     D £DBG_Str        S            100
      *
     C                   EVAL      £DBG_Str='OUT'
      *
      * Definisci variabili e registra definizione come eseguita
      *
      /IF NOT DEFINED(DMSE_INCLUDED)
      /DEFINE DMSE_INCLUDED
     C     £DBG_Str      DSPLY
      /ENDIF
      *
      * Definizioni da saltare perchè già presenti
      *
      /IF NOT DEFINED(DMSE_INCLUDED)
     C     £DBG_Str      DSPLY
      /ENDIF
      *
      * Pulisci define
      *
      /UNDEFINE DMSE_INCLUDED
      *
     C                   SETON                                        LR