      *====================================================================
      * smeup V6R1.020DV
      * Nome sorgente       : £TABUI1DS
      * Sorgente di origine : SMEDEV/QILEGEN(£TABUI1DS)
      * Esportato il        : 20240320 155127
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*===============================================================
     V* 25/11/22  V6R1    BUSFIO Aggiunto campo Disttiva tempo FUN
     V*===============================================================
      *      Main User Interface
     D UI1$DS          DS           100
      * N° 03 - Ambiente             TA *CNAA       2
     D  T$UI1A                 1      2
      * N° 04 - Coda lavoro          OJ            10
     D  T$UI1B                 3     12
      * N° 05 - Timeout code         **             1
     D  T$UI1C                13     13
      * N° 06 - Chiusura Job Emulat. V2 SI/NO       1    5
     D  T$UI1H                19     19
      * N° 07 - Emulatore esteso  SV V2 SI/NO       1
     D  T$UI1D                20     20
      * N° 08 - Exit JACFG1          **             1
     D  T$UI1E                21     21
      * N° 09 - Disattiva Tempo FUN  V2 SI/NO       1
     D  T$UI1F                22     22
