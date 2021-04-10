interface JSONVisitor {

    fun visit(component: JSONObject)

    fun visit(component: JSONArray)

    fun visit(component: JSONValue)

}