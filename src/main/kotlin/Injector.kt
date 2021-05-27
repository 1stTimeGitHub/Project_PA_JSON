import java.io.File
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible

@Target(AnnotationTarget.PROPERTY)
annotation class Inject
@Target(AnnotationTarget.PROPERTY)
annotation class InjectAdd

class Injector {

    companion object {

        private val setup : MutableMap<String, KClass<*>> = mutableMapOf()
        private val actions: MutableList<KClass<*>> = mutableListOf()

        init{
            val scanner = Scanner(File("src\\main\\resources\\configuration.properties"))
            while(scanner.hasNextLine()) {
                val line = scanner.nextLine()
                val parts = line.split("=")
                val plugins = parts[1].split(",")

                if(!parts[1].contains(","))
                    setup[parts[0]] = Class.forName(parts[1]).kotlin
                else{
                    plugins.forEach {
                        actions.add(Class.forName(it).kotlin)
                    }
                }
            }
            scanner.close()
        }

        fun <T:Any> create(type: KClass<T>) : T {
            val o = type.createInstance()
            type.declaredMemberProperties.forEach {
                if (it.hasAnnotation<Inject>()) {
                    it.isAccessible = true
                    val key = type.simpleName + "." + it.name
                    if (setup.isNotEmpty()) {
                        val obj = setup[key]!!.createInstance()
                        (it as KMutableProperty<*>).setter.call(o, obj)
                    }
                }
                else if (it.hasAnnotation<InjectAdd>()) {
                    it.isAccessible = true
                    val list = it.getter.call(o) as MutableList<Any>

                    for (obj in actions) {
                        list.add(obj.createInstance())
                    }
                }
            }
            return o
        }

    }

}