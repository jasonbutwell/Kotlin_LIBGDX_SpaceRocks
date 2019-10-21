package com.mygdx.game

import com.badlogic.gdx.Input
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Action
import com.mygdx.game.base.BaseActor

import com.mygdx.game.base.BaseScreen
import com.mygdx.game.entities.Explosion
import com.mygdx.game.entities.Rock
import com.mygdx.game.entities.Spaceship
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.mygdx.game.base.BaseActor.Companion.count
import com.mygdx.game.base.BaseActor.Companion.setWorldBounds
import com.mygdx.game.config.Settings

class LevelScreen : BaseScreen() {

    private lateinit var spaceship : Spaceship

    private var gameOver = false

    private val rockMinScale = .5f
    private val maxSplit = 2
    private val splitScale = 1.5f

    override fun initialize() {

        val baseScale = 5f
        val maxRocks = 6

        val space = BaseActor(0f, 0f, mainStage)

        with (space) {
            loadTexture("space.png")
            setSize(Settings.screenWidth.toFloat(), Settings.screenHeight.toFloat())
        }

        setWorldBounds(space)

        val xPos = Settings.screenWidth / 2.toFloat()
        val yPos = Settings.screenHeight / 2.toFloat()

        spaceship = Spaceship(xPos,yPos, mainStage)

        for (i in 0 until maxRocks)
            getRock(baseScale)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.X)
            spaceship.warp()

        if ( keycode == Input.Keys.SPACE )
            spaceship.shoot()

        return false
    }

    private fun getRock(baseScale : Float = 1f) {
        val x = MathUtils.random(Settings.screenWidth).toFloat()
        val y = MathUtils.random(Settings.screenHeight).toFloat()
        Rock(x,y, mainStage, baseScale)
    }

    private fun splitRock(x : Float, y : Float, scale : Float, splits : Int) {
        for (i in 1..splits)
            Rock(x, y, mainStage, scale)
    }

    override fun update(dt: Float) {
        if ( !gameOver )
            for (rock in BaseActor.getList(mainStage, "Rock")) {
                if (!spaceship.invulnerable && rock.overlaps((spaceship)))

                    // If shield gone

                    if (spaceship.shieldPower <= 0) {

                        val boomStarship = Explosion(0F, 0F, mainStage)
                        boomStarship.centerAtActor(spaceship)

                        spaceship.remove()

                        splitAndReplaceOldRock(rock)

                        gameOver()

                    } else {

                        // If shield not completely gone

                        spaceship.setInvulnerable()
                        spaceship.shieldPower -= (100/3)+1

                        splitAndReplaceOldRock(rock)
                    }

                // When Laser hits Rock

                for (laserActor in BaseActor.getList(mainStage, "Laser")) {
                    if (laserActor.overlaps(rock)) {
                        laserActor.remove()

                        splitAndReplaceOldRock(rock)
                    }
                }
            }

        if (!gameOver && count(mainStage, "Rock") == 0)
            youWin()
    }

    private fun splitAndReplaceOldRock(rock : BaseActor) {
        val boom = Explosion(0F,0F, mainStage, rock.scaleX)
        boom.centerAtActor(rock)

        if ( rock.scaleX > rockMinScale )
            splitRock(rock.x, rock.y, rock.scaleX / splitScale, maxSplit)

        rock.remove()
    }

    // Check all rocks are destroyed
    private fun youWin() {
        val messageWin = BaseActor(0f, 0f, uiStage)
        with(messageWin) {
            loadTexture("message-win.png")

            val x = Settings.screenWidth / 2
            val y = Settings.screenHeight / 2

            centerAtPosition(x.toFloat(), y.toFloat())
            setOpacity(0f)

            val pulse : Action = Actions.sequence(
                    Actions.scaleTo(1.55f, 1.55f, 2f),
                    Actions.scaleTo(0.95f,0.95f, 2f)
            )

            addAction(Actions.addAction(pulse))
            addAction(Actions.after(Actions.fadeIn(2f)))
            addAction(Actions.after(Actions.delay(3f)))
            addAction(Actions.after(Actions.fadeOut(2f)))
        }
        gameOver = true
    }

    // Set game over message

    private fun gameOver() {
        val messageLose = BaseActor(0f, 0f, uiStage)

        with (messageLose) {
            loadTexture("message-lose.png")

            val x = Settings.screenWidth / 2
            val y = Settings.screenHeight / 2

            centerAtPosition(x.toFloat(), y.toFloat())
            setOpacity(0f)

            val pulse : Action = Actions.sequence(
                    Actions.scaleTo(1.55f, 1.55f, 2f),
                    Actions.scaleTo(0.95f,0.95f, 2f)
            )

            addAction(Actions.addAction(pulse))
            addAction(Actions.after(Actions.fadeIn(2f)))
            addAction(Actions.after(Actions.delay(3f)))
            addAction(Actions.after(Actions.fadeOut(2f)))
        }
        gameOver = true
    }
}

