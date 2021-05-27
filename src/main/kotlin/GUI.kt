import org.eclipse.swt.SWT
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.graphics.Color
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.*

interface GUISetup {
    val shell: Shell
    val group: Group
    val tree: Tree
    val label: Label

    val serializer: JSONSerializer

    fun setup(component: JSONComponent)
}

interface Action {
    val name: String
    fun execute(gui: GUI)
}

class GUI {

    @Inject
    private lateinit var guiSetup: GUISetup
    @InjectAdd
    private var actions = mutableListOf<Action>()

    private var shell: Shell = Shell(Display.getDefault())
    private lateinit var group: Group
    lateinit var tree: Tree
    private lateinit var textField: Text
    lateinit var label: Label

    private val serializer = JSONSerializer.newSerializer()

    init {
        /**shell.setSize(1000, 700)
        shell.text = ("JSON GUI")
        shell.layout = GridLayout(1,false)
        }

        group = Group(shell, SWT.NONE)
        group.layoutData = GridData(SWT.FILL, SWT.FILL, true, true)
        group.layout = GridLayout(2, true)

        tree = Tree(group, SWT.PUSH)
        tree.layoutData = GridData(SWT.FILL, SWT.FILL, true, true)

        tree.addSelectionListener(object : SelectionAdapter() {
            override fun widgetSelected(e: SelectionEvent) {
                label.text = serializer.serialize(tree.selection.first().data as JSONComponent)
                serializer.flush()
                label.parent.layout()
            }
        })
         */
    }

    /**
     * @param component
     */
    fun open(component: JSONComponent) {
        if (this::guiSetup.isInitialized) {
            guiSetup.setup(component)
            shell = guiSetup.shell
            group = guiSetup.group
            tree = guiSetup.tree
            label = guiSetup.label
        }
        else {
            shell.setSize(1000, 700)
            shell.text = ("JSON GUI")
            shell.layout = GridLayout(1,false)

            group = Group(shell, SWT.NONE)
            group.layoutData = GridData(SWT.FILL, SWT.FILL, true, true)
            group.layout = GridLayout(2, true)

            tree = Tree(group, SWT.PUSH)
            tree.layoutData = GridData(SWT.FILL, SWT.FILL, true, true)
            val treeBuilder = JSONTreeBuilder.newTreeBuilder()
            treeBuilder.buildTree(tree, component)

            tree.addSelectionListener(object : SelectionAdapter() {
                override fun widgetSelected(e: SelectionEvent) {
                    label.text = serializer.serialize(tree.selection.first().data as JSONComponent)
                    serializer.flush()
                    label.parent.layout()
                }
            })

            label = Label(group, SWT.FILL)
            label.layoutData = GridData(SWT.FILL, SWT.FILL, true, true)
        }

        textField = Text(group, SWT.NONE)
        textField.layoutData = GridData(SWT.FILL, SWT.FILL, true, true)

        textField.addModifyListener {
            tree.paint(textField.text)
        }

        actions.forEach { action ->
            val button = Button(group, SWT.NONE)
            button.text = action.name

            button.addSelectionListener(object : SelectionAdapter() {
                override fun widgetSelected(e: SelectionEvent) {
                    action.execute(this@GUI)
                }
            })
        }

        tree.expandAll()
        shell.pack()
        shell.open()
        val display = Display.getDefault()

        while (!shell.isDisposed) {
            if (!display.readAndDispatch()) display.sleep()
        }

        display.dispose()
    }

    private fun Tree.paint(keyword: String) {
        transverse {
            if(it.text.contains(keyword, true) and keyword.isNotEmpty()) it.background = Color(0,200,255)
            else it.background = Color(255,255,255)
        }
    }

    private fun Tree.expandAll() = transverse { it.expanded = true }

    private fun Tree.transverse(visitor: (TreeItem) -> Unit) {
        fun TreeItem.traverse() {
            visitor(this)
            items.forEach {
                it.traverse()
            }
        }
        items.forEach { it.traverse() }
    }

    /**
     * @return The tree items.
     */
    fun getTreeItems(): Array<TreeItem> = tree.items

    /**
     * @return The selected item.
     */
    fun getSelectedItem(): TreeItem = tree.selection.first()

    /**
     * @return The text being displayed by the label.
     */
    fun getLabelText(): String = label.text

}