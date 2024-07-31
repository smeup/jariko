     D $FOO            S                   LIKE(£FOO)
     D STR             S             15    INZ('HELLO THERE')

     C                   EXSR      SUB01

     C     *INZSR        BEGSR
     C                   EXSR      £INIZI
     C                   ENDSR


     C     £INIZI        BEGSR
     C     *ENTRY        PLIST
     C     $FOO          PARM                    £FOO             10
     C                   ENDSR


     C     SUB01         BEGSR
     C                   IF        $FOO <> 'BAR'
     C     STR           DSPLY
     C                   ENDIF
     C                   ENDSR

      /API QILEGEN,£OAV