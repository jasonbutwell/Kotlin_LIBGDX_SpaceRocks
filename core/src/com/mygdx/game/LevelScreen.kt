package com.mygdx.game

import com.badlogic.gdx.Input
import com.mygdx.game.base.BaseActor

import com.mygdx.game.base.BaseScreen
import com.mygdx.game.entities.Spaceship

class LevelScreen : BaseScreen() {

    private lateinit var spaceship : Spaceship

    override fun initialize() {
        val space = BaseActor(0f, 0f, mainStage)

        space.loadTexture("space.png")
        space.setSize(800f, 600f)
        BaseActor.setWorldBounds(space)

        spaceship = Spaceship(400f, 300f, mainStage)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.X)
            spaceship.warp()

        if ( keycode == Input.Keys.SPACE )
            spaceship.shoot()

        return false
    }

    override fun update(dt: Float) {
    }
}

