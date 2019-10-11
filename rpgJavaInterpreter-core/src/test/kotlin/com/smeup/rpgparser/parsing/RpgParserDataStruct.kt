package com.smeup.rpgparser.parsing

import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.assertCanBeParsed
import com.smeup.rpgparser.execute
import com.smeup.rpgparser.interpreter.InternalInterpreter
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import com.smeup.rpgparser.rgpinterop.DirRpgProgramFinder
import com.smeup.rpgparser.rgpinterop.RpgSystem
import org.junit.Ignore
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RpgParserDataStruct {

    @Test
    fun parseSTRUCT_01_MYDS_isRecognizedCorrectly() {
        val cu = assertASTCanBeProduced("struct/STRUCT_01", true)
        cu.resolve()

        val dataDefinition = cu.getDataDefinition("MYDS")
        assertEquals(0, dataDefinition.fields[0].startOffset)
        assertEquals(5, dataDefinition.fields[0].endOffset)
        assertEquals(5, dataDefinition.fields[1].startOffset)
        assertEquals(15, dataDefinition.fields[1].endOffset)
        assertEquals(15, dataDefinition.elementSize())
    }

    @Test
    fun parseSTRUCT_01() {
        assertCanBeParsed("struct/STRUCT_01", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/STRUCT_01", true)

        cu.resolve()
        execute(cu, mapOf())
    }

    /**
     * Test for QUALIFIED support
     */
    @Test
    //@Ignore // The parser does not handle the dot notation for accessing fields
    fun parseSTRUCT_02() {
        assertCanBeParsed("struct/STRUCT_02", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/STRUCT_02", true)
        cu.resolve()
        execute(cu, mapOf())
    }

    @Test
    @Ignore // this is probably failing because of TIMESTAMP()
    fun parseSTRUCT_03() {
        assertCanBeParsed("struct/STRUCT_03", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/STRUCT_03", true)
        cu.resolve()
        execute(cu, mapOf())
    }

    @Test
    @Ignore // I am not sure we should handle the definition of two consecutive DS
    fun parseSTRUCT_04() {
        assertCanBeParsed("struct/STRUCT_04", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/STRUCT_04", true)
        cu.resolve()
        execute(cu, mapOf())
    }

    /**
     * Test for TEMPLATE and LIKEDS support
     */
    @Test
    //@Ignore // the parser does not handle this
    fun parseSTRUCT_05() {
        assertCanBeParsed("struct/STRUCT_05", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/STRUCT_05", true)
        cu.resolve()
        execute(cu, mapOf())
    }

    /**
     * Test for all data type
     */
    @Test
    //@Ignore // the parser does not handle this
    fun parseSTRUCT_06() {
        assertCanBeParsed("struct/STRUCT_06", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/STRUCT_06", true)
        cu.resolve()
        execute(cu, mapOf())
    }

    /**
     * Test for all data type
     */
    @Test
    //@Ignore // the parser does not handle this
    fun parseSTRUCT_06_runtime() {
        assertCanBeParsed("struct/STRUCT_06", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/STRUCT_06", true)
        cu.resolve()

        val interpreter = InternalInterpreter(JavaSystemInterface())
        interpreter.execute(cu, mapOf())

        var failed : Int = 0
        val annotations = interpreter.systemInterface.getExecutedAnnotation().toSortedMap()
        annotations.forEach { (line, annotation) ->
            try {
                assertTrue(annotation.result.asBoolean().value)

            } catch (e:AssertionError) {
                println("${annotation.programName}: $line ${annotation.expression.render()} ${annotation.result.asBoolean().value}")
                failed++
            }
        }
        if(failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }
}