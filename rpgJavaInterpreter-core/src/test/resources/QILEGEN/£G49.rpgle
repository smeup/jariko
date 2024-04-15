     V*=====================================================================
     V* MODIFICHE Ril.  T Au  Descrizione
     V* gg/mm/aa  nn.mm i xx  Breve descrizione
     V*=====================================================================
     V* 20/09/16  V5R1    BMA Aggiunta stringa di input a 30000 per funzione CON_E
     V*=====================================================================
     D*----------------------------------------------------------------
     D* OBIETTIVO
     D* Data una stringa di testo su cui operare, la routine sostitui-
     D* sce una serie di caratteri contigui contenuti in essa con un'
     D* altra serie di caratteri.
     D* Esempio:
     D*   nella stringa 'Il codice &1 non è corretto.'
     D*   la stringa '&1' deve essere sostituita con 'COD01'
     D*   il risultato sarà quindi 'Il codice COD01 non è corretto.'
     D*
     D* PREREQUISITI
     D*  D/COPY QILEGEN,£G49DS
     D*
     D* PARAMETRI
     D*  Input:  £G49FU: Funzione                   (10)
     D*          £G49ME: Metodo                     (10)
     D*          £G49SI: stringa in cui sostituire  (1024)
     D*          £G49SC: stringa da sostituire      (256)
     D*          £G49SD: nuova stringa sostituente  (256)
     D*  Output: £G49SI: stringa in cui è stato sostituito £G49SD
     D*          £G49MS: messaggio ritorno
     D*          £G49FI: file messaggi
     D*          *IN35 : Errore
     D*          *IN36 : Ricerca
     D*
     D* ESEMPIO DI CHIAMATA
     D*C                     MOVEL<funzione>£G49FU    P
     D*C                     MOVEL<metodo>  £G49ME    P
     D*C                     MOVEL<s in cui>£G49SI    P
     D*C                     MOVEL<s da sos>£G49SC    P
     D*C                     MOVEL<s nuova> £G49SD    P
     D*C                     EXSR £G49
     D*C                     MOVEL£G49SI    <campo stringa risultato>
     D*----------------------------------------------------------------
     D   D/COPY QILEGEN,£G49DS
     C     £G49          BEGSR
      *
     C                   SETOFF                  *IN35
     C                   SETOFF                  *IN36
     C                   EVAL       £G4935=*OFF
     C                   EVAL       £G4936=*OFF
      *
     C                   CALL      'B£G49G'
     C                   PARM                    £G49FU           10            -->
     C                   PARM                    £G49ME           10            -->
     C                   PARM                    £G49MS            7            <--
     C                   PARM                    £G49FI           10            <--
     C                   PARM                    £G4935                         <--
     C                   PARM                    £G4936                         <--
     C                   PARM                    £G49SI                         <-->
     C                   PARM                    £G49SC          256             -->
     C                   PARM                    £G49SD          256             -->
     C                   PARM                    £G49SE                         <-->
      *
     C                   EVAL       *IN35=£G4935
     C                   EVAL       *IN36=£G4936
      *
     C                   ENDSR