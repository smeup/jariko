   COP* *NOUI
     V*=====================================================================
     V* CHANGES   Rel.  T Au Description
     V* dd/mm/yy  nn.mm i xx Short description
     V*=====================================================================
     V* 11/03/16  V4.R1   GIAGIU Created
     V* 08/08/16  V5R1    ZS Translate Constant
     V* B£61020C  V5R1    BMA Added COP* *NOUI
     V*=====================================================================
     D* TARGET
     D*  Program finalized to test definition CONSTANT variables
     V*---------------------------------------------------------------------
      * different way to define CONSTANT variable
     D MAI1            C                   'ABCDEFGHIJKLMNOPQRS-
     D                                     TUVWXYZEEAOU'
     D MAI2            C                   'ABCDEFGHIJKLMNOPQRS-
     D                                             TUVWXYZEEAOU'
     D MAI3            C                   'ABCDEFGHIJKLMNOPQRS+
     D                                     TUVWXYZEEAOU'
     D MAI4            C                   'ABCDEFGHIJKLMNOPQRS+
     D                                             TUVWXYZEEAOU'
     D MAI5            C                   'ABCDEFGHIJKLMNOPQRSTUVWXYZEEAOU'
     D MAI6            C                   CONST('ABCDEFGH')
     D MIN1            C                   'abcdefghijklmnopqrs-
     D                                     tuvwxyzèéàòù'
     D JOBLIST         C                   CONST('JOBLIST   QTEMP')
     D JOBLIST1        C                   'JOBLIST   QTEMP'
     D JOBLIST2        C                   'joblist   qtemp'
     D ST1             C                   ''''
     D ST2             C                   ''''''
     D ST3             C                   '*'
     D ST4             C                   '%'
     D XOR             C                   CONST('1000010111010001011101101+
     D                                     01011001001000001001100100001100+
     D                                     00011010001110000100000')
      *
     D NUM01           C                   CONST(999)
     D NUM02           C                   CONST(999,123)
     D NUM03           C                   CONST(999.123)
     D MINNUM          C                   -999999999999999.9999999
     D MAXNUM          C                   999999999999999.9999999
     D INFINITO        C                   922337203670000
     D MINDAT          C                   0
     D MAXDAT          C                   99999999
     D MINORA          C                   0
     D MAXORA2         C                   999999
     D MAXORA3         C                   9999
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
     C                   CLEAR                   NNN022           22 6
     C                   CLEAR                   AAA100          100
    MU* VAL1(AAA100) VAL2('ABCDEFGHIJKLMNOPQRSTUVWXYZEEAOU') COMP(EQ)
     C                   EVAL      AAA100=MAI1
    MU* VAL1(AAA100) VAL2('ABCDEFGHIJKLMNOPQRS        TUVWXYZEEAOU') COMP(EQ)
     C                   EVAL      AAA100=MAI2
    MU* VAL1(AAA100) VAL2('ABCDEFGHIJKLMNOPQRSTUVWXYZEEAOU') COMP(EQ)
     C                   EVAL      AAA100=MAI3
    MU* VAL1(AAA100) VAL2('ABCDEFGHIJKLMNOPQRSTUVWXYZEEAOU') COMP(EQ)
     C                   EVAL      AAA100=MAI4
    MU* VAL1(AAA100) VAL2('ABCDEFGHIJKLMNOPQRSTUVWXYZEEAOU') COMP(EQ)
     C                   EVAL      AAA100=MAI5
    MU* VAL1(AAA100) VAL2('ABCDEFGH') COMP(EQ)
     C                   EVAL      AAA100=MAI6
    MU* VAL1(AAA100) VAL2('abcdefghijklmnopqrstuvwxyzèéàòù') COMP(EQ)
     C                   EVAL      AAA100=MIN1
    MU* VAL1(AAA100) VAL2('JOBLIST   QTEMP') COMP(EQ)
     C                   EVAL      AAA100=JOBLIST
    MU* VAL1(AAA100) VAL2('JOBLIST   QTEMP') COMP(EQ)
     C                   EVAL      AAA100=JOBLIST1
    MU* VAL1(AAA100) VAL2('joblist   qtemp') COMP(EQ)
     C                   EVAL      AAA100=JOBLIST2
    MU* VAL1(AAA100) VAL2('''') COMP(EQ)
     C                   EVAL      AAA100=ST1
    MU* VAL1(AAA100) VAL2('''''') COMP(EQ)
     C                   EVAL      AAA100=ST2
    MU* VAL1(AAA100) VAL2('*') COMP(EQ)
     C                   EVAL      AAA100=ST3
    MU* VAL1(AAA100) VAL2('%') COMP(EQ)
     C                   EVAL      AAA100=ST4
     C                   EVAL      AAA100=XOR
    MU* VAL1(NNN022) VAL2(0000000000000999.000000) COMP(EQ)
     C                   EVAL      NNN022=NUM01
    MU* VAL1(NNN022) VAL2(0000000000000999.123000) COMP(EQ)
     C                   EVAL      NNN022=NUM02
    MU* VAL1(NNN022) VAL2(0000000000000999.123000) COMP(EQ)
     C                   EVAL      NNN022=NUM03
    MU* VAL1(NNN022) VAL2(-0999999999999999.999999) COMP(EQ)
     C                   EVAL      NNN022=MINNUM
    MU* VAL1(NNN022) VAL2(0999999999999999.999999) COMP(EQ)
     C                   EVAL      NNN022=MAXNUM
    MU* VAL1(NNN022) VAL2(0922337203670000.000000) COMP(EQ)
     C                   EVAL      NNN022=INFINITO
    MU* VAL1(NNN022) VAL2(0000000000000000.000000) COMP(EQ)
     C                   EVAL      NNN022=MINDAT
    MU* VAL1(NNN022) VAL2(0000000099999999.000000) COMP(EQ)
     C                   EVAL      NNN022=MAXDAT
    MU* VAL1(NNN022) VAL2(0000000000000000.000000) COMP(EQ)
     C                   EVAL      NNN022=MINORA
    MU* VAL1(NNN022) VAL2(0000000000999999.000000) COMP(EQ)
     C                   EVAL      NNN022=MAXORA2
    MU* VAL1(NNN022) VAL2(0000000000009999.000000) COMP(EQ)
     C                   EVAL      NNN022=MAXORA3
    MU* Type="NOXMI"
     C                   SETON                                        LR
