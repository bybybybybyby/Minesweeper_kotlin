package minesweeper

import java.util.*
import kotlin.random.Random

class Minefield() {

    val debugPrinter = false
    val input = Scanner(System.`in`)
    var numOfMines = 0
    var gameFinished = false
    var gameWon = false
    val minefieldSize = 9
    var minefield: Array<CharArray> = Array(minefieldSize) { CharArray(minefieldSize) {'.'} }
    var minesMarked: Array<BooleanArray> = Array(minefieldSize) { BooleanArray(minefieldSize) {false} }
    var totalMinesMarked = 0


    fun startGame() {

        println("How many mines do you want on the field?")
        numOfMines = input.nextInt()
        setMines()
        countSurroundingMines()
        printMineField(true)

        while(!gameFinished) {
            markMine()
            printMineField(true)
            if (checkForWinner()) {
                gameFinished = true
                gameWon = true
            }
        }

        if (gameWon) {
            print("Congratulations! You found all the mines!")
        }

    }

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
        for (i in minefield.indices) {
            for (j in minefield.indices) {
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
     * Player mark/unmark coordinate as a mine
     * Can't mark where there is a number
     */
    fun markMine() {
        var valid = false
        while(!valid) {
            println("Set/delete mines marks (x and y coordinates): ")
            val y = input.nextInt() - 1   // Adjust user view input which is starting at index 1
            val x = input.nextInt() - 1   // Adjust user view input which is starting at index 1

            if (x !in 0 until minefieldSize || y !in 0 until minefieldSize) {
                println("Coordinate outside of minefield!  Try again!")
                continue
            } else if (minefield[x][y] in '1'..'9') {
                println("There is a number here!")
                continue
            }

            if (!minesMarked[x][y]) {
                minesMarked[x][y] = true;
                totalMinesMarked++
            } else if (minesMarked[x][y]) {
                minesMarked[x][y] = false;
                totalMinesMarked--
            }
            valid = true

            if (debugPrinter) {
                for (i in 0 until minefieldSize) {
                    for (j in 0 until minefieldSize) {
                        if (minesMarked[i][j]) {
                            println("${i+1}:${j+1}")
                        }
                    }
                }
            }
        }
    }

    /**
     * Check if user wins.  Win occurs when all mines are exactly marked (no extra or missing marks)
     */
    fun checkForWinner(): Boolean {
        // User can only win if the marks equal the number of mines
        if (totalMinesMarked != numOfMines) {
            return false
        }

        // Check each 'X' mine on minefield matches minesMarked
        for (x in 0 until minefieldSize) {
            for (y in 0 until minefieldSize) {
                if (minefield[x][y] == 'X' && !minesMarked[x][y]) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * Print the minefield
     */
    private fun printMineField(hideMines: Boolean) {

        // Print header
        println(" |123456789| ")
        println("-|---------|")

        for (x in minefield.indices) {
            print("${x + 1}|")
            for (y in minefield.indices) {
                if (minesMarked[x][y]) {
                    print("*")
                } else if (hideMines && minefield[x][y] == 'X') {
                    print('.')
                } else {
                    print(minefield[x][y])
                }
            }
            println("|")
        }

        // Print footer
        println("-|---------|")
    }

}