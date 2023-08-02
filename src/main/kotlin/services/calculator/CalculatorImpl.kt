package services.calculator

class CalculatorImpl : Calculator {
    override fun add(a: Double, b: Double) = a + b
    override fun subtract(a: Double, b: Double) = a - b
    override fun times(a: Double, b: Double) = a * b
    override fun divide(a: Double, b: Double) = a / b

}