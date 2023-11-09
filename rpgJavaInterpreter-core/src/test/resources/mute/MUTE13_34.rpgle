     V*=====================================================================
     V* MODIFICHE Rel.  T Au Description
     V* dd/mm/yy  nn.mm i xx Short description
     V*=====================================================================
     V* 09/02/23  004636  BUSFIO Creazione
     V*=====================================================================
     D*  OBIETTIVO
     D*  Finalized program operation of the OCCUR operational code in Smeup
     D*
     V*=====================================================================
      *  $ADS and $UDS are multiple occurrence data structures
     D $ADS            DS                  OCCURS(2)
     D  $COD                          2  0
     D  $DES                         20
     D $UDS            DS                  OCCURS(3)
     D  $USA                          3  0
     D  $USB                         10
     D  $USC                          1
      *
     D $U              S              1  0
     D $RES            S              5  0
     D $N              S              5  0
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      *
     C                   CLEAR                   $ADS
     C                   CLEAR                   $UDS
      * Set $ADS
     C     1             OCCUR     $ADS
     C                   EVAL      $COD = 11
     C                   EVAL      $DES = 'Articolo AR'                         COSTANTE
     C     2             OCCUR     $ADS
     C                   EVAL      $COD = 22
     C                   EVAL      $DES = 'Collaboratore CNCOL'                 COSTANTE
      * Set $UDS
     C     1             OCCUR     $UDS
     C                   EVAL      $USA = 1
     C                   EVAL      $USB = 'PROVA 1'                             COSTANTE
     C                   EVAL      $USC = 'A'
     C     2             OCCUR     $UDS
     C                   EVAL      $USA = 14
     C                   EVAL      $USB = 'PROVA 2'                             COSTANTE
     C                   EVAL      $USC = 'B'
     C     3             OCCUR     $UDS
     C                   EVAL      $USA = 114
     C                   EVAL      $USB = 'PROVA 3'                             COSTANTE
     C                   EVAL      $USC = 'C'
      *
      * $ADS is set to the first occurrence. The value of the current occurrence
      * of $ADS is placed in the result field $RES = 1.
    MU* VAL1($RES) VAL2(1) COMP(EQ)
     C     1             OCCUR     $ADS          $RES
      *
      * $ADS is set to the second occurrence using a variable $U. The value of the
      * current occurrence of $ADS is placed in the result field $RES = 2.
     C                   EVAL      $U=2
    MU* VAL1($RES) VAL2(2) COMP(EQ)
     C     $U            OCCUR     $ADS          $RES
      *
      * $UDS is set to the third occurrence. The values of $USA is 114
    MU* VAL1($USA) VAL2(114) COMP(EQ)
     C     3             OCCUR     $UDS
      *  The current occurrence of $UDS is the third occurrence, so $UDS is still
      * in third occurence. So the value of $USC is 'C'.
    MU* VAL1($USC) VAL2('C') COMP(EQ)
     C                   OCCUR     $UDS
      *
      * $UDS is set to the second occurrence using $U variable.
      * The values of $USA is 14
     C                   EVAL      $U=2
    MU* VAL1($USA) VAL2(14) COMP(EQ)
     C     $U            OCCUR     $UDS
      * The current occurrence of $UDS is the second occurrence, so $UDS is still
      * in second occurence. So the value of $USBC is 'PROVA 2   ' and $USC is 'B'.
    MU* VAL1($USB) VAL2('PROVA 2   ') COMP(EQ)
     C                   OCCUR     $UDS
    MU* VAL1($USC) VAL2('B') COMP(EQ)
     C                   OCCUR     $UDS
      *
      *  $ADS is set to the current occurrence of $UDS.
      *  So the occurrence of $ADS is 2.
      *  The result field, $RES, contains the value 2.
    MU* VAL1($RES) VAL2(2) COMP(EQ)
     C     $UDS          OCCUR     $ADS          $RES
    MU* VAL1($COD) VAL2(22) COMP(EQ)
     C                   OCCUR     $ADS
      *
      *  The $UDS's occurrence change by using index $N in the cicle.
      *  In particular, when the $UDS's occurence is the second, clear $USB.
      *  The result field, $RES, contains the value 2.
     C                   FOR       $N=1 TO 3
     C     $N            OCCUR     $UDS
     C                   IF        $N = 2
     C                   CLEAR                   $USB
     C                   ENDIF
     C                   ENDFOR
    MU* VAL1($USB) VAL2('          ') COMP(EQ)
     C     2             OCCUR     $UDS
      *
     C                   SETON                                        LR
      /COPY QILEGEN,£INZSR
      *---------------------------------------------------------------
     C     £INIZI        BEGSR
     C                   ENDSR
      *---------------------------------------------------------------
