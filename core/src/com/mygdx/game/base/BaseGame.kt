package com.mygdx.game.base

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer


abstract class BaseGame : Game() {

    init {
        game = this
    }

    override fun create() {

        // prepare for multiple classes/stages to receive discrete input

        val im = InputMultiplexer()
        Gdx.input.inputProcessor = im
    }

    companion object {

        lateinit var game : BaseGame

        fun setActiveScreen(s: BaseScreen) {
            game.setScreen(s)
        }
    }
}
