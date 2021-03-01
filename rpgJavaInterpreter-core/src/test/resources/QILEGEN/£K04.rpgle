      /IF NOT DEFINED(K04_INCLUDED)
      /DEFINE K04_INCLUDED
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 06/04/13  V3R2    BS Creazione
     V* 16/04/13  V3R2    BS Aggiunto Campo £K04FO
     V* 06/09/15  V4R1    BS Deviazione B£K04S per funzioni SCA
     V* 22/08/17  V5R1   BMA Aggiunto livello chiamata
     D* ==============================================================
     D*----------------------------------------------------------------
     D* OBIETTIVO
     D*
     D* PREREQUISITI
     D*   D/COPY QILEGEN,£K04DS
     D*
     D*----------------------------------------------------------------
     C     £K04          BEGSR
      *
1    C                   IF        £K04PG=' '
2    C                   IF        %SUBST(£K04FU:1:3)='SCA'
     C                   EVAL      £K04PG='B£K04S'+£K04LC
2x   C                   ELSE
     C                   EVAL      £K04PG='B£K04G'+£K04LC
2e   C                   ENDIF
1e   C                   ENDIF
      *
1   >C                   IF        ££B£2J = '1'
    >C                   CALL      £K04PG                               37
    >C                   PARM                    £K04FU
    >C                   PARM                    £K04ME
    >C                   PARM                    £K04MS
    >C                   PARM                    £K04FI
    >C                   PARM                    £K04VA
    >C                   PARM                    £K04CM
    >C     *IN35         PARM      *OFF          £K0435
    >C     *IN36         PARM      *OFF          £K0436
    >C                   PARM                    £K04DI
    >C                   PARM                    £K04DO
    >C                   PARM                    £K04FO
  M > *
1x  >C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      £K04PG
     C                   PARM                    £K04FU
     C                   PARM                    £K04ME
     C                   PARM                    £K04MS
     C                   PARM                    £K04FI
     C                   PARM                    £K04VA
     C                   PARM                    £K04CM
    >C     *IN35         PARM      *OFF          £K0435
    >C     *IN36         PARM      *OFF          £K0436
     C                   PARM                    £K04DI
     C                   PARM                    £K04DO
    >C                   PARM                    £K04FO
      *
1e  >C                   ENDIF
      *
1   >C   37              DO
    >C                   CALL      'B£GGP0  '
    >C                   PARM      £K04PG        £GGPNP           10
    >C                   PARM      *BLANKS       £GGPTP           10
    >C                   PARM      *BLANKS       £GGPPA          100
1e  >C                   ENDDO
      *
     C                   CLEAR                   £K04PG           10
     C                   CLEAR                   £K04LC            1
      *
     C                   ENDSR
      /ENDIF
