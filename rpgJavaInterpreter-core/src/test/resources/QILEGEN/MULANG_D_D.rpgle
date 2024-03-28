      *====================================================================
      * smeup V6R1.020DV
      * Nome sorgente       : MULANG_D_D
      * Sorgente di origine : SMEUP_DEM/QILEGEN(MULANG_D_D)
      * Esportato il        : 20240305 144511
      *====================================================================
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 04/10/23  V6.R1   LS Creato
     V* 30/11/26  005315  BERNI aggiunto campo a DS per gestione MDV
     V* 30/11/23  V6R1    ARRSTE Merge 005315 in SMEUP_DEM
     V* 04/12/23  DEM     BUSFIO Modificate £DBG_Str e £DBG_O_Str in varying
     V*=====================================================================
      * Per registrazione dei dati di un passo
     D £DBG_DS         DS
     D £DBG_Fun                      10                                         Funzione
     D £DBG_Pgm                      10                                         Programma
     D £DBG_Sez                      10                                         Sezione
     D £DBG_Pas                      10                                         Passo
     D £DBG_Str                    2560    VARYING                              Stringa
     D £DBG_Ind                      99                                         Indicatori
     D £DBG_Mdv                      18                                         Gestione MDV
      * Per chiamata dei passi singoli dove previsto
     D £DBG_I_Fun      S             10                                         Funzione chiamata
     D £DBG_I_Num      S              7  0                                      Numero esecuzioni
     D £DBG_I_Par      S            256                                         Parametri
     D £DBG_O_Str      S           2560    VARYING                              Risultato
     D £DBG_O_Msg      S             10                                         Messaggio
      *
     D £DBG_TIMINI     S               Z
     D £DBG_TIMEND     S               Z
     D £DBG_SAVSEZ     S             10
     D £DBG_SAVSCHPGM  S              7  0 DIM(10)
     D £DBG_SAVINI     S              1
      *
