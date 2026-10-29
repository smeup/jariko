     V* ==============================================================
     V* Purpose: indicators spread over several lines (conditioning
     V* line followed by an AND line) must be evaluated as an AND, no
     V* matter the numeric order of the indicators.
     V* Each pair of statements is mutually exclusive, so exactly one
     V* of them must be executed.
     V* Expected output: 1:A_ 2:_B 3:A_ 4:_B
     V* ==============================================================

      * --------------------------------------------------------------
      * HOW TO READ THIS PROGRAM (for Java developers)
      *
      * An "indicator" is a global boolean flag: there are 99 of them,
      * *IN01 ... *IN99, always available, all initially false. Here we
      * use number 53 and number 56:
      *    boolean[] in = new boolean[100];   // in[53], in[56]
      *
      * SETON  ... 56   ->  in[56] = true;
      * SETOFF ... 5356 ->  in[53] = false; in[56] = false;
      *
      * Almost every statement can be conditioned by indicators written
      * in columns 9-11 of its line:
      *    "   56"  ->  executed only if in[56] is true
      *    "  N56"  ->  executed only if in[56] is false (N = NOT)
      * When the conditions do not fit in the same line, the condition
      * is written on a line with no operation and the next line starts
      * with "AN" (AND) or "O" (OR):
      *
      *       C  N56
      *       CANN53              EVAL      R1='A'
      *
      *    is  if (!in[56] && !in[53]) { R1 = "A"; }
      * --------------------------------------------------------------

     D R1              S              1
     D R2              S              1
     D OUT             S             50    VARYING

      * Higher indicator first (56 then 53), 56 off
      * Java: in[53]=false; in[56]=false;
      *       if (!in[56] && !in[53]) R1="A";   // executed
      *       if ( in[56] && !in[53]) R2="B";   // skipped
     C                   SETOFF                                       5356
     C                   EVAL      R1='_'
     C                   EVAL      R2='_'
     C  N56
     CANN53              EVAL      R1='A'
     C   56
     CANN53              EVAL      R2='B'
     C                   EVAL      OUT='1:'+R1+R2
     C     OUT           DSPLY

      * Higher indicator first (56 then 53), 56 on
      * Java: in[56]=true;  (in[53] is still false)
      *       if (!in[56] && !in[53]) R1="A";   // skipped
      *       if ( in[56] && !in[53]) R2="B";   // executed
     C                   SETON                                        56
     C                   EVAL      R1='_'
     C                   EVAL      R2='_'
     C  N56
     CANN53              EVAL      R1='A'
     C   56
     CANN53              EVAL      R2='B'
     C                   EVAL      OUT='2:'+R1+R2
     C     OUT           DSPLY

      * Lower indicator first (53 then 56), 53 off
      * Same as above with the two indicators swapped in the condition:
      *       if (!in[53] && !in[56]) R1="A";   // executed
      *       if ( in[53] && !in[56]) R2="B";   // skipped
     C                   SETOFF                                       5356
     C                   EVAL      R1='_'
     C                   EVAL      R2='_'
     C  N53
     CANN56              EVAL      R1='A'
     C   53
     CANN56              EVAL      R2='B'
     C                   EVAL      OUT='3:'+R1+R2
     C     OUT           DSPLY

      * Lower indicator first (53 then 56), 53 on
      *       if (!in[53] && !in[56]) R1="A";   // skipped
      *       if ( in[53] && !in[56]) R2="B";   // executed
     C                   SETON                                        53
     C                   EVAL      R1='_'
     C                   EVAL      R2='_'
     C  N53
     CANN56              EVAL      R1='A'
     C   53
     CANN56              EVAL      R2='B'
     C                   EVAL      OUT='4:'+R1+R2
     C     OUT           DSPLY

      * LR = "last record" indicator: setting it on ends the program
      * (like returning from main)
     C                   SETON                                        LR
