package com.mygdx.game.entities

import com.mygdx.game.base.BaseActor
import com.badlogic.gdx.scenes.scene2d.Stage

class Explosion(x: Float, y:Float, s: Stage, sc : Float =1f) : BaseActor(x,y,s,sc) {

    init {
        loadAnimationFromSheet("explosion.png",6,6,.025f,false)
    }

    override fun act(dt: Float) {
        super.act(dt)

        if ( isAnimationFinished())
            remove()
    }
}