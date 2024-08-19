package com.smeup.rpgparser.smeup.dbmock

import com.smeup.rpgparser.interpreter.FileMetadata
import java.io.File

class TABDS01LDbMock : MetadataDbMock() {
    override val metadata =
        FileMetadata.createInstance(File("src/test/resources/smeup/metadata/TABDS01L.json").inputStream())
}