      /IF NOT DEFINED(UIBDS_INCLUDED)
      /DEFINE UIBDS_INCLUDED
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 14/12/06  V2R2    AS Aggiunte istruzioni per compilazione condizionale
     V* 25/02/08  V2R3    CC Aggiunto campo XS per passaggio setup
     V* 20/03/08  V2R3    CC Elim. campo XS. Gestione di tale campo setup lasciata a LOOCUP
     V* 04/07/08  V2R3    RL Aggiunti i campi £UIBRI e £UIBSG
     V* 02/12/09  V2R3    CM Aggiunto Setup del Setup
     V* 19/11/10  V3R1    AS Aggiunta £UibPR_Long per gestione INPUT
     V* 27/05/16  V4R1   BMA Aggiunta £UIB_C_3F per sostituzione caratteri non validi
     V* 01/03/17         BMA Aggiunto numero di sequenza
     V* B£70524A  V5R1   BMA Aggiunta £UIBDO per output £UIB e £UIBDI per input £UIB
     V* 20/09/17  V5R1   BMA Aggiunto programma chiamante
     V* 04/01/18  V5R1   BMA Aggiunta £UIB_S_3F per sostituzione caratteri non validi
     V* ==============================================================
     D*----------------------------------------------------------------
     D*
     D*----------------------------------------------------------------
     D £UIBDW          DS         31000
     D £UIBDS          DS         31000
     D  £UIBTM                       10
     D  £UIBMS                       10
     D  £UIBPG                       10                                         Componente/Programma
     D  £UIBFU                       10                                         Programma/Funzione
     D  £UIBME                       10                                         Funz.Metodo/Metodo
      *
     D  £UIBT1                        2
     D  £UIBP1                       10
     D  £UIBK1                       15
     D  £UIBT2                        2
     D  £UIBP2                       10
     D  £UIBK2                       15
     D  £UIBT3                        2
     D  £UIBP3                       10
     D  £UIBK3                       15
      *
     D  £UIBPA                      256
      *
     D  £UIB35                        1
     D  £UIB36                        1
     D  £UIBCM                        7                                         Codice Messaggio
     D  £UIBFM                       10                                         File Messaggi
     D  £UIBSC                       10
      *
     D  £UIBT4                        2
     D  £UIBP4                       10
     D  £UIBK4                       15
     D  £UIBT5                        2
     D  £UIBP5                       10
     D  £UIBK5                       15
     D  £UIBT6                        2
     D  £UIBP6                       10
     D  £UIBK6                       15
      * Campo server LoocUp di 15 caratteri
     D  £UIBSR                       15
      * Campo client LoocUp di 15 caratteri
     D  £UIBCL                       15
      * Setup Richiesto
     D  £UIBRI                       58
      * Setup Grafico
     D  £UIBSG                        4
      * Setup del Setup
     D  £UIBSS                      256
      * Numero di sequenza
     D  £UIBSQ                        3
      *
     D  £UIBD1              1001  31000
     D*----------------------------------------------------------------
     D £UIBPR          S           2000    VARYING
     D £UIBSU          S           2000    VARYING
     D £UIBPW          S           2000    VARYING
     D £UIBSW          S           2000    VARYING
     D £UibPR_long     S          32766    VARYING
     D £UIB_C_3F       C                   x'3F'
      * Carattere sostitutivo per il 3F. Viene valorizzato nela £JAX_INZ tramite richiamo del B£UT67
      * Qui viene inizializzato a 3F per far emergere casi in cui non sia stata fatta la £JAX_INZ
     D £UIB_S_3F       S              1    INZ(x'3F')
     D*----------------------------------------------------------------
     D £UIBDI          DS           512
      * Lunghezza in byte da inviare
     D  £UIBDI_LU                     5  0
      * Programma chiamante
     D  £UIBDI_P1                    10
     D*----------------------------------------------------------------
     D £UIBDO          DS           512
      * Lunghezza in byte restituita dalla coda
     D  £UIBDO_LU                     5  0
      * Lunghezza in byte dell'INPUT restituito nella £UIBD1
     D  £UIBDO_LI                     5  0
     D*----------------------------------------------------------------
      /ENDIF
