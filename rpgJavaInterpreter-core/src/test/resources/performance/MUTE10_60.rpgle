     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 06/11/20  002323  BERNI Creato
     V*=====================================================================
      * ENTRY
      * . Function
     D U$FUNZ          S             10
      * . Method
     D U$METO          S             10
      * . var1
     D U$VAR1          S             10
      * . var2
     D U$VAR2          S             15
      * . var3
     D U$VAR3          S             20
      * . var4
     D U$VAR4          S             25
      * . var5
     D U$VAR5          S             30
      * . var6
     D U$VAR6          S             35
      * . var7
     D U$VAR7          S             40
      * . Var8
     D U$VAR8          S             45
      * . Var9
     D U$VAR9          S             50
      * . Va11
     D U$VA11          S             10
      * . Va12
     D U$VA12          S             15
      * . Va13
     D U$VA13          S             20
      * . Va14
     D U$VA14          S             25
      * . Va15
     D U$VA15          S             30
      * . Va16
     D U$VA16          S             35
      * . Va17
     D U$VA17          S             40
      * . Va18
     D U$VA18          S             45
      * . Va19
     D U$VA19          S             50
      * . Va21
     D U$VA21          S             10
      * . Va22
     D U$VA22          S             15
      * . Va23
     D U$VA23          S             20
      * . Va24
     D U$VA24          S             25
      * . Va25
     D U$VA25          S             30
      * . Va26
     D U$VA26          S             35
      * . Va27
     D U$VA27          S             40
      * . Va28
     D U$VA28          S             45
      * . Va29
     D U$VA29          S             50
      * . Va31
     D U$VA31          S             10
      * . Va32
     D U$VA32          S             15
      * . Va33
     D U$VA33          S             20
      * . Va34
     D U$VA34          S             25
      * . Va35
     D U$VA35          S             30
      * . Va36
     D U$VA36          S             35
      * . Va37
     D U$VA37          S             40
      * . Va38
     D U$VA38          S             45
      * . Va39
     D U$VA39          S             50
      * . Va41
     D U$VA41          S             10
      * . Va42
     D U$VA42          S             15
      * . Va43
     D U$VA43          S             20
      * . Va44
     D U$VA44          S             25
      * . Va45
     D U$VA45          S             30
      * . Va46
     D U$VA46          S             35
      * . Va47
     D U$VA47          S             40
      * . Va48
     D U$VA48          S             45
      * . Va49
     D U$VA49          S             50
      * . Va50
     D U$VA50          S             50
      * . String
     D U$SVAR          S         210000
      * . Return Code
     D U$IN35          S              1
      *---------------------------------------------------------------
     D $N              S             10  0
     D $A              S             10
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
     C     *ENTRY        PLIST
     C                   PARM                    U$FUNZ
     C                   PARM                    U$METO
     C                   PARM                    U$VAR1
     C                   PARM                    U$VAR2
     C                   PARM                    U$VAR3
     C                   PARM                    U$VAR4
     C                   PARM                    U$VAR5
     C                   PARM                    U$VAR6
     C                   PARM                    U$VAR7
     C                   PARM                    U$VAR8
     C                   PARM                    U$VAR9
     C                   PARM                    U$VA11
     C                   PARM                    U$VA12
     C                   PARM                    U$VA13
     C                   PARM                    U$VA14
     C                   PARM                    U$VA15
     C                   PARM                    U$VA16
     C                   PARM                    U$VA17
     C                   PARM                    U$VA18
     C                   PARM                    U$VA19
     C                   PARM                    U$VA21
     C                   PARM                    U$VA22
     C                   PARM                    U$VA23
     C                   PARM                    U$VA24
     C                   PARM                    U$VA25
     C                   PARM                    U$VA26
     C                   PARM                    U$VA27
     C                   PARM                    U$VA28
     C                   PARM                    U$VA29
     C                   PARM                    U$VA31
     C                   PARM                    U$VA32
     C                   PARM                    U$VA33
     C                   PARM                    U$VA34
     C                   PARM                    U$VA35
     C                   PARM                    U$VA36
     C                   PARM                    U$VA37
     C                   PARM                    U$VA38
     C                   PARM                    U$VA39
     C                   PARM                    U$VA41
     C                   PARM                    U$VA42
     C                   PARM                    U$VA43
     C                   PARM                    U$VA44
     C                   PARM                    U$VA45
     C                   PARM                    U$VA46
     C                   PARM                    U$VA47
     C                   PARM                    U$VA48
     C                   PARM                    U$VA49
     C                   PARM                    U$VA50
     C                   PARM                    U$SVAR
     C                   PARM                    U$IN35
      *
     C                   ADD       1             $N
     C                   EVAL      $N=$N-1
     C                   MOVEL     1             $N
     C                   EVAL      $A='A'
     C                   MOVEL     'B'           $A
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
    RD* Initial subroutine
      *--------------------------------------------------------------*
     C     *INZSR        BEGSR
      *
      *
     C                   ENDSR
