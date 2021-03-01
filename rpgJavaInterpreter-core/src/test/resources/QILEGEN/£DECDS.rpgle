     D* ==============================================================
     D* MODIFICHE Ril.  T Au Descrizione
     D* gg/mm/aa  nn.mm i xx Breve descrizione
     D* ==============================================================
     D* 10/09/13  V4R1   GG  Creazione
     D* 28/08/16  V5R1   BS  Campo decodifica completa e livello
     D* 17/08/17  V5R1  BMA  Campi dati statistici
     D* 27/09/18  000155 BMA Condizionata restituzione campi output estesi
     D* B£80928A  V5R1   BMA Condizionata restituzione campi output estesi
     D* ==============================================================
      *
      * DS di Input
      * -----------
     D£DECDI           DS           512
      * Restituisci decodifica estesa e livello
     D £DECI_DELI                     1
      * Restituisci campi estesi (tutti)
     D £DECI_ROES                     1
      *
      * DS di Output
      * ------------
     D£DECDO           DS          2048
      * Oggetto parametro
     D £DECO_TPAR                    12
      * Codice parametro
     D £DECO_CPAR                    15
      * Decodifica Completa
     D £DECO_DESC                   256
      * Livello
     D £DECO_LIVE                     1
      * Data inserimento
     D £DECO_DTIN                     8
      * Ora inserimento
     D £DECO_ORIN                     6
      * Utente inserimento
     D £DECO_USIN                    10
      * Origine inserimento
     D £DECO_OGIN                    10
      * Data aggiornamento
     D £DECO_DTAG                     8
      * Ora aggiornamento
     D £DECO_ORAG                     6
      * Utente aggiornamento
     D £DECO_USAG                    10
      * Origine aggiornamento
     D £DECO_OGAG                    10
      *
      *----------------------------------------------------------------*
