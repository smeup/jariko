      *====================================================================
      * smeup V6R1.021DV
      * Nome sorgente       : £G40E
      * Sorgente di origine : SMEDEV/QILEGEN(£G40E)
      * Esportato il        : 20240409 121041
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 14/02/10  V2R3    BS Aggiunta variabile £40ANM
     V* 15/02/10  V2R3    BS Aggiunto Keyword VARYING alla variabile £40AE
     V* 15/02/10  V2R3    BS Aggiunta definizione DS £G40L0DS/L1DS/L2DS
     V* 21/02/10  V2R3    BS Rilascio in DEV modifiche del 15/02/10
     V* 21/02/10  V2R3    BS Aggiunto Campo Schema e £40TDS
     V* 28/02/10  V2R3    BS Aumentata Lunghezza Campo N° Elementi
     V* 24/08/10  V3R1    BS Aggiunto Campo £40F_MSR
     V* 31/08/10  V3R1    BS Aggiunti Campi Oggetto Secondario
     V* 06/09/10  V3R1    BS Allungata DS da 100 a 500 caratteri
     V* 14/09/15  V4R1    BS Aggiunto campo utente
     V* 09/10/18  V5R1    BS Aggiunta indicazione I/O su variabili DS
     V* 01/11/18  V5R1    BS Aggiunto campo annullata
     V* 16/08/19  001067 BMA Tolte schiere con definizione errata overlay
     V* 16/08/19  V5R1    BS Check-out 001067 in SMEDEV
     D*----------------------------------------------------------------
     D* Schiere x passaggio dati a £G40
     D*
     D* Codici oggetto
     D £40A            S             15    DIM(300)
     D £40B            S             12    DIM(300)
     D*
     D £G40DS          DS           500
     D  £G40PZ                        1                                         O Lista Dinamica
     D  £G40EX                        1                                         O Lista Estesa
     D  £G40FM                        1                                         O Formato Ext
     D  £G40AE                        1                                         I Utilizzo £40AE
     D  £G40FL                        1                                         I Applica Filtro JO
     D  £G40NS                        1                                         I Disabilita SQL
     D  £G40NE                        9S 0                                      O N° Elementi
     D  £G40SC                       15                                         O Schema
     D  £G40FO                       15                                         O Fonte
     D  £G40T2                        2                                         I Tipo Oggetto Sec.
     D  £G40P2                       10                                         I Parametro Ogg.Sec.
     D  £G40C2                       15                                         I Cod.Ogg.Sec.
     D  £G40UT                       10                                         O Utente
     D  £G40AN                        1                                         O Annullata
      *
     D £40AE           S          30000    VARYING
      *
     D £40KNM          S              5S 0 INZ(%ELEM(£40KK))
     D £40DNM          S              5S 0 INZ(%ELEM(£40D))
     D £40ANM          S              5S 0 INZ(%ELEM(£40A))
      *
      * DS in sui riversare l'output della funzione LISOG
     D £40KDS          DS         30000    INZ
     D £40KK                         15    DIM(2000)
      * DS in sui riversare l'output della funzione LISOD
     D £40DDS          DS         30000    INZ
     D £40D                          50    DIM(600)
     D  £40DK                        15    OVERLAY(£40D:1)
     D  £40DD                        35    OVERLAY(£40D:*NEXT)
      * DS in sui riversare l'output della funzione TST
     D £40TDS          DS                  INZ
     D  £40TIN                         Z
     D  £40TFI                         Z
     D  £40TMI                        7  0
     D  £40TNE                       11  0
      * DS in sui riversare l'output della funzione FON
     D £40FDS          DS                  INZ
     D  £40FDE                 1     30                                         Descrizione
     D  £40FRE                31     80                                         Tipo Record
     D  £40FNT                81     85  0                                      N° Oggetti Gestiti
     D  £40F_MSO              86     86                                         Gestione OG_SCA
     D  £40F_MWL              87     87                                         Gestione WHR_LI
     D  £40F_MWC              88     88                                         Gestione WHR_CO
     D  £40FLU                89     90                                         Livello Utente
     D  £40F_MSR              91     91                                         Gestione RE_SCA
     D  £40FNS                92     92                                         NO SQL
      *
     D  £40FTO              1096   7095    DIM(500)                             Oggetti Gestiti
     D**£40FTP              1096   2095    DIM(500)                             . Tipi Oggetto
     D**£40FPA              2096   7095    DIM(500)                             . Parametri Oggetto
