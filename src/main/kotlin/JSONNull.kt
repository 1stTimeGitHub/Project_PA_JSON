/**
 * Used to represent a null value.
 */
class JSONNull private constructor(){

    companion object {
        /**
         * Creates a new JSONNull.
         * @return a new JSONNull.
         */
        @JvmStatic fun newNull(): JSONNull{
            return JSONNull()
        }
    }

    /**
     * @return
     */
    override fun toString(): String {
        return "null"
    }

}