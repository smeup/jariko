     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 02/10/18  V5R1   BMA Creazione
     V*=====================================================================
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
     D $TIME           S               Z

     D AAA020          S             20
     D DayofWeek       S              1  0
      *
     C                   TIME                    $TIME
     C     $TIME         Subdur    D'1899-12-30' DayofWeek:*D
     C                   Div       7             DayOfWeek
     C                   Mvr                     DayOfWeek
     C*
     C                   If        DayOfWeek < 1.
     C                   Eval      DayOfWeek = DayOfWeek + 7.
     C                   Endif
     C                   EVAL      AAA020=%EDITC(DayOfWeek:'X')
     C                   DSPLY                   AAA020
      *
     C                   SETON                                        RT

