class JSONManager private constructor(private val components: HashMap<String, JSONComponent>) {

    companion object {
        @JvmStatic
        fun createEmptyManager() : JSONManager {
            return JSONManager(HashMap<String, JSONComponent>())
        }

        @JvmStatic
        fun createPopulatedManager(components: HashMap<String, JSONComponent>) : JSONManager {
            return JSONManager(components)
        }

    }

    fun serializeJson(fileName: String) {

    }

}