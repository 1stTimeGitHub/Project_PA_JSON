import kotlin.collections.HashMap

class JSONObject : JSONComponent, JSONArrayComponent {

    private var values = HashMap<String, Any>()

    override fun accept(visitor : JSONVisitor) {
        visitor.visit(this)
    }

}