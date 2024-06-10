     V* ==============================================================
     D* 07/06/24  nn.mm author
     D* D spec defined LIKE PARM is not resolved when:
     D* DEFINE statement throws an error: Error while creating data definition
     V* ==============================================================
     D MSG             S             10

     D OLDIPG          S                   LIKE(§CRIPG)
     C     OLDIPG        DSPLY
     C     DTAARA        DEFINE    *LDA          £UDLDA
     C*                   IN        £UDLDA
     C     *ENTRY        PLIST
     C                   PARM                    §CRIPG           10