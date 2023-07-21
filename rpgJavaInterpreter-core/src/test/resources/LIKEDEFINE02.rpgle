     C     £G54          BEGSR
     C                   CALL      'B£G54G'
      * cause: £G54HR defined inline inside a subroutine
     C                   PARM                    £G54HR            6 0
     C                   PARM                    £G54HZ            6 0
     C                   ENDSR

     C     ESE_P5A       BEGSR
     C                   EXSR      £G54
     C     £G54HR        DSPLY
     C     £G54HZ        DSPLY
      * symptom: Data reference not resolved: §§ORA
     C     §§ORA         DSPLY
     C     §§ORA         DSPLY
     C                   ENDSR
      * evaluation: See implementation of com.smeup.rpgparser.parsing.ast.DefineStmt
     C     *LIKE         DEFINE    £G54HR        §§ORA
     C     *LIKE         DEFINE    £G54HZ        §§ORB