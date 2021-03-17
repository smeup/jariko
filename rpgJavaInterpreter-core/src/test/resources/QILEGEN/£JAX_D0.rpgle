     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* 24/10/03          CM Aggiunta variabile £JAXSC
     V* 16/05/06          GR Aggiunta variabile £JAXNS per "servizi" senza scritture nel buffer
     V* 14/12/06  V2R2    AS Aggiunte variabili £JaxWT e £JaxDtRc
     V* 20/04/07  V2R2    AS Aggiunta variabile £Jax_FlushEn
     V* 25/02/08  V2R3    CC Aggiunte specifiche D per £JAX_BSETUP in £JAX_C3
     V* 12/03/08  V2R3    CC Aggiunte specifiche D per gestione campi setup in  £JAX_BSETUP
     V* 20/03/08  V2R3    CC Elim. metodo £JAX_BSETUP. Gestione campo XS di setup fatto da LOOCUP
     V* 20/11/10  V3R1    AS Aggiunta variabile £JAXNI per omettere INPUT dall'XML
     V* 08/05/12  V3R2    AS Aggiunta variabile £JAXNI_POS per contemporanee modifiche a £JAX_C0
     V* 19/06/17  V5R1   BMA Rilasciata modifica precedente
     V* 27/06/17  V5R1   BMA Portata £JAXCL da 35000 a 40000
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
     D £JaxWT          S              5  0
      *
      * Dati ricevuti (non PING o messaggi di chiusura)
     D £JaxDtRc        S              1
      *
      * Abilitazione allo svuotamento della coda
     D £Jax_FlushEn    S              1
      *
     D £JaxDSGen       DS
     D £JaxCP                     30000    INZ VARYING
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
     D £JaxLSi                         Z   INZ
     D £JaxLSt                         Z   INZ
     D £JaxLEt                         Z   INZ
     D £JaxLAt                       10  0 INZ
     D £JaxLCo                       40    INZ
      * Non riportare campo INPUT in Attributo Funzione dell'Header XML
     D £JAXNI          S              1
      * Campo di appoggio per la gestione del campo INPUT in Attributo Funzione dell'Header XML
     D £JAXNI_POS      S              5  0
