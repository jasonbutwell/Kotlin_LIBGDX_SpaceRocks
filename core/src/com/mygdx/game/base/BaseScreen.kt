package com.mygdx.game.base

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage

abstract class BaseScreen : Screen, InputProcessor {

    var mainStage: Stage = Stage()
    private var uiStage: Stage = Stage()

    init {
        initialize()
    }

    abstract fun initialize()

    abstract fun update(dt: Float)

    override fun keyDown(keycode: Int) = false

    override fun keyUp(keycode: Int) = false

    override fun keyTyped(c: Char) = false

    override fun mouseMoved(screenX: Int, screenY: Int) = false

    override fun scrolled(amount: Int) = false

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int) = false

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int) = false

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int) = false

    override fun render(dt: Float) {

        uiStage.act(dt)
        mainStage.act(dt)

        update(dt)

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        mainStage.draw()
        uiStage.draw()
    }

    // Methods required by Screen interface

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun dispose() {}

    override fun show() {

        // Add Input processor to uiStage and mainStage

        val im : InputMultiplexer = Gdx.input.inputProcessor as InputMultiplexer
        im.addProcessor(this)
        im.addProcessor(uiStage)
        im.addProcessor(mainStage)
    }

    override fun hide() {

        // Remove Input processor from uiStage and mainStage

        val im : InputMultiplexer = Gdx.input.inputProcessor as InputMultiplexer
        im.removeProcessor(this)
        im.removeProcessor(uiStage)
        im.removeProcessor(mainStage)
    }
}