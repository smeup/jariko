package com.smeup.rpgparser.smeup.dbmock

import com.smeup.rpgparser.interpreter.*
import java.io.File

object MULANGTLDbMock : DbMock {
    val metadata = FileMetadata.createInstance(File("src/test/resources/smeup/metadata/MULANGTL.json").inputStream())

    override fun createTable(): String = """
        CREATE TABLE ${metadata.tableName} (
            ${this.buildDbColumnsFromDbFields(metadata.fields)})
        """
        .trimIndent()
    override fun dropTable(): String = "DROP TABLE ${metadata.tableName}"
    override fun populateTable(): String {
        return """
            INSERT INTO ${metadata.tableName}(MLSYST, MLLIBR, MLFILE, MLTIPO, MLPROG, MLPSEZ, MLPPAS, MLPDES, MLSQIN, MLSQFI, MLAAAT, MLAAVA, MLNNAT, MLNNVA, MLINDI, MLTEES, MLUSES, MLDTES, MLORES, MLASLA, MLASNR, MLLIBE) 
            VALUES ('FOO', 'FOO', 'FOO', 'FOO', 'FOO', 'FOO', 'FOO', 'FOO', 'FOO', 'FOO', 'FOO', 'FOO', 1.0, 1.0, 'FOO', 1, 'FOO', 1, 1, 'FOO', 1, 'FOO')
        """
        .trimIndent()
    }
}
