     V* ==============================================================
     V* 05/09/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Comparing String and Boolean with `COMP` operator with
    O *  values not equal to `0` and `1`.
     V* ==============================================================
     D £C5G35          S              1

     C                   MOVEL     '2'           £C5G35
     C     £C5G35        COMP      *ON                                    35
     C     *IN35         DSPLY

     C                   MOVEL     'a'           £C5G35
     C     £C5G35        COMP      *ON                                    35
     C     *IN35         DSPLY

     C                   MOVEL     '-1'          £C5G35
     C     £C5G35        COMP      *ON                                    35
     C     *IN35         DSPLY

     C                   MOVEL     ' '           £C5G35
     C     £C5G35        COMP      *ON                                    35
     C     *IN35         DSPLY

     C                   MOVEL     ''            £C5G35
     C     £C5G35        COMP      *ON                                    35
     C     *IN35         DSPLY

     C                   MOVEL     '2'           £C5G35
     C     £C5G35        COMP      *OFF                                   35
     C     *IN35         DSPLY

     C                   MOVEL     'a'           £C5G35
     C     £C5G35        COMP      *OFF                                   35
     C     *IN35         DSPLY

     C                   MOVEL     '-1'          £C5G35
     C     £C5G35        COMP      *OFF                                   35
     C     *IN35         DSPLY

     C                   MOVEL     ' '           £C5G35
     C     £C5G35        COMP      *OFF                                   35
     C     *IN35         DSPLY

     C                   MOVEL     ''            £C5G35
     C     £C5G35        COMP      *OFF                                   35
     C     *IN35         DSPLY