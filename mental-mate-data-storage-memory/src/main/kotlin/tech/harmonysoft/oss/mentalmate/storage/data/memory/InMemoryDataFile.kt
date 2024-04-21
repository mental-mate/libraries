package tech.harmonysoft.oss.mentalmate.storage.data.memory

import java.io.InputStream
import tech.harmonysoft.oss.mentalmate.storage.data.DataStorageDir
import tech.harmonysoft.oss.mentalmate.storage.data.DataStorageFile

data class InMemoryDataFile(
    override val parent: DataStorageDir,
    override val name: String,
    val content: String
) : DataStorageFile {

    override fun getContent(): InputStream {
        return content.byteInputStream()
    }
}