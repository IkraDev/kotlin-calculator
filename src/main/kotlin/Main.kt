import services.calculator.CalculatorImpl

val calc = CalculatorImpl()

fun main() {
    println("enter a math operation without spaces or brackets:\n supported operations = [   +   -   *   /   ]")
    val str = readlnOrNull()?: return
    //println(resolveBrackets(str))
    println(calcMathString(recursiveResolveBrackets(str).second))
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
    //println(list)

    for (x in list) curr = operation(x.first, curr, x.second)
    return curr
}


fun recursivePunktRechnung(list: MutableList<Pair<String, Double>>, index: Int = 0): MutableList<Pair<String, Double>> {
    if (index > list.size - 2) return list
    if (list[index + 1].first in "*/") {
        val (sign, number) = list[index + 1]
        val result = operation(sign, if (list[index].first == "-") list[index].second * -1 else list[index].second, number)
        list[index] = "+" to result
        list.removeAt(index + 1)
        if (index +1 == list.size-1 && list[list.size-1].first in "*/") { //edgecase of last num needing correction
            val (signLast, numberLast) = list[index + 1]
            val resultLast = operation(signLast, if (list[index].first == "-") list[index].second * -1 else list[index].second, numberLast)
            list[index] = "+" to resultLast
            list.removeAt(index + 1)
        } else {
            recursivePunktRechnung(list, index + 1)
        }
    } else {
        recursivePunktRechnung(list, index + 1)
    }
    return list
}

fun recursiveResolveBrackets(str: String, currStr: String = "", currIndex: Int = 0, currLevel: Int = 0): Pair<Int, String> {
    if ("(" !in str) return Pair(0, str)
    var result = currStr
    val inputString = str.substring(currIndex)
    var currBracket = ""
    var ignoreCount = 0
    var ignoreTotal = 0
    for ((index, x) in inputString.withIndex()) {
        if (ignoreCount > 0) {
            ignoreCount--
            continue
        }
        when (x) {
            '(' -> {
                val (count, resolved) = recursiveResolveBrackets(inputString, result, index + 1, currLevel + 1)
                ignoreCount = count + currLevel
                ignoreTotal = count
                if (currLevel == 0) {
                    result += resolved
                } else {
                    currBracket += resolved
                }
            }
            ')' -> {
                if (currLevel > 0) {
                    val implant = currBracket
                    //println("$currLevel : Implant: $implant")
                    return Pair(currBracket.length + ignoreTotal, calcMathString(implant).toString())
                }
            }
            else -> if (currLevel == 0) result += x else currBracket += x
        }
        //println("$currLevel : Result: $result")
    }
    return Pair(0, result)
}






fun stringToList(strInput: String): MutableList<Pair<String, Double>> {
    val result = mutableListOf<Pair<String, Double>>()
    var sign = "+"
    val str = if (strInput[0].isDigit()) strInput else "0".plus(strInput) // so calculations can start with a negative
    val numbers = str.split("+", "-", "*", "/")
    val signs = str.filter { it == '+' || it == '-' || it == '*' || it == '/'}
    var negativeToGive = ""
    for ((index, number) in numbers.withIndex()) {
        if (number == "") {
            negativeToGive = "-"
            continue
        }
        result.add(Pair(sign, (negativeToGive + number).toDouble()))
        if (index < signs.length) {
            sign = signs[index].toString()
        }
        negativeToGive = ""
    }
    return result
}