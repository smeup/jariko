     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 27/03/23  004738  GUAGIA  Creazione
     V*=====================================================================
     D* OBJECTIVE
    MD*
     D* External RPG called for PARMS parameters (see MUTE12_15)
     D*  4 parameters
     V*---------------------------------------------------------------------
    M *---------------------------------------------------------------------
      /COPY QILEGEN,£PDS
     D PRMINT1         S              5  0
     D PRMINT2         S              5  0
     D RTRNPARAM       S              5  0
     D PRMSTR1         S             10
     D PRMSTR2         S             10
      *
      *
     C     *ENTRY        PLIST
     C                   PARM                    PRMINT1
     C                   PARM                    PRMSTR1
     C                   PARM                    PRMINT2
     C                   PARM                    PRMSTR2
      *
    MU* VAL1(RTRNPARAM) VAL2(4) COMP(EQ)
     C                   EVAL      RTRNPARAM=£PDSPR
     C                   SETON                                        LR
      *---------------------------------------------------------------------
