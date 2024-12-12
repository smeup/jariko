     D NUMAZI          C                   58
     D £JAXSWK         S            100    DIM(300)

     D                 DS
      * Error:
      * java.lang.NullPointerException
      *	at com.smeup.rpgparser.interpreter.BaseCompileTimeInterpreter.evaluateNumberOfElementsOf(compile_time_interpreter.kt:122)
      *	at com.smeup.rpgparser.interpreter.InjectableCompileTimeInterpreter.evaluateNumberOfElementsOf(compile_time_interpreter.kt:49)
      * ...
     D £JAXSW2                      100    DIM(%elem(£JAXSWK))
     D  £JAXSW2Key                   10    OVERLAY(£JAXSW2:01)