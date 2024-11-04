      *====================================================================
      * smeup V6R1.023DV
      * Nome sorgente       : £K37DS
      * Sorgente di origine : SMEDEV/QILEGEN(£K37DS)
      * Esportato il        : 20240617 113438
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 28/05/18         PEDSTE Creazione
     V* 11/07/18  000065 PEDSTE Aggiunta gestione definizione attributi
     V* 25/02/19  000491 PEDSTE Aggiunto £K37O_NN Nome Nodo
     V* 28/02/19  V5R1   CATMAS Check-out 000491 in SMEUP_TST
     V* 06/09/19  V5R1    CM Rilascio 000065, 000491 in SMEDEV
     V* 06/03/20  001647  PEDSTE Gestione forzatura "0" per campo numerico con valore 0
     V* 06/03/20  V5R1   BERNI  Check-out 001647 in SMEUP_TST
     V* 14/04/20  V5R1   PEDSTE Rilascio 001647 in SMEDEV
     V* 07/08/20  002052 PEDSTE Gestione json da xls oggettizzati smeup
     V* 07/08/20  V5R1   AS Check-out 002052 in SMEUP_TST
     V* ££10916A  V5R1    BERNI **********************
     V* ££10916A  V5R1    BERNI ************* RILASCIO INTERO SORGENTE DA SMEUP_TST A SMEDEV
     V* ££10916A  V5R1    BERNI **********************
     V* 01/07/22  003975  BUSFIO Aggiunta stringa 16Mb in input
     V* 05/07/22  V6R1   BMA Check-out 003975 in SMEDEV
     V* ==============================================================
      *
      * DS di Input
      * -----------
     D£K37DI           DS          2560
     D £K37I_FU                      10                                         Funzione
     D £K37I_ME                      10                                         Metodo
     D £K37I_PF                     256                                         Pathfile IFS
     D £K37I_PA                    1000                                         Percorso Attributo
     D £K37I_TI                      10                                         Tipo integrazione
     D £K37I_XO                       1                                         XLS oggettizzato
      * Stringa di Output (singola riga)
      * ------------
     D£K37I_ST         S               A   VARYING LEN(16773100)                Stringa input
      *
      * DS di Output
      * ------------
     D£K37DO           DS          1024
     D £K37O_TA                       1                                         Tipo attributo
     D £K37O_NU                      63 20                                      Valore numerico
     D £K37O_MS                       7                                         Msg
     D £K37O_FI                      10                                         File
     D £K37O_TX                     198                                         Testo/variabili msg
     D £K37O_ML                       2                                         Livello messaggio
     D £K37O_MT                       5                                         Tipo messaggio
     D £K37O_35                       1N                                        Indicatore 35
     D* Definizione campo
     D £K37O_NC                     010                                         Nome campo
     D £K37O_DC                     100                                         Descrizione
     D £K37O_OG                     012                                         Oggetto
     D £K37O_OA                     015                                         Attributo OAV
     D £K37O_LU                     003                                         Lunghezza
     D £K37O_DE                     001                                         Decimali
     D £K37O_VI                     001                                         Visualizzazione O/H
     D £K37O_RD                     001                                         Risalita Descrizione
     D £K37O_BC                     001                                         Aggiornamento batch
     D £K37O_NN                     150                                         Nome Nodo
     D £K37O_FZ                     001                                         Forzatura valore "0"
      *
      * Stringa di Output (valore alfanumerico)
      * ------------
     D£K37O_AL         S          32000    VARYING                              Valore alfanumerico
      *----------------------------------------------------------------*
