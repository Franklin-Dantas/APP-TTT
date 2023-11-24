package com.example.APP-TTT

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.APP-TTT.ui.theme.APP-TTTTheme

fun MainActivity = ComponentActivity {
    var currentPlayer by remember { mutableStateOf(Player.X) }
    var board by remember { mutableStateOf(Board()) }
    var winner by remember { mutableStateOf<Player?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Jogador atual: ${currentPlayer.name}")
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.size(300.dp)) {
            for (i in 0 until 3) {
                Row {
                    for (j in 0 until 3) {
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                if (winner == null && board[i, j] == null) {
                                    board = board.withMove(i, j, currentPlayer)
                                    winner = board.winner()
                                    currentPlayer = currentPlayer.other()
                                }
                            }
                        ) {
                            Text(text = board[i, j]?.name ?: "")
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            currentPlayer = Player.X
            board = Board()
            winner = null
        }) {
            Text(text = "Reiniciar")
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (winner != null) {
            Text(text = "O jogador ${winner!!.name} ganhou!")
        } else if (board.isFull()) {
            Text(text = "Empate!")
        }
    }
}

enum class Player(val other: Player) {
    X(O), O(X);
}

class Board(private val cells: List<Player?> = List(9) { null }) {
    operator fun get(i: Int, j: Int) = cells[i * 3 + j]
    fun withMove(i: Int, j: Int, player: Player) =
        Board(cells.toMutableList().also { it[i * 3 + j] = player })

    fun isFull() = cells.all { it != null }
    fun winner(): Player? {
        val lines = listOf(
            listOf(0, 1, 2),
            listOf(3, 4, 5),
            listOf(6, 7, 8),
            listOf(0, 3, 6),
            listOf(1, 4, 7),
            listOf(2, 5, 8),
            listOf(0, 4, 8),
            listOf(2, 4, 6)
        )

        return lines.map { line -> line.map { cells[it] } }.find { line ->
            line.all { it == Player.X }
        }?.first() ?: lines.map { line -> line.map { cells[it] } }.find { line ->
            line.all { it == Player.O }
        }?.first()
    }
}
