     D NBR             S              8  0
     D MSG             S             50
     C     1             IFEQ       1
     C                   CALL      'CAL02'
     C                   PARM                    NBR
     C                   ENDIF
     C     1             DO        4
     C                   CALL      'CAL02'
     C                   PARM                    NBR
     C                   ENDDO
     C                   SETON                                        LR
      *--------------------------------------------------------------*
