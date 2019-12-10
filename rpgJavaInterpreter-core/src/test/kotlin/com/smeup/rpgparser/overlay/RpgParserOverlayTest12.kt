package com.smeup.rpgparser.overlay

import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.assertCanBeParsed
import com.smeup.rpgparser.executeAnnotations
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.interpreter.ArrayType
import com.smeup.rpgparser.interpreter.CharacterType
import com.smeup.rpgparser.interpreter.DummyDBInterface
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

        val ds0011 = ds.getFieldByName("DS0011")
        assertEquals(NumberType(2, 0, RpgType.BINARY), ds0011.type)
        assertEquals(2, ds0011.size)

        val ds0012 = ds.getFieldByName("DS0012")
        assertEquals(NumberType(2, 0, RpgType.BINARY), ds0012.type)
        assertEquals(2, ds0012.size)

        val ds0013 = ds.getFieldByName("DS0013")
        assertEquals(NumberType(3, 0, RpgType.INTEGER), ds0013.type)
        assertEquals(1, ds0013.size)

        val ds0014 = ds.getFieldByName("DS0014")
        assertEquals(NumberType(3, 0, RpgType.UNSIGNED), ds0014.type)
        assertEquals(1, ds0014.size)

        val ds0015 = ds.getFieldByName("DS0015")
        assertEquals(NumberType(5, 0, RpgType.INTEGER), ds0015.type)
        assertEquals(2, ds0015.size)

        val ds0016 = ds.getFieldByName("DS0016")
        assertEquals(NumberType(5, 0, RpgType.UNSIGNED), ds0016.type)
        assertEquals(2, ds0016.size)

        val ds0017 = ds.getFieldByName("DS0017")
        assertEquals(NumberType(10, 0, RpgType.INTEGER), ds0017.type)
        assertEquals(4, ds0017.size)

        val ds0018 = ds.getFieldByName("DS0018")
        assertEquals(NumberType(10, 0, RpgType.UNSIGNED), ds0018.type)
        assertEquals(4, ds0018.size)

        val ds0019 = ds.getFieldByName("DS0019")
        assertEquals(NumberType(19, 0, RpgType.INTEGER), ds0019.type)
        assertEquals(8, ds0019.size)

        val ds0020 = ds.getFieldByName("DS0020")
        assertEquals(NumberType(19, 0, RpgType.UNSIGNED), ds0020.type)
        assertEquals(8, ds0020.size)

        // Data structure £40FDS
        val fds = ast.getDataDefinition("£40FDS")
        assertEquals(7095, fds.type.elementSize())
        assertEquals(7095, fds.elementSize())

        val fde = fds.getFieldByName("£40FDE")
        assertEquals(StringType(30), fde.type)

        val fre = fds.getFieldByName("£40FRE")
        assertEquals(StringType(50), fre.type)

        val fnt = fds.getFieldByName("£40FNT")
        assertEquals(NumberType(5, 0, RpgType.ZONED), fnt.type)

        val fmso = fds.getFieldByName("£40F_MSO")
        assertEquals(StringType(1), fmso.type)

        val fmwl = fds.getFieldByName("£40F_MWL")
        assertEquals(StringType(1), fmwl.type)

        val fmwc = fds.getFieldByName("£40F_MWC")
        assertEquals(StringType(1), fmwc.type)

        val flu = fds.getFieldByName("£40FLU")
        assertEquals(StringType(2), flu.type)

        val fmsr = fds.getFieldByName("£40F_MSR")
        assertEquals(StringType(1), fmsr.type)

        val fns = fds.getFieldByName("£40FNS")
        assertEquals(StringType(1), fns.type)

        val fto = fds.getFieldByName("£40FTO")
        assertEquals(ArrayType(StringType(12), 500), fto.type)
    }

    @Test
    fun parseMUTE12_02_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_02", considerPosition = true, withMuteSupport = true)
        cu.resolve(DummyDBInterface)

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
        val cu = assertASTCanBeProduced("overlay/MUTE12_03", considerPosition = true, withMuteSupport = true)
        cu.resolve(DummyDBInterface)
        // After th
    }

    @Test
    fun parseMUTE12_03_inz() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_03", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_03_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_03", considerPosition = true, withMuteSupport = true)
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
    fun parseMUTE12_06_syntax() {
        assertCanBeParsed("overlay/MUTE12_06", withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_06_ast() {
        assertASTCanBeProduced("overlay/MUTE12_06", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_06_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_06", considerPosition = true, withMuteSupport = true)
        cu.resolve(DummyDBInterface)
        val interpreter = InternalInterpreter(JavaSystemInterface())
        interpreter.execute(cu, mapOf())
    }
}
