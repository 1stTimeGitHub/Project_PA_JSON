class JSONStringSearcher private constructor(): JSONVisitor{

    companion object {
        /**
         * Creates a new JSONStringSearcher
         * @return a new JSONStringSearcher.
         */
        @JvmStatic fun newStringSearcher(): JSONStringSearcher {
            return JSONStringSearcher()
        }
    }

    private lateinit var matches: ArrayList<JSONValue>

    /**
     * @param component
     */
    override fun visit(component: JSONObject) {
        val itr = component.getIterator()

        while(itr.hasNext()) {
            val pair = itr.next()
            pair.value.accept(this)
        }
    }

    /**
     * @param component
     */
    override fun visit(component: JSONArray) {
        val itr = component.getIterator()

        while(itr.hasNext()) {
            val element = itr.next()
            element.accept(this)
        }
    }

    /**
     * @param component
     */
    override fun visit(component: JSONValue) {
        if(component.toString().endsWith("\""))
            matches.add(component)
    }

    /**
     * @param rootComponent is list containing the matches.
     */
    fun search(rootComponent: JSONComponent): ArrayList<JSONValue> {
        matches = ArrayList()

        return matches
    }

}