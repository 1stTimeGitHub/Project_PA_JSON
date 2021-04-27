import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation

@Target(AnnotationTarget.PROPERTY)
annotation class ExcludeProperty

class JSONInstantiator {
    
    companion object {
        @JvmStatic fun newInstantiator(): JSONInstantiator {
            return JSONInstantiator()
        }
    }

    fun instantiate(obj: Any): JSONComponent {
        when(obj) {
            is Boolean, is Char, is Short, is Int, is Long, is Float, is Double, is String -> return JSONValue.newValue(obj)
            is Collection<*> -> return handleCollection(obj.iterator())
            is Map<*,*> -> return handleMap(obj.iterator() as Iterator<MutableMap.MutableEntry<*, *>>)
        }

        return handleDataClass(obj)
    }

    private fun handleCollection(itr: Iterator<*>): JSONArray {
        val jArray = JSONArray.newArray()

        while(itr.hasNext()) {
            val element = itr.next()

            if (element != null) {
                jArray.addComponent(instantiate(element))

            }
        }
        return jArray;
    }

    private fun handleMap(itr: Iterator<MutableMap.MutableEntry<*,*>>): JSONObject {
        val jObject = JSONObject.newObject()

        while(itr.hasNext()) {
            val pair = itr.next()

            if(pair != null) {
                pair.value?.let { instantiate(it) }?.let { jObject.addComponent(pair.key.toString(), it) }
            }
        }

        return jObject
    }

    private fun handleDataClass(obj: Any): JSONObject {
        val jObject = JSONObject.newObject()
        val clazz = obj::class
        clazz.declaredMemberProperties.forEach {

            if(!it.hasAnnotation<ExcludeProperty>()) {
                it.call(obj)?.let { it1 -> instantiate(it1) }?.let { it2 -> jObject.addComponent(it.name, it2) }
            }

        }
        return jObject
    }
    
}