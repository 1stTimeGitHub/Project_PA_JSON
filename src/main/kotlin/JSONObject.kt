import kotlin.collections.HashMap

/**
 *
 */
class JSONObject private constructor(private val components: HashMap<String, JSONComponent>): JSONComponent {

    companion object {
        /**
         * @return a new JSONObject.
         */
        @JvmStatic fun newObject() : JSONObject {
            return JSONObject(HashMap())
        }
    }

    /**
     * Accepts a visitor.
     * @param visitor
     */
    override fun accept(visitor : JSONVisitor) {
        visitor.visit(this)
    }

    /**
     * Adds a JSONComponent to the JSONObject.
     */
    fun addComponent(componentName : String, component : JSONComponent) {
        components[componentName] = component
    }

    /**
     * Removes a JSONComponent from the JSONArray, if it is present.
     * @param
     */
    fun removeComponent(name : String) {
        components.remove(name)
    }

    /**
     * @return a Iterator for the JSONObject contents
     */
    fun getIterator(): MutableIterator<MutableMap.MutableEntry<String, JSONComponent>> {
        return components.iterator()
    }

}