package tech.harmonysoft.oss.mentalmate.storage.data.memory

import java.util.concurrent.ConcurrentHashMap
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import tech.harmonysoft.oss.common.collection.CollectionInitializer
import tech.harmonysoft.oss.common.collection.mapFirstNotNull
import tech.harmonysoft.oss.mentalmate.storage.data.DataStorage
import tech.harmonysoft.oss.mentalmate.storage.data.DataStorageDir
import tech.harmonysoft.oss.mentalmate.storage.data.DataStorageFile

@Primary
@Component
class MemoryDataStorage : DataStorage {

    private val data = ConcurrentHashMap<DataStorageDir, MutableCollection<DataStorageEntry>>()

    override fun listFiles(dir: DataStorageDir): Collection<DataStorageFile> {
        return data[dir]?.mapNotNull {
            if (it is DataStorageEntry.File) {
                it.data
            } else {
                null
            }
        } ?: emptyList()
    }

    override fun getDir(path: String): DataStorageDir {
        return DataStorageDir(path)
    }

    override fun createFile(dir: DataStorageDir, name: String, content: String): DataStorageFile {
        data[dir]?.mapFirstNotNull {
            if (it is DataStorageEntry.File && it.data.name == name) {
                it
            } else {
                null
            }
        }?.let {
            return it.data
        }

        val entries = data.getOrPut(dir, CollectionInitializer.mutableSet())
        val result = InMemoryDataFile(dir, name, content)
        entries += DataStorageEntry.File(result)
        return result
    }

    override fun clear(dir: DataStorageDir) {
        data.remove(dir)
    }
}