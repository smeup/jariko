      *====================================================================
      * smeup V6R1.021DV
      * Nome sorgente       : £DMSI
      * Sorgente di origine : SMEDEV/QILEGEN(£DMSI)
      * Esportato il        : 20240409 121048
      *====================================================================
      /IF NOT DEFINED(DMSI_INCLUDED)
      /DEFINE DMSI_INCLUDED
      *
      * 1° Messaggio di errore
      *
     D £DMS1M          DS           132
      *----------------------------------------------------------------
      *
      * Messaggio 1 livello
      *
     D £DMS1L          DS           132
      *----------------------------------------------------------------
      *
      * Messaggio 2 livello
      *
     D £DMS2L          DS           500
      *----------------------------------------------------------------
      *
      * STRINGA VARIABILI:
      * 512 bytes per gestire le variabili interne al messaggio (1o/2o)
      *
     D £DMSST          DS           512
      *----------------------------------------------------------------
      /ENDIF
