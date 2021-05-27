import org.eclipse.swt.SWT
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.*
import kotlin.reflect.KClass

class IconSetup : GUISetup {

    override val shell: Shell = Shell(Display.getDefault())
    override val group: Group = Group(shell, SWT.NONE)
    override val tree: Tree = Tree(group, SWT.PUSH)
    override val label: Label = Label(group, SWT.FILL)

    override val serializer: JSONSerializer = JSONSerializer.newSerializer()

    override fun setup(component: JSONComponent) {
        shell.setSize(1000, 700)
        shell.text = ("JSON GUI - IconSetup")
        shell.layout = GridLayout(1,false)

        group.layoutData = GridData(SWT.FILL, SWT.FILL, true, true)
        group.layout = GridLayout(2, true)

        tree.layoutData = GridData(SWT.FILL, SWT.FILL, true, true)
        val treeBuilder = JSONTreeBuilder.newTreeBuilder()
        treeBuilder.buildTree(tree, component)
        addIcons(tree.items)

        tree.addSelectionListener(object : SelectionAdapter() {
            override fun widgetSelected(e: SelectionEvent) {
                label.text = serializer.serialize(tree.selection.first().data as JSONComponent)
                serializer.flush()
                label.parent.layout()
            }
        })

    }

    /**
     * @param treeItems
     */
    private fun addIcons(treeItems: Array<TreeItem>) {
        for (treeItem in treeItems) {
            if (treeItem.data is JSONValue)
                treeItem.image = Image(Display.getDefault(), "src\\main\\resources\\file.png")
            else
                treeItem.image = Image(Display.getDefault(), "src\\main\\resources\\folder.png")

            addIcons(treeItem.items)
        }
    }

}

class ExcludeNodes : GUISetup {

    override val shell: Shell = Shell(Display.getDefault())
    override val group: Group = Group(shell, SWT.NONE)
    override val tree: Tree = Tree(group, SWT.PUSH)
    override val label: Label = Label(group, SWT.FILL)

    override val serializer: JSONSerializer = JSONSerializer.newSerializer()

    override fun setup(component: JSONComponent) {
        val tempShell = Shell(Display.getDefault())
        tempShell.text = "Nodes do exclude"
        tempShell.layout = GridLayout(1,false)

        val tempGroup = Group(tempShell, SWT.NONE)
        tempGroup.layoutData = GridData(SWT.FILL, SWT.FILL, true, true)
        tempGroup.layout = GridLayout(6, true)

        val objectLabel = Label(tempGroup, SWT.FILL)
        objectLabel.text = "Object"
        val objectButton = Button(tempGroup, SWT.RADIO)
        val arrayLabel = Label(tempGroup, SWT.FILL)
        arrayLabel.text = "Array"
        val arrayButton = Button(tempGroup, SWT.RADIO)
        val valueLabel = Label(tempGroup, SWT.FILL)
        valueLabel.text = "Value"
        val valuesButton = Button(tempGroup, SWT.RADIO)
        val finishButton = Button(tempGroup, SWT.NONE)
        finishButton.text = "Confirm"

        var excludedNode = -1

        finishButton.addSelectionListener(object : SelectionAdapter() {
            override fun widgetSelected(e: SelectionEvent) {
                excludedNode = when {
                    objectButton.selection -> 0
                    arrayButton.selection -> 1
                    else -> 2
                }
                tempShell.close()
            }
        })

        tempShell.pack()
        tempShell.open()
        val display = Display.getDefault()

        while (!tempShell.isDisposed) {
            if (!display.readAndDispatch()) display.sleep()
        }

        shell.setSize(1000, 700)
        shell.text = ("JSON GUI - ExcludeNodes")
        shell.layout = GridLayout(1,false)

        group.layoutData = GridData(SWT.FILL, SWT.FILL, true, true)
        group.layout = GridLayout(2, true)

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

        excludeNodes(tree.items, excludedNode)

    }

    /**
     * @param treeItems
     * @param excludeNode
     */
    private fun excludeNodes(treeItems: Array<TreeItem>, excludeNode: Int) {
        for (treeItem in treeItems) {
            when {
                (treeItem.data is JSONObject) and (excludeNode == 0) -> treeItem.dispose()
                (treeItem.data is JSONArray) and (excludeNode == 1) -> treeItem.dispose()
                (treeItem.data is JSONValue) and (excludeNode == 2) -> treeItem.dispose()
                else -> excludeNodes(treeItem.items, excludeNode)
            }
        }
    }

}

class RenameNodes : GUISetup {

    override val shell: Shell = Shell(Display.getDefault())
    override val group: Group = Group(shell, SWT.NONE)
    override val tree: Tree = Tree(group, SWT.PUSH)
    override val label: Label = Label(group, SWT.FILL)

    override val serializer: JSONSerializer = JSONSerializer.newSerializer()

    override fun setup(component: JSONComponent) {
        val tempShell = Shell(Display.getDefault())
        tempShell.text = "Nodes to rename"
        tempShell.layout = GridLayout(1,false)

        val tempGroup = Group(tempShell, SWT.NONE)
        tempGroup.layoutData = GridData(SWT.FILL, SWT.FILL, true, true)
        tempGroup.layout = GridLayout(6, true)

        val objectLabel = Label(tempGroup, SWT.FILL)
        objectLabel.text = "Object"
        val objectButton = Button(tempGroup, SWT.RADIO)
        val arrayLabel = Label(tempGroup, SWT.FILL)
        arrayLabel.text = "Array"
        val arrayButton = Button(tempGroup, SWT.RADIO)
        val valueLabel = Label(tempGroup, SWT.FILL)
        valueLabel.text = "Value"
        val valuesButton = Button(tempGroup, SWT.RADIO)
        val textField = Text(tempGroup, SWT.FILL)
        val finishButton = Button(tempGroup, SWT.NONE)
        finishButton.text = "Confirm"

        var nodesToRename = -1
        var newName = ""

        finishButton.addSelectionListener(object : SelectionAdapter() {
            override fun widgetSelected(e: SelectionEvent) {
                nodesToRename = when {
                    objectButton.selection -> 0
                    arrayButton.selection -> 1
                    else -> 2
                }
                newName = textField.text
                tempShell.close()
            }
        })

        tempShell.pack()
        tempShell.open()
        val display = Display.getDefault()

        while (!tempShell.isDisposed) {
            if (!display.readAndDispatch()) display.sleep()
        }

        shell.setSize(1000, 700)
        shell.text = ("JSON GUI - RenameNodes")
        shell.layout = GridLayout(1,false)

        group.layoutData = GridData(SWT.FILL, SWT.FILL, true, true)
        group.layout = GridLayout(2, true)

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

        renameNodes(tree.items, nodesToRename, newName)

    }

    /**
     * @param treeItems
     * @param nodesToRename
     * @param newName
     */
    private fun renameNodes(treeItems: Array<TreeItem>, nodesToRename: Int, newName: String) {
        for (treeItem in treeItems) {
            when {
                (treeItem.data is JSONObject) and (nodesToRename == 0) -> treeItem.text = newName
                (treeItem.data is JSONArray) and (nodesToRename == 1) -> treeItem.text = newName
                (treeItem.data is JSONValue) and (nodesToRename == 2) -> treeItem.text = newName
            }
            renameNodes(treeItem.items, nodesToRename, newName)
        }
    }

}