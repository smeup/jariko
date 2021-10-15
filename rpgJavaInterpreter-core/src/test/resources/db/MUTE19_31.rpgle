     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 04/08/21  003102  BUSFIO Creazione
     V* 05/08/21  003102  BUSFIO Aggiunta entry
     V* 16/09/21  003102  BUSFIO Aggiunta nuova variabile per la Entry
     V*=====================================================================
     D*  OBIETTIVO
     D*  Programma finalizzato ai test di ottimizzazione di reload:
     D*  SETLL READE su VERAPG0F - Record non trovato
     D*  Programma SPL: 31_SetllAndReadE_2SameKey_NoExist_VERAPG0F
     V*=====================================================================
     H/COPY QILEGEN,£INIZH
      * File
     FVERAPG3L  IF   E           K DISK
      *---------------------------------------------------------------
     D $$NOTFOUND      S              1
     D $FAIL           S              3    INZ('NO')                             Indicatore di Fail
      *
     D $TIMST          S               Z   INZ                                   Tempo iniziale
     D $TIMEN          S               Z   INZ                                   Tempo finale
     D $TIMMS          S             10I 0                                       Tempo millisecondi
      *
     D $MSG            S             52                                          Output
      *
     D MU_TIME         S             10                                          Entry - Tempo
     D MU_TSNAME       S             45                                          Entry - Test name
     D MU_FLNAME       S             10                                          Entry - File name
     D MU_TPOPER       S             15                                          Entry - Type oper
     D MU_FAIL         S              3                                          Entry - Test Fail
      *---------------------------------------------------------------
     I/COPY QILEGEN,£TABB£1DS
     I/COPY QILEGEN,£PDS
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
     C     *ENTRY        PLIST
     C                   PARM                    MU_TIME
     C                   PARM                    MU_TSNAME
     C                   PARM                    MU_FLNAME
     C                   PARM                    MU_TPOPER
     C                   PARM                    MU_FAIL
      * Begin time
     C                   TIME                    $TIMST
      *
     C     KEY001        KLIST
     C                   KFLD                    V£CDC
     C                   KFLD                    V£DATA
      *
     C                   EVAL      V£CDC='SMEGL.001'
     C                   EVAL      V£DATA=20210117
      *
     C     KEY001        SETLL     VERAPG3L
      *
     C     KEY001        READE     VERAPG3L
      *
     C                   IF        V£IDOJ = ''
    MU* VAL1($$NOTFOUND) VAL2('1') COMP(EQ)
     C                   EVAL      $$NOTFOUND='1'
     C                   ELSE
    MU* VAL1($$NOTFOUND) VAL2('1') COMP(EQ)
     C                   EVAL      $$NOTFOUND=''
     C                   EVAL      $FAIL='YES'
      *
     C                   ENDIF
      * End Time
     C                   TIME                    $TIMEN
      * Elapsed time
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
     C                   EVAL      $TIMMS=$TIMMS/1000
      *
     C*                   EVAL      $MSG = %CHAR($TIMMS)
     C*     $MSG          DSPLY     £PDSNU
      *
     C*                   IF        £PDSPR>0
     C                   EVAL      MU_TIME = %CHAR($TIMMS)
     C                   EVAL      MU_TSNAME = '31_SetllAndReadE_2SameKey'      COSTANTE
     C                                        +'_NoExist_VERAPG0F'              COSTANTE
     C                   EVAL      MU_FLNAME = 'VERAPG0F'                       COSTANTE
     C                   EVAL      MU_TPOPER = 'SETLL READE'                    COSTANTE
     C                   EVAL      MU_FAIL = $FAIL
     C*                   ENDIF
      *
     C                   SETON                                        LR
