package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.mygdx.game.base.BaseActor

import com.mygdx.game.base.BaseScreen
import com.mygdx.game.entities.Explosion
import com.mygdx.game.entities.Rock
import com.mygdx.game.entities.Spaceship
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.mygdx.game.config.Settings

class LevelScreen : BaseScreen() {

    private lateinit var spaceship : Spaceship

    private var gameOver = false

    override fun initialize() {
        val space = BaseActor(0f, 0f, mainStage)

        space.loadTexture("space.png")
        space.setSize(Settings.screenWidth.toFloat(),Settings.screenHeight.toFloat())
        BaseActor.setWorldBounds(space)

        val xPos = Settings.screenWidth/2.toFloat()
        val yPos = Settings.screenHeight/2.toFloat()

        spaceship = Spaceship(xPos,yPos, mainStage)

        Rock(600F,500F,mainStage)
        Rock(600F,300F,mainStage)
        Rock(600F,100F,mainStage)
        Rock(400F,100F,mainStage)
        Rock(200F,100F,mainStage)
        Rock(200F,300F,mainStage)
        Rock(200F,500F,mainStage)
        Rock(400F,500F,mainStage)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.X)
            spaceship.warp()

        if ( keycode == Input.Keys.SPACE )
            spaceship.shoot()

        return false
    }

    override fun update(dt: Float) {
        if ( !gameOver )
            for (rock in BaseActor.getList(mainStage, "Rock")) {
                if (rock.overlaps((spaceship)))
                    if (spaceship.shieldPower <= 0) {
                        val boomStarship = Explosion(0F, 0F, mainStage)
                        boomStarship.centerAtActor(spaceship)

                        val boomRock = Explosion(0F, 0F, mainStage)
                        boomRock.centerAtActor(rock)

                        spaceship.remove()
                        rock.remove()

                        //spaceship.setPosition(-1000F, -1000F)
                        gameOver()

                    } else {
                        spaceship.shieldPower -= 34
                        val boom = Explosion(0F, 0F, mainStage)
                        boom.centerAtActor(rock)
                        rock.remove()
                    }

                for (laserActor in BaseActor.getList(mainStage, "Laser")) {
                    if (laserActor.overlaps(rock)) {
                        val boom = Explosion(0F,0F,mainStage)
                        boom.centerAtActor(rock)
                        laserActor.remove()
                        rock.remove()
                    }
                }
            }

        if (!gameOver && BaseActor.count(mainStage, "Rock") == 0)
            youWin()

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
            addAction(Actions.fadeIn(1f))
            addAction(Actions.after(Actions.delay(3f)))
            addAction(Actions.after(Actions.fadeOut(1f)))
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
            addAction(Actions.fadeIn(1f))
            addAction(Actions.after(Actions.delay(3f)))
            addAction(Actions.after(Actions.fadeOut(1f)))
        }
        gameOver = true
    }
}

