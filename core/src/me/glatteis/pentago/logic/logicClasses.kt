package me.glatteis.pentago.logic

import com.badlogic.gdx.graphics.Color
import me.glatteis.pentago.floorMod
import java.util.*

/**
 * Created by Linus on 21.07.2016!
 */

class Subtile(val width: Int) {
    var board = Array(width, {
        Array<Chip>(width, {
            NoChip
        })
    })

    var rotationInDegrees = 0

    fun rotate(direction: RotateDirection) {
        rotationInDegrees += if (direction == RotateDirection.CLOCKWISE) -90 else 90
        rotationInDegrees = floorMod(rotationInDegrees, 360)
    }

    val cos = mapOf(
            Pair(0, 1),
            Pair(90, 0),
            Pair(180, -1),
            Pair(270, 0)
    )

    val sin = mapOf(
            Pair(0, 0),
            Pair(90, 1),
            Pair(180, 0),
            Pair(270, -1)
    )

    fun getRotated(x: Int, y: Int): Chip {
        val xRotated = ((x - 1) * cos[rotationInDegrees]!! -
                (y - 1) * sin[rotationInDegrees]!!) + 1
        val yRotated = ((y - 1) * cos[rotationInDegrees]!! -
                (x - 1) * sin[rotationInDegrees]!!) + 1
        return board[xRotated][yRotated]
    }
}


enum class Mode {
    PUT, ROTATE
}

open class Chip(val player: Player?) {

}

object NoChip: Chip(null)

open class Player(val color: Color, val name: String) {
    //Empty constructor for packets
    constructor() : this(Color(), "")
}

enum class RotateDirection {
    CLOCKWISE, COUNTERCLOCKWISE
}