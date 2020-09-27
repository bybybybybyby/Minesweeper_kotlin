package minesweeper

import java.util.*
import kotlin.random.Random

class Minefield {

    val debugPrinter = true
    val input = Scanner(System.`in`)
    var numOfMines = 0
    var gameFinished = false
    var gameWon = false
    var gameLost = false
    val minefieldSize = 9
    var minefield: Array<CharArray> = Array(minefieldSize) { CharArray(minefieldSize) {'.'} }
    var minesMarked: Array<BooleanArray> = Array(minefieldSize) { BooleanArray(minefieldSize) {false} }
    var uncoveredCells = Array(minefieldSize) { BooleanArray(minefieldSize) {false} }
    var totalMinesMarked = 0


    fun startGame() {

        println("How many mines do you want on the field?")
        numOfMines = input.nextInt()
        setMines()
        countSurroundingMines()
        printMineField()

        while(!gameFinished) {
//            markMine()
            var validInput = false
            while (!validInput) {
                println("Set/unset mine mark or claim a cell as free (eg: 3 2 free, 1 2 mine): ")
                val y = input.nextInt() - 1   // Adjust user view input which is starting at index 1
                val x = input.nextInt() - 1   // Adjust user view input which is starting at index 1
                val action = input.next()

                if (action == "mine") {
                    markMine(x, y)
                    validInput = true
                } else if (action == "free") {
                    markFree(x, y)
                    validInput = true
                } else {
                    println("Invalid input!")
                    continue
                }
            }

            printMineField()
            checkForWinner()

            if (gameWon) {
                println("Congratulations! You found all the mines!")
                gameFinished = true
            } else if (gameLost) {
                println("You suck")
                gameFinished = true
            }
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
    fun markMine(x: Int, y: Int) {
        var valid = false
        while(!valid) {
            if (x !in 0 until minefieldSize || y !in 0 until minefieldSize) {
                println("Coordinate outside of minefield!  Try again!")
                return
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
     * Player choose coordinate as free to uncover
     */
    fun markFree(x: Int, y: Int) {
        // If Player chooses mine, the game ends as loss
        if (minefield[x][y] == 'X') {
            println("You stepped on a mine and failed!")
            //TODO: Uncover ALL mines to show end locations
            gameLost = true
            gameFinished = true
            return
        }

        // If Player chooses a number hint, it will reveal just that cell
        if (minefield[x][y] in '1'..'8') {
            uncoveredCells[x][y] = true
        }
        // If Player chooses a cell that has no mines around it, it should automatically explore all cells around it
        // until no more can be explored automatically
        else if (minefield[x][y] == '.') {
            uncoveredCells[x][y] = true

            for (i in Math.max(x - 1, 0)..Math.min(x + 1, minefieldSize - 1)) {
                for (j in Math.max(y - 1, 0)..Math.min(y + 1, minefieldSize - 1)) {
                    if (!uncoveredCells[i][j] && i in 0..minefieldSize - 1 && j in 0..minefieldSize - 1) {
                        markFree(i, j)
                    }
                }
            }
        }
    }

    /**
     * Check if user wins.  Win occurs when all mines are exactly marked (no extra or missing marks)
     */
    fun checkForWinner() {
        // User can only win if the marks equal the number of mines
        if (totalMinesMarked != numOfMines) {
            return
        }

        // Check each 'X' mine on minefield matches minesMarked
        for (x in 0 until minefieldSize) {
            for (y in 0 until minefieldSize) {
                if (minefield[x][y] == 'X' && !minesMarked[x][y]) {
                    return
                }
            }
        }
        gameWon = true
    }

    /**
     * Print the minefield
     */
    private fun printMineField() {

        // Print header
        println(" |123456789| ")
        println("-|---------|")

        for (x in minefield.indices) {
            print("${x + 1}|")  // row numbering
            for (y in minefield.indices) {
                if (minesMarked[x][y]) {
                    print("*")
                } else if (uncoveredCells[x][y]) {
                    if (minefield[x][y] == '.') {
                        print('/')
                    } else {
                        print(minefield[x][y])
                    }
                } else {
                    print('.')
                }
            }
            println("|")
        }

        // Print footer
        println("-|---------|")
    }

}