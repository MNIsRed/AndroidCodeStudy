/**
 * 测试String，StringBuffer，StringBuilder
 */
class StringClassTest {
    fun testString(s0:String) {
        val s1 = "hello" + "world"
        val s2 = StringBuilder("hello").append("world")
        val s3 = "hello"
        val s4 = "world"
        //尽量使用StringBuilder进行拼接，比如下面代码会变成
        //(new StringBuilder()).append(s3).append(s4).toString();
        //如果是循环拼接，这里每次循环就都会创建新的StringBuilder和String对象了
        val s5 = s3 + s4
        //需要频繁拼接的字符串，用StringBuilder和StringBuffer比较好
        val s6 = java.lang.StringBuilder(s3 + s4)
        s6.append(s5)
    }
}