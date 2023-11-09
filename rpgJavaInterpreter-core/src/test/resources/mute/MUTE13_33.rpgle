     V*=====================================================================
     V* MODIFICHE Rel.  T Au Description
     V* dd/mm/yy  nn.mm i xx Short description
     V*=====================================================================
     V* 09/02/23  004636  GOBMAS New program
     V*=====================================================================
     D*  TARGET
     D*  Finalized program operation of the OCCUR operational code
     D*
     D* The OCCUR operation code specifies the occurrence of the data
     D* structure that is to be used next within an RPG IV program.
     D*
     D* The OCCUR operation establishes which occurrence of a multiple
     D* occurrence data structure is used next in a program. Only one
     D* occurrence can be used at a time. If a data structure with
     D* multiple occurrences or a subfield of that data structure is
     D* specified in an operation, the first occurrence of the data
     D* structure is used until an OCCUR operation is specified. After an
     D* OCCUR operation is specified, the occurrence of the data structure
     D* that was established by the OCCUR operation is used.
     V*=====================================================================
      *  DS1 and DS2 are multiple occurrence data structures.
      *  Each data structure has 5 occurrences.
     D DS1             DS                  OCCURS(5)
     D  FLDA                   1      5
     D  FLDB                   6     10  0
      *
     D DS2             DS                  OCCURS(3)
     D  FLDC                   1     10
     D  FLDD                  11     15  0
      *
     D RES             S              5  0
     D X               S              5  0
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
     C                   SETON                                        LR
      *
     C                   CLEAR                   DS1
     C                   CLEAR                   DS2
      * Set DS1
     C     1             OCCUR     DS1
     C                   EVAL      FLDA = 'DS101'
     C                   EVAL      FLDB = 1
     C     2             OCCUR     DS1
     C                   EVAL      FLDA = 'DS102'
     C                   EVAL      FLDB = 2
     C     3             OCCUR     DS1
     C                   EVAL      FLDA = 'DS103'
     C                   EVAL      FLDB = 3
     C     4             OCCUR     DS1
     C                   EVAL      FLDA = 'DS104'
     C                   EVAL      FLDB = 4
     C     5             OCCUR     DS1
     C                   EVAL      FLDA = 'DS105'
     C                   EVAL      FLDB = 5
      * Set DS2
     C     1             OCCUR     DS2
     C                   EVAL      FLDC = 'DS2_001'
     C                   EVAL      FLDD = 21
     C     2             OCCUR     DS2
     C                   EVAL      FLDC = 'DS2_002'
     C                   EVAL      FLDD = 22
     C     3             OCCUR     DS2
     C                   EVAL      FLDC = 'DS2_003'
     C                   EVAL      FLDD = 23
      *
      *  DS1 is set to the third occurrence.  The subfields FLDA
      *  and FLDB of the third occurrence can now be used.
      *
    MU* VAL1(FLDA) VAL2('DS103') COMP(EQ)
     C     3             OCCUR     DS1
      *
      *  The value of the current occurrence of DS1 is placed in the
      *  result field, RES.  Field RES must be numeric with zero decimal
      *  positions.  For example, if the current occurrence of DS1
      *  is 3, field RES contains the value 3.
      *
    MU* VAL1(RES) VAL2(3) COMP(EQ)
     C                   OCCUR     DS1           RES
      *
      *  DS1 is set to the third occurrence.  The subfields FLDA
      *  and FLDB of the third occurrence can now be used and modified.
     C                   CLEAR                   FLDA
    MU* VAL1(FLDA) VAL2('     ') COMP(EQ)
     C                   OCCUR     DS1
      *
      *  DS2 is set to the occurrence specified in field X.
      *  For example, if X = 2, DS2 is set to the second occurrence.
     C                   EVAL      X = 2
    MU* VAL1(FLDC) VAL2('DS2_002   ') COMP(EQ)
     C     X             OCCUR     DS2
      *
      *  DS1 is set to the current occurrence of DS2.  For example, if
      *  the current occurrence of DS2 is the second occurrence, DS1
      *  is set to the second occurrence.
      *
    MU* VAL1(FLDA) VAL2('DS102') COMP(EQ)
     C     DS2           OCCUR     DS1

     C
      *  DS1 is set to the current occurrence of DS2.  The value of the
      *  current occurrence of DS1 is then moved to the result field,
      *  RES.  For example, if the current occurrence of DS2 is the second
      *  occurrence, DS1 is set to the second occurrence.  The result
      *  field, RES, contains the value 2.
      *
    MU* VAL1(RES) VAL2(2) COMP(EQ)
     C     DS2           OCCUR     DS1           RES
      *
      *  DS1 is set to the current occurrence of X.  For example, if
      *  X = 5, DS1 is set to the fifth occurrence.
      *  If X is less than 1 or is greater than 5,
      *  an error occurs and  indicator 35  is set to return '1'.
      *
     C                   CLEAR                   RES
     C                   EVAL      *IN35 = *OFF
     C                   EVAL      X = 10
    MU* VAL1(*IN35) VAL2('1') COMP(EQ)
     C     X             OCCUR     DS1           RES                    35
      *
     C                   RETURN
      /COPY QILEGEN,£INZSR
      *---------------------------------------------------------------
     C     £INIZI        BEGSR
     C                   ENDSR
      *---------------------------------------------------------------
