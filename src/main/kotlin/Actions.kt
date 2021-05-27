import org.eclipse.swt.SWT
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.*
import java.io.File

class WriteToFile : Action {

    override val name = "Write"

    override fun execute(gui: GUI) {
        val file = File("src\\main\\resources\\SerializedJSON.txt")

        file.bufferedWriter().use { out ->
            out.write(gui.getLabelText())
        }
    }
}

class EditProperty : Action {

    override val name = "Edit"

    override fun execute(gui: GUI) {
        val shell = Shell(Display.getDefault())
        shell.text = "Edit"
        shell.layout = GridLayout(1,false)

        val group = Group(shell, SWT.NONE)
        group.layoutData = GridData(SWT.FILL, SWT.FILL, true, true)
        group.layout = GridLayout(2, true)

        val label = Label(group, SWT.FILL)
        label.text = "Name: "
        val textField = Text(group, SWT.FILL)
        val button = Button(group, SWT.NONE)
        button.text = "Edit"

        button.addSelectionListener(object : SelectionAdapter() {
            override fun widgetSelected(e: SelectionEvent) {
                val selectedItem = gui.getSelectedItem()

                if (selectedItem.data is JSONValue) {
                    selectedItem.text = textField.text
                    val newValue = JSONValue.newValue(textField.text)
                    selectedItem.data = newValue
                    gui.label.text = newValue.toString()
                    gui.label.parent.layout()
                }
            }
        })

        shell.pack()
        shell.open()
        val display = Display.getDefault()

        while (!shell.isDisposed) {
            if (!display.readAndDispatch()) display.sleep()
        }

    }

}

class Validate : Action {

    override val name = "Validate"

    override fun execute(gui: GUI) {
        val validation = validate(gui.getTreeItems())

        val shell = Shell(Display.getDefault())
        shell.text = "Edit"
        shell.layout = GridLayout(1,false)

        val group = Group(shell, SWT.NONE)
        group.layoutData = GridData(SWT.FILL, SWT.FILL, true, true)
        group.layout = GridLayout(2, true)

        val label = Label(group, SWT.FILL)
        if (validation) label.text = "Validation successful!" else label.text = "Validation failed!"

        shell.pack()
        shell.open()
        val display = Display.getDefault()

        while (!shell.isDisposed) {
            if (!display.readAndDispatch()) display.sleep()
        }

    }

    /**
     * @param treeItems The tree items being validated
     * @return True if all items were validated successfully, otherwise returns False
     */
    private fun validate(treeItems: Array<TreeItem>): Boolean {
        for (treeItem in treeItems) {
            if (treeItem.text.isNullOrEmpty() or !validate(treeItem.items)) {
                return false
            }
        }
        return true
    }

}