   COP* *NOUI
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 06/11/20  002266  BMA Created
     V* ==============================================================
      *
     D $TIMST          S               Z   INZ
     D $TIMEN          S               Z   INZ
     D $TIMMS          S             10I 0
      *
     D $COUNT          S             10I 0
      *
     D $MSG            S             52
     D $ARRAY          S             50    DIM(1000)
      *
     D DSTEST          DS           150
     D DSFLD1                        50
     D DSFLD2                       100
      *
      *--------------------------------------------------------------------------------------------*
      * ENTRY
      *--------------------------------------------------------------------------------------------*
     D  $METHOD        S             10
     D  $RESULT        S             50
      *--------------------------------------------------------------------------------------------*
     D  $RESPGM        S             50
      *--------------------------------------------------------------------------------------------*
      * Log MAIN - Start time
     C                   TIME                    $TIMST
      * Impostazioni iniziali
     C                   EXSR      IMP0
      *
1    C                   SELECT
     C                   WHEN      $METHOD='FUN1'
      * Internal routine
     C                   EXSR      RFUN1
     C                   WHEN      $METHOD='FUN2'
      * Call program ending in RT
     C                   EXSR      RFUN2
     C                   WHEN      $METHOD='FUN3'
      * Call program ending in LR
     C                   EXSR      RFUN3
     C                   WHEN      $METHOD='FUN4'
      * Call 'doped' java program
     C                   EXSR      RFUN4
1e   C                   ENDSL
      *
     C     G9MAIN        TAG
      *
     C                   EXSR      FIN0
      * Log MAIN - End time
     C                   TIME                    $TIMEN
      * Elapsed time
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
     C                   EVAL      $TIMMS=$TIMMS/1000
      * Display message
     C                   EVAL      $MSG='End MAIN RPG pgm - '
     C                                 +%TRIM($METHOD)
     C                                 +' - '+%TRIM($RESULT)
     C                                 +' - '+%TRIM(%EDITC($TIMMS:'Q'))
     C                                 +'ms'
     C                   DSPLY                   $MSG
      *
     C                   SETON                                        RT
      *--------------------------------------------------------------*
    RD*
      *--------------------------------------------------------------*
     C     IMP0          BEGSR
      *
     C                   CLEAR                   $RESULT
     C                   CLEAR                   $RESPGM
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Method FUN1
      *--------------------------------------------------------------*
     C     RFUN1         BEGSR
      *
     C                   EVAL      $COUNT=$COUNT+1
     C                   EVAL      $RESULT=%EDITC($COUNT:'Z')
     C                   IF        $COUNT<=1000
     C                   EVAL      $ARRAY($COUNT)=$RESULT
     C                   ENDIF
      *
     C                   IF        $COUNT<=10 OR $COUNT>=10
     C                   EVAL      DSFLD1=%EDITC($COUNT:'X')
     C                   EVAL      DSFLD2=$RESULT
     C                   ENDIF
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Method FUN2
      *--------------------------------------------------------------*
     C     RFUN2         BEGSR
      *
     C                   CALL      'MUTE10_75A'
     C                   PARM                    $RESPGM
      *
     C                   EVAL      $RESULT=$RESPGM
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Method FUN3
      *--------------------------------------------------------------*
     C     RFUN3         BEGSR
      *
     C                   CALL      'MUTE10_75B'
     C                   PARM                    $RESPGM
      *
     C                   EVAL      $RESULT=$RESPGM
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Method FUN4
      *--------------------------------------------------------------*
     C     RFUN4         BEGSR
      *
     C                   CALL      'MUTE10_75J'                         37
     C                   PARM                    $RESPGM
      *
     C                   EVAL      $RESULT=$RESPGM
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*
      *--------------------------------------------------------------*
     C     *INZSR        BEGSR
      *
      * ENTRY
     C     *ENTRY        PLIST
     C                   PARM                    $METHOD
     C                   PARM                    $RESULT
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*
      *--------------------------------------------------------------*
     C     FIN0          BEGSR
      *
     C                   ENDSR
