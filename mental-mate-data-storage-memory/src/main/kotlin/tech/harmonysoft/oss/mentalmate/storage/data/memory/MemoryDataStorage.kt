package tech.harmonysoft.oss.mentalmate.storage.data.memory

import java.util.concurrent.ConcurrentHashMap
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import tech.harmonysoft.oss.common.collection.CollectionInitializer
import tech.harmonysoft.oss.mentalmate.storage.data.DataStorage
import tech.harmonysoft.oss.mentalmate.storage.data.DataStorageDir

@Primary
@Component
class MemoryDataStorage : DataStorage<InMemoryDataFile> {

    private val data = ConcurrentHashMap<DataStorageDir, MutableCollection<InMemoryDataFile>>()

    override fun listFiles(dir: DataStorageDir): Collection<InMemoryDataFile> {
        return data[normalize(dir)] ?: emptyList()
    }

    override fun getDir(path: String): DataStorageDir {
        return normalize(DataStorageDir(path))
    }

    override fun createFile(dir: DataStorageDir, name: String, content: ByteArray): InMemoryDataFile {
        val normalizedDir = normalize(dir)
        data[normalizedDir]?.removeIf {
            it.name == name
        }

        val entries = data.getOrPut(normalizedDir, CollectionInitializer.mutableSet())
        return InMemoryDataFile(normalizedDir, name, content).also {
            entries += it
        }
    }

    override fun delete(file: InMemoryDataFile) {
        val i = file.name.lastIndexOf("/")
        val dirPath = if (i < 0) {
            "/"
        } else {
            file.name.substring(0, i)
        }
        data[getDir(dirPath)]?.removeIf { it.name == file.name }
    }

    private fun normalize(dir: DataStorageDir): DataStorageDir {
        var result = dir.path
        if (!result.startsWith("/")) {
            result = "/$result"
        }
        if (!result.endsWith("/")) {
            result = "$result/"
        }
        return if (result == dir.path) {
            dir
        } else {
            DataStorageDir(result)
        }
    }
}