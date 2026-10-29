     H DFTACTGRP(*NO)
      * ==============================================================
      * SUBARR_SCALAR01 - assignment of a scalar to %SUBARR
      *
      * Run on IBM i:  CALL SUBARR_SCALAR01   (interactive job, press Enter
      * after every DSPLY message).
      *
      * Hypothesis (H) to prove:
      *  T4  EVAL %SUBARR(ARR:3) = *BLANKS (no element count) sets
      *      to blank ALL the elements from 3 up to the LAST one.
      *  T5  EVAL %SUBARR(ARR:2:2) = *BLANKS blanks exactly 2 elements.
      *  T6  Same with *ZEROS on a numeric array.
      *
      * If the compiler rejects the assignment of a scalar to
      * %SUBARR, please send back the compiler messages (that is an
      * answer too).
      * Expected answer if H is true: every line ends with ": YES".
      * ==============================================================

     D MSG             S             60
     D LBL             S             40
     D YN              S              1
     D ARR             S              3    DIM(5)
     D NUM             S              3  0 DIM(5)

      * ---- T4: no element count -> up to the last element
     C                   EVAL      ARR(1)='AAA'
     C                   EVAL      ARR(2)='BBB'
     C                   EVAL      ARR(3)='CCC'
     C                   EVAL      ARR(4)='DDD'
     C                   EVAL      ARR(5)='EEE'
     C                   EVAL      %SUBARR(ARR:3)=*BLANKS

     C                   EVAL      YN='N'
     C                   IF        ARR(1)='AAA' AND ARR(2)='BBB'
     C                   EVAL      YN='Y'
     C                   ENDIF
     C                   EVAL      LBL='T4a elements 1-2 untouched'
     C                   EXSR      SHOW

     C                   EVAL      YN='N'
     C                   IF        ARR(3)=*BLANKS AND ARR(4)=*BLANKS
     C                   EVAL      YN='Y'
     C                   ENDIF
     C                   EVAL      LBL='T4b elements 3-4 blank'
     C                   EXSR      SHOW

     C                   EVAL      YN='N'
     C                   IF        ARR(5)=*BLANKS
     C                   EVAL      YN='Y'
     C                   ENDIF
     C                   EVAL      LBL='T4c LAST element (5) blank'
     C                   EXSR      SHOW

      * ---- T5: with element count
     C                   EVAL      ARR(1)='AAA'
     C                   EVAL      ARR(2)='BBB'
     C                   EVAL      ARR(3)='CCC'
     C                   EVAL      ARR(4)='DDD'
     C                   EVAL      ARR(5)='EEE'
     C                   EVAL      %SUBARR(ARR:2:2)=*BLANKS

     C                   EVAL      YN='N'
     C                   IF        ARR(1)='AAA' AND ARR(4)='DDD'
     C                   EVAL      YN='Y'
     C                   ENDIF
     C                   EVAL      LBL='T5a elements 1 and 4 untouched'
     C                   EXSR      SHOW

     C                   EVAL      YN='N'
     C                   IF        ARR(2)=*BLANKS AND ARR(3)=*BLANKS
     C                   EVAL      YN='Y'
     C                   ENDIF
     C                   EVAL      LBL='T5b elements 2-3 blank'
     C                   EXSR      SHOW

      * ---- T6: numeric array
     C                   EVAL      NUM(1)=1
     C                   EVAL      NUM(2)=2
     C                   EVAL      NUM(3)=3
     C                   EVAL      NUM(4)=4
     C                   EVAL      NUM(5)=5
     C                   EVAL      %SUBARR(NUM:4)=*ZEROS

     C                   EVAL      YN='N'
     C                   IF        NUM(3)=3
     C                   EVAL      YN='Y'
     C                   ENDIF
     C                   EVAL      LBL='T6a element 3 untouched'
     C                   EXSR      SHOW

     C                   EVAL      YN='N'
     C                   IF        NUM(4)=0 AND NUM(5)=0
     C                   EVAL      YN='Y'
     C                   ENDIF
     C                   EVAL      LBL='T6b elements 4-5 zero'
     C                   EXSR      SHOW

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
