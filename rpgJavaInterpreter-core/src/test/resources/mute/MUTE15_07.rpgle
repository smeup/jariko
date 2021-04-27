     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 27/04/21          BUSFIO Creazione mute
     V* ==============================================================
     D*  OBIETTIVO
     D*  Programma finalizzato ai test sulle procedure della £PJAX_PC1 : RXATP
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
      * P_RxATP
     C                   EXSR      FRXATP
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
    RD*  Test RxATP
      *--------------------------------------------------------------*
     C     FRXATP        BEGSR
      * Recupero il contenuto della variabile £UIBD1
     C                   EVAL      §INPUT=TXT(01)
     C                   EVAL      £JAXATP=P_RXATP(§INPUT)
     C                   EVAL      $N=1
      *
1    C                   DO        2
    MU* VAL1($RESULT) VAL2(TXTRES($N)) COMP(EQ)
     C                   EVAL      $RESULT=%TRIM(£JAXATP_V($N))
     C                   EVAL      $N=$N+1
1e   C                   ENDDO
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
PR1(Ahi quanto a dir qual era è cosa dura,esta selva selvaggia) PR2(Ed una lupa, che di tutte brame)
** CTDATA TXTRES
Ahi quanto a dir qual era è cosa dura,esta selva selvaggia
Ed una lupa, che di tutte brameo valor
