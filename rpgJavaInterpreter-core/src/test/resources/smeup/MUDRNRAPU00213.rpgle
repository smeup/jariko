     D £DBG_Str        S             2
     C     £G00          BEGSR
      *
     C                   Z-ADD     $1            £G00$1
     C                   Z-ADD     $2            £G00$2
     C                   Z-ADD     $3            £G00$3

     C                   Z-ADD     01            £G00A1            2 0
     C                   Z-ADD     02            £G00A2            2 0
     C                   Z-ADD     03            £G00A3            2 0
     C                   Z-ADD     04            £G00D1            2 0
     C                   Z-ADD     05            £G00D2            2 0
     C                   Z-ADD     06            £G00D3            2 0
     C                   Z-ADD     07            £G00E2            2 0
     C                   Z-ADD     08            £G00E3            2 0
     C                   Z-ADD     10            £G00R1            2 0
     C                   Z-ADD     60            £G00R2            2 0
      *
     C     0             IFEQ      0
     C                   Z-ADD     £G00A1        $1                5 0
     C                   Z-ADD     £G00A2        $2                5 0
     C                   Z-ADD     £G00A3        $3                5 0
     C                   ENDIF
      *
     C                   Z-ADD     £G00$1        $1
     C                   Z-ADD     £G00$2        $2
     C                   Z-ADD     £G00$3        $3
     C     *LIKE         DEFINE    $1            £G00$1
     C     *LIKE         DEFINE    $2            £G00$2
     C     *LIKE         DEFINE    $2            £G00$3
      *
     C                   ENDSR
     C                   EVAL      £DBG_Str='ok'
     C     £DBG_Str      DSPLY