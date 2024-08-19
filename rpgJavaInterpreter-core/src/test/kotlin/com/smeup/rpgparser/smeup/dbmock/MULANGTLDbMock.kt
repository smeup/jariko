package com.smeup.rpgparser.smeup.dbmock

import com.smeup.rpgparser.interpreter.FileMetadata
import java.io.File

class MULANGTLDbMock : MetadataDbMock() {
    override val metadata =
        FileMetadata.createInstance(File("src/test/resources/smeup/metadata/MULANGTL.json").inputStream())
}
