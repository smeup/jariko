     V* ============================================================================================
     V* RPG program that calls an other RPG program than changes *entry parameter
     V* ============================================================================================
     V* Author: Mattia Bonardi
     V* Company: Smeup.UP spa
      *--------------------------------------------------------------------------------------------*
      * General variables
      *--------------------------------------------------------------------------------------------*
      * *entry value
     D VALUE           S            100
      * Log variable
     D LOG             S             30
      *--------------------------------------------------------------------------------------------*
     C     *ENTRY        PLIST
     C                   PARM                    VALUE
      * 
     C                   EVAL      LOG='START TST_001'          
     C     LOG           DSPLY
     
      * Call ECHO_PGM RPG program
      *
     C                   CALL      'ECHO_PGM'
     C                   PARM                    VALUE
     C     VALUE         DSPLY
      *
      * Call TST_001_2 RPG program
      *
     C                   CALL      'TST_001_2'
     C                   PARM                    VALUE
      *
     C                   EVAL      LOG='END TST_001'      
     C     LOG           DSPLY 
     C                   SETON                                        LR 
      *--------------------------------------------------------------------------------------------*