     D £DBG_Pas        S              3
     D £DBG_Str        S           2000
      *
     D A50_N156        S             15P 6
     D A50_A1          S                   LIKE(£DBG_Str)
     D A50_N70         S              7P 0 Inz(0123456)
     D A50_N60         S              7S 0 Inz(-0123456)
     D A50_N72         S              7P 2 Inz(01234.56)
     D A50_N62         S              7S 2 Inz(-01234.56)
     D NNN             S              6  0 INZ(100000)
      *
     C                   EVAL      £DBG_Str=''
     C                   EXSR      SEZ_A50
      *
     C                   SETON                                        LR
      **************************************************************
     C     SEZ_A50       BEGSR
    OA* A£.BIFN(EDITC  )
     D* %EDITC tutti gli EDITC di una variabile con valori diversi
     C                   EVAL      £DBG_Pas='P01'
     C                   EVAL      £DBG_Str=''
     C                   EVAL      A50_N156=123,456
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+' A('
     C                   EXSR      SEZ_A50_A
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+%TRIMR(A50_A1)+') '
     C                   EVAL      A50_N156=0,000123
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+' B('
     C                   EXSR      SEZ_A50_A
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+%TRIMR(A50_A1)+') '
     C                   EVAL      A50_N156=12345,00
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+' C('
     C                   EXSR      SEZ_A50_A
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+%TRIMR(A50_A1)+') '
      * Expected:
      *'A(        123.456000 /         123.456000 /       123.456000'
      *' /       123.456000 /         123.456000   /         123.456'
      *'000   /       123.456000   /       123.456000   /         12'
      *'3.456000  /         123.456000  /       123.456000  /       '
      *'123.456000  /          123.456000 /          123.456000 /   '
      *'     123.456000 /        123.456000 / 000000123456000 /     '
      *'  123456000) B(           .000123 /            .000123 /    '
      *'      .000123 /          .000123 /            .000123   /   '
      *'         .000123   /          .000123   /          .000123  '
      *' /            .000123  /            .000123  /          .000'
      *'123  /          .000123  /             .000123 /            '
      *' .000123 /           .000123 /           .000123 / 000000000'
      *'000123 /             123) C(     12,345.000000 /      12,345'
      *'.000000 /     12345.000000 /     12345.000000 /      12,345.'
      *'000000   /      12,345.000000   /     12345.000000   /     1'
      *'2345.000000   /      12,345.000000  /      12,345.000000  / '
      *'    12345.000000  /     12345.000000  /       12,345.000000 '
      *'/       12,345.000000 /      12345.000000 /      12345.00000'
      *'0 / 000012345000000 /     12345000000)                      '
      *
     C     £DBG_Str      DSPLY
    OA* A£.BIFN(EDITC  )
     D* %EDITC formato codice 1,2,3,4
     C                   EVAL      £DBG_Pas='P02'
     C                   EVAL      £DBG_Str=
     C                              '1('+%EDITC(0000.00:'1')
     C                                  +%EDITC(A50_N70:'1')
     C                                  +%EDITC(A50_N60:'1')
     C                                  +%EDITC(A50_N72:'1')
     C                                  +%EDITC(A50_N62:'1')+') '
     C                             +'2('+%EDITC(0000.00:'2')
     C                                  +%EDITC(A50_N70:'2')
     C                                  +%EDITC(A50_N60:'2')
     C                                  +%EDITC(A50_N72:'2')
     C                                  +%EDITC(A50_N62:'2')+') '
     C                             +'3('+%EDITC(0000.00:'3')
     C                                  +%EDITC(A50_N70:'3')
     C                                  +%EDITC(A50_N60:'3')
     C                                  +%EDITC(A50_N72:'3')
     C                                  +%EDITC(A50_N62:'3')+') '
     C                             +'4('+%EDITC(0000.00:'4')
     C                                  +%EDITC(A50_N70:'4')
     C                                  +%EDITC(A50_N60:'4')
     C                                  +%EDITC(A50_N72:'4')
     C                                  +%EDITC(A50_N62:'4')+') '
      * Expected:
      *'1(     .00  123,456  123,456 1,234.56 1,234.56) 2(          '
      *'123,456  123,456 1,234.56 1,234.56) 3(    .00 123456 123456 '
      *'1234.56 1234.56) 4(        123456 123456 1234.56 1234.56)   '
      *
     C     £DBG_Str      DSPLY
    OA* A£.BIFN(EDITC  )
     D* %EDITC formato codice A,B,C,D
     C                   EVAL      £DBG_Pas='P03'
     C                   EVAL      £DBG_Str=
     C                              'A('+%EDITC(0000.00:'A')
     C                                  +%EDITC(A50_N70:'A')
     C                                  +%EDITC(A50_N60:'A')
     C                                  +%EDITC(A50_N72:'A')
     C                                  +%EDITC(A50_N62:'A')+') '
     C                             +'B('+%EDITC(0000.00:'B')
     C                                  +%EDITC(A50_N70:'B')
     C                                  +%EDITC(A50_N60:'B')
     C                                  +%EDITC(A50_N72:'B')
     C                                  +%EDITC(A50_N62:'B')+') '
     C                             +'C('+%EDITC(0000.00:'C')
     C                                  +%EDITC(A50_N70:'C')
     C                                  +%EDITC(A50_N60:'C')
     C                                  +%EDITC(A50_N72:'C')
     C                                  +%EDITC(A50_N62:'C')+') '
     C                             +'D('+%EDITC(0000.00:'D')
     C                                  +%EDITC(A50_N70:'D')
     C                                  +%EDITC(A50_N60:'D')
     C                                  +%EDITC(A50_N72:'D')
     C                                  +%EDITC(A50_N62:'D')+') '
      * Expected:
      *'A(     .00    123,456    123,456CR 1,234.56   1,234.56CR) B('
      *'            123,456    123,456CR 1,234.56   1,234.56CR) C(  '
      *'  .00   123456   123456CR 1234.56   1234.56CR) D(          1'
      *'23456   123456CR 1234.56   1234.56CR)                       '
      *
     C     £DBG_Str      DSPLY
    OA* A£.BIFN(EDITC  )
     D* %EDITC formato codice K,J,L,M
     C                   EVAL      £DBG_Pas='P04'
     C                   EVAL      £DBG_Str=
     C                              'K('+%EDITC(0000.00:'K')
     C                                  +%EDITC(A50_N70:'J')
     C                                  +%EDITC(A50_N60:'J')
     C                                  +%EDITC(A50_N72:'J')
     C                                  +%EDITC(A50_N62:'J')+') '
     C                             +'J('+%EDITC(0000.00:'J')
     C                                  +%EDITC(A50_N70:'J')
     C                                  +%EDITC(A50_N60:'J')
     C                                  +%EDITC(A50_N72:'J')
     C                                  +%EDITC(A50_N62:'J')+') '
     C                             +'L('+%EDITC(0000.00:'L')
     C                                  +%EDITC(A50_N70:'L')
     C                                  +%EDITC(A50_N60:'L')
     C                                  +%EDITC(A50_N72:'L')
     C                                  +%EDITC(A50_N62:'L')+') '
     C                             +'M('+%EDITC(0000.00:'M')
     C                                  +%EDITC(A50_N70:'M')
     C                                  +%EDITC(A50_N60:'M')
     C                                  +%EDITC(A50_N72:'M')
     C                                  +%EDITC(A50_N62:'M')+') '
      * Expected:
      *'K(           123,456   123,456- 1,234.56  1,234.56-) J(     '
      *'.00   123,456   123,456- 1,234.56  1,234.56-) L(    .00  123'
      *'456  123456- 1234.56  1234.56-) M(         123456  123456- 1'
      *'234.56  1234.56-)                                           '
      *
     C     £DBG_Str      DSPLY
    OA* A£.BIFN(EDITC  )
     D* %EDITC formato codice N,O,P,Q
     C                   EVAL      £DBG_Pas='P05'
     C                   EVAL      £DBG_Str=
     C                              'N('+%EDITC(0000.00:'N')
     C                                  +%EDITC(A50_N70:'N')
     C                                  +%EDITC(A50_N60:'N')
     C                                  +%EDITC(A50_N72:'N')
     C                                  +%EDITC(A50_N62:'N')+') '
     C                             +'O('+%EDITC(0000.00:'O')
     C                                  +%EDITC(A50_N70:'O')
     C                                  +%EDITC(A50_N60:'O')
     C                                  +%EDITC(A50_N72:'O')
     C                                  +%EDITC(A50_N62:'O')+') '
     C                             +'P('+%EDITC(0000.00:'P')
     C                                  +%EDITC(A50_N70:'P')
     C                                  +%EDITC(A50_N60:'P')
     C                                  +%EDITC(A50_N72:'P')
     C                                  +%EDITC(A50_N62:'P')+') '
     C                             +'Q('+%EDITC(0000.00:'Q')
     C                                  +%EDITC(A50_N70:'Q')
     C                                  +%EDITC(A50_N60:'Q')
     C                                  +%EDITC(A50_N72:'Q')
     C                                  +%EDITC(A50_N62:'Q')+') '
      * Expected:
      *'N(      .00   123,456  -123,456  1,234.56 -1,234.56) O(     '
      *'       123,456  -123,456  1,234.56 -1,234.56) P(     .00  12'
      *'3456 -123456  1234.56 -1234.56) Q(          123456 -123456  '
      *'1234.56 -1234.56)                                           '
      *
     C     £DBG_Str      DSPLY
    OA* A£.BIFN(EDITC  )
     D* %EDITC formato codice X,Y,Z
     C                   EVAL      £DBG_Pas='P06'
     C                   EVAL      £DBG_Str=
     C                              'X('+%EDITC(0000.00:'X')
     C                                  +%EDITC(A50_N70:'X')
     C                                  +%EDITC(A50_N60:'X')
     C                                  +%EDITC(A50_N72:'X')
     C                                  +%EDITC(A50_N62:'X')+') '
     C                             +'Y('+%EDITC(0000.00:'Y')
     C                                  +%EDITC(A50_N70:'Y')
     C                                  +%EDITC(A50_N60:'Y')
     C                                  +%EDITC(A50_N72:'Y')
     C                                  +%EDITC(A50_N62:'Y')+') '
     C                             +'Z('+%EDITC(0000.00:'Z')
     C                                  +%EDITC(A50_N70:'Z')
     C                                  +%EDITC(A50_N60:'Z')
     C                                  +%EDITC(A50_N72:'Z')
     C                                  +%EDITC(A50_N62:'Z')+') '
      * Expected:
      *'X(0000000123456012345O0123456012345O) Y( 0/00/00 12/34/56 12'
      *'/34/56 12/34/56 12/34/56) Z(       123456 123456 123456 1234'
      *'56)                                                         '
      *
     C     £DBG_Str      DSPLY
    OA* A£.BIFN(EDITC  )
     D* %EDITC Tutti editc di una variabile
     C                   EVAL      £DBG_Pas='P08'
     C                   EVAL      A50_N156=123,456
     C                   EXSR      SEZ_A50_A
     C                   EVAL      £DBG_Str=%TRIMR(A50_A1)
      * Expected:
      *'        123.456000 /         123.456000 /       123.456000 /'
      *'       123.456000 /         123.456000   /         123.45600'
      *'0   /       123.456000   /       123.456000   /         123.'
      *'456000  /         123.456000  /       123.456000  /       12'
      *'3.456000  /          123.456000 /          123.456000 /     '
      *'   123.456000 /        123.456000 / 000000123456000 /       '
      *'123456000                                                   '
      *
     C     £DBG_Str      DSPLY
    OA* A£.BIFN(EDITC  )
     D* %EDITC Tutti editc di una variabile (100.000 volte)
     C                   EVAL      £DBG_Pas='P09'
     C                   EVAL      A50_N156=123,456
     C                   DO        NNN
     C                   EXSR      SEZ_A50_A
     C                   ENDDO
     C                   EVAL      £DBG_Str=%TRIMR(A50_A1)
      * Expected:
      *'        123.456000 /         123.456000 /       123.456000 /'
      *'       123.456000 /         123.456000   /         123.45600'
      *'0   /       123.456000   /       123.456000   /         123.'
      *'456000  /         123.456000  /       123.456000  /       12'
      *'3.456000  /          123.456000 /          123.456000 /     '
      *'   123.456000 /        123.456000 / 000000123456000 /       '
      *'123456000                                                   '
     C     £DBG_Str      DSPLY
      *
     C                   ENDSR
      **************************************************************
     C     SEZ_A50_A     BEGSR
     C                   EVAL      A50_A1=
     C                              %EDITC(A50_N156:'1')+' / '
     C                             +%EDITC(A50_N156:'2')+' / '
     C                             +%EDITC(A50_N156:'3')+' / '
     C                             +%EDITC(A50_N156:'4')+' / '
     C                             +%EDITC(A50_N156:'A')+' / '
     C                             +%EDITC(A50_N156:'B')+' / '
     C                             +%EDITC(A50_N156:'C')+' / '
     C                             +%EDITC(A50_N156:'D')+' / '
     C                             +%EDITC(A50_N156:'J')+' / '
     C                             +%EDITC(A50_N156:'K')+' / '
     C                             +%EDITC(A50_N156:'L')+' / '
     C                             +%EDITC(A50_N156:'M')+' / '
     C                             +%EDITC(A50_N156:'N')+' / '
     C                             +%EDITC(A50_N156:'O')+' / '
     C                             +%EDITC(A50_N156:'P')+' / '
     C                             +%EDITC(A50_N156:'Q')+' / '
     C                             +%EDITC(A50_N156:'X')+' / '
     C                             +%EDITC(A50_N156:'Z')
     C                   ENDSR