     D STR2            S              2    INZ('AB')
     D STR3            S              3    INZ('XYZ')
     D STR4            S              4    INZ('CDEF')
     D STR6            S              6    INZ('GHIJKL')
      *
     D RES1            S              6
     D RES2            S              6
     D RES3            S              6    INZ('AB    ')
     D RES4            S              6
     D RES5            S              6    INZ('AB    ')
     D RES6            S              6    INZ('99    ')
     D DBG             S             52
      *
      * CAT('AB','CDEF') '      ' -> 'ABCDEF'
     C     STR2          CAT(P)    STR4          RES1
     C                   EVAL      DBG='('+RES1+')'
     C     DBG           DSPLY
      *
      * CAT('CDEF','GHIJKL') '      '  -> 'CDEFGH'
     C     STR4          CAT(P)    STR6          RES2
     C                   EVAL      DBG='('+RES2+')'
     C     DBG           DSPLY
      *
      * CAT('CD','EF') 'AB    ' -> 'CDEF  '
     C     'CD'          CAT(P)    'EF'          RES3
     C                   EVAL      DBG='('+RES3+')'
     C     DBG           DSPLY
      *
      * CAT('AB','CDEF':1) '      ' -> 'AB CDE'
     C     STR2          CAT(P)    STR4:1        RES4
     C                   EVAL      DBG='('+RES4+')'
     C     DBG           DSPLY
      *
      * CAT(-,'CDEF') 'AB    ' -> 'AB    '
     C                   CAT(P)    STR4          RES5
     C                   EVAL      DBG='('+RES5+')'
     C     DBG           DSPLY
      *
      * CAT(-,'XYZ':1) '99    ' -> '99 XYZ'
     C                   CAT(P)    STR3:1        RES6
     C                   EVAL      DBG='('+RES6+')'
     C     DBG           DSPLY
      *
     C                   SETON                                        LR