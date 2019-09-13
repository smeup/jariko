      *
      * Extract all blank-delimited words from a sentence.
      * source: IBM Information Center
      *
      *
     D SENTENCE        S            128A   INZ('Lorem ipsum dolor sit amet sed')
     D WORDCNT         S              8  0 INZ(0)
     D WORDINC         S              8  0 INZ(0)
     D I               S              8  0 INZ(0)
     D J               S              8  0 INZ(0)
     D ONE             S              8  0 INZ(1)
     D EOF             S              1A   INZ('#')
     D TMP             S              1A   INZ(' ')
     D WORDS           S             20A   DIM(128)
     D DSP             S             20A
     D U$FUNZ          S             20A
     D U$METO          S             20A
     D U$SVARSK        S             20A
      *
      * PARAM test
     C     *ENTRY        PLIST
     C                   PARM                    U$FUNZ
     C                   PARM                    U$METO
     C                   PARM                    U$SVARSK
      *
     C                   EVAL   TMP = U$FUNZ
      * CLEAR test
     C                   CLEAR                   WORDCNT
      * MOVE test
     C                   MOVE      ONE           WORDINC
     C                   FOR I = 1 BY WORDINC TO %LEN(SENTENCE)
      * Is there a blank?
     C                   IF %SUBST(SENTENCE: I: 1) = ' '
     C                      EVAL   WORDINC = 1
      * ITER test
     C                      ITER
     C                   ENDIF
      * We've found a word - determine its length
     C                   FOR J = I+1 TO %LEN(SENTENCE)
     C                   IF %SUBST(SENTENCE: J: 1) = ' '
      * LEAVE test
     C                      LEAVE
     C                   ENDIF
     C                   ENDFOR
     C                   EVAL      WORDINC = J - I
     C                   EVAL      WORDCNT = WORDCNT + 1
      * does not work
      *C                   EVAL      WORDS(WORDCNT) = %SUBST(SENTENCE: I: WORDINC )
     C                   SELECT
     C                   WHEN     WORDINC = 5
     C                    EVAL     WORDS(WORDCNT) = %SUBST(SENTENCE: I: 5)
     C                   WHEN     WORDINC = 4
     C                    EVAL     WORDS(WORDCNT) = %SUBST(SENTENCE: I: 4)
     C                   WHEN     WORDINC = 3
     C                    EVAL     WORDS(WORDCNT) = %SUBST(SENTENCE: I: 3)
     C                   WHEN     WORDINC = 2
     C                    EVAL     WORDS(WORDCNT) = %SUBST(SENTENCE: I: 3)
     C                   WHEN     WORDINC = 1
     C                    EVAL     WORDS(WORDCNT) = %SUBST(SENTENCE: I: 3)
     C                   ENDSL
     C                   ENDFOR
     C
      * MOVE test
     C                   MOVE      EOF            TMP
      * marks the end of list
     C                   EVAL     WORDS(WORDCNT+1) = TMP
     C                   EXSR      PRINT
     C                   SETON                                        LR
      *--------------------------------------------------------------*
      * Shows the results
      *--------------------------------------------------------------*
     C     PRINT         BEGSR
     C                   FOR I = 1 TO %ELEM(WORDS)
     C                   IF %TRIM(WORDS(I) : 1) = EOF
     C                    LEAVE
     C                   ENDIF
     C                   EVAL   DSP = %CHAR(I) + ' - ' + WORDS(I)
     C                   DSPLY                     DSP
     C                   ENDFOR
     C                   ENDSR
      *--------------------------------------------------------------*



















