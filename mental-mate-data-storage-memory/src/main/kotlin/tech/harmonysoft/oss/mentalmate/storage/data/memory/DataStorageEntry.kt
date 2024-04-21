package tech.harmonysoft.oss.mentalmate.storage.data.memory

import tech.harmonysoft.oss.mentalmate.storage.data.DataStorageDir

sealed class DataStorageEntry {

    data class File(val data: InMemoryDataFile) : DataStorageEntry()

    data class Dir(val data: DataStorageDir) : DataStorageEntry()
}