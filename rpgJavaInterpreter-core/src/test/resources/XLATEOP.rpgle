     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 02/01/24  000000  COSANT Creazione
     V*=====================================================================
     D STRING1         S             11
     D RESULT1         S             11
     D STRING2         S             26
     D RESULT2         S             26
     D STRING3         S              8
     D RESULT3         S             52
     D V1              S              1
     D V2              S              1
     D A               S             27
     D B               S             26
     D UP              S             26
     D LO              S             26
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      * In the following example, all values in STRING1 are translated.
      * All empty space will be replaced with a dash character.
      * As a result, RESULT='999-999-999'.
     C                   EVAL      STRING1= '999 999 999'
     C                   EVAL      V1= ' '
     C                   EVAL      V2= '-'
     C     V1:V2         XLATE     STRING1       RESULT1
     C     RESULT1       DSPLY
      *
      * In the following example, all values in STRING2 are translated.
      * Each character in A will be replaced with the corresponding
      * character in B. Eg: K -> V, e -> a .. etc.
      * As a result, RESULT2='http://xxx.smaup.comuuuuuu'.
     C                   EVAL      A= 'Key                       w'
     C                   EVAL      B= 'Value                    u'
     C                   EVAL      STRING2= 'http://xxx.smeup.com      '
     C     A:B           XLATE     STRING2       RESULT2
     C     RESULT2       DSPLY
      *
      * In the following example, all values in STRING3 are translated to
      * uppercase.  As a result, RESULT3='RPG DEPT'.
     C                   EVAL      UP= 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'
     C                   EVAL      LO= 'abcdefghijklmnopqrstuvwxyz'
     C                   EVAL      STRING3= 'rpg dept'
     C     LO:UP         XLATE     STRING3       RESULT3
     C     RESULT3       DSPLY
      *
      * In the following example, all values in STRING are translated to
      * lowercase starting from position 6. As a result, RESULT3='RPG Dept'.
     C     UP:LO         XLATE     RESULT3:6     RESULT3
     C     RESULT3       DSPLY
      *
     C                   SETON                                        LR
