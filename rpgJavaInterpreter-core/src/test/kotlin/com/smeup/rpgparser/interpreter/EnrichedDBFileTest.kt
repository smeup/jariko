package com.smeup.rpgparser.interpreter

import com.smeup.dbnative.file.DBFile
import com.smeup.dbnative.file.Record
import com.smeup.dbnative.file.Result
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import org.junit.Before
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test

class EnrichedDBFileTest {

    private lateinit var dbFile: DBFile
    private lateinit var fileDefinition: FileDefinition
    private lateinit var jarikoMetadata: FileMetadata

    private val turnOnChainCacheFlagConfig = Configuration().apply {
        jarikoCallback.featureFlagIsOn = { featureFlag: FeatureFlag -> featureFlag == FeatureFlag.ChainCacheFlag }
    }

    private val turnOffChainCacheFlagConfig = Configuration().apply {
        jarikoCallback.featureFlagIsOn =
            { featureFlag: FeatureFlag -> if (featureFlag == FeatureFlag.ChainCacheFlag) false else featureFlag.on }
    }

    @Before
    fun setup() {
        dbFile = mock()
        fileDefinition = mock()
        jarikoMetadata = mock()
    }

    @Test
    fun `chain should return result from cache if cache is enabled and key exists`() {
        val configuration = turnOnChainCacheFlagConfig
        val systemInterface = JavaSystemInterface(configuration)
        MainExecutionContext.execute(configuration = configuration, systemInterface = systemInterface) {
            val enrichedDBFile = EnrichedDBFile(dbFile, fileDefinition, jarikoMetadata)
            val result = mock<Result>()
            whenever(dbFile.chain(anyString())).thenReturn(result)
            enrichedDBFile.chain("testKey1")
            enrichedDBFile.chain("testKey1")
            enrichedDBFile.chain("testKey2")
            verify(dbFile, times(1)).chain("testKey1")
            verify(dbFile, times(1)).chain("testKey2")
        }
    }

    @Test
    fun `chain should return result from dbFile if cache is not enabled`() {
        val configuration = turnOffChainCacheFlagConfig
        val systemInterface = JavaSystemInterface(configuration)
        MainExecutionContext.execute(configuration = configuration, systemInterface = systemInterface) {
            val enrichedDBFile = EnrichedDBFile(dbFile, fileDefinition, jarikoMetadata)
            val result = mock<Result>()
            whenever(dbFile.chain(anyString())).thenReturn(result)
            enrichedDBFile.chain("testKey1")
            enrichedDBFile.chain("testKey1")
            enrichedDBFile.chain("testKey2")
            verify(dbFile, times(2)).chain("testKey1")
            verify(dbFile, times(1)).chain("testKey2")
        }
    }

    @Test
    fun `delete should clear cache and return result from dbFile`() {
        val configuration = turnOnChainCacheFlagConfig
        val systemInterface = JavaSystemInterface(configuration)
        MainExecutionContext.execute(configuration = configuration, systemInterface = systemInterface) {
            val enrichedDBFile = EnrichedDBFile(dbFile, fileDefinition, jarikoMetadata)
            val record = mock<Record>()
            val result = mock<Result>()
            whenever(dbFile.chain(anyString())).thenReturn(result)
            whenever(dbFile.delete(record)).thenReturn(result)
            enrichedDBFile.chain("testKey1")
            enrichedDBFile.chain("testKey1")
            verify(dbFile, times(1)).chain("testKey1")
            enrichedDBFile.delete(record)
            enrichedDBFile.chain("testKey1")
            verify(dbFile, times(2)).chain("testKey1")
        }
    }

    @Test
    fun `write should clear cache and return result from dbFile`() {
        val configuration = turnOnChainCacheFlagConfig
        val systemInterface = JavaSystemInterface(configuration)
        MainExecutionContext.execute(configuration = configuration, systemInterface = systemInterface) {
            val enrichedDBFile = EnrichedDBFile(dbFile, fileDefinition, jarikoMetadata)
            val record = mock<Record>()
            val result = mock<Result>()
            whenever(dbFile.chain(anyString())).thenReturn(result)
            whenever(dbFile.write(record)).thenReturn(result)
            enrichedDBFile.chain("testKey1")
            enrichedDBFile.chain("testKey1")
            verify(dbFile, times(1)).chain("testKey1")
            enrichedDBFile.write(record)
            enrichedDBFile.chain("testKey1")
            verify(dbFile, times(2)).chain("testKey1")
        }
    }

    @Test
    fun `update should clear cache and return result from dbFile`() {
        val configuration = turnOnChainCacheFlagConfig
        val systemInterface = JavaSystemInterface(configuration)
        MainExecutionContext.execute(configuration = configuration, systemInterface = systemInterface) {
            val enrichedDBFile = EnrichedDBFile(dbFile, fileDefinition, jarikoMetadata)
            val record = mock<Record>()
            val result = mock<Result>()
            whenever(dbFile.chain(anyString())).thenReturn(result)
            whenever(dbFile.update(record)).thenReturn(result)
            enrichedDBFile.chain("testKey1")
            enrichedDBFile.chain("testKey1")
            verify(dbFile, times(1)).chain("testKey1")
            enrichedDBFile.update(record)
            enrichedDBFile.chain("testKey1")
            verify(dbFile, times(2)).chain("testKey1")
        }
    }
}