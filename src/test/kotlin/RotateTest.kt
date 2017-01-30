import org.junit.Assert
import org.junit.Test

class RotateTest {

    var originalMatrix = 1

    val boxBlur: FloatArray = floatArrayOf(1F,2F,3F,4F)

    @Test
    fun flip(){
        val reversed: List<Float> = boxBlur.reversed()
        Assert.assertArrayEquals(floatArrayOf(4f,3f,2f,1f), reversed.toFloatArray(),0F)
    }

    @Test
    fun rotateClockWise() {

    }

}