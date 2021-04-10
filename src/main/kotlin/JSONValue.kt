class JSONValue constructor(private var value : Any) : JSONComponent, JSONArrayComponent {

    override fun accept(visitor : JSONVisitor) {
        visitor.visit(this)
    }

}