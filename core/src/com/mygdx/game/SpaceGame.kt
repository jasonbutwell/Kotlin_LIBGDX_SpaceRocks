package com.mygdx.game

import com.mygdx.game.base.BaseGame

class SpaceGame : BaseGame() {

    override fun create() {
        super.create()
        setActiveScreen(LevelScreen())
    }
}