package com.smeup.rpgparser.overlay

import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.assertCanBeParsed
import com.smeup.rpgparser.executeAnnotations
import com.smeup.rpgparser.interpreter.ArrayType
import com.smeup.rpgparser.interpreter.CharacterType
import com.smeup.rpgparser.interpreter.InternalInterpreter
import com.smeup.rpgparser.interpreter.NumberType
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.parsetreetoast.RpgType
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import org.junit.Ignore
import org.junit.Test
import kotlin.test.assertEquals

class RpgParserOverlayTest12 {

    @Test
    fun parseMUTE12_01_syntax() {
        assertCanBeParsed("overlay/MUTE12_01", withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_01_ast() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_01", considerPosition = true, withMuteSupport = true)
        val f1 = cu.dataDefinitions.find { it.name == "§F1" }!!
        val uibds = cu.dataDefinitions.find { it.name == "£UIBDS" }!!
        assertEquals(36, f1.fields.size)
        assertEquals(36, uibds.fields.size)
        assertEquals(uibds.type, f1.type)
    }

    @Test
    fun recognizeArrayFieldInDS() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_01", considerPosition = true, withMuteSupport = true)
        val ds1 = cu.dataDefinitions.find { it.name == "DS1" }!!
        val AR1 = ds1.getFieldByName("AR1")
        assertEquals(ArrayType(CharacterType(10), 10), AR1.type)
    }

    @Test
    @Ignore // Require qualified array support
    fun parseMUTE12_01_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_01", considerPosition = true, withMuteSupport = true)
        cu.resolve()

        val interpreter = InternalInterpreter(JavaSystemInterface())

        interpreter.execute(cu, mapOf())
        val annotations = interpreter.systemInterface.getExecutedAnnotation().toSortedMap()
        var failed: Int = executeAnnotations(annotations)
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }

    @Test
    fun parseMUTE12_02_syntax() {
        assertCanBeParsed("overlay/MUTE12_02", withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_02_ast() {
        val ast = assertASTCanBeProduced("overlay/MUTE12_02", considerPosition = true, withMuteSupport = true)
        val ds = ast.getDataDefinition("DS00DS")

        val ds0004 = ds.getFieldByName("DS0004")
        assertEquals(NumberType(8, 3, RpgType.PACKED), ds0004.type)
        assertEquals(6, ds0004.size)

        val ds0005 = ds.getFieldByName("DS0005")
        assertEquals(NumberType(7, 3, RpgType.PACKED), ds0005.type)
        assertEquals(6, ds0005.size)
    }

    @Test
    @Ignore // 26 annotations still failing
    fun parseMUTE12_02_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_02", considerPosition = true, withMuteSupport = true)
        cu.resolve()

        val interpreter = InternalInterpreter(JavaSystemInterface())

        interpreter.execute(cu, mapOf())
        val annotations = interpreter.systemInterface.getExecutedAnnotation().toSortedMap()
        val failed: Int = executeAnnotations(annotations)
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }

    @Test
    fun parseMUTE12_03_syntax() {
        assertCanBeParsed("overlay/MUTE12_03", withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_03_ast() {
        assertASTCanBeProduced("overlay/MUTE12_03", considerPosition = true, withMuteSupport = true)
    }

    @Test
    @Ignore
    fun parseMUTE12_03_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_03", considerPosition = true, withMuteSupport = true)
        cu.resolve()
        val interpreter = InternalInterpreter(JavaSystemInterface())
        interpreter.execute(cu, mapOf())
        val annotations = interpreter.systemInterface.getExecutedAnnotation().toSortedMap()
        var failed: Int = executeAnnotations(annotations)
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }

    @Test
    fun parseMUTE12_04_syntax() {
        assertCanBeParsed("overlay/MUTE12_04", withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_04_ast() {
        assertASTCanBeProduced("overlay/MUTE12_04", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_04_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_04", considerPosition = true, withMuteSupport = true)
        cu.resolve()
        val interpreter = InternalInterpreter(JavaSystemInterface())
        interpreter.execute(cu, mapOf())
    }

    @Test
    fun parseMUTE12_05_syntax() {
        assertCanBeParsed("overlay/MUTE12_05", withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_05_ast() {
        assertASTCanBeProduced("overlay/MUTE12_05", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_05_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_05", considerPosition = true, withMuteSupport = true)
        cu.resolve()

        val interpreter = InternalInterpreter(JavaSystemInterface())

        interpreter.execute(cu, mapOf())
        val annotations = interpreter.systemInterface.getExecutedAnnotation().toSortedMap()
        var failed: Int = executeAnnotations(annotations)
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }
    @Test
    @Ignore // Not yet implemented
    fun parseMUTE12_06_syntax() {
        assertCanBeParsed("overlay/MUTE12_06", withMuteSupport = true)
    }

    @Test
    @Ignore // Not yet implemented
    fun parseMUTE12_06_ast() {
        assertASTCanBeProduced("overlay/MUTE12_06", considerPosition = true, withMuteSupport = true)
    }

    @Test
    @Ignore // Not yet implemented
    fun parseMUTE12_06_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_06", considerPosition = true, withMuteSupport = true)
        cu.resolve()
        val interpreter = InternalInterpreter(JavaSystemInterface())
        interpreter.execute(cu, mapOf())
    }
}
