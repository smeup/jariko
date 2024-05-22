      *====================================================================
      * smeup V6R1.021DV
      * Nome sorgente       : MULANGTD10
      * Sorgente di origine : QTEMP/SRC(MULANGTD10)
      * Esportato il        : 20240429 170419
      *====================================================================
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 29/04/24  V6R1    GUAGIA Creazione
     V*=====================================================================
     D VARPARM         S                   LIKE(£VARPARM)
      *---------------------------------------------------------------------
     C     *ENTRY        PLIST
     C                   PARM                    £VARPARM         10
     C                   EVAL      VARPARM=':'+%TRIM(£VARPARM)
     C                   EVAL      £VARPARM='mod'+%TRIM(VARPARM)
      *
     C                   SETON                                          LR
