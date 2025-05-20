     D                 DS
     D SMEP                         200    DIM(10000)
     D  SMEP_G_P                     02  0 OVERLAY(SMEP:01)
     D  SMEP_G_A                     02  0 OVERLAY(SMEP:*NEXT)
     D  SMEP_P01                     07  0 OVERLAY(SMEP:*NEXT)
     D  SMEP_P02                     07  0 OVERLAY(SMEP:*NEXT)
     D  SMEP_TVA                     20    OVERLAY(SMEP:*NEXT)
     D  SMEP_VAL                    100    OVERLAY(SMEP:*NEXT)

     C                   CLEAR                   SMEP_G_P
     C                   CLEAR                   SMEP_G_A
     C                   CLEAR                   SMEP_P01
     C                   CLEAR                   SMEP_P02
     C                   CLEAR                   SMEP_TVA
     C                   CLEAR                   SMEP_VAL