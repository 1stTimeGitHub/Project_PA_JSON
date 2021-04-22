/**
 *
 */
class JSONValue private constructor(private val value: Any): JSONComponent {

    companion object{
        /**
         * @return a new JSONValue
         */
        @JvmStatic fun newValue(value: Any): JSONValue {
            return JSONValue(value)
        }
    }

    /**
     * Accepts a visitor
     * @param visitor
     */
    override fun accept(visitor : JSONVisitor) {
        visitor.visit(this)
    }

    /**
     * @return
     */
    override fun toString(): String {
        return value.toString()
    }

}