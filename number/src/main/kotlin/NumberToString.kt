import kotlin.text.StringBuilder

fun convertNumberPartToString(amount: Float): String {
    val number = "%.2f".format(amount).replace(",", ".")
    val (dollar, cents) = number.split(".")

    val sb = StringBuilder()
    if (dollar.toInt() > 999) sb.append(convert3digits(dollar.substring(0, dollar.length - 3).toInt())).append(" THOUSAND")
    if (dollar.toInt() > 1) sb.append(convert3digits(dollar.takeLast(3).toInt())).append(" DOLLARS").append(" AND ")
    if (dollar.toInt() == 1) sb.append("ONE DOLLAR").append(" AND ")
    if (cents.toInt() != 1) sb.append(convert2Digits(cents.toInt(), true)).append(" CENTS")
    if (cents.toInt() == 1) sb.append(convert2Digits(cents.toInt(), true)).append(" CENT")
    return sb.toString()
}

fun convert3digits(digits: Int): String {
    val sb = StringBuilder()
    if (digits > 99) sb.append(convert1Digit(digits.toString().substring(0, 1).toInt()) + " HUNDRED")
    if (digits > 99 && digits.rem(100) != 0) sb.append(" AND ")
    sb.append(convert2Digits(digits.toString().takeLast(2).toInt()))
    return sb.toString()
}

fun convert2Digits(digits: Int, showZero: Boolean = false): String {
    val teens = listOf("TEN", "ELEVEN", "TWELVE", "THIRTEEN", "FOURTEEN", "FIFTEEN", "SIXTEEN", "SEVENTEEN", "EIGHTEEN", "NINETEEN")
    return when (digits) {
        in 0..9 -> convert1Digit(digits, showZero)
        in 10..19 -> teens[digits - 10]
        in 20..29 -> "TWENTY ${convert1Digit(digits - 20)}"
        in 30..39 -> "THIRTY ${convert1Digit(digits - 30)}"
        in 40..49 -> "FORTY ${convert1Digit(digits - 40)}"
        in 50..59 -> "FIFTY ${convert1Digit(digits - 50)}"
        in 60..69 -> "SIXTY ${convert1Digit(digits - 60)}"
        in 70..79 -> "SEVENTY ${convert1Digit(digits - 70)}"
        in 80..89 -> "EIGHTY ${convert1Digit(digits - 80)}"
        in 90..99 -> "NINETY ${convert1Digit(digits - 90)}"
        else -> return "NA"
    }.trim()
}

fun convert1Digit(digit: Int, showZero: Boolean = false): String = if(!showZero && digit == 0) "" else listOf("ZERO", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE")[digit]