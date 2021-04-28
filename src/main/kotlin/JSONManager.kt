import java.nio.file.Path
import java.nio.file.Paths

/**
 *
 */
class JSONManager private constructor(private val jsonRoots: HashMap<String, JSONComponent>, private val instantiator: JSONInstantiator) {

    companion object {
        /**
         * Creates an empty JSONManager.
         * @return a new JSONManager.
         */
        @JvmStatic fun newManager() : JSONManager {
            return JSONManager(HashMap(), JSONInstantiator.newInstantiator())
        }

        /**
         * Creates a new JSONManager from a given HashMap.
         * @param components
         * @return a new JSONManager.
         */
        @JvmStatic fun newPopulatedManager(components: HashMap<String, JSONComponent>) : JSONManager {
            return JSONManager(components, JSONInstantiator.newInstantiator())
        }
    }

    /**
     * @param fileName
     */
    fun createJSON(fileName: String) {
        if(jsonRoots.containsKey(fileName)) return
    }

    /**
     * Deletes the JSON file with the given name.
     * @param fileName Name of the JSON file to be deleted.
     */
    fun deleteJSON(fileName: String) {
        jsonRoots.remove(fileName)
    }

    /**
     * Serializes the JSON file stored in memory with a given name.
     * @param fileName Name of the JSON file to be serialized.
     * @param path Path to the directory where the JSON file will be created.
     */
    fun serializeJson(fileName: String, path: Path) {
        val writer = JSONSerializer.newSerializer()
        writer.serialize(jsonRoots.getValue(fileName))
        writer.writeToFile(Paths.get(path.toString() + "\\${fileName}.txt"))
    }

    /**
     * @param fileName Name of the JSON file to be searched.
     * @param keyword The property being searched.
     * @return a list with the JSONObjects containing the desired property.
     */
    fun searchObjectProperty(fileName: String, keyword: String): ArrayList<JSONObject>? {
        val searcher = JSONPropertySearcher.newPropertySearcher()
        return searcher.search(keyword, jsonRoots.getValue(fileName))
    }

    /**
     * @param fileName
     * @param obj
     */
    fun instantiateJSON(fileName: String, obj: Any) {
        jsonRoots[fileName] = instantiator.instantiate(obj)
    }

}