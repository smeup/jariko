     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 27/04/21          BUSFIO Creazione mute
     V* ==============================================================
     D*  OBIETTIVO
     D*  Programma finalizzato ai test sulle procedure della £PJAX_PC1 : RXATV
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
      * P_RxATV
     C                   EXSR      FRXATV
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
    RD*  Test P_RxATV
      *--------------------------------------------------------------*
     C     FRXATV        BEGSR
      * Recupero il contenuto dalla schiera TXTATV
     C                   EVAL      $N=1
     C                   EVAL      §INPUT=TXT($N)
     C                   EVAL      £JAXATV=P_RXATV(§INPUT)
2    C                   DO        5
      *
    MU* VAL1($RESULT) VAL2(TXTRES($N)) COMP(EQ)
     C                   EVAL      $RESULT=%TRIM(£JAXATV_V($N))
      *
     C                   EVAL      $N=$N+1
2e   C                   ENDDO
      *
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
pr1="O" pr2="buono" pr3="Appollo," pr4="a l'ultimo" pr5="lavoro"
** CTDATA TXTRES
O
buono
Appollo,
a l'ultimo
lavoro
