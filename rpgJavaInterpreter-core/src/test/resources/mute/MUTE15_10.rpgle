     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 27/04/21          BUSFIO Creazione mute
     V* ==============================================================
     D*  OBIETTIVO
     D*  Programma finalizzato ai test sulle procedure della £PJAX_PC1 : RXELE
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
     D $N              S              2  0
     D Xml_Con         S          65000    VARYING
     D NODINI          S              5  0
     D NODLEN          S              5  0
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      * P_RxELE
     C                   EXSR      FRXELE
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
    RD*  Test RxELE
      *--------------------------------------------------------------*
     C     FRXELE        BEGSR
      * Recupero valore all'interno di un Tag XML
    MU* VAL1($RESULT) VAL2(TXT(1)) COMP(EQ)
    MU* VAL1(Xml_Con) VAL2(TXTRES(1)) COMP(EQ)
     C                   EVAL      $RESULT=P_RxELE('Setup':'POS':01:TXT(1)      COSTANTE
     C                                    :NODINI:NODLEN:Xml_Con)
      *
    MU* VAL1($RESULT) VAL2(TXT(2)) COMP(EQ)
    MU* VAL1(Xml_Con) VAL2(TXTRES(2)) COMP(EQ)
     C                   EVAL      $RESULT=P_RxELE('Setup':'POS':01:TXT(2)      COSTANTE
     C                                    :NODINI:NODLEN:Xml_Con)
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
<Setup>Prova ricerca per setup</Setup>
<Set><h1>Prova</h1><Setup>Recupero valore annidato dei tag</Setup></Set>
** CTDATA TXTRES
Prova ricerca per setup
Recupero valore annidato dei tag
