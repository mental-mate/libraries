package tech.harmonysoft.oss.mentalmate.storage.data.memory

import java.io.InputStream
import tech.harmonysoft.oss.mentalmate.storage.data.DataStorageDir
import tech.harmonysoft.oss.mentalmate.storage.data.DataStorageFile

class InMemoryDataFile(
    override val parent: DataStorageDir,
    override val name: String,
    private val content: ByteArray
) : DataStorageFile {

    override fun getContent(): InputStream {
        return content.inputStream()
    }

    override fun toString(): String {
        return "$parent/$name"
    }
}