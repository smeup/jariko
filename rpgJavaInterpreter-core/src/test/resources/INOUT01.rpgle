     D £DBG_Str        S             50    VARYING
     D IndicatorArray  S              1    DIM(99)
     D TestArray       S              1    DIM(99)

      // Initialize some indicators manually
     C                   SETON                                        01 02 03

      // Test OUT: Copy internal indicators to external array 
     C                   OUT                   IndicatorArray

      // Check that the first few indicators were copied correctly
     C                   EVAL      £DBG_Str= 'OUT: ' + IndicatorArray(1) + IndicatorArray(2) + IndicatorArray(3)
     C     £DBG_Str      DSPLY

      // Clear all indicators
     C                   SETOFF                                       01 02 03

      // Setup test array with different values
     C                   EVAL      TestArray(1) = '1'
     C                   EVAL      TestArray(2) = '0'  
     C                   EVAL      TestArray(3) = '1'
     C                   EVAL      TestArray(4) = '1'

      // Test IN: Copy external array to internal indicators
     C                   IN                    TestArray

      // Check that the indicators were set correctly
     C                   EVAL      £DBG_Str= 'IN: ' + %CHAR(*IN01) + %CHAR(*IN02) + %CHAR(*IN03) + %CHAR(*IN04)
     C     £DBG_Str      DSPLY

     C                   SETON                                        LR