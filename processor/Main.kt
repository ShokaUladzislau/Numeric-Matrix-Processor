cit package processor

import kotlin.math.pow
import kotlin.system.exitProcess

class Matrix(var size: Pair<Int, Int>) {
    var matrixNumbers = Array(size.first) { Array(size.second) { 0.0 } }
}

fun main() {
    while (true) {
        println(
            "\n" +
                    "1. Add matrices\n" +
                    "2. Multiply matrix by a constant\n" +
                    "3. Multiply matrices\n" +
                    "4. Transpose matrix\n" +
                    "5. Calculate a determinant\n" +
                    "6. Inverse matrix\n" +
                    "0. Exit"
        )
        print("Your choice: ")
        when (readLine()) {
            "1" -> addMatrices()
            "2" -> {
                val matrix = inputMatrix()
                print("Enter constant: ")
                multiplicationMatrixByConstant(matrix, readLine()!!.toDouble())
            }
            "3" -> multiplicationMatrixByMatrix()
            "4" -> transposeMatrix()
            "5" -> {
                val determinant = calculateDeterminant(inputMatrix().matrixNumbers)
                if (determinant % 1 == 0.0) print(determinant.toInt()) else print(determinant)
            }
            "6" -> inverseMatrix()
            "0" -> exitProcess(0)
        }
    }
}

fun inputMatrix(): Matrix {
    print("Enter size of matrix: ")
    val matrix = Matrix(inputMatrixSize())
    println("Enter matrix:")
    fillMatrix(matrix)
    return matrix
}

fun inputMatrices(): Pair<Matrix, Matrix> {
    print("Enter size of first matrix: ")
    val firstMatrix = Matrix(inputMatrixSize())
    fillMatrix(firstMatrix)
    print("Enter size of second matrix: ")
    val secondMatrix = Matrix(inputMatrixSize())
    fillMatrix(secondMatrix)
    return Pair(firstMatrix, secondMatrix)
}

fun inputMatrixSize(): Pair<Int, Int> {
    val input = readLine()!!
    return Pair(input.split(" ").first().toInt(), input.split(" ").last().toInt())
}

fun fillMatrix(matrix: Matrix) {
    for (i in 0 until matrix.size.first) {
        matrix.matrixNumbers[i] = readLine()!!.split(" ").map { it.toDouble() }.toTypedArray()
    }
}

fun printMatrix(matrix: Matrix) {
    println("The result is:")
    for (i in 0 until matrix.size.first) {
        for (j in 0 until matrix.size.second) {
            val element = matrix.matrixNumbers[i][j]
            if (element % 1 == 0.0) print("${element.toInt()} ") else print("$element ")
        }
        println()
    }
}

fun addMatrices() {
    val (firstMatrix, secondMatrix) = inputMatrices()
    if (firstMatrix.size == secondMatrix.size) {
        val resultMatrix = Matrix(firstMatrix.size)
        for (i in 0 until resultMatrix.size.first) {
            for (j in 0 until resultMatrix.size.second) {
                resultMatrix.matrixNumbers[i][j] = firstMatrix.matrixNumbers[i][j] + secondMatrix.matrixNumbers[i][j]
            }
        }
        printMatrix(resultMatrix)
    } else {
        println("The operation cannot be performed.")
    }
}

fun multiplicationMatrixByConstant(matrix: Matrix, multiplier: Double) {
    val resultMatrix = Matrix(matrix.size)
    for (i in 0 until matrix.size.first) {
        for (j in 0 until matrix.size.second) {
            resultMatrix.matrixNumbers[i][j] = matrix.matrixNumbers[i][j] * multiplier
        }
    }
    printMatrix(resultMatrix)
}

