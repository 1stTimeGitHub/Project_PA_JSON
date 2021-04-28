/**
 * JSONPropertySearcher is responsible for searching for JSONObject properties.
 * The property searcher can be reused.
 */
class JSONPropertySearcher private constructor(): JSONVisitor {

    companion object {
        /**
         * Creates a new JSONPropertySearcher.
         * @return a new JSONPropertySearcher.
         */
        @JvmStatic fun newPropertySearcher(): JSONPropertySearcher {
            return JSONPropertySearcher()
        }
    }

    private lateinit var keyword: String
    private lateinit var matches: ArrayList<JSONObject>

    /**
     * @param component JSONObject being visited.
     */
    override fun visit(component: JSONObject) {
        val itr = component.getIterator()

        while(itr.hasNext()) {
            val pair = itr.next()

            if(pair.key == keyword) {
                matches.add(component)
            }

            pair.value.accept(this)
        }
    }

    /**
     *  @param component JSONArray being visited.
     */
    override fun visit(component: JSONArray) {
        val itr = component.getIterator()

        while(itr.hasNext()) {
            val element = itr.next()
            element.accept(this)
        }
    }

    /**
     * @param component JSONValue being visited.
     */
    override fun visit(component: JSONValue) { }

    /**
     * @param rootComponent
     * @return A List containing the JSONObjects with the desired property.
     */
    fun search(keyword: String, rootComponent: JSONComponent): ArrayList<JSONObject> {
        this.keyword = keyword
        this.matches = ArrayList()

        rootComponent.accept(this)
        return matches
    }
}