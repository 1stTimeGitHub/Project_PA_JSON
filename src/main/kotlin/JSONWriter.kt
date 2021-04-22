import java.io.File
import java.lang.StringBuilder

/**
 *
 */
class JSONWriter private constructor(private val buffer: StringBuilder, private val file: File, private var indentation: Int): JSONVisitor {

    companion object {
        /**
         * Creates new JSONWriter.
         * @return a new JSONWriter
         */
        @JvmStatic fun newWriter(path: String): JSONWriter {
            return JSONWriter(StringBuilder(), File(path), 0)
        }
    }

    /**
     * @param component
     */
    override fun visit(component: JSONObject) {
        val itr = component.getIterator()

        buffer.append("{\n")
        indentation++

        while(itr.hasNext()) {
            val pair = itr.next()

            writeIndentation()
            buffer.append("\"${pair.key}\": ")
            pair.value.accept(this)

            if(itr.hasNext()) buffer.append(",\n")
        }

        indentation--
        buffer.append("\n}")
        attemptToFinalizeWriting()
    }

    /**
     * @param component
     */
    override fun visit(component: JSONArray) {
        val itr = component.getIterator()

        buffer.append("[\n")
        indentation++

        while(itr.hasNext()) {
            val element = itr.next()

            writeIndentation()
            element.accept(this)

            if(itr.hasNext()) buffer.append(",\n")
        }

        indentation--
        buffer.append("\n]")
        attemptToFinalizeWriting()
    }

    /**
     * @param component
     */
    override fun visit(component: JSONValue) {
        buffer.append(component.toString())
        attemptToFinalizeWriting()
    }

    /**
     * Writes the current required indentation to the buffer.
     */
    private fun writeIndentation() {
        for(i in 0 until indentation) {
            buffer.append("\t")
        }
    }

    /**
     * If the indentation reaches 0, the contents of the buffer are written to the file.
     */
    private fun attemptToFinalizeWriting() {
        if(indentation != 0) return

        file.bufferedWriter().use { out ->
            out.write(buffer.toString())
        }
    }

}