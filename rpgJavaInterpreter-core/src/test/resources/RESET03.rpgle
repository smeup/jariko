     H DFTACTGRP(*NO)
      * ==============================================================
      * RESET03 - what do INZ and RESET do on array subfields?
      *
      * Run on IBM i:  CALL RESET03   (interactive job, press Enter
      * after every DSPLY message).
      *
      * Hypothesis (H) to prove, in parallel what jariko must do:
      *  T1  INZ(*HIVAL) of an array subfield is applied to EVERY
      *      element of the array, not only to the first one.
      *  T2  RESET of an array field (LISATT) restores the INZ values
      *      of the subfields that overlay it: CODRIO goes back to
      *      *HIVAL in every element; the subfield without INZ goes
      *      back to blanks.
      *  T3  Same as T2 with INZ('XXX'): after RESET ARR, SUB1 is
      *      'XXX' (NOT blank) and SUB2 is blank.
      *
      * Expected answer if H is true: every line ends with ": YES".
      * Please send back all the lines, specially the ones with "NO".
      * ==============================================================

     D MSG             S             60
     D LBL             S             40
     D YN              S              1

      * Same structure as LISATT in B£OAV0 (PNTDAT is char here, so we
      * never read a numeric field that was not initialized)
     D                 DS
     D LISATT                         8    DIM(3)
     D  CODRIO                        5    OVERLAY(LISATT:01) INZ(*HIVAL)
     D  PNTDAT                        3    OVERLAY(LISATT:*NEXT)

      * Same as RESET02 (jariko test)
     D                 DS
     D ARR                            6    DIM(3)
     D  SUB1                          3    OVERLAY(ARR:01) INZ('XXX')
     D  SUB2                          3    OVERLAY(ARR:*NEXT)

      * ---- T1: initial value of every element
     C                   EVAL      YN='N'
     C                   IF        CODRIO(1)=*HIVAL
     C                   EVAL      YN='Y'
     C                   ENDIF
     C                   EVAL      LBL='T1a CODRIO(1) is *HIVAL at start'
     C                   EXSR      SHOW

     C                   EVAL      YN='N'
     C                   IF        CODRIO(3)=*HIVAL
     C                   EVAL      YN='Y'
     C                   ENDIF
     C                   EVAL      LBL='T1b CODRIO(3) is *HIVAL at start'
     C                   EXSR      SHOW

     C                   EVAL      YN='N'
     C                   IF        SUB1(3)='XXX'
     C                   EVAL      YN='Y'
     C                   ENDIF
     C                   EVAL      LBL='T1c SUB1(3) is XXX at start'
     C                   EXSR      SHOW

      * ---- T2: RESET of the array field LISATT
     C                   EVAL      CODRIO(1)='AAAAA'
     C                   EVAL      CODRIO(3)='ZZZZZ'
     C                   EVAL      PNTDAT(1)='007'
     C                   EVAL      PNTDAT(3)='009'
     C                   RESET                   LISATT

     C                   EVAL      YN='N'
     C                   IF        CODRIO(1)=*HIVAL
     C                   EVAL      YN='Y'
     C                   ENDIF
     C                   EVAL      LBL='T2a after RESET LISATT CODRIO(1)=HIVAL'
     C                   EXSR      SHOW

     C                   EVAL      YN='N'
     C                   IF        CODRIO(3)=*HIVAL
     C                   EVAL      YN='Y'
     C                   ENDIF
     C                   EVAL      LBL='T2b after RESET LISATT CODRIO(3)=HIVAL'
     C                   EXSR      SHOW

     C                   EVAL      YN='N'
     C                   IF        PNTDAT(1)=*BLANKS
     C                   EVAL      YN='Y'
     C                   ENDIF
     C                   EVAL      LBL='T2c after RESET LISATT PNTDAT(1)=blank'
     C                   EXSR      SHOW

     C                   EVAL      YN='N'
     C                   IF        PNTDAT(3)=*BLANKS
     C                   EVAL      YN='Y'
     C                   ENDIF
     C                   EVAL      LBL='T2d after RESET LISATT PNTDAT(3)=blank'
     C                   EXSR      SHOW

      * ---- T3: RESET of ARR (INZ('XXX') on the overlaying SUB1)
     C                   EVAL      ARR(1)='AAABBB'
     C                   EVAL      ARR(2)='CCCDDD'
     C                   RESET                   ARR

     C                   EVAL      YN='N'
     C                   IF        SUB1(1)='XXX'
     C                   EVAL      YN='Y'
     C                   ENDIF
     C                   EVAL      LBL='T3a after RESET ARR SUB1(1)=XXX'
     C                   EXSR      SHOW

     C                   EVAL      YN='N'
     C                   IF        SUB1(2)='XXX'
     C                   EVAL      YN='Y'
     C                   ENDIF
     C                   EVAL      LBL='T3b after RESET ARR SUB1(2)=XXX'
     C                   EXSR      SHOW

     C                   EVAL      YN='N'
     C                   IF        SUB2(1)=*BLANKS
     C                   EVAL      YN='Y'
     C                   ENDIF
     C                   EVAL      LBL='T3c after RESET ARR SUB2(1)=blank'
     C                   EXSR      SHOW

      * Show the raw value too: between the brackets there must be
      * XXX followed by 3 blanks if H is true, 6 blanks otherwise
     C                   EVAL      MSG='T3d ARR(1)=['+ARR(1)+']'
     C     MSG           DSPLY

     C                   SETON                                        LR

      * Shows "<label>: YES" when YN='Y', "<label>: NO" otherwise
     C     SHOW          BEGSR
     C                   IF        YN='Y'
     C                   EVAL      MSG=%TRIM(LBL)+': YES'
     C                   ELSE
     C                   EVAL      MSG=%TRIM(LBL)+': NO'
     C                   ENDIF
     C     MSG           DSPLY
     C                   ENDSR
