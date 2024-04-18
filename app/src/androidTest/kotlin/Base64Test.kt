import android.util.Base64
import android.util.Log
import org.junit.Test

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/04/17
 *     desc   : 测试Base64
 *     version: 1.0
 * </pre>
 */
class Base64Test {
    @Test
    fun test() {
        println("==开始测试==")
        val waitEncodeTest = "https://www.freeformatter.com/java-regex-tester.html#ad-output".toByteArray()

        printEncodeAndDecode(Base64.encode(waitEncodeTest, Base64.CRLF))
        printEncodeAndDecode(Base64.encode(waitEncodeTest, Base64.NO_CLOSE))
        //没有=
        printEncodeAndDecode(Base64.encode(waitEncodeTest, Base64.NO_PADDING))
        //一行输出
        printEncodeAndDecode(Base64.encode(waitEncodeTest, Base64.NO_WRAP))
        printEncodeAndDecode(Base64.encode(waitEncodeTest, Base64.URL_SAFE))

        println("==结束测试==")
    }

    private fun printEncodeAndDecode(input:ByteArray){
        println(String(input.also {
            println(String(Base64.decode(it, Base64.DEFAULT)))
        }))
    }
}