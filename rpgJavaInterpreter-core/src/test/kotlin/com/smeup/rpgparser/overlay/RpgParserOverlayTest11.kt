package com.smeup.rpgparser.overlay

import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.assertCanBeParsed
import com.smeup.rpgparser.executeAnnotations
import com.smeup.rpgparser.interpreter.DummyDBInterface
import com.smeup.rpgparser.interpreter.InternalInterpreter
import com.smeup.rpgparser.interpreter.NumberType
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import com.smeup.rpgparser.rgpinterop.DirRpgProgramFinder
import com.smeup.rpgparser.rgpinterop.RpgSystem
import org.junit.Ignore
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

@Ignore // randomly fails, probably does not find the .rpgle file
class RpgParserOverlayTest11 {

    @Test
    fun parseMUTE11_11C_syntax() {
        assertCanBeParsed("overlay/MUTE11_11C", withMuteSupport = true)
    }

    @Test
    fun parseMUTE11_11C_ast() {
        assertASTCanBeProduced("overlay/MUTE11_11C", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE11_11C_runtime() {
        RpgSystem.addProgramFinder(DirRpgProgramFinder(File("src/test/resources/overlay")))
        val cu = assertASTCanBeProduced("overlay/MUTE11_11C", considerPosition = true, withMuteSupport = true)
        cu.resolve(DummyDBInterface)

        val interpreter = InternalInterpreter(JavaSystemInterface())
        interpreter.execute(cu, mapOf())
        val annotations = interpreter.systemInterface.getExecutedAnnotation().toSortedMap()
        var failed: Int = executeAnnotations(annotations)
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }

    @Test
    fun parseMUTE11_15_syntax() {
        assertCanBeParsed("overlay/MUTE11_15", withMuteSupport = true)
    }

    @Test
    fun parseMUTE11_15_ast() {
        val cu = assertASTCanBeProduced("overlay/MUTE11_15", considerPosition = true, withMuteSupport = true)
        cu.resolve(DummyDBInterface)

        val FUND1 = cu.getDataDefinition("£FUND1")
        val FUNQT = FUND1.getFieldByName("£FUNQT")
        assertEquals(Pair(442, 457), FUNQT.offsets)
        assertEquals(NumberType(entireDigits = 10, decimalDigits = 5, rpgType = ""), FUNQT.type)
        assertEquals(15, FUNQT.size)
    }

    @Test
    fun parseMUTE11_15_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE11_15", considerPosition = true, withMuteSupport = true)
        cu.resolve(DummyDBInterface)

        val interpreter = InternalInterpreter(JavaSystemInterface())
        interpreter.execute(cu, mapOf())
        val annotations = interpreter.systemInterface.getExecutedAnnotation().toSortedMap()
        var failed: Int = executeAnnotations(annotations)
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }

    @Test
    fun parseMUTE11_16_runtime() {
        RpgSystem.addProgramFinder(DirRpgProgramFinder(File("src/test/resources/overlay")))
        val cu = assertASTCanBeProduced("overlay/MUTE11_16", considerPosition = true, withMuteSupport = true)
        cu.resolve(DummyDBInterface)

        val interpreter = InternalInterpreter(JavaSystemInterface())
        interpreter.execute(cu, mapOf())
        val annotations = interpreter.systemInterface.getExecutedAnnotation().toSortedMap()
        var failed: Int = executeAnnotations(annotations)
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }
}
