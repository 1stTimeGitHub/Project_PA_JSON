class JSONArray : JSONComponent {

    private var objects = ArrayList<JSONArrayComponent>()

    override fun accept(visitor : JSONVisitor) {
        visitor.visit(this)
    }

}