import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JSONSerializerTest {

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

    private lateinit var rootComponent : JSONComponent
    private val expectedSerialization ="{\n" +
            "\t\"courses\": [\n" +
            "\t\t\"PA\",\n" +
            "\t\t\"ICO\",\n" +
            "\t\t\"ABD\"\n" +
            "\t],\n" +
            "\t\"student type\": \"Master\",\n" +
            "\t\"name\": \"Bob\",\n" +
            "\t\"failed courses\": null,\n" +
            "\t\"grades\": {\n" +
            "\t\t\"PA\": 20.0,\n" +
            "\t\t\"ABD\": 18.1,\n" +
            "\t\t\"ICO\": 17.6\n" +
            "\t},\n" +
            "\t\"age\": 999\n" +
            "}"

    @BeforeAll
    fun setUp() {
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
    fun serializeTest() {
        val rootSerializer = JSONSerializer.newSerializer()
        val serializedRoot = rootSerializer.serialize(rootComponent)

        Assertions.assertEquals(expectedSerialization,serializedRoot)
    }

}