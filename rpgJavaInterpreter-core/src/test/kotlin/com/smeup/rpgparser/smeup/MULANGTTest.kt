package com.smeup.rpgparser.smeup

import com.smeup.dbnative.DBNativeAccessConfig
import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.ConnectionConfig
import com.smeup.rpgparser.execution.DspfConfig
import com.smeup.rpgparser.execution.ReloadConfig
import com.smeup.rpgparser.execution.SimpleDspfConfig
import com.smeup.rpgparser.execution.SimpleReloadConfig
import com.smeup.rpgparser.interpreter.FeatureFlag
import kotlin.test.AfterTest
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
    open fun setUp() {
        smeupConfig = Configuration()
        val path = javaClass.getResource("/smeup/metadata")!!.path
        val connectionConfigs = listOf(ConnectionConfig(
            fileName = "*",
            url = "jdbc:hsqldb:hsql://127.0.0.1:9001/mainDb",
            user = "SA",
            password = "",
            driver = "org.hsqldb.jdbc.JDBCDriver"
        ))
        val reloadConfig = SimpleReloadConfig(metadataPath = path, connectionConfigs = connectionConfigs)
        smeupConfig.reloadConfig = ReloadConfig(
            nativeAccessConfig = DBNativeAccessConfig(connectionConfigs.map {
                com.smeup.dbnative.ConnectionConfig(
                    fileName = it.fileName,
                    url = it.url,
                    user = it.user,
                    password = it.password,
                    driver = it.driver,
                    impl = it.impl
                )
            }),
            metadataProducer = { dbFile: String -> reloadConfig.getMetadata(dbFile = dbFile) })
        val dspfConfig = SimpleDspfConfig(displayFilePath = path)
        smeupConfig.dspfConfig = DspfConfig(
            metadataProducer = { displayFile -> dspfConfig.getMetadata(displayFile) },
            dspfProducer = { displayFile -> dspfConfig.dspfProducer(displayFile) }
        )
    }

    @AfterTest()
    open fun tearDown() {}

    /**
     * Configuration to enable the Z-ADD legacy behavior.
     *
     * When this configuration is active, the interpreter applies truncation rules consistent with
     * AS400 behavior during Z-ADD operations. This includes truncation of integer and decimal parts
     * based on the target's type.
     *
     * The feature flag `FeatureFlag.ZAddLegacy` is checked to determine if the legacy behavior should be enabled.
     */
    protected val turnOnZAddLegacyFlagConfig = Configuration().apply {
        jarikoCallback.featureFlagIsOn = { featureFlag: FeatureFlag -> featureFlag == FeatureFlag.ZAddLegacyFlag }
    }

    /**
     * Configuration to disable the Z-ADD legacy behavior.
     *
     * When this configuration is active, the interpreter does not apply truncation rules during Z-ADD operations
     * as they would occur on AS400. Instead, default behavior is followed.
     *
     * The feature flag `FeatureFlag.ZAddLegacy` is explicitly set to `false` if checked.
     * Other feature flags retain their default behavior based on their `on` property.
     */
    protected val turnOffZAddLegacyFlagConfig = Configuration().apply {
        jarikoCallback.featureFlagIsOn =
            { featureFlag: FeatureFlag -> if (featureFlag == FeatureFlag.ZAddLegacyFlag) false else featureFlag.on }
    }
}