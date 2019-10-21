package com.mygdx.game.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.mygdx.game.base.BaseActor
import com.badlogic.gdx.math.MathUtils
import com.mygdx.game.config.Settings
import java.lang.System.currentTimeMillis

class Spaceship(x: Float, y: Float, stage: Stage) : BaseActor(x, y, stage) {

    private val degreesPerSecond = 120F
    private val thrusters : Thrusters

    private var shield: Shield
    var shieldPower: Int = 0

    private val maxLasers = 5

    var invulnerable = false
    private var invulnerableTime : Long = 3 * 1000
    private var invulnerableTimer : Long = 0

    private var shieldGone = false
    private var thrustOn = false

    init {
        loadTexture("spaceship.png")
        setBoundaryPolygon(8)

        acceleration = 300F
        maxSpeed = 250F
        deceleration = 10F

        thrusters = Thrusters(stage = stage)
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

        setPosition(MathUtils.random(Settings.screenWidth).toFloat(), MathUtils.random(Settings.screenHeight).toFloat())

        val warp2 = Warp(0f, 0f, this.stage)
        warp2.centerAtActor(this)
    }

    fun setInvulnerable() {
        invulnerableTimer = currentTimeMillis() + invulnerableTime
        invulnerable = true
        flashPhase()
    }

    fun setVulnerable() {
        invulnerable = false
        this.actions.clear()
        this.setOpacity(1f)
    }

    fun checkInvulnerable() {
        if (invulnerable)
            if (invulnerableTimer < currentTimeMillis() )
                setVulnerable()
    }

    fun shoot() {
        if (stage == null) return

        val i = 0
        //for (i in 0 .. 360 step 5)
            if ( getList(stage,"Laser").count() < maxLasers ) {
                val laser = Laser(0F, 0F, stage)
                laser.centerAtActor(this)
                laser.rotation = rotation +i
                laser.setMotionAngle(rotation +i)
            }
    }

    fun flashWarning() {
        val shipColor : Color = color
        val pulse = Actions.sequence(
                Actions.color(Color.RED),
                Actions.delay(0.125f),
                Actions.color(shipColor),
                Actions.delay(0.125f)
        )
        addAction(Actions.forever(pulse))
        shieldGone = true
    }

    fun flashPhase() {
        val pulse = Actions.sequence(
                Actions.alpha(0f,.1f),
                Actions.alpha(1f,.1f)
        )
        addAction(Actions.forever(pulse))
    }

    fun setThrusters() {

        if (!thrustOn) {
            thrustOn = !thrustOn
            thrusters.addAction(Actions.fadeIn(.5f))
        }
        else {
            thrustOn = !thrustOn
            thrusters.addAction(Actions.fadeOut(.5f))
        }
    }

    override fun act(dt: Float) {
        super.act(dt)

        handleInput(degreesPerSecond, dt)

        shield.setOpacity(shieldPower / 100f)

        if (shieldPower <= 0 && shield.isVisible)
            shield.isVisible = false
        else
            if (!shieldGone && this.actions.isEmpty && !shield.isVisible)
                flashWarning()
            else
                checkInvulnerable()

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

            if ( !thrustOn )
                setThrusters()
        }
        else
            if ( thrustOn )
                setThrusters()
    }
}