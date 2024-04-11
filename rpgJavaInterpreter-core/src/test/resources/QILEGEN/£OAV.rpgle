      * This is a dummy and mocked implementation.
      * The original file doesn't have the DS' declaration
      *  and has a different implementation of `£OAV` subroutine.
      * In addition, has a magic inclusion of `£OAVDS`.

     D£OAVDI           DS          1024
     D £OAVI_CTRA                     1
     D £OAVI_PARM                   100
     D£OAVDO           DS         32000
     D £OAVO_NOAU                     1
     D £OAVO_VAES                 30000    VARYING
     D £OAVO_FLDN                    10
     D £OAVO_OGGA                    50
     D £OAVO_LUNG                     5
     D £OAVO_DECI                     2
     D £OAVO_OBBL                     1
     D £OAVO_NCTP                     1
     D £OAVO_NCCO                     1
     D £OAVO_INOU                     1
     D £OAVO_CASE                     2
     D £OAVO_TEFI                     1
     D £OAVO_LUGR                     5
     D £OAVO_LOGM                     1
     D £OAVO_FLKY                     1
     D £OAVO_AUEN                     1
     D £OAVO_SPAR                     1
     D £OAVO_PARC                    10
     D £OAVO_SEZI                    30
     D £OAVO_ORDI                    30

     D £OAVFU          S             10
     D £OAVME          S             10
     D £OAVT1          S             10
     D £OAVP1          S             10
     D £OAVC1          S             10
     D £OAVAT          S             10
      *

     C     £OAV          BEGSR
      *
     C                   EVAL      £OAVO_VAES=' FOO '
      *
     C                   ENDSR
