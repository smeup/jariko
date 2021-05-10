     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 27/04/21          BUSFIO Creazione mute
     V* ==============================================================
     D*  OBIETTIVO
     D*  Programma finalizzato ai test sulle procedure della £PJAX_PC1 : RXLATE
     V*--------------------------------------------------------------------------------------------*
      /COPY QILEGEN,£JAX_PD1
      *--------------------------------------------------------------------------------------------*
      * Schiera per test
     D TXT             S            100    DIM(15) CTDATA PERRCD(1)             _NOTXT
      * Schiera per risultati
     D TXTRES          S            100    DIM(15) CTDATA PERRCD(1)             _NOTXT
      *
     D $RESULT         S          30000
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      * P_RxLATE
     C                   EXSR      FRXLATE
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
    RD*  Test RxLATE
      *--------------------------------------------------------------*
     C     FRXLATE       BEGSR
      * Sostituisco la lettera 'e' con la lettera 'a'
    MU* VAL1($RESULT) VAL2(TXTRES(1)) COMP(EQ)
     C                   EVAL      $RESULT=P_RxLATE(TXT(1):'e':'a')
      * Sostituisco la lettera 'n' con '!' al precedente risultato
    MU* VAL1($RESULT) VAL2(TXTRES(2)) COMP(EQ)
     C                   EVAL      $RESULT=P_RxLATE($RESULT:'n':'!')
      * Sostituisco la lettera 'A' con 'E' al precedente risultato
    MU* VAL1($RESULT) VAL2(TXTRES(3)) COMP(EQ)
     C                   EVAL      $RESULT=P_RxLATE($RESULT:'A':'E')
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
Ahi quanto a dir qual era è cosa dura,esta selva selvaggia forte che nel pensier rinova la paura!
** CTDATA TXTRES
Ahi quanto a dir qual ara è cosa dura,asta salva salvaggia forta cha nal pansiar rinova la paura!
Ahi qua!to a dir qual ara è cosa dura,asta salva salvaggia forta cha !al pa!siar ri!ova la paura!
Ehi qua!to a dir qual ara è cosa dura,asta salva salvaggia forta cha !al pa!siar ri!ova la paura!
