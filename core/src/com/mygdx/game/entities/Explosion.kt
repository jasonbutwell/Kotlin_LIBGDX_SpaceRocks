package com.mygdx.game.entities

import com.mygdx.game.base.BaseActor
import com.badlogic.gdx.scenes.scene2d.Stage

class Explosion(x: Float, y:Float, s: Stage) : BaseActor(x,y,s) {
    init {
        loadAnimationFromSheet("explosion.png",6,6,0.3f,false)
    }

    override fun act(dt: Float) {
        super.act(dt)

        if ( isAnimationFinished())
            remove()
    }
}