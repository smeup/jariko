   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 11/12/19  001362  BERNI  Creato
     V* 11/12/19  V5R1    BMA   Check-out 001362 in SMEUP_TST
     V*=====================================================================
     D*  Pgm testing performance with big array
     V*---------------------------------------------------------------------
     D FIRAR           S          10000    DIM(500)                             First Array
     D ENDAR           S          10000    DIM(500)                             Final Array
     D $N              S              3  0
      * Main
     C                   EXSR      F_EXEC
      *
    MU* Type="NOXMI"
    MU* TIMEOUT(7500)
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
