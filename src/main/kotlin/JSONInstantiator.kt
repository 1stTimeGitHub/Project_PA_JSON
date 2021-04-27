import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

/**
 * Should be used on properties to exclude them from instantiation.
 */
@Target(AnnotationTarget.PROPERTY)
annotation class ExcludeProperty

/**
 * Should be used on properties to assign them a new identifier upon instantiation.
 */
@Target(AnnotationTarget.PROPERTY)
annotation class Identifier(val id: String)

/**
 * JSONInstantiator is responsible for instantiating data classes into the built JSON model.
 * This class is immutable.
 */
class JSONInstantiator {
    
    companion object {
        /**
         * Creates a new JSONInstantiator.
         * @return a new JSONInstantiator.
         */
        @JvmStatic fun newInstantiator(): JSONInstantiator {
            return JSONInstantiator()
        }
    }

    /**
     * @param obj is the object being instantiated.
     * @return a JSONComponent representation of the object.
     */
    fun instantiate(obj: Any): JSONComponent {
        when(obj) {
            is Boolean, is Char, is Short, is Int, is Long, is Float, is Double, is String -> return JSONValue.newValue(obj)
            is Enum<*> -> return JSONValue.newValue(obj.toString())
            is Collection<*> -> return handleCollection(obj.iterator())
            is Map<*,*> -> return handleMap(obj.iterator())
        }

        return handleDataClass(obj)
    }

    /**
     * Handles the instantiation of collections.
     * @param itr is an iterator for the Collection being instantiated.
     * @return a JSONArray representation of the collection.
     */
    private fun handleCollection(itr: Iterator<*>): JSONArray {
        val jArray = JSONArray.newArray()

        while(itr.hasNext()) {
            val element = itr.next()

            if(element != null) jArray.addComponent(instantiate(element))
        }

        return jArray
    }

    /**
     * Handles the instantiation of Maps.
     * @param itr is an iterator for the Map being instantiated.
     * @return a JSONObject representation of the Map.
     */
    private fun handleMap(itr: Iterator<Map.Entry<*,*>>): JSONObject {
        val jObject = JSONObject.newObject()

        while(itr.hasNext()) {
            val pair = itr.next()
            pair.value?.let { instantiate(it) }?.let { jObject.addComponent(pair.key.toString(), it) }
        }

        return jObject
    }

    /**
     * Handles the instantiation of data classes.
     * @param obj is the object to be instantiated.
     * @return a JSONObject representation of the data class.
     */
    private fun handleDataClass(obj: Any): JSONObject {
        val jObject = JSONObject.newObject()
        val clazz = obj::class
        clazz.declaredMemberProperties.forEach {
            if(it.hasAnnotation<ExcludeProperty>()) return@forEach

            if(it.hasAnnotation<Identifier>()) {
                it.findAnnotation<Identifier>()?.let { it1 ->
                    it.getter.call(obj)?.let { it2 -> instantiate(it2) }?.let { it3 ->
                        jObject.addComponent(it1.id, it3)
                    }
                }
            }
            else {
                it.call(obj)?.let { it1 -> instantiate(it1) }?.let { it2 -> jObject.addComponent(it.name, it2) }
            }
        }

        return jObject
    }

}