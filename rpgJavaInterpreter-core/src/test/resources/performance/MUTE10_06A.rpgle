      *====================================================================
      * smeup V6R1.025DV
      * Nome sorgente       : MUTE10_06A
      * Sorgente di origine : QTEMP/MUSRC(MUTE10_06A)
      * Esportato il        : 20241004 155649
      *====================================================================
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 11/12/19  001362  BERNI Creato
     V* 11/12/19  V5R1    BMA   Check-out 001362 in SMEUP_TST
     V* 11/12/19  001368  BMA   Workaround for PARM syntax
     V* 11/12/19  V5R1    BERNI Check-out 001368 in SMEUP_TST
     V* 13/12/19  001378  BMA   Corretta annotation
     V* 16/12/19  001378  BMA   Adeguamenti
     V* 17/12/19  V5R1    PEDSTE Check-out 001378 in SMEUP_TST
     V* 23/12/19  V5R1    PEDSTE Check-out 001362 001368 001378 in SMEDEV
     V* 06/09/23  005098  BERNI  Ridenominato partendo da MUTE10_06 per nomenclatura sbagliata
     V* 07/09/23  V6R1    BMA Check-out 005098 in SMEDEV
     V*=====================================================================
     D*  Pgm testing performance with big array
     V*---------------------------------------------------------------------
     D FIRAR           S          10000    DIM(500)                             First Array
     D ENDAR           S          10000    DIM(500)                             Final Array
     D $N              S              3  0
     D XXRET           S              1
      * Main
     C                   EXSR      F_EXEC
      *
      * Test entry parameter XXRET: 1=RT, Anything else=LR
     C                   IF        XXRET='1'
     C                   SETON                                        RT
     C                   ELSE
     C                   SETON                                        LR
     C                   ENDIF
      *
      *---------------------------------------------------------------------
    RD* Routine test Move of Array
      *---------------------------------------------------------------------
     C     F_EXEC        BEGSR
      *
      * Entry parameters
     C     *ENTRY        PLIST
     C                   PARM                    FIRAR
     C                   PARM                    XXRET             1
      * Array shift
     C                   EVAL      ENDAR=FIRAR
      *
     C                   CLEAR                   $N
      * Loop on Array
     C                   DO        500
     C                   EVAL      $N=$N+1
     C                   EVAL      FIRAR($N)=%TRIM(ENDAR($N))+' Final'          COSTANTE
     C                   ENDDO
      *
     C                   ENDSR
