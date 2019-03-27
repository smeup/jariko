     V*=====================================================================
     V* Date      Release Au Description
     V* dd/mm/yy  nn.mm   xx Brief description
     V*=====================================================================
     V* 27/03/19  V5R1    CM Creato
     V*=====================================================================
     H/COPY QILEGEN,£INIZH
      *---------------------------------------------------------------
     I/COPY QILEGEN,£TABB£1DS
     I/COPY QILEGEN,£PDS
      *---------------------------------------------------------------
     D                 DS
     D U$SVARSK                    1050    DIM(200)
     D  $$SVARCD                     50    OVERLAY(U$SVARSK:1)                  Name
     D  $$SVARVA                   1000    OVERLAY(U$SVARSK:*NEXT)              Value
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
     C                   EVAL      $$SVARCD(1)='Url'                            COSTANTE
     C                   EVAL      $$SVARVA(1)='http://xxx.Smeup.com'           COSTANTE
     C                   EVAL      $$SVARCD(2)='x'                              COSTANTE
     C                   EVAL      $$SVARVA(2)='w'                              COSTANTE
      *
     C                   CALL      'JD_001'
     C                   PARM      'INZ'         U$FUNZ           10
     C                   PARM                    U$METO           10
     C                   PARM                    U$SVARSK
     C                   PARM                    U$IN35            1
      *
     C                   EVAL      U$FUNZ='EXE'
     C                   CALL      'JD_001'
     C                   PARM                    U$FUNZ           10
     C                   PARM                    U$METO           10
     C                   PARM                    U$SVARSK
     C                   PARM                    U$IN35            1
      *
     C                   CALL      'JD_001'
     C                   PARM      'CLO'         U$FUNZ           10
     C                   PARM                    U$METO           10
     C                   PARM                    U$SVARSK
     C                   PARM                    U$IN35            1
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
     C/COPY QILEGEN,£INZSR
      *---------------------------------------------------------------
    RD* Initial subroutine (as *INZSR)
      *--------------------------------------------------------------*
     C     £INIZI        BEGSR
     C                   ENDSR
