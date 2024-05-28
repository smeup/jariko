     V* ==============================================================
     D* 09/05/24
     D* Purpose: Must fire the following errors
     D* line 9 - Factor 1 can't be null
     D* line 14 - Factor 2 can't be null
     V* ==============================================================

     C                   SETOFF                                           55
     C     *IN55         DOUEQ
     C                   SETON                                            55
     C                   ENDDO

     C                   SETOFF                                           55
     C                   DOUEQ     *ON
     C                   SETON                                            55
     C                   ENDDO