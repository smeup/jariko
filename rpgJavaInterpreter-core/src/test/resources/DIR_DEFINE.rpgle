      *
      * Definisci variabili e registra definizione come eseguita
      *
      /IF NOT DEFINED(DMSE_INCLUDED)
      /DEFINE DMSE_INCLUDED
     D £D1             S             17    DIM(100)                             Cod.Msg + Msgf
     D £D2             S             45    DIM(100)                             Variabili ass.
     D £D3             S             80    DIM(100)                             Txt schiera
     D £D4             S              1    DIM(45)                              Segmenta in 45
      /ENDIF
      *
      * Definizioni da saltare perchè già presenti
      *
      /IF NOT DEFINED(DMSE_INCLUDED)
     D £D1             S             17    DIM(100)                             Cod.Msg + Msgf
     D £D2             S             45    DIM(100)                             Variabili ass.
     D £D3             S             80    DIM(100)                             Txt schiera
     D £D4             S              1    DIM(45)                              Segmenta in 45
      /ENDIF
      *
      * Pulisci define
      *
      /UNDEFINE(DMSE_INCLUDED)