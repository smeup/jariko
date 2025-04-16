      *C                   CALL      'BIG_DO_LOOP'                        30
      *C                   SETON                                        LR

     FVERAPG7L  IF   E           K DISK

      * Execute subroutine
     C                   EXSR      TSTREC

     C     TSTREC        BEGSR
      * Create key
     C     K7L           KLIST
     C                   KFLD                    $$COMM
     C                   KFLD                    $$NAME
     C                   KFLD                    $$DATE
      * Write a Record
      * Set variables
     C                   EVAL      $$COMM=''
     C                   EVAL      $$NAME='BUSFIO'
     C                   EVAL      $$DATE=20210117
     C                   EVAL      $FNDREC=0
      * Read cicle
     C     K7L           SETLL     VERAPG7L
     C                   DO        *HIVAL
     C     K7L           READE     VERAPG7L
      * End of File - Exit
     C                   IF        %EOF
     C                   LEAVE
     C                   ENDIF

     C                   ENDDO
     C                   ENDSR
