package me.glatteis.pentago

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import me.glatteis.pentago.connection.LocalConnector
import me.glatteis.pentago.gui.Board
import me.glatteis.pentago.gui.Textures
import me.glatteis.pentago.logic.GameLogic
import me.glatteis.pentago.logic.Player
import me.glatteis.pentago.menues.MainMenu
import java.util.*

/**
 * Created by Linus on 21.07.2016!
 */

class Pentago : Game() {

    override fun create() {
        PentagoCore.instance = this

        Textures.load()

        setScreen(MainMenu)
    }

    override fun render() {
        Gdx.gl.glClearColor(PentagoCore.backgroundColor.r, PentagoCore.backgroundColor.g, PentagoCore.backgroundColor.b,
                PentagoCore.backgroundColor.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
        screen.render(Gdx.graphics.deltaTime)
    }

}