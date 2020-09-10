     D DSTEMP          DS            64
     D VAIDOJ                  1     10A
     D VAATV0                 11     11A
     D VADATA                 12     19S 0
     D VANOME                 20     34A
     D VACDC                  35     49A
     D VACOD1                 50     64A
      *--------------------------------------------------------------------------------------------*
     D ARRAGE          S             78A   DIM(12) CTDATA PERRCD(1)             _NOTXT
     D $X              S              2  0
      *--------------------------------------------------------------------------------------------*
     C                   EVAL      $X = 2
     C*                  EVAL      VAATV0 = %SUBST(ARRAGE($X):11:1 )
     C*                  EVAL      VADATA = %INT(%SUBST(ARRAGE($X):12:8 ))
     C*                  EVAL      VANOME = %SUBST(ARRAGE($X):20:15)
     C                   EVAL      VACDC  = %SUBST(ARRAGE($X):35:15)
     C                   EVAL      VACOD1 = %SUBST(ARRAGE($X):50:15)
     C     VADATA        DSPLY
     C     VANOME        DSPLY
     C                   SETON                                        LR
** CTDATA ARRAGE
0002001790220200901BEN122                        ERB
0002000423220200901BERWSD                        ERB
0002001467220200901BUSDFO                        ERB
0002001754 20200901CRISRS                        ERB
0001998829220200901DE234O                        ERB
0002000088220200901FACDDO                        ERB
0002000381220200901ARMFDD                        NOV
0001999049 20200901BOFDGD                        NOV
0002001975 20200901CAMSGF                        NOV
0002001679220200901DEBFGF                        NOV
0002001976220200901DESGFV                        NOV
