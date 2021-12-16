     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 27/04/21          BUSFIO Creazione mute
     V* ==============================================================
     D*  OBIETTIVO
     D*  Programma finalizzato ai test sulle procedure della £PJAX_PC1 : RXSPL
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
      * P_RxSPL
     C                   EXSR      FRXSPL
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
    RD*  Test RxSPL
      *--------------------------------------------------------------*
     C     FRXSPL        BEGSR
      * Divido a meta il numero di parole con |
    MU* VAL1($RESULT) VAL2(TXTRES(1)) COMP(EQ)
     C                   EVAL      $RESULT=P_RxSPL(TXT(1):'1')
      *
    MU* VAL1($RESULT) VAL2(TXTRES(2)) COMP(EQ)
     C                   EVAL      $RESULT=P_RxSPL(TXT(2):'1')
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
Prova Stringa
Prova Stringa complessa da più parole
** CTDATA TXTRES
Prova|Stringa
Prova Stringa complessa|da più parole
