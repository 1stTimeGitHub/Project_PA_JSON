import java.io.File
import java.lang.StringBuilder
import java.nio.file.Path

/**
 *
 */
class JSONSerializer private constructor(private val buffer: StringBuilder, private var indentation: Int): JSONVisitor {

    companion object {
        /**
         * Creates a new JSONWriter.
         * @return a new JSONWriter
         */
        @JvmStatic fun newSerializer(): JSONSerializer {
            return JSONSerializer(StringBuilder(), 0)
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
        buffer.append("\n")
        writeIndentation()
        buffer.append("}")
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
        buffer.append("\n")
        writeIndentation()
        buffer.append("]")
    }

    /**
     * @param component
     */
    override fun visit(component: JSONValue) {
        buffer.append(component.toString())
    }

    /**
     * Serializes a JSONComponent.
     * @param rootComponent is the root of the JSON file.
     */
    fun serialize(rootComponent: JSONComponent): String {
        rootComponent.accept(this)
        return buffer.toString()
    }

    /**
     * Writes the serialized contents to a file.
     * @param path path for the new file.
     */
    fun writeToFile(path: Path) {
        val file = File(path.toString())

        file.bufferedWriter().use { out ->
            out.write(buffer.toString())
        }
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
     *
     */
    fun flush() {
        buffer.delete(0, buffer.length)
    }

}