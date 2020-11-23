package com.jariko.samples

import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.JarikoCallback
import com.smeup.rpgparser.execution.executePgmWithStringArgs
import com.smeup.rpgparser.execution.getProgram
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.parsetreetoast.RpgType
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import java.io.File
import java.math.BigDecimal

object RpgSamples

/**
 * This function execute fibonacciOf number passed as argument
 * The rpgle program in retrieve from local sources from programPath
 * @param fibonacciOf integer to calculate fibonacci series
 */
fun fibonacciOf(fibonacciOf: Int) {
    val programPath = File(RpgSamples.javaClass.getResource("/rpg").file).path
    val programName = "fibonacci.rpgle"
    val programArgs = listOf(fibonacciOf.toString())
    var output: Long = 0
    val jarikoCallback = JarikoCallback(
            exitInRT = { false },
            onExitPgm = { _: String, symbolTable: ISymbolTable, _: Throwable? ->
                output = symbolTable["FINAL_VAL"].asInt().value
            }
    )
    execWithCallback(programPath, programName, programArgs, jarikoCallback)
    print(output)
}

/**
 * This function is used to execute rpgle from programPath, named filename,
 * with programArgs as parameters showing var values by JarikoCallback
 * @param programPath path to rpgle programs
 * @param programName rpgle program filename
 * @param programArgs parm to pass to rpgle
 * @param jarikoCallback instance of JarikoCallback
 */
fun execWithCallback(programPath: String, programName: String, programArgs: List<String>, jarikoCallback: JarikoCallback) {
    val rpgProgramFinders = listOf(DirRpgProgramFinder(File(programPath)))
    val configuration = Configuration(
            jarikoCallback = jarikoCallback
    )
    println("Running $programName ...")
    executePgmWithStringArgs(
            programName = programName,
            programFinders = rpgProgramFinders,
            programArgs = programArgs,
            configuration = configuration
    )
    println("... done.")
}

/**
 * This function is used to execute rpgle source as programSource,
 * with programArgs as parameters showing var values by JarikoCallback
 * @param programSource source of rpgle program
 * @param programArgs parm to pass to rpgle
 * @param jarikoCallback instance of JarikoCallback
 */
fun execWithCallBack(programSource: String, programArgs: List<String>, jarikoCallback: JarikoCallback) {
    println("Running source: $programSource ...")
    val configuration = Configuration(
            jarikoCallback = jarikoCallback
    )
    val systemInterface = JavaSystemInterface()
    val commandLineProgram = getProgram(programSource, systemInterface)
    commandLineProgram.singleCall(programArgs, configuration = configuration)
    println("... done.")
}

/**
 * Draft version of how to pass a DS to Jariko.
 * */
fun passDSToJariko() {
    // testing program
    val pgm = "     DMsg              S              3\n" +
            "     DP1               DS\n" +
            "     D Name                           5A\n" +
            "     D Surname                        5A\n" +
            "     D Nbr                            5  2\n" +
            "     C     *ENTRY        PLIST\n" +
            "     C                   PARM                    P1\n" +
            "     C     Name          DSPLY\n" +
            "     C     Surname       DSPLY\n" +
            "     C     Nbr           DSPLY\n" +
            "     C     Msg           DSPLY\n" +
            "     C                   SETON                                        LR"
    // set len of DS
    val dsLen = 15
    val commandLineProgram = getProgram(nameOrSource = pgm)
    // create instance
    val dataStructValue = DataStructValue("".padStart(dsLen))
    // set DS fields fields
    // note explicitEndOffset is required but not significant
    dataStructValue.set(
        field = FieldDefinition(
            name = "Name",
            type = StringType(5, false),
            explicitStartOffset = 0,
            explicitEndOffset = 0
        ),
        value = StringValue("John")
    )
    dataStructValue.set(
        field = FieldDefinition(
            name = "Surname",
            type = StringType(5, false),
            explicitStartOffset = 5,
            explicitEndOffset = 0
        ),
        value = StringValue("Smith")
    )
    // note rpgType = RpgType.ZONED is always required regardless of number type definition in rpg program
    dataStructValue.set(
        field = FieldDefinition(
            name = "Nbr",
            type = NumberType(entireDigits = 3, decimalDigits = 2, rpgType = RpgType.ZONED),
            explicitStartOffset = 10, explicitEndOffset = 0
        ),
        value = DecimalValue(BigDecimal.valueOf(12.12)))
    // call program passing the string representation of dataStructValue
    commandLineProgram.singleCall(arrayListOf(dataStructValue.value))
}
