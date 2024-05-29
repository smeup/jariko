     C     £D5C          BEGSR
     C                   SELECT
     C     £D5CFU        WHENEQ    'CAR'
     C                   MOVEL(P)  D$TIPA        £D5CCO
     C                   MOVEL(P)  D$TROT        £D5CTE
     C                   MOVEL(P)  D$CODI        £D5CCD
     C                   MOVEL(P)  D$COD1        £D5CC1
     C                   MOVEL(P)  D$COD2        £D5CC2
     C                   MOVEL(P)  D$COD3        £D5CC3
     C                   MOVEL(P)  D$DTVA        £D5CPE
     C                   MOVEL(P)  'CAL'         £D5CFU
     C     £D5CFU        WHENEQ    'SIG'
     C                   Z-ADD     0             D50
     C                   ENDSL
     C                   MOVEL(P)  'D5CO01E'     £D5CPG           10
     C                   CAT       £D5CLC:0      £D5CPG
  MO>C                   IF        ££B£2J = '1'
  MO>C                   CALL      £D5CPG                               37
  MO>C                   PARM                    £D5CFU           10            Funzione
  MO>C                   PARM                    £D5CME           10            Metodo
  MO>C                   PARM                    £D5CCO           12            tab D5S
  MO>C                   PARM                    £D5CCD           15            codice
  MO>C                   PARM                    £D5CTE            3            Estensione
  MO>C                   PARM                    £D5CC1           15            codice 1
  MO>C                   PARM                    £D5CC2           15            codice 2
  MO>C                   PARM                    £D5CC3           15            codice 3
  MO>C                   PARM                    £D5CPE           10            data/periodo
  MO>C                   PARM      D50           D51
  MO>C                   PARM                    D52
  MO>C     D50           PARM                    D53
  MO>C                   PARM      *BLANKS       £D5CCM            2            Ultimo Cmd
  MO>C                   PARM                    £D5CMS            7            MSG
  MO>C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      £D5CPG
     C                   PARM                    £D5CFU           10            Funzione
     C                   PARM                    £D5CME           10            Metodo
     C                   PARM                    £D5CCO           12            tab D5S
     C                   PARM                    £D5CCD           15            codice
     C                   PARM                    £D5CTE            3            Estensione
     C                   PARM                    £D5CC1           15            codice 1
     C                   PARM                    £D5CC2           15            codice 2
     C                   PARM                    £D5CC3           15            codice 3
     C                   PARM                    £D5CPE           10            data/periodo
     C                   PARM      D50           D51
     C                   PARM                    D52
     C     D50           PARM                    D53
     C                   PARM      *BLANKS       £D5CCM            2            Ultimo Cmd
     C                   PARM                    £D5CMS            7            MSG
  MO>C                   ENDIF
  MO>C   37              DO
  MO>C                   CALL      'B£GGP0  '
  MO>C                   PARM      £D5CPG        £GGPNP           10
  MO>C                   PARM      *BLANKS       £GGPTP           10
  MO>C                   PARM      *BLANKS       £GGPPA          100
  MO>C                   ENDDO
     C     1             IFEQ      2
     C                   MOVEL     *BLANKS       D$TIPA           12
     C                   MOVEL     *BLANKS       D$TROT            3
     C                   MOVEL     *BLANKS       D$CODI           15
     C                   MOVEL     *BLANKS       D$COD1           15
     C                   MOVEL     *BLANKS       D$COD2           15
     C                   MOVEL     *BLANKS       D$COD3           15
     C                   MOVEL     *BLANKS       D$DTVA           10
     C                   MOVEL     £D5CLC        £D5CLC            1
     C                   ENDIF
     C                   ENDSR
