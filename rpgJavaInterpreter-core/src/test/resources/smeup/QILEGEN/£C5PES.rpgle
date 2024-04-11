      *====================================================================
      * smeup V6R1.021DV
      * Nome sorgente       : £C5PES
      * Sorgente di origine : SMEDEV/QILEGEN(£C5PES)
      * Esportato il        : 20240411 144730
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*===============================================================
     V* 24/12/07  V2R3  i BS Estese Tipologie Pertinenza/Condizione
     V* 08/03/17  V5R1    CM Sostituito cartattere x'B4' con £HEXB4
     V*===============================================================
     D* Obiettivo
     D*--------------------------------------------------------------
     D* Filtrare un record contabile in funzione della condizione
     D* di pertinenza e condizione
     D* ESEMPIO DI CHIAMATA
     D*C                   MOVEL     <xxFL01>      £C5P01
     D*C                   MOVEL     <xxFL02>      £C5P02
     D*C                   MOVEL     <xxFL05>      £C5P05
     D*C                   MOVEL     <Pert-2carat> £C5PPE
     D*C                   MOVEL     <Cond-2carat> £C5PCO
     D*C                   MOVEL     <Icpy-2carat> £C5PEI
     D*C                   EXSR      £C5PES
      *----------------------------------------------------------------
      /COPY QILEGEN,£HEX
     C     £C5PES        BEGSR
      *
     C                   CLEAR                   £C5PEP            2
     C                   CLEAR                   £C5PEC            2
     C                   CLEAR                   £C5PEI            2
      *
     C                   SELECT
2    C                   WHEN      £C5P01 = '1'
     C                   EVAL      £C5PEP=£ATRPU(02)+£C5P01
2    C                   WHEN      £C5P01 >= '3'
     C                   EVAL      £C5PEP=£ATRPU(01)+£C5P01
2    C                   OTHER
     C                   EVAL      £C5PEP=£ATRPU(11)+' '
1    C                   ENDSL
      *
     C                   SELECT
2    C*******            WHEN      £C5P02 = '0'
     C*******            EVAL      £C5PEC=£ATRPU(03)+£C5P02
2    C*******            WHEN      £C5P02 = '3'
     C*******            EVAL      £C5PEC=£ATRPU(04)+£C5P02
2    C                   WHEN      £C5P02 < '6'
     C                   EVAL      £C5PEC=£ATRPU(04)+£C5P02
2    C                   OTHER
     C                   EVAL      £C5PEC=£ATRPU(11)+' '
1    C                   ENDSL
      *
     C                   IF        £C5P05='1'
     C                   EVAL      £C5PEI=£ATRPU(10)+£HEXB4
     C                   ELSE
     C                   EVAL      £C5PEI=£ATRPU(11)+' '
     C                   ENDIF
      *
     C                   ENDSR
