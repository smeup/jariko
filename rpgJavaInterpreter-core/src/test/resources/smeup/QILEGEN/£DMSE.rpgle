      *====================================================================
      * smeup V6R1.021DV
      * Nome sorgente       : £DMSE
      * Sorgente di origine : SMEDEV/QILEGEN(£DMSE)
      * Esportato il        : 20240409 121048
      *====================================================================
      /IF NOT DEFINED(DMSE_INCLUDED)
      /DEFINE DMSE_INCLUDED
      *
      *
      *  /COPY da inserire insieme con: £DMS
      *
      *----------------------------------------------------------------
     D £D1             S             17    DIM(100)                             Cod.Msg + Msgf
     D £D2             S             45    DIM(100)                             Variabili ass.
     D £D3             S             80    DIM(100)                             Txt schiera
     D £D4             S              1    DIM(45)                              Segmenta in 45
      /ENDIF
