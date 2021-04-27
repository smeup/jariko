     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 27/04/21          BUSFIO Creazione mute
     V* ==============================================================
     D*  OBIETTIVO
     D*  Programma finalizzato ai test sulle procedure della £PJAX_PC1 : RXURL
     V*--------------------------------------------------------------------------------------------*
      /COPY QILEGEN,£JAX_PD1
      *--------------------------------------------------------------------------------------------*
      * Schiera per test
     D TXT             S            100    DIM(15) CTDATA PERRCD(1)             _NOTXT
      * Schiera per risultati
     D TXTRES          S            100    DIM(15) CTDATA PERRCD(1)             _NOTXT
      *
     D §INPUT          S          30000    VARYING
     D $RESULT         S          30000
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      * P_RxURL
     C                   EXSR      FRXURL
      *
     C                   EXSR      FIN0
      *
     C                   SETON                                        LR
      *--------------------------------------------------------------*
    RD*  Impostazioni iniziali
      *--------------------------------------------------------------*
     C     IMP0          BEGSR
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Test RxURL
      *--------------------------------------------------------------*
     C     FRXURL        BEGSR
     C                   EVAL      §INPUT=TXT(1)
      * Recupero valori all'interno della variabile §INPUT
    MU* VAL1($RESULT) VAL2(TXTRES(1)) COMP(EQ)
     C                   EVAL      $RESULT=P_RxURL(§INPUT)
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Routine impostazioni di avvio programma
      *--------------------------------------------------------------*
     C     £INIZI        BEGSR
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Chiusura e invio su coda del buffer residuo
      *--------------------------------------------------------------*
     C     FIN0          BEGSR
      *
     C                   ENDSR
      /COPY QILEGEN,£JAX_PC1
** CTDATA TXT
http||:prov@di§unURL£WEBvedere%se sostituisce"correttamente$RXURLR
** CTDATA TXTRES
http%7C%7C:prov@di%C2%A7unURL%C2%A3WEB%20per%20vedere%25se%20sostituisce%22correttamente%24RXURL
