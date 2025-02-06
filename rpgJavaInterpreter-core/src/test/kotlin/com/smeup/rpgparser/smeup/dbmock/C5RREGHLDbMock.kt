package com.smeup.rpgparser.smeup.dbmock

import com.smeup.rpgparser.interpreter.FileMetadata
import com.smeup.rpgparser.utils.DbMock
import java.io.File

class C5RREGHLDbMock : DbMock {
    override val metadata =
        FileMetadata.createInstance(File("src/test/resources/smeup/metadata/C5RREGHL.json").inputStream())
}
