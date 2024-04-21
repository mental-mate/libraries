package tech.harmonysoft.oss.mentalmate.storage.data

interface DataStorage {

    fun listFiles(dir: DataStorageDir): Collection<DataStorageFile>

    fun getDir(path: String): DataStorageDir

    fun createFile(dir: DataStorageDir, name: String, content: String): DataStorageFile

    fun clear(dir: DataStorageDir)
}