package services.calculator

interface Calculator {
    fun add(a: Double, b: Double): Double
    fun subtract(a: Double, b: Double): Double
    fun times(a: Double, b: Double): Double
    fun divide(a: Double, b: Double): Double

}