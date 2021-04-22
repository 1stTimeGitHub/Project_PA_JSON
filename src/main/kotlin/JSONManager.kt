import java.nio.file.Path

/**
 *
 */
class JSONManager private constructor(private val components: HashMap<String, JSONComponent>) {

    companion object {
        /**
         * Creates an empty JSONManager.
         * @return a new JSONManager.
         */
        @JvmStatic fun newManager() : JSONManager {
            return JSONManager(HashMap())
        }

        /**
         * Creates a new JSONManager from a given HashMap.
         * @param components
         * @return a new JSONManager.
         */
        @JvmStatic fun newPopulatedManager(components: HashMap<String, JSONComponent>) : JSONManager {
            return JSONManager(components)
        }
    }

    /**
     * @param fileName
     */
    fun createJSON(fileName: String) {
        if(components.containsKey(fileName)) return
    }

    /**
     * Deletes the JSON file with the given name.
     * @param fileName Name of the JSON file to be deleted
     */
    fun deleteJSON(fileName: String) {
        components.remove(fileName)
    }

    /**
     * Serializes the JSON file stored in memory with a given name.
     * @param fileName Name of the JSON file
     * @param path Path to the directory where the JSON file will be created
     */
    fun serializeJson(fileName: String, path: Path) {
        val writer = JSONWriter.newWriter(path.toString() + "\\${fileName}.txt")
        components[fileName]?.accept(writer)
    }

}