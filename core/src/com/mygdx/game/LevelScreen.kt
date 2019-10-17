package com.mygdx.game

import com.badlogic.gdx.Input
import com.mygdx.game.base.BaseActor

import com.mygdx.game.base.BaseScreen
import com.mygdx.game.entities.Explosion
import com.mygdx.game.entities.Rock
import com.mygdx.game.entities.Spaceship

class LevelScreen : BaseScreen() {

    private lateinit var spaceship : Spaceship

    private var gameOver = false

    override fun initialize() {
        val space = BaseActor(0f, 0f, mainStage)

        space.loadTexture("space.png")
        space.setSize(800f, 600f)
        BaseActor.setWorldBounds(space)

        spaceship = Spaceship(400f, 300f, mainStage)

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
        for (rock in BaseActor.getList(mainStage, "Rock")) {
            if (rock.overlaps((spaceship)))
                if (spaceship.shieldPower <= 0) {
                    var boom = Explosion(0F, 0F, mainStage)
                    spaceship.remove()
                    spaceship.setPosition(-1000F, -1000F)
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
    }
}

