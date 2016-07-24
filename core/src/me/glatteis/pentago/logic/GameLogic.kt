package me.glatteis.pentago.logic

import me.glatteis.pentago.PentagoCore
import me.glatteis.pentago.connection.Connection
import me.glatteis.pentago.connection.LocalConnector
import me.glatteis.pentago.gui.GUIChip

/**
 * Created by Linus on 21.07.2016!
 */

class GameLogic(val tileWidth: Int, val width: Int, val height: Int, val players: List<Player>, val pointsToWin: Int) {

    var turnPlayer = (Math.random() * players.size).toInt()
    var mode = Mode.PUT
    val connection = (PentagoCore.connector as LocalConnector).connection
    var playerHasWon = false

    val board = Array(width) {
        Array(height) {
            Subtile(tileWidth)
        }
    }

    init {
        connection.setTurnPlayer(players[turnPlayer])
    }

    fun handleInput(subtileX: Int, subtileY: Int, tileXRelative: Int, tileYRelative: Int) {
        if (mode == Mode.PUT && !playerHasWon) {
            if (board[subtileX][subtileY].board[tileXRelative][tileYRelative] != NoChip) return
            val newChip = Chip(players[turnPlayer])
            board[subtileX][subtileY].board[tileXRelative][tileYRelative] = newChip
            connection.addDisplayedGUIChip(subtileX, subtileY, tileXRelative, tileYRelative, GUIChip.fromChip(newChip))
            mode = Mode.ROTATE
            val playerThatWon = testIfWon()
            if (playerThatWon != null) {
                playerHasWon = true
                connection.displayGameWon(playerThatWon)
            }
            //printString()
        }
    }

    fun handleTurn(subtileX: Int, subtileY: Int, direction: RotateDirection) {
        if (mode == Mode.ROTATE && !playerHasWon) {
            board[subtileX][subtileY].rotate(direction)
            mode = Mode.PUT
            connection.rotateSubtile(subtileX, subtileY, direction)
            val playerThatWon = testIfWon()
            if (playerThatWon != null) {
                playerHasWon = true
                connection.displayGameWon(playerThatWon)
                return
            }
            newTurn()
        }
    }

    fun newTurn() {
        turnPlayer += 1
        turnPlayer %= players.size
        connection.setTurnPlayer(players[turnPlayer])
    }

    fun testIfWon(): Player? {
        val allRotatedSubtiles = Array(width, {
            Array(height, {
                Array(tileWidth, {
                    Array<Chip>(tileWidth, {
                        NoChip
                    })
                })
            })
        })
        for (a in 0..width - 1) for (b in 0..height - 1) {
            allRotatedSubtiles[a][b] = board[a][b].rotated()
        }
        for (r in 0..width - 1) for (h in 0..height - 1) {
            for (x in 0..tileWidth - 1) for (y in 0..tileWidth - 1) {
                val chip = allRotatedSubtiles[r][h][x][y]
                if (chip.player == null) continue
                val player = chip.player
                for (i in -1..1) {
                    for (j in -1..1) {
                        var count = 0
                        var locationX = r * tileWidth + x
                        var locationY = h * tileWidth + y
                        if (i == 0 && j == 0) continue
                        while (locationX / tileWidth < width && locationY / tileWidth < height &&
                                locationX >= 0 && locationY >= 0 &&
                                allRotatedSubtiles[locationX / tileWidth][locationY / tileWidth]
                                        [locationX % tileWidth][locationY % tileWidth].player == player) {
                            locationX += i
                            locationY += j
                            count++
                        }
                        if (count == pointsToWin) return player
                    }
                }
            }
        }
        return null
    }

    fun printString() {
        for (i in board.indices) {
            for (j in board[0].indices) {
                println("$i $j")
                val subtile = board[i][j]
                for (sub in subtile.rotated()) {
                    for (chip in sub) {
                        if (chip.player == null) print("*")
                        else print(chip.player.color.toString()[0])
                    }
                    println()
                }
            }
        }
    }

}