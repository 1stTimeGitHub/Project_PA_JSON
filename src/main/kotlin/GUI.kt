import org.eclipse.swt.SWT
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.*

class GUI private constructor(component: JSONComponent) {

    private val shell: Shell
    private val tree: Tree
    private val label: Label

    companion object {
        @JvmStatic fun newGUI(component: JSONComponent) : GUI{
            return GUI(component)
        }
    }

    init {
        shell = Shell(Display.getDefault())
        shell.setSize(1000, 700)
        shell.text = ("JSON GUI")
        shell.layout = GridLayout(1,false)

        val group = Group(shell, SWT.NONE);
        group.layoutData = GridData(SWT.FILL, SWT.FILL, true, true)
        group.layout = GridLayout(2, true)

        tree = Tree(group, SWT.PUSH)
        tree.layoutData = GridData(SWT.FILL, SWT.FILL, true, true)


        val treeBuilder = JSONTreeBuilder.newTreeBuilder()
        treeBuilder.buildTree(tree, component)

        label = Label(group, SWT.FILL)
        label.layoutData = GridData(SWT.FILL, SWT.FILL, true, true)

        tree.addSelectionListener(object : SelectionAdapter() {
            override fun widgetSelected(e: SelectionEvent) {
                label.text = tree.selection.first().data.toString()
                label.parent.layout()
            }
        })
    }

    fun open() {
        tree.expandAll()
        shell.pack()
        shell.open()
        val display = Display.getDefault()

        while (!shell.isDisposed) {
            if (!display.readAndDispatch()) display.sleep()
        }

        display.dispose()
    }

}