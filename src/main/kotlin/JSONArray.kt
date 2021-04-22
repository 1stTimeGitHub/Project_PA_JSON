/**
 *
 */
class JSONArray private constructor(private val components: ArrayList<JSONComponent>): JSONComponent {

    companion object{
        /**
         * @return a new JSONArray.
         */
        @JvmStatic fun newArray(): JSONArray {
            return JSONArray(ArrayList())
        }
    }

    /**
     * Accepts a visitor.
     * @param visitor
     */
    override fun accept(visitor: JSONVisitor) {
        visitor.visit(this)
    }

    /**
     * Adds a JSONComponent to the JSONArray.
     * @param component The component to be added
     */
    fun addComponent(component: JSONComponent) {
        components.add(component);
    }

    /**
     * Removes a JSONComponent from the JSONArray, if it is present.
     * @param component The component to be removed
     */
    fun removeComponent(component: JSONComponent) {
        components.remove(component)
    }

    /**
     * @return a Iterator for the JSONArray contents
     */
    fun getIterator(): MutableIterator<JSONComponent> {
        return components.iterator()
    }

}