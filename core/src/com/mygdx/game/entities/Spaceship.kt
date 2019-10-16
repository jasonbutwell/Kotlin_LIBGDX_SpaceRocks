package com.mygdx.game.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.mygdx.game.base.BaseActor
import com.badlogic.gdx.math.MathUtils

class Spaceship(x: Float, y: Float, stage: Stage) : BaseActor(x, y, stage) {

    private val degreesPerSecond = 120F
    private val thrusters : Thrusters

    private var shield: Shield
    var shieldPower: Int = 0

    init {
        loadTexture("spaceship.png")
        setBoundaryPolygon(8)

        acceleration = 200F
        maxSpeed = 150F
        deceleration = 10F

        thrusters = Thrusters(0F, 0F, stage)
        thrusters.setPosition(-thrusters.width,height/2 - thrusters.height/2)
        thrusters.setOpacity(0F)
        addActor(thrusters)

        shield = Shield(0f, 0f, stage)
        addActor(shield)
        shield.centerAtPosition(width / 2, height / 2)
        shieldPower = 100
    }

    fun warp() {
        if (stage == null)
            return

        val warp1 = Warp(0f, 0f, this.stage)
        warp1.centerAtActor(this)

        setPosition(MathUtils.random(800).toFloat(), MathUtils.random(600).toFloat())

        val warp2 = Warp(0f, 0f, this.stage)
        warp2.centerAtActor(this)
    }

    override fun act(dt: Float) {

        super.act(dt)

        handleInput(degreesPerSecond, dt)

        shield.setOpacity(shieldPower / 100f);
        if (shieldPower <= 0)
            shield.setVisible(false);

        applyPhysics(dt)    // Apply the velocity
        wrapAroundWorld()   // Stop ship going outside of world
    }

    private fun handleInput(degreesPerSecond: Float, dt: Float) {

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            rotateBy(degreesPerSecond * dt)

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            rotateBy(-degreesPerSecond * dt)

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            accelerateAtAngle(rotation)
            thrusters.addAction(Actions.fadeIn(.5f))
        }
        else
            thrusters.addAction(Actions.fadeOut(.5f))
    }

    private fun wrapAroundWorld() {

        if (x + width < 0)
            x = worldBounds.width   // Object goes outside left side

        if( x > worldBounds.width)  // Object goes outside right side
            x = -width

        if (y + height < 0)
            y = worldBounds.height  // Object goes below bottom

        if (y > worldBounds.height) // Object goes above top
            y = -height
    }
}