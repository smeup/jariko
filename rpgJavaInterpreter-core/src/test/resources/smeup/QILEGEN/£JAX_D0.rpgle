     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* 20/03/24  KOKOS   BERNI Riportata dalla SMEDEV
     V* 21/03/24          BERNI Modificati tipi Z non gestiti
     V* ==============================================================
      *
      * Generale
     D £JaxSC          S             10    INZ
     D £JaxRC          S             10    INZ
     D £JaxIB          S              1    INZ
      * Numero caratteri Massimi ricevibili da Looc.UP, se dovesse essere variato
      * . bisogna modificare questo valore e ricompilare tutti i pgm che lo utilizzano.
      * . Aggiungere 1 alla lunghezza per omaggiare la posizione iniziale.
      * . Es( Eval Q$STRI=%SUBST(Q$STRI:$I:£JaxMCR-$I)
     D £JaxMCR         S              5  0 INZ(25001)
      *
      * tempo di attesa sulla coda (Waiting Time):
     D £JaxWT          S                   LIKE(£JaxWE)
      *
      * Dati ricevuti (non PING o messaggi di chiusura)
     D £JaxDtRc        S              1
      *
      * Abilitazione allo svuotamento della coda
     D £Jax_FlushEn    S              1
      *
     D £JaxDSGen       DS
     D £JaxCP                    999999    INZ VARYING
     D £JaxCL                     40000    INZ VARYING
     D £JaxWS                     65000    INZ VARYING
     D £Jax35                         1    INZ
      *
      * Scrittura Coda
     D £JaxNS          S              1    INZ                                  Non scrivere buffer
     D £JaxDSCoda      DS
     D £JaxCT                        10    INZ
     D £JaxBF                     30000    INZ VARYING
     D £JaxSQ                         3  0 INZ
     D £JaxLU                         5P 0 INZ
     D £JaxLB                        10    INZ
     D £JaxWE                         5P 0 INZ
      *
      * Log
     D £JaxDSLog       DS
     D £JaxLTr                       20    INZ
     D £JaxLTrU                      20    INZ
     D £JaxLLb                        5  0 INZ
     D £JaxLLc                        5  0 INZ
     D £JaxLBu                      140    INZ
     D £JaxLSi                       26    INZ
     D £JaxLSt                       26    INZ
     D £JaxLEt                       26    INZ
     D £JaxLAt                       10  0 INZ
     D £JaxLCo                       40    INZ
      * Non riportare campo INPUT in Attributo Funzione dell'Header XML
     D £JAXNI          S              1
      * Campo di appoggio per la gestione del campo INPUT in Attributo Funzione dell'Header XML
     D £JAXNI_POS      S              5  0
      *
      * Tempi esecuzione Funzione
     D £JaxTimST       S             26    INZ                                  Tempo iniziale
     D £JaxTimEN       S             26    INZ                                  Tempo finale
     D £JaxTimDF       S             30  0 INZ                                  Differenza tempi
      * Indicatore per segnalare il passaggio nella £JAX_INZ
     D £JaxTimIN       S              1    INZ
