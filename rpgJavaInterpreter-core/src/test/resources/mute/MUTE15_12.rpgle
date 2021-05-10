     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 27/04/21          BUSFIO Creazione mute
     V* ==============================================================
     D*  OBIETTIVO
     D*  Programma finalizzato ai test sulle procedure della £PJAX_PC1 : RXSOS e RXSOC
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
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      * P_RXSOS e P_RXSOC
     C                   EXSR      FRXSOSC
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
    RD*  Test RXSOS e RXSOC
      *--------------------------------------------------------------*
     C     FRXSOSC       BEGSR
      * Sostituisco caratteri dell'XML
    MU* VAL1($RESULT) VAL2(TXTRES(1)) COMP(EQ)
     C                   EVAL      $RESULT=P_RxSOS(TXT(1):'')
      * Riconverto ai caratteri XML
    MU* VAL1($RESULT) VAL2(TXT(1)) COMP(EQ)
     C                   EVAL      $RESULT=P_RxSOC(TXTRES(1))
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
<Colonna Cod="COD" Txt="Collaboratore" Ogg="CNCOL"/>
** CTDATA TXTRES
&lt;Colonna Cod=&quot;COD&quot; Txt=&quot;Collaboratore&quot; Ogg=&quot;CNCOL&quot;/&gt;
