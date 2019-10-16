package com.mygdx.game.entities

import com.badlogic.gdx.scenes.scene2d.Stage
import com.mygdx.game.base.BaseActor

class Thrusters(x: Float, y: Float, stage: Stage) : BaseActor(x, y, stage) {

    init {
        loadTexture("fire.png")
    }
}