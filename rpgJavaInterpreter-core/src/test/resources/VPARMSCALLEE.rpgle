     D                SDS
     D  £PDSPR           *PARMS

     D £40AE           S          30000    VARYING
     D §40AE           S                   LIKE(£40AE)

     D £40A            S             15    DIM(300)

     C                   IF        £PDSPR>=16
     C                   EVAL      £40AE=§40AE
     C                   ENDIF

     C     *ENTRY        PLIST
     C                   PARM                    £40A
     C                   PARM                    §40AE