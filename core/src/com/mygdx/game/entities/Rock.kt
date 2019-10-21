package com.mygdx.game.entities

import com.badlogic.gdx.math.MathUtils
import com.mygdx.game.base.BaseActor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions

class Rock(x: Float, y: Float, s: Stage, sc : Float = 1f) : BaseActor(x,y,s,sc) {

    init {
        loadTexture("rock.png")
        setBoundaryPolygon(8)

        val random = MathUtils.random(100F)

        addAction(Actions.forever(Actions.rotateBy(30f+random,1f)))
        setSpeed(50+random)
        maxSpeed = 50+random
        deceleration = 0F
        setMotionAngle(MathUtils.random(360F))
    }

    override fun act(dt : Float ) {
        super.act(dt)
        applyPhysics(dt)
        wrapAroundWorld()
    }
}