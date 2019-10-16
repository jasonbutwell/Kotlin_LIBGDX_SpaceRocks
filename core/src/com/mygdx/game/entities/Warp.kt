package com.mygdx.game.entities

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.mygdx.game.base.BaseActor

class Warp(x: Float, y: Float, stage: Stage) : BaseActor(x, y, stage) {
    init {
        loadAnimationFromSheet("warp.png",4,8,0.05f,true)

        addAction(Actions.delay(1f))
        addAction(Actions.after(Actions.fadeOut(0.5f)))
        addAction(Actions.after(Actions.removeActor()))
    }
}