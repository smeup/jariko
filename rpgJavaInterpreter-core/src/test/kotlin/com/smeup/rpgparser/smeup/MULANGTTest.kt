package com.smeup.rpgparser.smeup

import com.smeup.dbnative.DBNativeAccessConfig
import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.ReloadConfig
import com.smeup.rpgparser.execution.SimpleReloadConfig
import kotlin.test.BeforeTest

/**
 * This is the base class for all the tests related to the MULANGT program.
 * For each MULANGTnn program we have MULANGTnnPurposesTest and MULANGTnnPurposesTestCompiled.
 * @see MULANGT60VideoTest where nn is 60 and TestPurposes is Video
 * @see MULANGT60VideoTestCompiled see above
 */
abstract class MULANGTTest : AbstractTest() {

    /**
     * The configuration used to execute some kind of tests, for example all test required files.
     * The required files are placed in the resources/smeup/metadata folder.
     */
    lateinit var smeupConfig: Configuration

    @BeforeTest
    fun setUp() {
        smeupConfig = Configuration()
        val path = javaClass.getResource("/smeup/metadata")!!.path
        val reloadConfig = SimpleReloadConfig(metadataPath = path, connectionConfigs = listOf())
        smeupConfig.reloadConfig = ReloadConfig(
            nativeAccessConfig = DBNativeAccessConfig(emptyList()),
            metadataProducer = { dbFile: String -> reloadConfig.getMetadata(dbFile = dbFile) })
    }
}