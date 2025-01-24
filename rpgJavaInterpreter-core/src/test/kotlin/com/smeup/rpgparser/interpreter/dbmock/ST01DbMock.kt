package com.smeup.rpgparser.interpreter.dbmock

import com.smeup.rpgparser.interpreter.FileMetadata
import com.smeup.rpgparser.utils.DbMock
import java.io.File

class ST01DbMock : DbMock {
    override val metadata =
        FileMetadata.createInstance(File("src/test/resources/symboltable/metadata/ST01.json").inputStream())
}
