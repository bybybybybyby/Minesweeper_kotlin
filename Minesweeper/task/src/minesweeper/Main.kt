package minesweeper
import java.util.*
import kotlin.math.min
import kotlin.random.Random

fun main() {
    val scanner = Scanner(System.`in`)

    println("How many mines do you want on the field?")
    val numOfMines = scanner.nextInt()

    val minefield = Minefield(numOfMines)
    minefield.setMines()
    minefield.countSurroundingMines()
    minefield.print()

}
