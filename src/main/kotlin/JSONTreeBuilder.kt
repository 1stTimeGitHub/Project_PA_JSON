import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Tree
import org.eclipse.swt.widgets.TreeItem

class JSONTreeBuilder private constructor(private val serializer: JSONSerializer): JSONVisitor {

    companion object {
        @JvmStatic fun newTreeBuilder(): JSONTreeBuilder {
            return JSONTreeBuilder(JSONSerializer.newSerializer())
        }
    }

    private lateinit var root: Tree
    private lateinit var parent: TreeItem
    private lateinit var text: String

    override fun visit(component: JSONObject) {
        val objectItem : TreeItem = if(!this::parent.isInitialized) TreeItem(root, SWT.NONE) else TreeItem(parent, SWT.NONE)

        if(text.isNullOrEmpty())
            text = "(object)"

        objectItem.text = text
        objectItem.data = serializer.serialize(component)
        serializer.flush()
        parent = objectItem

        val itr = component.getIterator()

        while(itr.hasNext()) {
            val pair = itr.next()
            text = pair.key
            pair.value.accept(this)
            parent = objectItem
        }
    }

    override fun visit(component: JSONArray) {
        val arrayItem : TreeItem = if(!this::parent.isInitialized) TreeItem(root, SWT.NONE) else TreeItem(parent, SWT.NONE)

        if(text.isNullOrEmpty())
            text = "(array)"

        arrayItem.text = text
        arrayItem.data = serializer.serialize(component)
        serializer.flush()

        parent = arrayItem

        val itr = component.getIterator()

        while(itr.hasNext()) {
            text = ""
            itr.next().accept(this)
            parent = arrayItem
        }
    }

    override fun visit(component: JSONValue) {
        val valueItem : TreeItem = if(!this::parent.isInitialized) TreeItem(root, SWT.NONE) else TreeItem(parent, SWT.NONE)

        if(text.isNullOrEmpty())
            text = "(value) : $component"
        else
            text = "$text: $component"

        valueItem.text = text
        valueItem.data = serializer.serialize(component)
        serializer.flush()

    }

    fun buildTree(root: Tree, component: JSONComponent) {
        this.root = root
        text = ""
        component.accept(this)
    }

}