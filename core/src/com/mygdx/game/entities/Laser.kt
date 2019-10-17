package com.mygdx.game.entities

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.mygdx.game.base.BaseActor

class Laser(x: Float, y: Float, s: Stage) : BaseActor(x, y, s)  {

    init {
        loadTexture("laser.png")
        addAction(Actions.delay(1f))
        addAction(Actions.after(Actions.fadeOut(0.5f)))
        addAction(Actions.after(Actions.removeActor()))

        setSpeed(400F)
        maxSpeed = 400F
        deceleration = 0F
    }

    override fun act(dt: Float ) {
        super.act(dt)
        applyPhysics(dt)
        wrapAroundWorld()
    }
}