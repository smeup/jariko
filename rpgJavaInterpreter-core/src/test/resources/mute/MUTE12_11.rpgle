     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 23/03/23  004738  GUAGIA  Creazione
     V* 27/03/23  004738  GUAGIA  Fix righe
     V*=====================================================================
     D* OBJECTIVE
     D*
     D*
     D*
      *---------------------------------------------------------------------
     D PRMINT1         S              5  0
     D PRMINT2         S              5  0
     D PRMSTR1         S             10
     D PRMSTR2         S             10
      /COPY QILEGEN,£PDS
      *---------------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------------
      * Only 2 Parametes
     U* VAL1(£PDSSC) VAL2(0) COMP(EQ)
     C                   CALL      'MUTE12_11A'
    MC                   PARM      20            PRMINT1
     C                   PARM      'Hello'       PRMSTR1                        COSTANTE
      *
    M * Only 3 Parametes
     U* VAL1(£PDSSC) VAL2(0) COMP(EQ)
     C                   CALL      'MUTE12_11B'
     C                   PARM      30            PRMINT1
     C                   PARM      'World'       PRMSTR1                        COSTANTE
     C                   PARM      30            PRMINT2
      *
      * Only 4 Parametes
     U* VAL1(£PDSSC) VAL2(0) COMP(EQ)
     C                   CALL      'MUTE12_11C'
     C                   PARM      40            PRMINT1
     C                   PARM      'Hello'       PRMSTR1                        COSTANTE
     C                   PARM      40            PRMINT2
     C                   PARM      'World'       PRMSTR2                        COSTANTE
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------------
