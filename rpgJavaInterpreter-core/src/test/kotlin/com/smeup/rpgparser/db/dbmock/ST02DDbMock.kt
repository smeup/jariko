package com.smeup.rpgparser.db.dbmock

import com.smeup.rpgparser.interpreter.FileMetadata
import com.smeup.rpgparser.utils.DbMock
import java.io.File

class ST02DDbMock : DbMock {
    override val metadata =
        FileMetadata.createInstance(File("src/test/resources/db/metadata/ST02D.json").inputStream())
}
