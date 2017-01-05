import org.junit.Test
import java.awt.Color

class Test {

    @Test
    fun basicTest() {
        val rgbInt = Color.CYAN.rgb
        println(rgbInt)
    }

    @Test
    fun fore() {
        for (i: Int in 0..10 - 1) {
            println(i)
        }
    }

    @Test
    fun arra() {
        val listOf = listOf<String>("colorRoof", "b")
        println(listOf.size)
    }

}