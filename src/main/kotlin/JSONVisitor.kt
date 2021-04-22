/**
 *
 */
interface JSONVisitor {

    /**
     * @param component
     */
    fun visit(component: JSONObject)

    /**
     * @param component
     */
    fun visit(component: JSONArray)

    /**
     * @param component
     */
    fun visit(component: JSONValue)

}