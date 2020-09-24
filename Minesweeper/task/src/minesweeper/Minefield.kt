package minesweeper

import kotlin.math.min
import kotlin.random.Random

class Minefield(val numOfMines: Int) {

//    val numOfMines: Int = numOfMines
    val minefieldSize = 9
    var minefield: Array<CharArray> = Array(minefieldSize) { CharArray(minefieldSize) {'.'} }

    /**
     * Set mines in random locations
     */
    fun setMines() {
        var minesSet = 0
        while (minesSet < numOfMines) {
            val height = Random.nextInt(minefield.size)
            val width = Random.nextInt(minefield.size)

            if (minefield[height][width] == '.') {
                minefield[height][width] = 'X'
                minesSet++
            }
        }
    }

    /**
     * Check around every cell and determine hint numbers corresponding to how many mines are surrounding it
     */
    fun countSurroundingMines() {
        for (i in 0..minefield.size - 1) {
            for (j in 0..minefield.size - 1) {
                var count: Char = '0'

                if (minefield[i][j] == '.') {
                    // Check above
                    if (i > 0 && minefield[i - 1][j] == 'X') {
                        count++
                    }
                    // Check above-right
                    if (i > 0 && j < minefield.size - 1 && minefield[i - 1][j + 1] == 'X') {
                        count++
                    }
                    // Check right
                    if (j < minefield.size - 1 && minefield[i][j + 1] == 'X') {
                        count++
                    }
                    // Check below-right
                    if (i < minefield.size - 1 && j < minefield.size - 1 && minefield[i + 1][j + 1] == 'X') {
                        count++
                    }
                    // Check below
                    if (i < minefield.size - 1 && minefield[i + 1][j] == 'X') {
                        count++
                    }
                    // Check below-left
                    if (i < minefield.size - 1 && j > 0 && minefield[i + 1][j - 1] == 'X') {
                        count++
                    }
                    // Check left
                    if (j > 0 && minefield[i][j - 1] == 'X') {
                        count++
                    }
                    // Check above-left
                    if (i > 0 && j > 0 && minefield[i - 1][j - 1] == 'X') {
                        count++
                    }
                }


                if (count > '0') {
                    minefield[i][j] = count
                }
            }

        }
    }

    /**
     * Print the minefield
     */
    fun print() {
        for (i in minefield) {
            println(i)
        }
    }

}