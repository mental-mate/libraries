package tech.harmonysoft.oss.cucumber.glue

import io.cucumber.java.en.Given
import org.springframework.beans.factory.annotation.Autowired
import tech.harmonysoft.oss.mentalmate.storage.data.DataStorage
import tech.harmonysoft.oss.mentalmate.storage.data.DataStorageDir

class DataStorageStepDefinitions {

    @Autowired private lateinit var storage: DataStorage

    @Given("^there is a data file ([^\\s]+) with the following content:$")
    fun createFile(path: String, content: String) {
        val i = path.lastIndexOf("/")
        val dir = DataStorageDir(path.substring(0, i))
        storage.createFile(dir, path.substring(i + 1), content)
    }
}