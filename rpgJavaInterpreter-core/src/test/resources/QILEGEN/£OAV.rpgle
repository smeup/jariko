      * Api containing only variables definition
     D £OAVFU          S             10
     D £OAVME          S             10
     D £OAVT1          S              2
     D £OAVP1          S             10
     D £OAVC1          S             15
     D £OAVT2          S              2
     D £OAVP2          S             10
     D £OAVC2          S             15
     D £OAVAT          S             15
     D £OAVDA          S              8  0
      *
     D £OAVOV          S             15
     D £OAVON          S             15  5
     D £OAVOD          S              8  0
     D £OAVOA          S              8  0
     D £OAVOT          S              2
     D £OAVOP          S             10
     D £OAVCT          S             15
     D £OAVLI          S              2  0
     D £OAVDV          S             15
     D £OAVIN          S             35
     D £OAVSI          S             35
     D £OAVM1          S              7
     D £OAVM2          S              7
     D £OAVMS          S              7
     D £OAVFI          S             10
     D £OAVCM          S              2
     D £OAV35          S              1
     D £OAV36          S              1
      *
     D £OAVDI          DS          1024
     D £OAVI_CTRA                     1
     D £OAVI_PARM                   100
      *
     D £OAVDO          DS         32000
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


     C     £OAV          BEGSR
     C     £OAVFU        CAT       £OAVME:0      £OAVO_VAES
     C                   CAT       £OAVT1:0      £OAVO_VAES
     C                   CAT       £OAVP1:0      £OAVO_VAES
     C                   CAT       £OAVC1:0      £OAVO_VAES
     C                   CAT       £OAVAT:0      £OAVO_VAES
     C                   ENDSR