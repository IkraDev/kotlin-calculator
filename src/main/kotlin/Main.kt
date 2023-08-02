import services.calculator.CalculatorImpl

val calc = CalculatorImpl()

fun main() {
    println("enter a math operation without spaces or brackets:\n supported operations = [   +   -   *   /   ]")
    val str = readlnOrNull()?: return
    println(calcMathString(str))
}

fun operation(op: String, a: Double, b: Double): Double =
    when (op) {
        "+" -> calc.add(a, b)
        "-" -> calc.subtract(a, b)
        "*" -> calc.times(a, b)
        "/" -> calc.divide(a, b)

        else -> throw Exception()
    }

fun calcMathString(str: String): Double {
    var curr = 0.0
    val list = recursivePunktRechnung(stringToList(str), 0)

    for (x in list) curr = operation(x.first, curr, x.second)
    return curr
}

fun recursivePunktRechnung(list: MutableList<Pair<String, Double>>, index: Int): MutableList<Pair<String, Double>> {
    if (index > list.size -1) return list

    if (list[index+1].first in "*/") {
        val number = operation(
            list[index+1].first,
            if (list[index].first == "-") list[index].second * -1 else list[index].second,
            list[index+1].second
        )
        val sign = "+"
        val pair = Pair(sign, number)
        list.removeAt(index + 1)
        list[index] = pair
        recursivePunktRechnung(list, index + 1)
    }

    return list
}

fun stringToList(strInput: String): MutableList<Pair<String, Double>> {
    val result = mutableListOf<Pair<String, Double>>()
    var sign = "+"
    val str = if (strInput[0].isDigit()) strInput else "0".plus(strInput) // so calculations can start with a negative
    val numbers = str.split("+", "-", "*", "/")
    val signs = str.filter { it == '+' || it == '-' || it == '*' || it == '/'}
    for ((index, number) in numbers.withIndex()) {
        result.add(Pair(sign, number.toDouble()))
        if (index < signs.length) {
            sign = signs[index].toString()
        }
    }
    return result
}