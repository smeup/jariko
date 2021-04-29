     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 27/04/21          BUSFIO Creazione mute
     V* ==============================================================
     D*  OBIETTIVO
     D*  Programma finalizzato ai test sulle procedure della £PJAX_PC1 : RXATT
     V*--------------------------------------------------------------------------------------------*
      /COPY QILEGEN,£JAX_PD1
      *--------------------------------------------------------------------------------------------*
      * Schiera per test
     D TXT             S            100    DIM(15) CTDATA PERRCD(1)             _NOTXT
      * Schiera per risultato
     D TXTRES          S            100    DIM(15) CTDATA PERRCD(1)             _NOTXT
      *
     D §INPUT          S          30000    VARYING
     D $RESULT         S          30000
     D $N              S              2  0
     D $$CODU          S             06                                          Codice Col
     D $$NOME          S             35                                          Descrizone Col
     D $$CODS          S             15                                          Codice Sede
     D $$SEDE          S             15                                          Descrizione Sede
     D $$MESS          S            500                                          Messaggio
     D $$DATA          S              8  0
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      * P_RxATT
     C                   EXSR      FRXATT
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
    RD*  Test P_RxATT
      *--------------------------------------------------------------*
     C     FRXATT        BEGSR
     C                   EVAL      §INPUT=TXT(1)
      * Recupero valori all'interno della variabile $INPUT
    MU* VAL1($$CODU) VAL2('ROSMAR') COMP(EQ)
     C                   EVAL      $$CODU=P_RxATT(§INPUT:'COD(':'')
    MU* VAL1($$NOME) VAL2('Rossi Mario') COMP(EQ)
     C                   EVAL      $$NOME=P_RxATT(§INPUT:'NOME(':'')
    MU* VAL1($$DATA) VAL2(20210419) COMP(EQ)
     C                   EVAL      $$DATA=%INT(P_RxATT(§INPUT:'DATA(':''))
    MU* VAL1($$CODS) VAL2('ERB') COMP(EQ)
     C                   EVAL      $$CODS=P_RxATT(§INPUT:'CODSEDE(':'')
    MU* VAL1($$SEDE) VAL2('Erbusco') COMP(EQ)
     C                   EVAL      $$SEDE=P_RxATT(§INPUT:'SEDE(':'')
    MU* VAL1($$MESS) VAL2(TXTRES(1)) COMP(EQ)
     C                   EVAL      $$MESS=P_RxATT(§INPUT:'MSG(':'')
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
DATA(20210419) MSG(Lorem) COD(ROSMAR) NOME(Rossi Mario) CODSEDE(ERB) SEDE(Erbusco)
** CTDATA TXTRES
Lorem
