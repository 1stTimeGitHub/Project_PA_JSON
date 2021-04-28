import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JSONPropertySearchTest {

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

        val instantiator = JSONInstantiator.newInstantiator()
        rootComponent = instantiator.instantiate(TestDataClass("Bob", 999, StudentType.Master, courses, grades, null,"aaa@bbb.ccc"))
    }

    @Test
    fun searchTest() {
        val propertySearcher = JSONPropertySearcher.newPropertySearcher()
        val searchResult = propertySearcher.search("name", rootComponent)

        val searchSerializer = JSONSerializer.newSerializer()
        val serializedSearch = searchSerializer.serialize(searchResult[0])

        val rootSerializer = JSONSerializer.newSerializer()
        val serializedRoot = rootSerializer.serialize(rootComponent)

        Assertions.assertEquals(serializedRoot, serializedSearch)
    }


}