      *====================================================================
      * smeup V6R1.021DV
      * Nome sorgente       : £C5P
      * Sorgente di origine : SMEDEV/QILEGEN(£C5P)
      * Esportato il        : 20240411 144730
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*===============================================================
     V* 24/12/07  V2R3  i BS Estese Tipologie Pertinenza/Condizione
     V* 24/06/10  V2R3    SP Aggiunta chiamata per generazione SQL e mod. ENTRY C5C5P0
     V* C500823A  V3R1    BS Aggiunto controllo opzionale per il flag 19
     V*===============================================================
     D* Obiettivo
     D*--------------------------------------------------------------
     D* Filtrare un record contabile in funzione della condizione
     D* di pertinenza e condizione
     D* ESEMPIO DI CHIAMATA
     D*C                   MOVEL     <xxFL01>      £C5P01
     D*C                   MOVEL     <xxFL02>      £C5P02
     D*C                   MOVEL     <xxFL05>      £C5P05
     D*C                   MOVEL     <Pert-2carat> £C5PPE
     D*C                   MOVEL     <Cond-2carat> £C5PCO
     D*C                   EXSR      £C5P
     D*C                   IF        *IN35
     D*C                   ITER
     D*C* Se servono i caratteri speciali
     D*C                   EXSR      £C5PES
     D*C                   ENDIF
     D* ESEMPIO DI CHIAMATA COSTRUZIONE SQL
     D*C                   EVAL      £C5PFU='SQL'
     D*C                   EVAL      £C5PPR=<Prefisso> (es: per C5RREG0F: 'R5')
     D*C                   EVAL      £C5PPE=<Pert-2carat>
     D*C                   EVAL      £C5PCO=<Cond-2carat>
     D*C                   EXSR      £C5P
     D*C                   IF        *IN35=*OFF
     D*C                   EVAL      SQL_SRTING=%TRIM(SQL_SRTING)+' AND '+£C5PSQ
     D*C                   ELSE
     D*C                    ... ERRORE COSTRUZIONE SQL
     D*C                   ENDIF
      *----------------------------------------------------------------
     C     £C5P          BEGSR
1    C                   SELECT
      *  .. costruzione SQL
1x   C                   WHEN      £C5PFU='SQL'
     C                   CALL      'C5C5P0'
     C                   PARM      'SQL'         £C5PFU           10
     C                   PARM      *BLANKS       £C5PME           10
     C                   PARM                    £C5P01            1
     C                   PARM                    £C5P02            1
     C                   PARM                    £C5P05            1
     C                   PARM                    £C5PPE            2
     C                   PARM                    £C5PCO            2
     C     *IN35         PARM      *OFF          £C5P35            1
     C                   PARM                    £C5PPR           10            prefisso campi
     C                   PARM                    £C5PSQ          200            stringa SQL
     C                   PARM                    £C5PKO            1
     C                   PARM                    £C5P19            1
      *  .. altre funzioni
1x   C                   OTHER
      *
     C                   SETOFF                                       35
      *
     C                   IF        £C5PKO<>' ' AND £C5P19=' '
     C                   EVAL      *IN35=*ON
     C                   ENDIF
      *
2    C                   IF        £C5PPE = '  '
     C                   EVAL      £C5PPE = '12'
2e   C                   ENDIF
2    C                   IF        £C5PCO = '  '
     C                   EVAL      £C5PCO = '66'
2e   C                   ENDIF
      *
2    C*********          IF        £C5P01 < %SUBST(£C5PPE:01:01)
2    C*********                    OR £C5P01 > %SUBST(£C5PPE:02:01)
     C*********          SETON                                        35
1    C*********          ENDIF
2    C                   IF        £C5PPE<>' 9' AND %SUBST(£C5PPE:1:1)<>'*'
3    C                   IF        £C5P01 <> %SUBST(£C5PPE:01:01)
     C                             AND £C5P01 <> %SUBST(£C5PPE:02:01)
     C                   SETON                                        35
3e   C                   ENDIF
2e   C                   ENDIF
      *
2    C*********          IF        £C5P02 < %SUBST(£C5PCO:01:01)
2    C*********                    OR £C5P02 > %SUBST(£C5PCO:02:01)
     C*********          SETON                                        35
1    C*********          ENDIF
2    C                   IF        £C5PCO<>' 9' AND %SUBST(£C5PCO:1:1)<>'*'
3    C                   IF        £C5P02 <> %SUBST(£C5PCO:01:01)
     C                             AND £C5P02 <> %SUBST(£C5PCO:02:01)
     C                   SETON                                        35
3e   C                   ENDIF
2e   C                   ENDIF
      *
2    C                   IF        NOT(*IN35) AND
     C                             (%SUBST(£C5PPE:1:1)='*' OR
     C                              %SUBST(£C5PCO:1:1)='*')
     C                   CALL      'C5C5P0'
     C                   PARM      'FIL'         £C5PFU           10
     C                   PARM      *BLANKS       £C5PME           10
     C                   PARM                    £C5P01            1
     C                   PARM                    £C5P02            1
     C                   PARM                    £C5P05            1
     C                   PARM                    £C5PPE            2
     C                   PARM                    £C5PCO            2
     C     *IN35         PARM      *OFF          £C5P35            1
     C                   PARM                    £C5PKO            1
     C                   PARM                    £C5P19            1
2e   C                   ENDIF
      *
     C                   CLEAR                   £C5P01            1
     C                   CLEAR                   £C5P02            1
     C                   CLEAR                   £C5P05            1
     C                   CLEAR                   £C5P19            1
     C                   CLEAR                   £C5PPE            2
     C                   CLEAR                   £C5PCO            2
     C                   CLEAR                   £C5PKO            1
      *
1e   C                   ENDSL
      *
     C                   ENDSR
