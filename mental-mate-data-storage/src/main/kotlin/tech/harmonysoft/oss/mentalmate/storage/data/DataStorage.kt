package tech.harmonysoft.oss.mentalmate.storage.data

interface DataStorage<FILE : DataStorageFile> {

    fun listFiles(dir: DataStorageDir): Collection<FILE>

    fun getDir(path: String): DataStorageDir

    fun createFile(dir: DataStorageDir, name: String, content: ByteArray): FILE

    fun delete(file: FILE)
}