package tech.harmonysoft.oss.mentalmate.storage.data

import java.io.InputStream

interface DataStorageFile {

    val name: String

    val parent: DataStorageDir

    fun getContent(): InputStream
}