fun multiplicationMatrixByMatrix() {
    val (firstMatrix, secondMatrix) = inputMatrices()
    val resultMatrix = Matrix(Pair(firstMatrix.size.first, secondMatrix.size.second))
    for (i in resultMatrix.matrixNumbers.indices) {
        for (j in resultMatrix.matrixNumbers[i].indices) {
            for (k in secondMatrix.matrixNumbers.indices) {
                resultMatrix.matrixNumbers[i][j] += (firstMatrix.matrixNumbers[i][k] * secondMatrix.matrixNumbers[k][j])
            }
        }
    }
    printMatrix(resultMatrix)
}

fun transposeMatrix() {
    println(
        "\n" +
                "1. Main diagonal\n" +
                "2. Side diagonal\n" +
                "3. Vertical line\n" +
                "4. Horizontal line\n"
    )
    print("Your choice: ")
    val option = readLine()
    val matrix = inputMatrix()
    val resultMatrix = Matrix(Pair(matrix.size.second, matrix.size.first))
    when (option) {
        "1" -> printMatrix(transposeByMainDiagonal(matrix, resultMatrix))
        "2" -> printMatrix(transposeBySecondDiagonal(matrix, resultMatrix))
        "3" -> printMatrix(transposeByVerticalLine(matrix, resultMatrix))
        "4" -> printMatrix(transposeByHorizontalLine(matrix, resultMatrix))
    }
}

fun transposeByMainDiagonal(matrix: Matrix, resultMatrix: Matrix): Matrix {
    for (i in 0 until matrix.size.second) {
        for (j in 0 until matrix.size.first) {
            resultMatrix.matrixNumbers[i][j] = matrix.matrixNumbers[j][i]
        }
    }
    return resultMatrix
}

fun transposeBySecondDiagonal(matrix: Matrix, resultMatrix: Matrix): Matrix {
    for (i in 0 until matrix.size.second) {
        for (j in 0 until matrix.size.first) {
            resultMatrix.matrixNumbers[i][j] =
                matrix.matrixNumbers[matrix.size.first - 1 - j][matrix.size.second - 1 - i]
        }
    }
    return resultMatrix
}

fun transposeByVerticalLine(matrix: Matrix, resultMatrix: Matrix): Matrix {
    for (i in 0 until matrix.size.second) {
        for (j in 0 until matrix.size.first) {
            resultMatrix.matrixNumbers[i][j] = matrix.matrixNumbers[i][matrix.size.second - 1 - j]
        }
    }
    return resultMatrix
}

fun transposeByHorizontalLine(matrix: Matrix, resultMatrix: Matrix): Matrix {
    for (i in 0 until matrix.size.second) {
        for (j in 0 until matrix.size.first) {
            resultMatrix.matrixNumbers[i][j] = matrix.matrixNumbers[matrix.size.first - 1 - i][j]
        }
    }
    return resultMatrix
}

fun calculateDeterminant(matrix: Array<Array<Double>>):Double {
    var d = 0.0
    if (matrix.size == 2) return (matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0])
    if (matrix.size != 2) {
        for (j in matrix[0].indices) {
            d += (-1.0).pow(2.0 + j).toInt() * matrix[0][j] * calculateDeterminant(matrix.filterIndexed { index, _ ->  index != 0 }.map { it.filterIndexed { index, _ ->  index != j }.toTypedArray() }.toTypedArray())
        }
    }
    return d
}

fun inverseMatrix() {
    val matrix = inputMatrix()
    val resultMatrix = Matrix(Pair(matrix.size.second, matrix.size.first))
    val d = calculateDeterminant(matrix.matrixNumbers)
    var coMatrix = Matrix(Pair(matrix.size.second, matrix.size.first))
    for (i in matrix.matrixNumbers.indices) {
        for (j in matrix.matrixNumbers[0].indices) {
            coMatrix.matrixNumbers[i][j] = (-1.0).pow(2.0 + j + i) * calculateDeterminant(matrix.matrixNumbers.filterIndexed { index, _ ->  index != i }.map { it.filterIndexed { index, _ ->  index != j }.toTypedArray() }.toTypedArray())
        }
    }
    coMatrix = transposeByMainDiagonal(coMatrix, resultMatrix)
    multiplicationMatrixByConstant(coMatrix, 1 / d)
}