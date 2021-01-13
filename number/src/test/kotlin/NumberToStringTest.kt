import org.junit.Test
import kotlin.test.assertEquals

class NumberToStringTest {


    @Test
    fun testSingleDigitConverter() {
        println(convert1Digit(2))
        println(convert1Digit(0,true))
    }

    @Test
    fun testThreeDigitConverter() {
        println(convert3digits(122))
        println(convert3digits(904))
    }

    @Test
    fun testTwoDigitConverter() {
        println(convert2Digits(2,false))
        println(convert2Digits(11,false))
        println(convert2Digits(20,false))
        println(convert2Digits(25,false))
        println(convert2Digits(29,false))
        println(convert2Digits(30,false))
    }

    @Test
    fun test() {
        assertEquals("FORTY SEVEN DOLLARS AND FIFTY CENTS", convertNumberPartToString(47.50f))
        assertEquals("FIVE DOLLARS AND ZERO CENTS", convertNumberPartToString(5f))
        assertEquals("TWO HUNDRED AND FIVE DOLLARS AND THIRTY ONE CENTS", convertNumberPartToString(205.31f))
        assertEquals("FOUR THOUSAND DOLLARS AND ZERO CENTS", convertNumberPartToString(4000.0f))
        assertEquals("ONE DOLLAR AND ONE CENT", convertNumberPartToString(1.01f))
        println(convertNumberPartToString(0.23f))
    }
}