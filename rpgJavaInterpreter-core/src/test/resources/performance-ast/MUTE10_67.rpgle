     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 06/11/20  002323  BERNI Creato
     V*=====================================================================
     D $N              S             10  0
     D $A              S             10
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      *
     C                   ADD       1             $N
     C                   EVAL      $N=$N-1
     C                   MOVEL     1             $N
     C                   EVAL      $A='A'
     C                   MOVEL     'B'           $A
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
    RD* Initial subroutine
      *--------------------------------------------------------------*
     C     *INZSR        BEGSR
      *
     C     *ENTRY        PLIST
     C                   PARM                    U$FUNZ           10
     C                   PARM                    U$METO           10
     C                   PARM                    U$VAR1           10
     C                   PARM                    U$VAR2           15
     C                   PARM                    U$VAR3           20
     C                   PARM                    U$VAR4           25
     C                   PARM                    U$VAR5           30
     C                   PARM                    U$VAR6           35
     C                   PARM                    U$VAR7           40
     C                   PARM                    U$VAR8           45
     C                   PARM                    U$VAR9           50
     C                   PARM                    U$VA11           10
     C                   PARM                    U$VA12           15
     C                   PARM                    U$VA13           20
     C                   PARM                    U$VA14           25
     C                   PARM                    U$VA15           30
     C                   PARM                    U$VA16           35
     C                   PARM                    U$VA17           40
     C                   PARM                    U$VA18           45
     C                   PARM                    U$VA19           50
     C                   PARM                    U$VA21           10
     C                   PARM                    U$VA22           15
     C                   PARM                    U$VA23           20
     C                   PARM                    U$VA24           25
     C                   PARM                    U$VA25           30
     C                   PARM                    U$VA26           35
     C                   PARM                    U$VA27           40
     C                   PARM                    U$VA28           45
     C                   PARM                    U$VA29           50
     C                   PARM                    U$VA31           10
     C                   PARM                    U$VA32           15
     C                   PARM                    U$VA33           20
     C                   PARM                    U$VA34           25
     C                   PARM                    U$VA35           30
     C                   PARM                    U$VA36           35
     C                   PARM                    U$VA37           40
     C                   PARM                    U$VA38           45
     C                   PARM                    U$VA39           50
     C                   PARM                    U$VA41           10
     C                   PARM                    U$VA42           15
     C                   PARM                    U$VA43           20
     C                   PARM                    U$VA44           25
     C                   PARM                    U$VA45           30
     C                   PARM                    U$VA46           35
     C                   PARM                    U$VA47           40
     C                   PARM                    U$VA48           45
     C                   PARM                    U$VA49           50
     C                   PARM                    U$VA50           50
     C                   PARM                    U$SVAR        21000
     C                   PARM                    U$IN35            1
      *
     C                   ENDSR
