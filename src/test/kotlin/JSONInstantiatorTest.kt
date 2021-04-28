import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.nio.file.Paths

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JSONInstantiatorTest {

    enum class StudentType {
        Bachelor, Master, Doctoral
    }

    data class TestDataClass constructor(
        val name: String,
        val age: Int,
        @Identifier("student type") val studentType: StudentType,
        var courses: ArrayList<String>,
        var grades: HashMap<String,Float>,
        @Identifier("failed courses") var failedCourses: ArrayList<String>?,
        @ExcludeProperty val email: String
    )

    private lateinit var testDataClass : TestDataClass
    private lateinit var rootComponent : JSONComponent

    @BeforeAll
    fun setUp() {
        val courses = ArrayList<String>()
        courses.add("PA")
        courses.add("ICO")
        courses.add("ABD")

        val grades = HashMap<String, Float>()
        grades["PA"] = 20.0F
        grades["ICO"] = 17.6F
        grades["ABD"] = 18.1F

        testDataClass = TestDataClass("Bob", 999, StudentType.Master, courses, grades, null,"aaa@bbb.ccc")

        val jName = JSONValue.newValue("Bob")
        val jAge = JSONValue.newValue(999)
        val jStudentType = JSONValue.newValue(StudentType.Master.toString())

        val jCourses = JSONArray.newArray()
        jCourses.addComponent(JSONValue.newValue("PA"))
        jCourses.addComponent(JSONValue.newValue("ICO"))
        jCourses.addComponent(JSONValue.newValue("ABD"))

        val jMap = JSONObject.newObject()
        jMap.addComponent("PA", JSONValue.newValue(20.0F))
        jMap.addComponent("ICO", JSONValue.newValue(17.6F))
        jMap.addComponent("ABD", JSONValue.newValue(18.1F))

        val jFailedCourses = JSONValue.newValue(null)

        val jRootComponent = JSONObject.newObject()
        jRootComponent.addComponent("name", jName)
        jRootComponent.addComponent("age", jAge)
        jRootComponent.addComponent("student type", jStudentType)
        jRootComponent.addComponent("courses", jCourses)
        jRootComponent.addComponent("grades", jMap)
        jRootComponent.addComponent("failed courses", jFailedCourses)

        rootComponent = jRootComponent

    }

    @Test
    fun instantiateTest() {
        val instantiator = JSONInstantiator.newInstantiator()

        val objectSerializer = JSONSerializer.newSerializer()
        val instantiatedDataClass = instantiator.instantiate(testDataClass)
        val serializedObject = objectSerializer.serialize(instantiatedDataClass)

        val rootSerializer = JSONSerializer.newSerializer()
        val serializedRoot = rootSerializer.serialize(rootComponent)

        Assertions.assertEquals(serializedRoot, serializedObject)
    }

}