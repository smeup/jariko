      /IF NOT DEFINED(TABUI1DS_INCLUDED)
      /DEFINE TABUI1DS_INCLUDED
      *      Main User Interface
     D UI1$DS          DS           100
      * N° 03 - Ambiente             TA *CNAA       2
     D  T$UI1A                 1      2
      * N° 04 - Coda lavoro          OJ            10
     D  T$UI1B                 3     12
      * N° 05 - Timeout code         **             1
     D  T$UI1C                13     13
      * N° 06 - Chiusura Job Emulat. V2 SI/NO       1    5
     D  T$UI1H                19     19
      * N° 07 - Emulatore esteso  SV V2 SI/NO       1
     D  T$UI1D                20     20
      * N° 08 - Exit JACFG1          **             1
     D  T$UI1E                21     21
      /ENDIF
