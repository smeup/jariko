     D                 DS
     D FIRST                               DIM(9999)
     D  FIRST_F1                           LIKE(SECOND_F2) OVERLAY(FIRST:1)
     D  FIRST_F2                           LIKE(SECOND_F1) INZ(0)
     D                                     OVERLAY(FIRST:*NEXT)

     D SECOND          DS          5000
     D  SECOND_F1                     8  0
     D  SECOND_F2                     1
