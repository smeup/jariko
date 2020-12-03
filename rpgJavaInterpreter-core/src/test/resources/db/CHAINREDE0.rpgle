      * Sample tables created with
      * CALL QSYS.CREATE_SQL_SAMPLE ('SAMPLE')
      * See:
      * https://www.ibm.com/support/knowledgecenter/en/ssw_ibm_i_74/sqlp/rbafysamptblx.htm
      * We use here the EMPLOYEE Table:
      * https://www.ibm.com/support/knowledgecenter/en/ssw_ibm_i_74/sqlp/rbafyemployee.htm
      * ------------------------------------------------------------------
     FEMPLOYEE     if   e           k disk
     D msg             S             52    inz(*blanks)
      *
      * Reads records from file
     C                   exsr      showData
     C     'EOF'         dsply
      * Closing resources.
     C                   seton                                        lr
      *-------------------------------------------------------------------------
     C     showData      begsr
     C                   dow       not %eof
     C                   ReadE     EMPLOYEE
     C                   eval      msg = %trim(FIRSTNME) + ' ' + %trim(LASTNAME)
     C     msg           dsply
     C                   enddo
     C                   endsr
