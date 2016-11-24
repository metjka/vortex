import org.junit.Test
import java.awt.Color

/**
 * Created by metka on 14.8.2016.
 */
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

}